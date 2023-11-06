import java.util.LinkedList;
import java.util.Queue;

public class GraphObject {
    public LinkedList<GraphVertex> graphVertices = new LinkedList<>();
    public LinkedList<GraphEdge> graphEdges = new LinkedList<>();

    public void printGraph() {
        for (GraphVertex v : graphVertices) {

            String nodeEdgesString = "";

            for (GraphEdge e : v.getEdges()) {
                nodeEdgesString += ", " + e.toString();
            }
            System.out.println("(" + v.getData() + ")" + nodeEdgesString);
        }
    }

    public String toString() {
        String returnString = "";

        for (GraphVertex v : graphVertices) {

            String nodeEdgesString = "";

            for (GraphEdge e : v.getEdges()) {
                nodeEdgesString += ", " + e.toString();
            }
            returnString += ("(" + v.getData() + ")" + nodeEdgesString + "\n");
        }

        return  returnString;
    }

    public void printGraphBFS(GraphVertex sourceNode) {
        Queue<GraphVertex> queue = new LinkedList<>();
        queue.add(sourceNode);

        while (!queue.isEmpty()) {
            GraphVertex node = queue.remove();
            Queue<GraphEdge> nodeEdges = new LinkedList<>();
            nodeEdges.addAll(node.getEdges());
            String nodeEdgesString = "";
            if (node.getColor() != 2) {
                while (!nodeEdges.isEmpty()) {
                    GraphVertex removedNode = nodeEdges.remove().getVertex();
                    if (removedNode.getColor() != 2) {
                        queue.add(removedNode);
                    }
                    nodeEdgesString += ", " + removedNode.getData();

                }
                System.out.println("(" + node.getData() + ")" + nodeEdgesString);
            }
            node.setColor(2);
        }
        resetColors();
    }

    /*
    public void printGraphDFS(GraphVertex beginNode) {
        GraphVertex beginningNode = beginNode;
        Queue<GraphEdge> nodeEdges = beginningNode.getEdges();
        beginningNode.setColor(2);
        System.out.println("(" + beginningNode.getData() + ")");
        while (!nodeEdges.isEmpty()) {
            GraphVertex nextNode = nodeEdges.remove();
            if (nextNode.getColor() != 2) {
                printGraphDFS(nextNode);
            }
        }
    }
    */

    public LinkedList<GraphEdge> DFS(GraphVertex sourceNode, int searchedNode) {
        LinkedList<GraphEdge> path = new LinkedList<>();
        GraphEdge[] nodeEdges = sourceNode.getEdges().toArray(new GraphEdge[sourceNode.getEdges().size()]);

        if (sourceNode.getData() == searchedNode) { //is node is found
            path.add(null);
            return path;
        } else {
            sourceNode.setColor(2);

            for (int i = 0; i < nodeEdges.length; i++) {
                GraphEdge edge = nodeEdges[i];
                GraphVertex vertex = edge.getVertex();
                if (vertex.getColor() != 2) { //if vertex hasn't been visited
                    LinkedList<GraphEdge> vertexPath = DFS(vertex, searchedNode);
                    if (!vertexPath.isEmpty()) { //seeing if path exists
                        if (vertexPath.getFirst() == null) {
                            vertexPath.addFirst(sourceNode.getEdge(searchedNode));
                            vertexPath.removeLast();
                        } else {
                            vertexPath.addFirst(sourceNode.getEdge(vertexPath.getFirst().getNode().getData()));
                        }
                        path.addAll(vertexPath);
                        return path;
                    }
                }
            }
        }
        return path;
    }

