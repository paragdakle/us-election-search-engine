package api.routes;

import api.controller.QueryController;
import core.indexing.io.RandomAccessFileHandler;
import core.utils.Utils;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static spark.Spark.*;

public class Router {

    public static Map<String, Short> indexHeaders = null;

    public static HashSet<String> stopwords = null;

    public static void main(String[] args) {

        loadIndexHeaders();
        stopwords = Utils.loadStopwords("src/main/resources/stopwords.txt");

        port(8080);
        //GET REST APIS
        get("/hello", (req, res) -> "Current time is " + new Date(System.currentTimeMillis()).toString());

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("message", "U.S. Elections Search Engine!");
            return new VelocityTemplateEngine().render(
                    new ModelAndView(model, "templates/search.vm")
            );
        });

        //POST REST APIS
        post("/search", (request, response) -> {
            QueryController queryController = new QueryController();
            return queryController.handleRequest(request);
        });
    }

    private static void loadIndexHeaders() {
        RandomAccessFileHandler randomAccessFileHandler = new RandomAccessFileHandler("output/index_uncompressed");
        indexHeaders = randomAccessFileHandler.readIndexHeaders();
    }
}
