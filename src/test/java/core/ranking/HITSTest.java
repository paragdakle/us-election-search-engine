package core.ranking;

import core.ranking.model.Graph;
import org.junit.Test;

import java.io.File;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HITSTest {

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
    public void computeHITSTest() {
        Graph graph = getDefaultGraph4();
        HITS hits = new HITS(graph);
        hits.computeHITSScores();

        Map<String, Double[]> hubScores = hits.getHubScores();
        Map<String, Double[]> authorityScores = hits.getAuthorityScores();

        for(String key: hubScores.keySet()) {
            System.out.println(key + " " + hubScores.get(key)[0] + " " + authorityScores.get(key)[0]);
        }
        assertEquals(10, hubScores.size());
        assertEquals(10, authorityScores.size());
    }

    @Test
    public void storeGraphTest() {
        Graph graph = getDefaultGraph4();
        HITS hits = new HITS(graph);
        hits.computeHITSScores();
        String filePath = "output/hits.txt";
        hits.writeResults(filePath);
        File file = new File(filePath);
        assertTrue(file.exists());
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

    private Graph getDefaultGraph3() {
        Graph graph = new Graph();

        graph.addNode("a");
        graph.addNode("b");
        graph.addNode("c");

        graph.addEdge("a", "c");
        graph.addEdge("b", "c");

        return graph;
    }

    private Graph getDefaultGraph4() {
        Graph graph = new Graph();

        graph.addNode("D1");
        graph.addNode("D2");
        graph.addNode("D3");
        graph.addNode("D4");
        graph.addNode("D5");
        graph.addNode("D6");
        graph.addNode("D7");
        graph.addNode("D8");
        graph.addNode("D9");
        graph.addNode("D10");

        graph.addEdge("D1", "D2");
        graph.addEdge("D1", "D3");
        graph.addEdge("D1", "D5");
        graph.addEdge("D1", "D6");
        graph.addEdge("D1", "D9");
        graph.addEdge("D2", "D4");
        graph.addEdge("D2", "D7");
        graph.addEdge("D2", "D9");
        graph.addEdge("D3", "D1");
        graph.addEdge("D3", "D6");
        graph.addEdge("D3", "D10");
        graph.addEdge("D4", "D2");
        graph.addEdge("D4", "D6");
        graph.addEdge("D4", "D9");
        graph.addEdge("D5", "D6");
        graph.addEdge("D5", "D7");
        graph.addEdge("D5", "D9");
        graph.addEdge("D5", "D10");
        graph.addEdge("D6", "D2");
        graph.addEdge("D6", "D3");
        graph.addEdge("D6", "D8");
        graph.addEdge("D7", "D1");
        graph.addEdge("D7", "D3");
        graph.addEdge("D7", "D9");
        graph.addEdge("D8", "D1");
        graph.addEdge("D8", "D5");
        graph.addEdge("D8", "D10");
        graph.addEdge("D9", "D2");
        graph.addEdge("D9", "D5");
        graph.addEdge("D9", "D10");
        graph.addEdge("D10", "D1");
        graph.addEdge("D10", "D3");
        graph.addEdge("D10", "D6");


        return graph;
    }
}
