package core.ranking.model;

import java.util.LinkedList;
import java.util.List;

public class Node {

    private String name;

    private List<Node> sourceNodes;

    private List<Node> targetNodes;

    private double weight = 0.0;

    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Node> getSourceNodes() {
        return sourceNodes;
    }

    public List<Node> getTargetNodes() {
        return targetNodes;
    }

    public void addSourceNode(Node node) {
        if(this.sourceNodes == null) {
            this.sourceNodes = new LinkedList<>();
        }
        this.sourceNodes.add(node);
    }

    public void addTargetNode(Node node) {
        if (this.targetNodes == null) {
            this.targetNodes = new LinkedList<>();
        }
        this.targetNodes.add(node);
    }

    public double getNodeCapacity() {
        return this.targetNodes == null ? 0.0 : this.targetNodes.size() * 1.0;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }

        if(!(o instanceof Node)) {
            return false;
        }

        Node node = (Node) o;
        return this.name.equalsIgnoreCase(node.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
