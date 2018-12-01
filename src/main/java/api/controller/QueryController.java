package api.controller;

import api.Utils.Constants;
import api.model.StandardResponse;
import api.routes.Router;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import core.query.handler.DocumentHandler;
import core.query.handler.QueryHandler;
import spark.Request;

import java.util.Map;

public class QueryController implements IController {

    @Override
    public String handleRequest(Request request) {
        String queryString = request.queryParams("query");
        if(queryString == null) {
            return toGson(StandardResponse.getFailureResponse(Constants.INVALID_PARAMETERS));
        }
        String rModelType = request.queryParams("relevance");
        if(rModelType == null) {
            return toGson(StandardResponse.getFailureResponse(Constants.INVALID_PARAMETERS));
        }
        String cAlgorithmType = request.queryParams("clustering");
        if(cAlgorithmType == null) {
            return toGson(StandardResponse.getFailureResponse(Constants.INVALID_PARAMETERS));
        }
        String qeAlgorithmType = request.queryParams("qe");
        if(qeAlgorithmType == null) {
            return toGson(StandardResponse.getFailureResponse(Constants.INVALID_PARAMETERS));
        }

        QueryHandler queryHandler = new QueryHandler(queryString);
        queryHandler.populateQueryObject();
        queryHandler.populateQueryVector(Router.indexHeaders);

        DocumentHandler documentHandler = new DocumentHandler(core.utils.Constants.CORPUS_DIR_PATH);
        documentHandler.loadDocuments();

        Map<String, Double> results = queryHandler.getTopKDocuments(documentHandler.getDocuments(), QueryHandler.SIMPLE_COSINE_SIMILARITY,10);
        JsonArray jsonElement = new JsonArray();
        int counter = 1;
        for(String key: results.keySet()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("rank", counter++);
            jsonObject.addProperty("id", key);
            jsonObject.addProperty("score", results.get(key));
            jsonElement.add(jsonObject);
        }

        return toGson(StandardResponse.getSuccessResponse(jsonElement));
    }

    private String toGson(StandardResponse standardResponse) {
        return new Gson().toJson(standardResponse);
    }
}
