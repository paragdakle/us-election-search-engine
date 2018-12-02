package core.ranking;

import core.io.FileHandler;
import core.ranking.model.Graph;
import core.ranking.model.Node;
import core.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public static Graph extractSubGraph(Graph graph, Set<String> rootSetURL) {
        Graph subGraph = new Graph();
        Map<String, Node> nodes = graph.getNodes();

        for(String rootUrl: rootSetURL) {
            subGraph.addNode(rootUrl);
            if(nodes.containsKey(rootUrl)) {
                Node node = nodes.get(rootUrl);
                if (node.getTargetNodes() != null) {
                    for (Node tagetNode : node.getTargetNodes()) {
                        subGraph.addNode(tagetNode.getName());
                        subGraph.addEdge(rootUrl, tagetNode.getName());
                    }
                }
                if (node.getSourceNodes() != null) {
                    for (Node sourceNode : node.getSourceNodes()) {
                        subGraph.addNode(sourceNode.getName());
                        subGraph.addEdge(sourceNode.getName(), rootUrl);
                    }
                }
            }
            else {
                System.out.println(rootUrl + " is not present in the graph");
            }
        }

        return subGraph;
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

    }
}
