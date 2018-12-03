package core.query.handler;

import api.Utils.Constants;
import api.routes.Router;
import core.filter.IFilter;
import core.filter.QueryFilter;
import core.nlp.Tokenizer;
import core.query.model.Document;
import core.query.model.Query;
import core.ranking.ConstructGraphHelper;
import core.ranking.HITS;
import core.ranking.model.Graph;
import core.utils.Formatter;
import core.utils.Utils;
import org.apache.commons.codec.binary.Base64;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class QueryHandler {

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

    public Map<String, Double> getTopKDocuments(Document[] documents, byte method, int K, boolean base64Decode) {
        if(K > documents.length) {
            K = documents.length;
        }
        Map<String, Double> csResults = getTopKDocumentsWithCosineSim(documents, K, base64Decode);
        if(method == Constants.SIMPLE_COSINE) {
            return csResults;
        }
        if(method == Constants.SIMPLE_COSINE_W_PR) {
            return reorderDocumentsWPR(csResults);
        }
        if (method == Constants.SIMPLE_COSINE_W_HITS) {
            Graph subGraph = ConstructGraphHelper.extractSubGraph(Router.mainGraph, csResults.keySet());
            HITS hits = new HITS(subGraph);
            hits.computeHITSScores();
            return reorderDocumentsWHITS(hits, csResults.keySet());
        }
        return null;
    }

    private Map<String, Double> getTopKDocumentsWithCosineSim(Document[] documents, int K, boolean base64Decode) {
        Map<String, Double> queryDocumentCosineScores = new LinkedHashMap<>();
        for (Document document : documents) {
            if(document != null) {
                double score = Utils.computeDotProduct(query.getVector(), document.getVector());
                if (score > 0) {
                    queryDocumentCosineScores.put(document.getName(), score);
                }
            }
        }
        queryDocumentCosineScores = new Utils<String>().sortMap(queryDocumentCosineScores);
        Map<String, Double> result = new LinkedHashMap<>();
        for(String key: queryDocumentCosineScores.keySet()) {
            if(base64Decode) {
                result.put(new String(Base64.decodeBase64(key)), queryDocumentCosineScores.get(key));
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

    private Map<String, Double> reorderDocumentsWPR(Map<String, Double> csResults) {
        Map<String, Double> prResults = new LinkedHashMap<>(csResults);
        if(Router.pageRank != null) {
            for(String key: csResults.keySet()) {
                prResults.put(key, Router.pageRank.getOrDefault(key, 0.0));
            }
            return new Utils<String>().sortMap(prResults);
        }
        return prResults;
    }

    private Map<String, Double> reorderDocumentsWHITS(HITS hits, Set<String> csResultsKeys) {
        Map<String, Double> hitsResults = new LinkedHashMap<>();
        Map<String, Double> hubScores = new LinkedHashMap<>();
        Map<String, Double> authorityScores = new LinkedHashMap<>();
        Map<String, Double[]> actualHubScores= hits.getHubScores();
        Map<String, Double[]> actualAuthScores= hits.getAuthorityScores();
//        for(String key: csResultsKeys) {
        for(String key: actualAuthScores.keySet()) {
            if(actualHubScores.containsKey(key)) {
                hubScores.put(key, actualHubScores.get(key)[1]);
            }
            if(actualAuthScores.containsKey(key)) {
                authorityScores.put(key, actualAuthScores.get(key)[1]);
            }
        }
        Utils<String> utils = new Utils<>();
        hubScores = utils.sortMap(hubScores);
        authorityScores = utils.sortMap(authorityScores);
        byte counter = 0;
        for(String key: hubScores.keySet()) {
            if(counter < 10)
                hitsResults.put(key + "__hub__", hubScores.get(key));
            else
                break;
            counter++;
        }
        counter = 10;
        for(String key: authorityScores.keySet()) {
            if(counter > 0)
                hitsResults.put(key + "__auth__", authorityScores.get(key));
            else
                break;
            counter--;
        }
        return hitsResults;
    }
}
