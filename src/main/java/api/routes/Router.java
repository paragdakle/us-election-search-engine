package api.routes;

import api.controller.QueryController;
import core.io.FileHandler;
import core.io.RandomAccessFileHandler;
import core.utils.Utils;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.*;

import static spark.Spark.*;

public class Router {

    public static Map<String, Short> indexHeaders = null;

    public static Map<String, Double> pageRank = null;

    public static Map<String, Double> hubScores = null;

    public static Map<String, Double> authorityScores = null;

    public static HashSet<String> stopwords = null;

    public static void main(String[] args) {

        loadIndexHeaders();
        loadPageRankScores();
        loadPageRankScores();
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

    private static void loadPageRankScores() {
        FileHandler fileHandler = new FileHandler("output/pagerank.txt");
        List<String> content = fileHandler.readFileContent();
        if(content != null && content.size() > 0) {
            if (pageRank == null) {
                pageRank = new HashMap<>();
            }
            content.forEach((item) -> {
                String[] contentSplit = item.split(" ");
                if (contentSplit.length == 2) {
                    pageRank.put(contentSplit[0], Double.parseDouble(contentSplit[1]));
                }
            });
        }
    }

    private static void loadHITSScores() {
        FileHandler fileHandler = new FileHandler("output/hits.txt");
        List<String> content = fileHandler.readFileContent();
        if(content != null && content.size() > 0) {
            if (authorityScores == null) {
                authorityScores = new HashMap<>();
            }
            if (hubScores == null) {
                hubScores = new HashMap<>();
            }
            content.forEach((item) -> {
                String[] contentSplit = item.split(" ");
                if (contentSplit.length == 3) {
                    double hubScore = Double.parseDouble(contentSplit[1]);
                    double authScore = Double.parseDouble(contentSplit[2]);
                    if (hubScore == 0.0) {
                        authorityScores.put(contentSplit[0], authScore);
                    } else if (authScore == 0.0) {
                        authorityScores.put(contentSplit[0], hubScore);
                    }
                }
            });
        }
    }
}
