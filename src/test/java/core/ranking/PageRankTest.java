package core.ranking;

import core.ranking.model.Graph;
import core.ranking.model.Node;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PageRankTest {

    @Test
    public void createGraphTest() {

        Graph graph = getDefaultGraph();

        assertNotNull(graph.getNodes());
        assertEquals(15, graph.getNodes().size());
        assertNotNull(graph.getNodes().get("p6"));
        assertEquals(4.0, graph.getNodes().get("p6").getNodeCapacity(), 0.0);
        assertNotNull(graph.getNodes().get("p2"));
        assertEquals(6.0, graph.getNodes().get("p2").getNodeCapacity(), 0.0);
    }

    @Test
    public void computePageRankTest() {
        Graph graph = getDefaultGraph2();
        PageRank pageRank = new PageRank(graph);
        pageRank.computeGraphPageRank();

        graph = pageRank.getWebGraph();

        Map<String, Node> nodes = graph.getNodes();
        assertEquals(4, nodes.size());
        assertEquals(1.49, nodes.get("A").getWeight(), 0.01);
        assertEquals(0.78, nodes.get("B").getWeight(), 0.01);
        assertEquals(1.58, nodes.get("C").getWeight(), 0.01);
        assertEquals(0.15, nodes.get("D").getWeight(), 0.01);
    }

    private Graph getDefaultGraph() {
        Graph graph = new Graph();

        graph.addNode("p1");
        graph.addNode("p2");
        graph.addNode("p3");
        graph.addNode("p4");
        graph.addNode("p5");
        graph.addNode("p6");
        graph.addNode("p7");
        graph.addNode("p8");
        graph.addNode("p9");
        graph.addNode("p10");
        graph.addNode("p11");
        graph.addNode("p12");
        graph.addNode("p13");
        graph.addNode("p14");
        graph.addNode("p15");

        graph.addEdge("p2", "p1");
        graph.addEdge("p5", "p1");
        graph.addEdge("p8", "p1");
        graph.addEdge("p1", "p2");
        graph.addEdge("p4", "p2");
        graph.addEdge("p1", "p3");
        graph.addEdge("p2", "p3");
        graph.addEdge("p4", "p3");
        graph.addEdge("p5", "p3");
        graph.addEdge("p7", "p3");
        graph.addEdge("p8", "p3");
        graph.addEdge("p1", "p4");
        graph.addEdge("p2", "p4");
        graph.addEdge("p12", "p4");
        graph.addEdge("p13", "p4");
        graph.addEdge("p10", "p5");
        graph.addEdge("p11", "p5");
        graph.addEdge("p12", "p5");
        graph.addEdge("p13", "p5");
        graph.addEdge("p2", "p6");
        graph.addEdge("p3", "p6");
        graph.addEdge("p5", "p6");
        graph.addEdge("p9", "p6");
        graph.addEdge("p15", "p6");
        graph.addEdge("p2", "p7");
        graph.addEdge("p5", "p7");
        graph.addEdge("p6", "p7");
        graph.addEdge("p9", "p7");
        graph.addEdge("p1", "p8");
        graph.addEdge("p2", "p8");
        graph.addEdge("p6", "p8");
        graph.addEdge("p7", "p8");
        graph.addEdge("p7", "p9");
        graph.addEdge("p10", "p9");
        graph.addEdge("p6", "p10");
        graph.addEdge("p7", "p10");
        graph.addEdge("p8", "p10");
        graph.addEdge("p9", "p10");
        graph.addEdge("p3", "p11");
        graph.addEdge("p4", "p11");
        graph.addEdge("p5", "p11");
        graph.addEdge("p10", "p11");
        graph.addEdge("p11", "p12");
        graph.addEdge("p6", "p13");
        graph.addEdge("p8", "p13");
        graph.addEdge("p9", "p13");
        graph.addEdge("p10", "p13");
        graph.addEdge("p12", "p13");
        graph.addEdge("p14", "p13");
        graph.addEdge("p9", "p14");
        graph.addEdge("p10", "p14");
        graph.addEdge("p10", "p15");
        graph.addEdge("p13", "p15");
        graph.addEdge("p14", "p15");

        return graph;
    }

    private Graph getDefaultGraph1() {
        Graph graph = new Graph();

        graph.addNode("p1");
        graph.addNode("p2");
        graph.addNode("p3");
        graph.addNode("p4");
        graph.addNode("p5");
        graph.addNode("p6");
        graph.addNode("p7");

        graph.addEdge("p5", "p1");
        graph.addEdge("p1", "p2");
        graph.addEdge("p1", "p3");
        graph.addEdge("p1", "p4");
        graph.addEdge("p2", "p4");
        graph.addEdge("p3", "p4");
        graph.addEdge("p2", "p5");
        graph.addEdge("p3", "p6");
        graph.addEdge("p5", "p6");
        graph.addEdge("p5", "p7");
        graph.addEdge("p6", "p7");
        return graph;
    }

    private Graph getDefaultGraph2() {
        Graph graph = new Graph();

        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");

        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "C");
        graph.addEdge("C", "A");
        graph.addEdge("D", "C");

        return graph;
    }
}
