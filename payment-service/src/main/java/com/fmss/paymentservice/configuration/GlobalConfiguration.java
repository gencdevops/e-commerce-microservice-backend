package com.fmss.paymentservice.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@Configuration
@EnableMongoRepositories("com.fmss.paymentservice.repository")
public class GlobalConfiguration {
}
