package com.fmss.logservice.utils;

import com.fmss.logservice.model.LogModel;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class FileWriterUtil {

    private static final String FILE_PATH = "allLogFile.txt";


    public static void writeToFile(LogModel logModel) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true));
            writer.write(logModel.getMessage());
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
