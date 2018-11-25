package core.query.handler;

import core.filter.HTMLFilter;
import core.filter.IFilter;
import core.indexing.io.FileHandler;
import core.nlp.Tokenizer;
import core.query.model.Document;
import core.utils.Constants;
import core.utils.Utils;

import java.io.File;
import java.io.IOException;

public class DocumentHandler {

    private Document[] documents;

    private String corpusPath;

    private FileHandler fileHandler;

    private int fileCount = Constants.MAX_BLOCK_FILES_COUNT;

    public DocumentHandler(String corpusPath) {
        this.corpusPath = corpusPath;
    }

    public DocumentHandler(String corpusPath, int fileCount) {
        this.corpusPath = corpusPath;
        this.fileCount = fileCount;
    }

    public Document[] getDocuments() {
        return documents;
    }

    public void loadDocuments() {
        fileHandler = new FileHandler(corpusPath);
        if(!corpusPath.isEmpty()) {
            File file = new File(corpusPath);
            if(file.isDirectory()) {
                int counter = 0;
                documents = new Document[fileCount];
                for(File file1: file.listFiles()) {
                    if(counter < fileCount) {
                        try {
                            if (file1.isFile()) {
                                documents[counter] = toDocument(fileHandler.readFileContents(file1), counter, file1.getName());
                            }
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                            documents[counter] = null;
                        }
                        counter++;
                    }
                }
            }
            else {
                documents = new Document[1];
                try {
                    documents[0] = toDocument(fileHandler.readFileContents(file), 0, file.getName());
                }
                catch (IOException e) {
                    System.out.println(e.getMessage());
                    documents[0] = null;
                }
            }
        }
    }

    private Document toDocument(String fileContent, int id, String name) {
        Tokenizer tokenizer = new Tokenizer(Tokenizer.LEMMA_TOKENS, Utils.loadStopwords("src/main/resources/stopwords.txt"));
        IFilter filter = new HTMLFilter();
        filter.construct();
        fileContent = filter.filter(fileContent);
        tokenizer.tokenize(name, fileContent);
        Document document = new Document(id);
        document.setName(name);
        tokenizer.getTokenMap()
                .get(name)
                .forEach(document::addTerm);
        //Currently generating document vector here. Will move it to a different location later if needed.
        //Following lnc schema.
        document.generateVector();
        return document;
    }
}
