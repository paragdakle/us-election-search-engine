package core.nlp;

import core.filter.IFilter;
import core.indexing.io.FileHandler;
import core.indexing.io.IIOHandler;

import java.util.*;

public class Tokenizer {

    public static final byte PLAIN_TOKENS = 0;
    public static final byte LEMMA_TOKENS = 1;
    public static final byte STEM_TOKENS = 2;

    private Map<String, List<String>> tokenMap;

    private byte mode;

    private Lemmatizer lemmatizer;

    private Stemmer stemmer;

    private HashSet<String> stopWords;

    public Tokenizer() {
        this.tokenMap = new LinkedHashMap<>();
        stopWords = new HashSet<>(1);
    }

    public Tokenizer(byte mode) {
        this.tokenMap = new LinkedHashMap<>();
        this.mode = mode;
        if(this.mode == LEMMA_TOKENS) {
            lemmatizer = new Lemmatizer();
        }
        else if(this.mode == STEM_TOKENS) {
            stemmer = new Stemmer();
        }
        stopWords = new HashSet<>(1);
    }

    public Tokenizer(byte mode, HashSet<String> stopWords) {
        this.tokenMap = new LinkedHashMap<>();
        this.mode = mode;
        if(this.mode == LEMMA_TOKENS) {
            lemmatizer = new Lemmatizer();
        }
        else if(this.mode == STEM_TOKENS) {
            stemmer = new Stemmer();
        }
        this.stopWords = stopWords;
    }

    public Map<String, List<String>> getTokenMap() {
        return this.tokenMap;
    }

    public void tokenize(String dataPath, IFilter filter, boolean doFormatting) {
        if(filter == null) {
            return;
        }
        filter.construct();
        this.tokenize(new FileHandler(dataPath, filter, doFormatting));
    }

    public void tokenize(IIOHandler<String, String> handler) {
        Map<String, String> content = handler.read();
        content.forEach(this::tokenize);
    }

    public void tokenize(String filename, String text) {
        String[] contentSplit;
        if(mode == LEMMA_TOKENS) {
            contentSplit = lemmatizer.lemmatize(text);
        }
        else if(mode == STEM_TOKENS) {
            contentSplit = stemmer.stem(text);
        }
        else {
            contentSplit = text.split(" ");
        }
        tokenMap.put(filename, new ArrayList<>());
        for (String item : contentSplit) {
            item = item.trim();
            if(!item.equals("") && !stopWords.contains(item)) {
                tokenMap.get(filename).add(item);
            }
        }
    }
}
