package indexing;

import nlp.Tokenizer;
import org.junit.Test;
import utils.Utils;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class IndexTest {

    String outFile = "output/index_uncompressed";

    @Test
    public void createIndexTest() {
        Utils.deleteFile(outFile);
        createIndex("data/data1");
        File file = new File(outFile);
        assertTrue(file.exists());
    }

    @Test
    public void mergeIndexTest() {
        Utils.deleteFile(outFile);
        createIndex("data/data1");
        createIndex("data/data2");
        createIndex("data/data3");
        File file = new File(outFile);
        assertTrue(file.exists());
    }

    private void createIndex(String corpusPath) {
        Tokenizer tokenizer = new Tokenizer(Tokenizer.LEMMA_TOKENS, Utils.loadStopwords("src/main/resources/stopwords.txt"));
        tokenizer.tokenize(corpusPath, new SGMLFilter(), true);
        SPIMI spimi = new SPIMI();
        spimi.createIndex(tokenizer.getTokenMap(), outFile);
    }


}
