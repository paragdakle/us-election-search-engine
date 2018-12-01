package core.query.handler;

import api.routes.Router;
import core.filter.IFilter;
import core.filter.QueryFilter;
import core.nlp.Tokenizer;
import core.query.model.Document;
import core.query.model.Query;
import core.utils.Formatter;
import core.utils.Utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class QueryHandler {

    public static final byte SIMPLE_COSINE_SIMILARITY = 0;
    public static final byte CS_PAGERANK = 1;
    public static final byte CS_HITS = 2;

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

    public Map<String, Double> getTopKDocuments(Document[] documents, byte method, int K) {
        if(K > documents.length) {
            K = documents.length;
        }
        if(method == SIMPLE_COSINE_SIMILARITY) {
            return getTopKDocumentsWithCosineSim(documents, K, method);
        }
        if(method == CS_PAGERANK) {
            return getTopKDocumentsWithCosineSim(documents, K, method);
        }
        return null;
    }

    private Map<String, Double> getTopKDocumentsWithCosineSim(Document[] documents, int K, byte method) {
        Map<String, Double> queryDocumentCosineScores = new LinkedHashMap<>();
        for (Document document : documents) {
            double score = Utils.computeDotProduct(query.getVector(), document.getVector());
            if(score > 0) {
                queryDocumentCosineScores.put(document.getName(), score);
            }
        }
        queryDocumentCosineScores = new Utils<String>().sortMap(queryDocumentCosineScores);
        Map<String, Double> result = new LinkedHashMap<>();
        for(String key: queryDocumentCosineScores.keySet()) {
            if(method == CS_PAGERANK && Router.pageRank != null) {
                result.put(key, Router.pageRank.get(key));
            }
            else if(method == CS_HITS) {
                if(Router.authorityScores != null && Router.authorityScores.containsKey(key)) {
                    result.put(key, Router.authorityScores.get(key));
                }
                else if(Router.hubScores != null && Router.hubScores.containsKey(key)) {
                    result.put(key, Router.hubScores.get(key));
                }
                else {
                    result.put(key, 0.0);
                }
            }
            else {
                result.put(key, queryDocumentCosineScores.get(key));
            }
            if (--K == 0) {
                break;
            }
        }
        return result;
    }
}
