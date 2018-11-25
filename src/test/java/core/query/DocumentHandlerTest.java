package core.query;

import core.query.handler.DocumentHandler;
import org.junit.Test;
import core.query.model.Document;
import core.utils.Constants;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DocumentHandlerTest {

    @Test
    public void loadDocumentsTest() {
        DocumentHandler documentHandler = new DocumentHandler(Constants.CORPUS_DIR_PATH, 500);
        documentHandler.loadDocuments();
        for (Document document : documentHandler.getDocuments()) {
            assertNotNull(document);
        }
    }

    @Test
    public void loadDocumentTokensTest() {
        DocumentHandler documentHandler = new DocumentHandler(Constants.CORPUS_DIR_PATH, 500);
        documentHandler.loadDocuments();
        for (Document document : documentHandler.getDocuments()) {
            assertTrue(document.getTermMap().size() != 0);
        }
    }

    @Test
    public void loadDocumentVectorTest() {
        DocumentHandler documentHandler = new DocumentHandler(Constants.CORPUS_DIR_PATH, 500);
        documentHandler.loadDocuments();
        for (Document document : documentHandler.getDocuments()) {
            assertNotNull(document.getVector());
            assertTrue(document.getVector().size() != 0);
        }
    }
}
