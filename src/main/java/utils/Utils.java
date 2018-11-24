package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

public class Utils {

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if(file.exists())
            file.delete();
    }

    public static HashSet<String> loadStopwords(String stopWordsFilePath) {
        HashSet<String> stopWords = new HashSet<>(1);
        try {
            File file = new File(stopWordsFilePath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                stopWords.add(line.toLowerCase().trim());
            }
            reader.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return stopWords;
    }

    public static double computeDotProduct(Map<String, Double> vector1, Map<String, Double> vector2) {
        double dotProduct = 0.0;
        for(String key: vector1.keySet()) {
            if(vector2.containsKey(key)) {
                dotProduct = dotProduct + (vector1.get(key) * vector2.get(key));
            }
        }
        return dotProduct;
    }
}
