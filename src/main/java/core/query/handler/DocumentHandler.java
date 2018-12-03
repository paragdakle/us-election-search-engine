package core.query.handler;

import api.routes.Router;
import core.io.FileHandler;
import core.query.model.Document;
import core.utils.Constants;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentHandler {

    private Document[] documents;

    public Map<String, Document[]> rocchioDocuments;

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
                        if (file1.isFile()) {
                            documents[counter] = toDocument(fileHandler.readFileContent(file1), counter, file1.getName().replace(".txt", ""));
                        }
                        counter++;
                    }
                }
            }
            else {
                documents = new Document[1];
                documents[0] = toDocument(fileHandler.readFileContent(file), 0, file.getName());
            }
        }
        loadRocchioDocuments();
    }

    private void loadRocchioDocuments() {
        fileHandler = new FileHandler(Constants.ROCCHIO_CORPUS_DIR_PATH);
        rocchioDocuments = new HashMap<>();
        if(!corpusPath.isEmpty()) {
            File file = new File(Constants.ROCCHIO_CORPUS_DIR_PATH);
            if(file.isDirectory()) {
                for (File file1 : file.listFiles()) {
                    if (file1.isFile()) {
                        List<String> rocchioFileNames = fileHandler.readFileContent(file1);
                        Document[] rocchioDocumentsArray = new Document[rocchioFileNames.size()];
                        int counter = 0;
                        for(String filename: rocchioFileNames) {
                            File file2 = new File(Constants.TOKENIZED_CORPUS_DIR_PATH + filename);
                            if(file2.isFile()) {
                                rocchioDocumentsArray[counter++] = toDocument(fileHandler.readFileContent(file2), counter, file2.getName().replace(".txt", ""));
                            }
                        }
                        rocchioDocuments.put(file1.getName().replace(".txt", ""), rocchioDocumentsArray);
                    }
                }
            }
        }
    }

    private Document toDocument(List<String> fileTokens, int id, String name) {
        if(fileTokens == null) {
            return null;
        }
        Document document = new Document(id);
        document.setName(name);
        fileTokens.forEach(document::addTerm);
        if(Router.pageRank != null && Router.pageRank.containsKey(name)) {
            document.setPageRank(Router.pageRank.get(name));
        }
        //Currently generating document vector here. Will move it to a different location later if needed.
        //Following lnc schema.
        document.generateVector();
        return document;
    }
}
