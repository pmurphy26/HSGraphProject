import java.util.LinkedList;

public class GraphVertex {

    private int data;
    private int color = 0;//0 = unvisited, 1 = on, 2 = visited
    private LinkedList<GraphEdge> edges = new LinkedList<>();

    public GraphVertex(int data) {
        this.data = data;
        color = 0;
    }

    public GraphVertex() {
        color = 0;
    }

    public int getData() {return data;}

    public int getColor() {return color;}

    public void setColor(int color) {
        if (color <= 2 && color >= 0)
            this.color = color;
    }

    public LinkedList<GraphEdge> getEdges() {return edges;}

    public GraphEdge getEdge(int searchedNum) {
        for (GraphEdge g : edges) {
            if (g.getVertex().getData() == searchedNum) {
                return g;
            }
        }
        return null;
    }

    public void addEdge(GraphVertex newEdge) {
        edges.add(new GraphEdge(this, newEdge));
    }

    public GraphVertex[] addEdges(int[] nums) {
        GraphVertex[] addedNums = new GraphVertex[nums.length];

        for (int i = 0; i < nums.length; i++) {
            GraphVertex node = createGraphNode(nums[i]);
            addEdge(node);
            addedNums[i] = node;
            node.addEdge(this);
        }

        return addedNums;
    }

    public GraphVertex[] addEdges(GraphVertex[] nodes) {
        GraphVertex[] addedNums = new GraphVertex[nodes.length];

        for (int i = 0; i < nodes.length; i++) {
            addEdge(nodes[i]);
            addedNums[i] = nodes[i];
            nodes[i].addEdge(this);
        }

        return addedNums;
    }

    public boolean removeEdge(GraphVertex edge) {
        edge.removeEdge(this);
        return edges.remove(edge);
    }

    public GraphVertex createGraphNode(int n) {
        return new GraphVertex(n);
    }
}
