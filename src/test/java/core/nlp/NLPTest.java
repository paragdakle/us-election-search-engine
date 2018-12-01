package core.nlp;

import core.filter.HTMLFilter;
import core.io.FileHandler;
import core.utils.Utils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NLPTest {

    String text = "my dog also likes eating sausage";
    String[] actualLemmas = new String[] {"my", "dog", "also", "like", "eating", "sausage"};
    String[] actualStems = new String[] {"my", "dog", "also", "like", "eat", "sausag"};
    String[] actualTokens = new String[] {"my", "dog", "also", "likes", "eating", "sausage"};

    @Test
    public void lemmatizeTest() {
        Lemmatizer lemmatizer = new Lemmatizer();
        String[] lemmas = lemmatizer.lemmatize(text);
        for(byte counter = 0; counter < lemmas.length; counter++) {
            assertEquals(actualLemmas[counter], lemmas[counter]);
        }
    }

    @Test
    public void stemmerTest() {
        Stemmer stemmer = new Stemmer();
        String[] stems = stemmer.stem(text);
        for(byte counter = 0; counter < stems.length; counter++) {
            assertEquals(actualStems[counter], stems[counter]);
        }
    }

    @Test
    public void plainTokenizerTest() {
        Tokenizer tokenizer = new Tokenizer(Tokenizer.PLAIN_TOKENS);
        FileHandler handler = new FileHandler("src/test/resources/simple.txt");
        tokenizer.tokenize(handler);
        tokenizer.getTokenMap().forEach((key, value) -> {
            for(byte counter = 0; counter < value.size(); counter++) {
                assertEquals(value.get(counter), actualTokens[counter]);
            }
        });
    }

    @Test
    public void lemmaTokenizerTest() {
        Tokenizer tokenizer = new Tokenizer(Tokenizer.LEMMA_TOKENS);
        FileHandler handler = new FileHandler("src/test/resources/simple.txt");
        tokenizer.tokenize(handler);
        tokenizer.getTokenMap().forEach((key, value) -> {
            for(byte counter = 0; counter < value.size(); counter++) {
                assertEquals(value.get(counter), actualLemmas[counter]);
            }
        });
    }

    @Test
    public void lemmaHTMLTokenizerTest() {
        Tokenizer tokenizer = new Tokenizer(Tokenizer.LEMMA_TOKENS, Utils.loadStopwords("src/main/resources/stopwords.txt"));
        tokenizer.tokenize("src/test/resources/seed1.txt", new HTMLFilter(), true);
        tokenizer.getTokenMap().forEach((key, value) -> {
            for(byte counter = 0; counter < value.size(); counter++) {
                assertEquals(value.get(counter), actualLemmas[counter]);
            }
        });
    }

    @Test
    public void stemTokenizerTest() {
        Tokenizer tokenizer = new Tokenizer(Tokenizer.STEM_TOKENS);
        FileHandler handler = new FileHandler("src/test/resources/simple.txt");
        tokenizer.tokenize(handler);
        tokenizer.getTokenMap().forEach((key, value) -> {
            for(byte counter = 0; counter < value.size(); counter++) {
                assertEquals(value.get(counter), actualStems[counter]);
            }
        });
    }
}
