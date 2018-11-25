package core.query.handler;

import core.filter.IFilter;
import core.filter.QueryFilter;
import core.nlp.Tokenizer;
import core.query.model.Document;
import core.query.model.Query;
import core.utils.Formatter;
import core.utils.Utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryHandler {

    public static final byte SIMPLE_COSINE_SIMILARITY = 0;

    private String queryStr;

    private Query query = null;

    public QueryHandler() {
        this.queryStr = "";
    }

    public QueryHandler(String queryStr) {
        this.queryStr = queryStr;
    }

    public String getQueryStr() {
        return queryStr;
    }

    public void setQueryStr(String queryStr) {
        this.queryStr = queryStr;
    }

    public Query getQuery() {
        return query;
    }

    /*
     //Uncomment this section if core.query object is required to be set from outside
    public void setQuery(Query core.query) {
        this.core.query = core.query;
    }
    */

    public void populateQueryObject() {
        if(!queryStr.isEmpty()) {
            //Currently hardcoding each core.query id as we believe only one core.query will
            //handled per QueryHandler object.
            query = new Query((short) 0);

            Tokenizer tokenizer = new Tokenizer(Tokenizer.LEMMA_TOKENS, Utils.loadStopwords("src/main/resources/stopwords.txt"));
            String fQueryStr = Formatter.formatInput(queryStr);
            IFilter filter = new QueryFilter();
            filter.construct();
            fQueryStr = filter.filter(fQueryStr.trim());
            tokenizer.tokenize("query", fQueryStr);
            tokenizer.getTokenMap()
                    .get("query")
                    .forEach(query::addTerm);
        }
    }

    public void populateQueryVector(Map<String, Short> indexHeaders) {
        if(query != null && query.getTermMap().size() > 0 && indexHeaders != null) {
            query.generateVector(indexHeaders);
        }
    }

    public Map<Integer, Double> getTopKDocuments(Document[] documents, byte method, int K) {
        if(K > documents.length) {
            K = documents.length;
        }
        if(method == SIMPLE_COSINE_SIMILARITY) {
            return getTopKDocumentsWithCosineSim(documents, K);
        }
        return null;
    }

    private Map<Integer, Double> getTopKDocumentsWithCosineSim(Document[] documents, int K) {
        Map<Integer, Double> queryDocumentCosineScores = new LinkedHashMap<>();
        for (Document document : documents) {
            queryDocumentCosineScores.put(document.getId(), Utils.computeDotProduct(query.getVector(), document.getVector()));
        }
        queryDocumentCosineScores = sortDocumentsByCosineScore(queryDocumentCosineScores);
        Map<Integer, Double> result = new LinkedHashMap<>();
        for(Integer key: queryDocumentCosineScores.keySet()) {
            result.put(key, queryDocumentCosineScores.get(key));
            if (--K == 0) {
                break;
            }
        }
        return result;
    }

    private Map<Integer, Double> sortDocumentsByCosineScore(Map<Integer, Double> documents) {
        return documents.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }
}
