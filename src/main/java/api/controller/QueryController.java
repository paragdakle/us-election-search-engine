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
        QueryHandler queryHandler = new QueryHandler(queryString);
        queryHandler.populateQueryObject();
        queryHandler.populateQueryVector(Router.indexHeaders);

        DocumentHandler documentHandler = new DocumentHandler(core.utils.Constants.CORPUS_DIR_PATH);
        documentHandler.loadDocuments();

        Map<Integer, Double> results = queryHandler.getTopKDocuments(documentHandler.getDocuments(), QueryHandler.SIMPLE_COSINE_SIMILARITY,10);
        JsonArray jsonElement = new JsonArray();
        int counter = 1;
        for(Integer key: results.keySet()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("rank", counter++);
            jsonObject.addProperty("id", key);
            jsonObject.addProperty("score", results.get(key));
            jsonElement.add(jsonObject);
        }

        return new Gson().toJson(new StandardResponse(Constants.SUCCESS_STATUS, jsonElement));
    }
}
