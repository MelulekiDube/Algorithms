package flow_networks;

import java.util.ArrayList;
import java.util.List;

public class Algorithms {

    private static MaxFlow_Graph createGraph(){
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node('s'));
        for(char c='a'; c <= 'd'; ++c) {
            nodes.add(new Node(c));
        }
        nodes.add(new Node('t'));
        nodes.get(0).addEdge(nodes.get(1), 11);
        nodes.get(0).addEdge(nodes.get(2), 12);
        nodes.get(1).addEdge(nodes.get(2), 10);
        nodes.get(1).addEdge(nodes.get(3), 12);
        nodes.get(2).addEdge(nodes.get(1), 4);
        nodes.get(2).addEdge(nodes.get(4), 14);
        nodes.get(3).addEdge(nodes.get(2), 9);
        nodes.get(3).addEdge(nodes.get(5), 20);
        nodes.get(4).addEdge(nodes.get(3), 7);
        nodes.get(4).addEdge(nodes.get(5), 4);
        MaxFlow_Graph maxFlowGraph = new MaxFlow_Graph(nodes.get(0), nodes.get(5));
        return maxFlowGraph;
    }

    public static void main(String[] args) {
        MaxFlow maxFlow = new MaxFlow(createGraph());
        System.out.println(maxFlow.getMaxFlow());
    }
}