    public LinkedList<GraphVertex> breadthFirstSearch(GraphVertex sourceNode, int searchedNode) {
        Queue<GraphVertex> queue = new LinkedList<>();
        LinkedList<GraphVertex> visitedNodes = new LinkedList<>();
        GraphVertex foundNode = new GraphVertex();
        foundNode = null;


        queue.add(sourceNode);
        while (!queue.isEmpty()) {
            GraphVertex node = queue.remove();
            Queue<GraphEdge> nodeEdges = new LinkedList<>();
            nodeEdges.addAll(node.getEdges());

            if (node.getColor() != 2) {
                while (!nodeEdges.isEmpty()) {
                    GraphVertex removedNode = nodeEdges.remove().getVertex();
                    if (removedNode.getColor() != 2) {
                        queue.add(removedNode);
                    }
                }
            }
            node.setColor(2);
            visitedNodes.add(node);

            if (node.getData() == searchedNode) { //checking if found
                foundNode = node;
                queue.clear();
            }
        }
        resetColors();

        //taking visited nodes and finding  shortest path
        if (foundNode == null) {
            return null;
        }
        else {
            LinkedList<GraphVertex> path = new LinkedList<>();
            path.add(foundNode);

            while (visitedNodes != null) {
                Queue<GraphVertex> nodesPath = new LinkedList<>();
                nodesPath.addAll(visitedNodes);
                LinkedList<GraphVertex> newList = findPathBack(nodesPath, path.getFirst().getEdges());
                visitedNodes = newList;
                if (newList != null) {
                    path.addFirst(newList.getLast());
                }
            }
            return path;
        }
    }

    public LinkedList<GraphVertex> findPathBack(Queue<GraphVertex> visitedNodes,
                                                LinkedList<GraphEdge> searchedNode) {
        LinkedList<GraphVertex> path = new LinkedList<>();

        while (!visitedNodes.isEmpty()) {
            Queue<GraphEdge> searchedNodes = new LinkedList<>();
            searchedNodes.addAll(searchedNode);
            GraphVertex node = visitedNodes.remove();
            path.add(node);
            while (!searchedNodes.isEmpty()) {
                GraphVertex search = searchedNodes.remove().getVertex();
                if (search == node) {
                    return path;
                }
            }
        }

        return null;
    }

    public GraphVertex get(int searchedNode) {
        for (GraphVertex v : graphVertices) {
            if (v.getData() == searchedNode)
                return v;
        }
        return null;
    }

    public boolean contains(int seachedNode) {
        if (get(seachedNode) != null) {
            return true;
        }
        return false;
    }

    public void resetColors() {
        for (GraphVertex v : graphVertices) {
            v.setColor(0);
        }
    }

    public LinkedList<GraphEdge> dijkstraSearch(GraphVertex sourceNode, int searchedInt) {
        LinkedList<GraphSearchVertex> gsv = initializeSearch(sourceNode);

        LinkedList<GraphSearchVertex> queue = new LinkedList<>();
        queue.addAll(gsv);

        while (!queue.isEmpty()) {
            sortNodesDistance(queue);
            GraphSearchVertex u = queue.remove();

            //s = s u {u}
            u.getVertex().setColor(2);//?? not sure if this is right

            for (GraphEdge e : u.getVertex().getEdges()) {
                GraphVertex vtx = e.getVertex();
                vtx.setColor(1);
                GraphSearchVertex v;
                for (GraphSearchVertex gs : gsv) {
                    if (gs.getVertex().getData() == vtx.getData()) {
                        v = gs;
                        relax(u, v);
                    }
                }
            }
        }

        LinkedList<GraphEdge> returnPath = new LinkedList<>();
        GraphSearchVertex pathVertex = getSearchVertex(gsv, searchedInt);

        if (getSearchVertex(gsv, searchedInt) == null) return returnPath;

        returnLoop:
        while (pathVertex.getPrevious() != null) {
            for (GraphSearchVertex gv : gsv) {
                if (gv.getVertex().getData() == pathVertex.getVertex().getData()) {
                    if (pathVertex.getPrevious() == null) {
                        break returnLoop;
                    }
                    returnPath.addFirst(pathVertex.getVertex().getEdge(pathVertex.getPrevious().getData()));
                    pathVertex = getSearchVertex(gsv, pathVertex.getPrevious().getData());
                }
            }
        }

        return returnPath;

        //use queue
        //colors
        //if black don't add
        //if grey update best path length
        //if white add to queue
        //grey = in queue, black = visited
        //cut when length is longer than already found length to searched node
    }

