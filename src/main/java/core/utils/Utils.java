package core.utils;

import core.ranking.model.Graph;
import core.ranking.model.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Utils<K> {

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

    public Map<K, Double> sortMap(Map<K, Double> documents) {
        return documents.entrySet()
                .stream()
                .sorted(Map.Entry.<K, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    public static void generateGraphStatistics(Graph graph) {
        if(graph != null && graph.getNodes() != null) {
            List<Integer> inLinks = new ArrayList<>();
            List<Integer> outLinks = new ArrayList<>();
            int totalLinks = 0;
            System.out.println("Number of nodes in the graph: " + graph.getNodes().size());
            for(Node node: graph.getNodes().values()) {
                if(node.getSourceNodes() != null) {
                    inLinks.add(node.getSourceNodes().size());
                    totalLinks += node.getSourceNodes().size();
                }
                if(node.getTargetNodes() != null) {
                    outLinks.add(node.getTargetNodes().size());
                }
            }
            System.out.println("Total number of links in the graph: " + totalLinks);
            inLinks.sort((o1, o2) -> -o1.compareTo(o2));
            System.out.println("Largest number of incoming links: " + inLinks.get(0));
            outLinks.sort((o1, o2) -> -o1.compareTo(o2));
            System.out.println("Largest number of outgoing links: " + outLinks.get(0));
        }
    }
}
