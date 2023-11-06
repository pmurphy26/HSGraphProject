public class GraphSearchVertex {
    private GraphVertex vertex;
    private GraphVertex previous;
    private int distance;

    public GraphSearchVertex(GraphVertex vertex, GraphVertex previous, int distance) {
        this.vertex = vertex;
        this.previous = previous;
        this.distance = distance;
    }

    public GraphVertex getVertex() {
        return vertex;
    }

    public GraphVertex getPrevious() {
        return previous;
    }

    public int getDistance() {
        return distance;
    }

    public void setPrevious(GraphVertex previous) {
        this.previous = previous;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}


