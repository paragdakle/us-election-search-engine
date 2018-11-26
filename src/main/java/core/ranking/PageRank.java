package core.ranking;

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
        while (true) {
            boolean doBreak = true;
            for(String key: nodes.keySet()) {
                Node node = nodes.get(key);
                double weight = 1.0 - dampingFactor;
                if(node.getSourceNodes() != null) {
                    for (Node sourceNode : node.getSourceNodes()) {
                        weight += (dampingFactor * (sourceNode.getWeight() / sourceNode.getNodeCapacity()));
                    }
                }
                nodeScore.put(key, weight);
            }
            for(Node node: nodes.values()) {
                double weight = nodeScore.get(node.getName());
                if(node.getWeight() != weight) {
                    doBreak = false;
                    node.setWeight(weight);
                }
            }
            if(doBreak) {
                break;
            }
        }
    }
}
