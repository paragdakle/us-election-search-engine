package core.utils;

import core.filter.HTMLFilter;
import core.nlp.Tokenizer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CorpusHelper {

    private String corpusPath;

    private String outputCorpusDirPath;

    CorpusHelper(String corpusPath, String outputCorpusDirPath) {
        this.corpusPath = corpusPath;
        this.outputCorpusDirPath = outputCorpusDirPath;
    }

    public void saveFilteredCorpus() {
        Tokenizer tokenizer = new Tokenizer(Tokenizer.LEMMA_TOKENS, Utils.loadStopwords("src/main/resources/stopwords.txt"));
        tokenizer.tokenize(corpusPath, new HTMLFilter(), true);

        tokenizer.getTokenMap().forEach((filename, tokens) -> {
            File file = new File(outputCorpusDirPath + "/" + filename);
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for(String token: tokens) {
                    writer.write(token + "\n");
                }
                writer.close();
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public static void main(String[] args) {
        CorpusHelper corpusHelper = new CorpusHelper(Constants.CORPUS_DIR_PATH, "output/corpus/");
        corpusHelper.saveFilteredCorpus();
    }
}
