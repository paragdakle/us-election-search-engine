package nlp;

import filter.IFilter;
import filter.HTMLFilter;
import indexing.io.FileHandler;
import indexing.io.IIOHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Tokenizer {

    private final byte TOTAL_TOKENS_INDEX = 0;
    private final byte UNIQUE_WORDS_INDEX = 1;
    private final byte ONCE_WORDS_INDEX = 2;
    private final byte AVG_WORDS_DOC_INDEX = 3;

    public static final byte PLAIN_TOKENS = 0;
    public static final byte LEMMA_TOKENS = 1;
    public static final byte STEM_TOKENS = 2;

    private Map<String, List<String>> tokenMap;

    private IFilter filter;

    private byte mode;

    private Lemmatizer lemmatizer;

    private Stemmer stemmer;

    public Tokenizer() {
        this.tokenMap = new LinkedHashMap<>();
        filter = new HTMLFilter();
    }

    public Tokenizer(byte mode) {
        this.tokenMap = new LinkedHashMap<>();
        filter = new HTMLFilter();
        this.mode = mode;
        if(this.mode == LEMMA_TOKENS) {
            lemmatizer = new Lemmatizer();
        }
        else if(this.mode == STEM_TOKENS) {
            stemmer = new Stemmer();
        }
    }

    public Map<String, List<String>> getTokenMap() {
        return this.tokenMap;
    }

    public IFilter getFilter() {
        return this.filter;
    }

    public void tokenize(String dataPath, IFilter filter, boolean doFormatting) {
        this.tokenize(new FileHandler(dataPath, filter, doFormatting));
    }

    public void tokenize(IIOHandler<String, String> handler) {
        if(filter == null) {
            return;
        }
        filter.construct();
        Map<String, String> content = handler.read();
        content.forEach((filename, contents) -> {
            String[] contentSplit;
            if(mode == LEMMA_TOKENS) {
                contentSplit = lemmatizer.lemmatize(contents);
            }
            else if(mode == STEM_TOKENS) {
                contentSplit = stemmer.stem(contents);
            }
            else {
                contentSplit = contents.split(" ");
            }
            tokenMap.put(filename, new ArrayList<>());
            for (String item : contentSplit) {
                item = item.trim();
                if(!item.equals("")) {
                    tokenMap.get(filename).add(item);
                }
            }
        });
    }
}