    public LinkedList<GraphEdge> dijkstraSearchRedo(GraphVertex sourceNode, int searchedInt) {
        LinkedList<GraphSearchVertex> queue = new LinkedList<>();
        LinkedList<GraphSearchVertex> visitedVertexes = new LinkedList<>();
        queue.add(new GraphSearchVertex(sourceNode, null, 0));

        GraphSearchVertex searchedVtx = null;

        while (!queue.isEmpty()) {
            sortNodesDistance(queue);
            GraphSearchVertex u = queue.remove();

            //s = s u {u}
            u.getVertex().setColor(2);
            visitedVertexes.addLast(u);

            if (u.getVertex().getData() == searchedInt)
                searchedVtx = u;

            if (searchedVtx == null ||
                    (searchedVtx != null && searchedVtx.getDistance() > u.getDistance())) {

                for (GraphEdge e : u.getVertex().getEdges()) {
                    GraphVertex vtx = e.getVertex();

                    if (vtx.getColor() != 2) { //if node hasn't been visited yet
                        GraphSearchVertex v = new GraphSearchVertex(vtx, null, -1);
                        if (vtx.getColor() != 1) { //if node hasn't been added to queue
                            queue.add(v);
                            vtx.setColor(1);
                        }

                        relax(u, v);
                    }
                }
            }
        }


        LinkedList<GraphEdge> returnPath = new LinkedList<>();
        GraphSearchVertex pathVertex = getSearchVertex(visitedVertexes, searchedInt);

        if (pathVertex == null) return returnPath;

        returnLoop:
        while (pathVertex.getPrevious() != null) {
            for (GraphSearchVertex gv : visitedVertexes) {
                if (gv.getVertex().getData() == pathVertex.getVertex().getData()) {
                    if (pathVertex.getPrevious() == null) {
                        break returnLoop;
                    }
                    returnPath.addFirst(pathVertex.getVertex().getEdge(pathVertex.getPrevious().getData()));
                    pathVertex = getSearchVertex(visitedVertexes, pathVertex.getPrevious().getData());
                }
            }
        }

        return returnPath;
        //use queue
        //colors
        //if black don't add
        //if grey update best path length
        //if white add to queue
        //grey = in queue, black = visited
        //cut when length is longer than already found length to searched node
    }

    public LinkedList<GraphEdge> BelmanFordSearch(GraphVertex sourceNode, int searchedInt) {
        addEdgesToGraph();
        LinkedList<GraphEdge> path = new LinkedList<>();

        LinkedList<GraphSearchVertex> gsv = initializeSearchBelmanFord(sourceNode);

        //checking if graph contains searched Number
        if (getSearchVertex(gsv, searchedInt) == null) return path;

        //looping through edges
        for (int i = 1; i < graphEdges.size() - 1; i++) {
            for (int j = 0; j < graphEdges.size(); j++) {
                GraphSearchVertex u = getSearchVertex(gsv, graphEdges.get(j).getNode().getData());
                GraphSearchVertex v = getSearchVertex(gsv, graphEdges.get(j).getVertex().getData());

                if (u.getVertex().getData() == sourceNode.getData()) {
                    relax(u, v);
                } else {
                    if (u.getPrevious() == null) {
                        relax(u, v);
                    } else if (u.getPrevious().getData() != graphEdges.get(j).getVertex().getData()) {
                        relax(u, v);
                    }
                }
            }

        }

        //checking for infinite path
        for (int j = 0; j < graphEdges.size(); j++) {
            GraphSearchVertex u = getSearchVertex(gsv, graphEdges.get(j).getNode().getData());
            GraphSearchVertex v = getSearchVertex(gsv, graphEdges.get(j).getVertex().getData());

            if (u.getVertex().getData() != sourceNode.getData() && u.getPrevious() != null &&
                    u.getPrevious().getData() != graphEdges.get(j).getVertex().getData()
                    && v.getDistance() > u.getDistance() + graphEdges.get(j).getWeight()) {
                return path;
            }
        }

        GraphSearchVertex pathVertex = getSearchVertex(gsv, searchedInt);
        GraphSearchVertex sourceVertex = getSearchVertex(gsv, sourceNode.getData());
        sourceVertex.setPrevious(null);

        returnLoop:
        while (pathVertex.getPrevious() != null) {
            for (GraphSearchVertex gv : gsv) {
                if (gv.getVertex().getData() == pathVertex.getVertex().getData()) {
                    if (pathVertex.getPrevious() == null) {
                        break returnLoop;
                    }
                    path.addFirst(pathVertex.getVertex().getEdge(pathVertex.getPrevious().getData()));
                    pathVertex = getSearchVertex(gsv, pathVertex.getPrevious().getData());
                }
            }
        }

        return path;
    }

