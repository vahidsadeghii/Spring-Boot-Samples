package com.example.restapi.common;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CSVUtil {
    public static List<List<String>> readCSV(String fileName, String... headers) {
        List<List<String>> lines = new ArrayList<>();
        try (FileReader fileReader = new FileReader(fileName)) {
            CSVParser csvParser = CSVFormat
                    .DEFAULT
                    .withHeader(headers)
                    .withFirstRecordAsHeader()
                    .parse(fileReader);

            csvParser.forEach(
                    line -> {
                        ArrayList<String> values = new ArrayList<>();
                        Iterator<String> lineIterator = line.iterator();
                        while (lineIterator.hasNext()) {
                            values.add(lineIterator.next());
                        }

                        lines.add(values);
                    }
            );
        } catch (Exception e) {
            System.err.printf("Error in parse file : %s%n", fileName);
        }

        return lines;
    }
}
