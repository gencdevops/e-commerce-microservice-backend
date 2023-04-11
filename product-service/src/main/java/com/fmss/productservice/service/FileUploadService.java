package com.fmss.productservice.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    @Value("${aws.bucket-name}")
    String bucketName;

    @Value("${aws.access-key}")
    String accessKey;

    @Value("${aws.secret-key}")
    String secretKey;

    private AmazonS3 s3client;

    @Bean
    public void fileUploadConfig() {

        final var awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.US_EAST_2)
                .build();
        final var listObjectsRequest = new ListObjectsV2Request()
                .withBucketName(bucketName);
        ListObjectsV2Result result;
        do {
            result = s3client.listObjectsV2(listObjectsRequest);
            listObjectsRequest.setContinuationToken(result.getNextContinuationToken());
        } while (result.isTruncated());
    }

    public String storeFile(File file, String fileName) throws IOException {
        final var is = new BufferedInputStream(new FileInputStream(file));
        final var metadata = new ObjectMetadata();
        metadata.setContentType(URLConnection.guessContentTypeFromStream(is));
        metadata.setContentLength(file.length());
        s3client.putObject(new PutObjectRequest(bucketName, fileName, is, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return s3client.getUrl(bucketName, fileName).toString();
    }

    public String getFilePath(String fileName) {
        return s3client.getUrl(bucketName, fileName).toString();
    }
}

