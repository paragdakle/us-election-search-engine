package core.query.handler;

import api.routes.Router;
import core.io.FileHandler;
import core.query.model.Document;
import core.utils.Constants;

import java.io.File;
import java.util.List;

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
                        if (file1.isFile()) {
                            documents[counter] = toDocument(fileHandler.readFileContent(file1), counter, file1.getName());
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
        if(Router.authorityScores != null && Router.authorityScores.containsKey(name)) {
            document.setType(document.TYPE_AUTHORITY);
            document.setWeight(Router.authorityScores.get(name));
        }
        if(Router.hubScores != null && Router.hubScores.containsKey(name)) {
            document.setType(document.TYPE_HUB);
            document.setWeight(Router.hubScores.get(name));
        }
        //Currently generating document vector here. Will move it to a different location later if needed.
        //Following lnc schema.
        document.generateVector();
        return document;
    }
}
