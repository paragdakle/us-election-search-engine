package core.ranking;

import core.io.FileHandler;
import core.ranking.model.Graph;
import core.ranking.model.Node;

import java.util.HashMap;
import java.util.Map;

public class PageRank {

    private Graph webGraph;

    private double dampingFactor = 0.85;

    public PageRank(Graph graph) {
        this.webGraph = graph;
    }

    public PageRank(Graph graph, double dampingFactor) {
        this.webGraph = graph;
        this.dampingFactor = dampingFactor;
    }

    public Graph getWebGraph() {
        return this.webGraph;
    }

    public void computeGraphPageRank() {
        webGraph.initializeGraphWeights(1.0 / webGraph.getNodes().size());
        Map<String, Node> nodes = webGraph.getNodes();
        Map<String, Double> nodeScore = new HashMap<>();
        double startingWeight = (1.0 - dampingFactor);
        int counter = 1;
        while (true) {
            boolean doBreak = true;
            for(String key: nodes.keySet()) {
                Node node = nodes.get(key);
                double weight = startingWeight;
                if(node.getSourceNodes() != null) {
                    for (Node sourceNode : node.getSourceNodes()) {
                        weight += (dampingFactor * (sourceNode.getWeight() / sourceNode.getNodeCapacity()));
                    }
                }
                nodeScore.put(key, weight);
            }
//            System.out.println(counter++);
            for(Node node: nodes.values()) {
                double weight = nodeScore.get(node.getName());
                if(Math.abs(node.getWeight() - weight) > 0.00001) {
                    doBreak = false;
                    node.setWeight(weight);
                }
//                System.out.println(node.getName() + " " + node.getWeight());
            }
            if(doBreak) {
                break;
            }
        }
    }

    public void writeResults(String filePath) {
        FileHandler handler = new FileHandler(filePath);
        handler.writeGraphToFile(webGraph, true);
    }
}
