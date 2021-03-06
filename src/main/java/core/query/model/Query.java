package core.query.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Query {

    private short id;

    private Map<String, Short> termMap;

    private short maxTF;

    private short length;

    private Map<String, Double> vector;

//    private PriorityQueue<PriorityQueueItem> vector;

    public Query(short id) {
        this.id = id;
        termMap = new HashMap<>(1);
    }

    public void addTerm(String term) {
        if(termMap.containsKey(term)) {
            short frequency = termMap.get(term);
            //Filter in future if MaxTF is not used anywhere.
            if(maxTF < frequency + 1) {
                maxTF = (short)(frequency + 1);
            }
            termMap.put(term, (short)(frequency + 1));
        }
        else {
            termMap.put(term, (short) 1);
            if(maxTF < 1) {
                maxTF = (short) 1;
            }
        }
        length++;
    }

    public short getMaxTF() {
        return maxTF;
    }

    public short getLength() {
        return length;
    }

    public Map<String, Short> getTermMap() {
        return termMap;
    }

    public short getId() {
        return id;
    }

    public Map<String, Double> getVector() {
        return vector;
    }


//    public PriorityQueue<PriorityQueueItem> getVector() {
//        return vector;
//    }

    public void generateVector(Map<String, Short> indexHeaders) {
//        vector = new PriorityQueue<>((o1, o2) -> -o1.getScore().compareTo(o2.getScore()));
        vector = new LinkedHashMap<>();
        double normalizationValue = 0;
        for(String term: termMap.keySet()) {
            int df = indexHeaders.containsKey(term) ? indexHeaders.get(term) : 0;
            double value1 = Math.log10(indexHeaders.size() * 1.0 / df * 1.0);

//            int tf = termMap.containsKey(term) ? termMap.get(term) : 0;
            /*
             * This is a simplification implemented for faster core.query vector computation.
             * To turn off, uncomment previous line, comment the next line and also uncomment
             * the normalization section.
             */
            int tf = termMap.containsKey(term) ? 1 : 0;
            double tfwt = tf == 0 ? 0 : (1 + Math.log10(tf));
            double tfidf = tfwt * value1;
            normalizationValue = normalizationValue + (tfidf * tfidf);
//            PriorityQueueItem item = new PriorityQueueItem(term, tfidf);
//            vector.add(item);
            vector.put(term, tfidf);
        }
        normalizationValue = Math.sqrt(normalizationValue);
        for(String term: termMap.keySet()) {
            vector.put(term, vector.get(term) / normalizationValue);
        }
    }

}
