package com.assets.services;

import java.io.FileWriter;
import java.util.Arrays;
import java.util.Date;

import com.assets.utils.CSVUtils;

public class Audit {

    public static void logAction(String action) throws Exception {

        String csvFile = "audit.csv";
        FileWriter writer = new FileWriter(csvFile, true);

        Date current = new Date();
        CSVUtils.writeLine(writer, Arrays.asList(action, current.toString()));

        writer.flush();
        writer.close();

    }

}