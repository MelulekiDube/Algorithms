package flow_networks;

import java.util.Objects;

class Edge {
    int flow, capacity;
    Node from, dest;

    Edge(int c, Node f, Node to) {
        flow = Integer.MAX_VALUE;
        capacity = c;
        dest = to;
        from = f;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Edge))
            return false;
        Edge edge = (Edge) obj;
        return edge.flow == flow && edge.capacity == capacity && from.equals(edge.from) && dest.equals(edge.dest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flow, capacity, from, dest);
    }
}
