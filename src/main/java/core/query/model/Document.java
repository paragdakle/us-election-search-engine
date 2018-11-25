package core.query.model;

import java.util.HashMap;
import java.util.Map;

public class Document {

    private int id;

    private String name;

    private Map<String, Short> termMap;

    private short maxTF;

    private short length;

    private Map<String, Double> vector;

    public Document(int id) {
        this.id = id;
        termMap = new HashMap<>(1);
    }

    public void addTerm(String term) {
        if(termMap.containsKey(term)) {
            short frequency = termMap.get(term);
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Double> getVector() {
        return vector;
    }

    public void generateVector() {
        vector = new HashMap<>();
        double normalizationValue = 0;
        for(String term: termMap.keySet()) {
            int tf = termMap.containsKey(term) ? termMap.get(term) : 0;
            double tfwt = tf == 0 ? 0 : (1 + Math.log10(tf));
            normalizationValue = normalizationValue + (tfwt * tfwt);
            vector.put(term, tfwt);
        }
        normalizationValue = Math.sqrt(normalizationValue);
        for(String term: termMap.keySet()) {
            vector.put(term, vector.get(term) / normalizationValue);
        }
    }

}