    private GraphSearchVertex getSearchVertex(LinkedList<GraphSearchVertex> list, int i) {
        for (GraphSearchVertex gsv : list) {
            if (gsv.getVertex().getData() == i) {
                return gsv;
            }
        }

        return null;
    }

    public LinkedList<GraphSearchVertex> initializeSearch(GraphVertex sourceVertex) {
        LinkedList<GraphSearchVertex> sv = new LinkedList<>();

        for (GraphVertex g : graphVertices) {
            sv.add(new GraphSearchVertex(g, null, -1));
        }

        getSearchVertex(sv, sourceVertex.getData()).setDistance(0);

//        for (GraphSearchVertex gsv : sv) {
//            if (gsv.getVertex().getData() == sourceVertex.getData()) {
//                gsv.setDistance(0);
//            }
//        }

        return sv;
    }

    public LinkedList<GraphSearchVertex> initializeSearchBelmanFord(GraphVertex sourceVertex) {
        LinkedList<GraphSearchVertex> sv = new LinkedList<>();

        for (GraphVertex g : graphVertices) {
            sv.add(new GraphSearchVertex(g, null, 100000));
        }

        getSearchVertex(sv, sourceVertex.getData()).setDistance(0);

//        for (GraphSearchVertex gsv : sv) {
//            if (gsv.getVertex().getData() == sourceVertex.getData()) {
//                gsv.setDistance(0);
//            }
//        }

        return sv;
    }

    public void relax(GraphSearchVertex u, GraphSearchVertex v) {
        GraphEdge edge = v.getVertex().getEdge(u.getVertex().getData());
        if (v.getDistance() == -1 || v.getDistance() > u.getDistance() + edge.getWeight()) {
            v.setDistance(u.getDistance() + edge.getWeight());
            v.setPrevious(u.getVertex());
        }
    }

    public void addToGraph(GraphVertex[] v) {
        for (int i = 0; i < v.length; i++) {
            if (!graphVertices.contains(v[i]))
                graphVertices.add(v[i]);
        }
    }

    public void addToGraph(GraphVertex v) {
        if (!graphVertices.contains(v))
            graphVertices.add(v);
    }

    public void addEdgesToGraph() {
        for (GraphVertex v : graphVertices) {
            for (GraphEdge e : v.getEdges()) {
                if (!graphEdges.contains(e)) {
                    graphEdges.add(e);
                }
            }
        }
    }

    public void sortNodesDistance(LinkedList<GraphSearchVertex> gsv) {
        for (int n = gsv.size(); n > 1; n--) {
            int maxPos = 0;
            searchLoop:
            for (int k = 0; k < n; k++) {
                if (gsv.get(k).getDistance() > gsv.get(maxPos).getDistance()) {
                    maxPos = k;
                }
                if (gsv.get(k).getDistance() == -1) {
                    maxPos = k;
                    break searchLoop;
                }
            }

            GraphSearchVertex a = gsv.get(n-1);
            GraphSearchVertex temp = gsv.get(maxPos);  //---|
            gsv.remove(maxPos);
            gsv.add(maxPos, a);
            gsv.remove(n-1);
            gsv.add(n-1, temp);
        }
    }
}
