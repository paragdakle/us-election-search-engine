package core.query;

import core.io.RandomAccessFileHandler;
import core.query.handler.DocumentHandler;
import core.query.handler.QueryHandler;
import org.junit.Test;
import core.utils.Constants;

import java.util.Map;

import static org.junit.Assert.*;

public class QueryHandlerTest {

    @Test
    public void loadQueryObjectTest() {
        QueryHandler queryHandler = new QueryHandler();
        queryHandler.setQueryStr("what similarity laws must be obeyed when constructing aeroelastic models" +
                " of heated high speed aircraft");
        queryHandler.populateQueryObject();
        assertNotNull(queryHandler.getQuery());
    }

    @Test
    public void loadQueryObjectFailTest() {
        QueryHandler queryHandler = new QueryHandler();
        queryHandler.populateQueryObject();
        assertNull(queryHandler.getQuery());
    }

    @Test
    public void loadQueryVectorTest() {
        QueryHandler queryHandler = new QueryHandler();
        queryHandler.setQueryStr("what similarity laws must be obeyed when constructing aeroelastic models" +
                " of heated high speed aircraft");
        queryHandler.populateQueryObject();
        RandomAccessFileHandler randomAccessFileHandler = new RandomAccessFileHandler("output/index_uncompressed");
        Map<String, Short> indexHeaders = randomAccessFileHandler.readIndexHeaders();
        queryHandler.populateQueryVector(indexHeaders);
        assertNotNull(queryHandler.getQuery().getVector());
        assertTrue(queryHandler.getQuery().getVector().size() > 0);
    }

    @Test
    public void calculateCosineSimilarityTest() {
        System.out.println(System.currentTimeMillis());
        QueryHandler queryHandler = new QueryHandler();
        queryHandler.setQueryStr("what similarity laws must be obeyed when constructing aeroelastic models" +
                " of heated high speed aircraft");
        queryHandler.populateQueryObject();
        RandomAccessFileHandler randomAccessFileHandler = new RandomAccessFileHandler("output/index_uncompressed");
        Map<String, Short> indexHeaders = randomAccessFileHandler.readIndexHeaders();
        queryHandler.populateQueryVector(indexHeaders);

        DocumentHandler documentHandler = new DocumentHandler(Constants.CORPUS_DIR_PATH, 1400);
        documentHandler.loadDocuments();

        int k = 10;
        Map<String, Double> cosineScores = queryHandler.getTopKDocuments(documentHandler.getDocuments(), api.Utils.Constants.SIMPLE_COSINE, k, true);
        assertNotNull(cosineScores);
        assertEquals(k, cosineScores.size());
        System.out.println(System.currentTimeMillis());
    }
}
