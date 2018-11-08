package indexing;

import indexing.io.RandomAccessFileHandler;
import indexing.model.PostingFileItem;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class SPIMI {

    private Map<String, LinkedList<PostingFileItem>> index = null;
    private String[] docIds;
    private int maxTermLength = -1;
    private static HashSet<String> stopWords;
    private final String outFile = "index_uncompressed";
    private byte indexCounter = 0;

    private void createIndex(Map<String, List<String>> termMap, String outFile) {
        docIds = new String[termMap.size() + 1];
        int counter = 1;
        docIds[0] = "";

        for(String docId: termMap.keySet()) {
            try {
                if(!docIds[counter - 1].equals(docId)) {
                    docIds[counter] = docId;
                }
                List<String> termList = termMap.get(docId);
                if(index == null) {
                    index = new LinkedHashMap<>();
                }
                for(String term: termList) {
                    if(stopWords.contains(term)) {
                        continue;
                    }
                    if(maxTermLength < term.length()) {
                        maxTermLength = term.length();
                    }
                    if(index.containsKey(term)) {
                        LinkedList<PostingFileItem> postingList = index.get(term);
                        PostingFileItem postingFileItem = new PostingFileItem(counter);
                        int itemIndex = postingList.indexOf(postingFileItem);
                        if(itemIndex > -1) {
                            postingList.get(itemIndex).incrementTermFrequency();
                        }
                        else {
                            postingList.add(postingFileItem);
                        }
                    }
                    else {
                        PostingFileItem postingFileItem = new PostingFileItem(counter);
                        LinkedList<PostingFileItem> postingList = new LinkedList<>();
                        postingList.add(postingFileItem);
                        index.put(term, postingList);
                    }
                }
                counter++;
            }
            catch (OutOfMemoryError e) {
                System.out.println("No more space left for the index. Writing out the index and resuming index creation.");
                writeIndexToFile(outFile);
            }
        }
        writeIndexToFile(outFile);
    }

    private void writeIndexToFile(String outFile) {
        if(index.size() == 0) {
            return;
        }
        sortIndex();
        RandomAccessFileHandler handler = new RandomAccessFileHandler(outFile + "_" + indexCounter);
        handler.write(index);
        indexCounter++;
        index.clear();
    }

    private void writeDocMapAndStats(Map<String, List<String>> tokenMap, String mapAndStatsFile) {
        try {
            File file = new File(mapAndStatsFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            if(file.isDirectory()) {
                System.out.println("Invalid file path provided. Kindly provide a valid file path!");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for(int counter = 1; counter < docIds.length; counter++) {
                writer.write(counter + " " + docIds[counter] + " " + tokenMap.get(docIds[counter]).size() + "\n");
                writer.flush();
            }
            writer.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void sortIndex() {
        index = index.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.naturalOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }
}
