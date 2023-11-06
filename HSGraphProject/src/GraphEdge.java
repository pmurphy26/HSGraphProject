public class GraphEdge {
    GraphVertex vertex; //other node
    GraphVertex node; //this node
    private int weight;

    public GraphEdge(GraphVertex node, GraphVertex vertex) {
        this.vertex = vertex;
        this.node = node;
        weight = 0;
    }

    public GraphEdge(GraphVertex node, GraphVertex vertex, int weight) {
        this.vertex = vertex;
        this.node = node;
        this.weight = weight;
    }

    public void setWeight(int n) {
        weight = n;
        vertex.getEdge(node.getData()).setWeight(n, false);
    }

    public void setWeight(int n, boolean r) {
        weight = n;
    }

    public GraphVertex getVertex() {
        return vertex;
    }

    public GraphVertex getNode() {
        return node;
    }

    public int getWeight() {
        return weight;
    }

    public void printEdge() {
        System.out.println("(" + node.getData() + ", " + vertex.getData()
                + ", " + weight + ")");
    }

    public String toString() {
        return ("{" + vertex.getData()+ ", " + weight + "}");
    }

    public boolean contains(GraphEdge e) {
        return (e.getVertex() == this.getVertex()) && (e.getNode() == this.getNode());
    }
}


