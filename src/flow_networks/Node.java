package flow_networks;

import java.util.*;

class Node {
    char value;
    Map<Node, Edge> edges;

    Node(char c) {
        this.value = c;
        edges = new HashMap<>();
    }

    void addEdge(Node to, int c) {
        edges.put(to, new Edge(c, this, to));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node)) return false;

        Node node = (Node) obj;
        return value == node.value;
    }

    public int hashCode() {
        return Objects.hash(value);
    }
}
