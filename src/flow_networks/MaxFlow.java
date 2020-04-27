package flow_networks;

import java.util.*;


public class MaxFlow {
    MaxFlow_Graph maxFlowGraph;
    private Map<Node, Node> map, revMap;

    MaxFlow(MaxFlow_Graph g) {
        maxFlowGraph = g;
        map = new HashMap<>();
        revMap = new HashMap<>();
    }

    public void initZeroFlow() {
        if (maxFlowGraph == null || maxFlowGraph.source == null || maxFlowGraph.destination == null)
            return;
        helper(maxFlowGraph.source, new HashSet<>());
    }

    private void helper(Node node, Set<Node> seen) {
        if (!seen.add(node)) return;
        for (Map.Entry<Node, Edge> entry : node.edges.entrySet()) {
            Edge e = entry.getValue();
            e.flow = 0;
            helper(e.dest, seen);
        }
    }

    private void ensureResidualNodesExist() {
        if (map != null && !map.isEmpty()) return;

        if (map == null) {
            map = new HashMap<>();
            revMap = new HashMap<>();
        }
        Node cs = copy(maxFlowGraph.source, new HashSet<>());
        map.put(maxFlowGraph.source, cs);
        for (Map.Entry<Node, Node> e : map.entrySet()) {
            revMap.put(e.getValue(), e.getKey());
        }
    }

    private Node copy(Node node, Set<Node> seen) {
        if (!seen.add(node)) return null;
        if (map.containsKey(node)) return map.get(node);
        Node n = new Node(node.value);
        for (Map.Entry<Node, Edge> entry : node.edges.entrySet()) {
            Edge e = entry.getValue();
            Node et = copy(e.dest, seen);
            if (et != null)
                map.put(e.dest, et);
        }
        map.put(node, n);
        return n;
    }

    private void createResidual() {
        //getNode mapping
        ensureResidualNodesExist();
        //clear alreadyExisting Edges
        for (Map.Entry<Node, Node> e : map.entrySet()) {
            e.getValue().edges.clear();
        }

        //add Edges
        for (Map.Entry<Node, Node> e : map.entrySet()) {
            for (Map.Entry<Node, Edge> entry : e.getKey().edges.entrySet()) {
                Edge edge = entry.getValue();
                int residual = edge.capacity - edge.flow;
                if (residual > 0) {
                    e.getValue().addEdge(map.get(edge.dest), residual);
                }
                if (edge.flow > 0) {
                    map.get(edge.dest).addEdge(e.getValue(), edge.flow);
                }
            }
        }
    }

    int getFlow() {
        int sum = 0;
        for (Map.Entry<Node, Edge> entry : maxFlowGraph.source.edges.entrySet()) {
            Edge e = entry.getValue();
            sum += e.flow;
        }
        return sum;
    }

    private List<Edge> getResidualPath() {
        List<Edge> residualPath = new ArrayList<>();
        Set<Node> seen = new HashSet<>();
        Node start = map.get(maxFlowGraph.source);
        seen.add(start);
        for (Map.Entry<Node, Edge> entry : start.edges.entrySet()) {
            Edge e = entry.getValue();
            if (canReachT(e.dest, residualPath, seen)) {
                residualPath.add(e);
                break;
            }
        }
        Collections.reverse(residualPath);
        return residualPath;
    }

    private List<Edge> getResidualPathOptimized(){
        List<Edge> residualPath = null;
        Queue<Pair<Node, List<Edge>>> q = new ArrayDeque<>();
        q.offer(new Pair<>(map.get(maxFlowGraph.source), new ArrayList<>()));
        Node destination = map.get(maxFlowGraph.destination);

        while(!q.isEmpty()){
            Pair<Node, List<Edge>> currentPair = q.poll();
            if(destination.equals(currentPair.first)){
                residualPath =  currentPair.second;
                break;
            }
            for(Map.Entry<Node, Edge> entry: currentPair.first.edges.entrySet()){
                List<Edge> updated = new ArrayList<>(currentPair.second);
                updated.add(entry.getValue());
                q.offer(new Pair<>(entry.getValue().dest, updated));
            }
        }
        return residualPath;
    }

    private boolean canReachT(Node node, List<Edge> edges, Set<Node> seen) {
        if (!seen.add(node)) return false;
        if (node.equals(map.get(maxFlowGraph.destination))) return true;
        for (Map.Entry<Node, Edge> entry : node.edges.entrySet()) {
            Edge edge = entry.getValue();
            if (canReachT(edge.dest, edges, seen)) {
                edges.add(edge);
                return true;
            }
        }
        return false;
    }

    private int getCf(List<Edge> edges) {
        int min = Integer.MAX_VALUE;
        for (Edge edge : edges) {
            min = Math.min(min, edge.capacity);
        }
        return min;
    }


    private void updatePath(int cf, List<Edge> residualPath) {
        for(Edge edge : residualPath){
            Node from = edge.from, to = edge.dest;
            Node originalFrom = revMap.get(from), orignalTo = revMap.get(to);
            if(originalFrom.edges.containsKey(orignalTo)){
                originalFrom.edges.get(orignalTo).flow+=cf;
            }
            else if(orignalTo.edges.containsKey(originalFrom)){
                orignalTo.edges.get(originalFrom).flow -= cf;
            }
        }
    }

    private void printGraph(Node n, Set<Node> seen, boolean residual) {
        if (!seen.add(n)) return;
        String values = (residual) ? "%d" : "%d:%d ";
        String format = "%s - " + values + " -> %s\n";
        for (Map.Entry<Node, Edge> entry : n.edges.entrySet()) {
            Edge e = entry.getValue();
            if (residual) {
                System.out.printf(format, e.from.value, e.capacity, e.dest.value);
            } else {
                System.out.printf(format, e.from.value, e.flow, e.capacity, e.dest.value);
            }
            printGraph(e.dest, seen, residual);
        }
    }

    int getMaxFlow() {
        initZeroFlow();
        createResidual();
        List<Edge> residualPath = getResidualPathOptimized();
        while ((residualPath!=null && !residualPath.isEmpty())){
            int cf = getCf(residualPath);
            updatePath(cf, residualPath);
            createResidual();
            residualPath = getResidualPath();
        }
        return getFlow();
    }
}
