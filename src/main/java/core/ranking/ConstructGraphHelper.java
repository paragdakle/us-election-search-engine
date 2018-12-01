package core.ranking;

import core.io.FileHandler;
import core.ranking.model.Graph;
import core.utils.Utils;

import java.util.List;

public class ConstructGraphHelper {

    private String filePath;

    private Graph graph;

    public ConstructGraphHelper(String filePath) {
        this.filePath = filePath;
        graph = new Graph();
    }

    public void constructGraph() {
        FileHandler fileHandler = new FileHandler(filePath);
        List<String> content = fileHandler.readFileContent();
        if(content != null) {
            content.forEach((item) -> {
                String[] contentSplit = item.trim().split(" ");
                graph.addNode(contentSplit[0]);
                if(contentSplit.length == 2) {
                    graph.addNode(contentSplit[1]);
                    graph.addEdge(contentSplit[0], contentSplit[1]);
                }
            });
        }
    }

    public Graph getGraph() {
        return graph;
    }

    public static void main(String[] args) {
        ConstructGraphHelper constructGraphHelper = new ConstructGraphHelper("src/main/resources/links.txt");
        constructGraphHelper.constructGraph();
        Utils.generateGraphStatistics(constructGraphHelper.getGraph());
        PageRank pageRank = new PageRank(constructGraphHelper.getGraph());
        pageRank.computeGraphPageRank();
        pageRank.writeResults("output/pagerank.txt");

        constructGraphHelper = new ConstructGraphHelper("src/main/resources/links.txt");
        constructGraphHelper.constructGraph();
        HITS hits = new HITS(constructGraphHelper.getGraph());
        hits.computeHITSScores();
        hits.writeResults("output/hits.txt");

    }
}
