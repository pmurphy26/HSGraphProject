import java.io.*;
import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        /*ClassWork classWork = new ClassWork();
        classWork.convertToLinkedList();

        linkedListImplimentation linkedList = new linkedListImplimentation(classWork.linkedObjectArray[0]);
        classWork.addRestOfStuff(linkedList);

        System.out.println(linkedList.indexOf(classWork.linkedObjectArray[1]));

         */

        GraphObject graph = new GraphObject();
        graph = createRandGraph(25);
        setRandEdgeWeights(graph);

//        graph = createRandGraph(20);
//        PrintWriter printWriter = new PrintWriter("C:/Users/murff/Desktop/RandGraph3.txt");
//        printWriter.write(graph.toString() + "*");
//        printWriter.close();

        graph.printGraph();
        System.out.println("solution from the first node to the node containing 5 is: ");
        for (GraphEdge e : graph.BelmanFordSearch(graph.get(0),5)) {
            e.printEdge();
        }
        //System.out.println(graph.dijkstraSearch(graph.get(0), 11));
        //System.out.println(graph.dijkstraSearchRedo(graph.get(0), 11));
        //System.out.println(graph.BelmanFordSearch(graph.get(0),11));

    }

    public static GraphObject createRandGraph(int numOfNodes) {
        GraphObject graph = new GraphObject();

        for (int i = 0; i < numOfNodes; i++) {
            graph.addToGraph(new GraphVertex(i));
        }

        for (int i = 0; i < numOfNodes; i++) {
            int numOfEdges = (int) (Math.random() * 2 + 1.5);

            while (graph.get(i).getEdges().size() < numOfEdges) {
                int randEdge = (int) (Math.random() * numOfNodes);

                if (randEdge != graph.get(i).getData() && graph.get(i).getEdge(randEdge) == null
                        && graph.get(randEdge).getEdges().size() < 4) {
                    graph.get(i).addEdges(new GraphVertex[] {graph.get(randEdge)});
                }
            }
        }



        return graph;
    }

    public static GraphObject createGraphFromFile(String fileName) throws IOException {
        File file = new File (fileName);
        Scanner scan = new Scanner(file);
        scan.useDelimiter("\\n");

        GraphObject graph = new GraphObject();

        while (scan.hasNextLine()) {
            String stringRead = scan.next();

            if (stringRead.contains("(")) {
                int startOfRead;
                int endOfRead;

                //reading vertex
                startOfRead = stringRead.indexOf("(");
                endOfRead = stringRead.indexOf(")");

                String portionRead = stringRead.substring(startOfRead + 1, endOfRead);
                int vertexNum = Integer.valueOf(portionRead);

                if (!graph.contains(vertexNum)) graph.addToGraph(new GraphVertex(vertexNum));

                //reading edges
                while (stringRead.contains("{")) {
                    startOfRead = stringRead.indexOf("{");
                    endOfRead = stringRead.indexOf("}");
                    portionRead = stringRead.substring(startOfRead + 1, endOfRead);
                    stringRead = stringRead.substring(endOfRead + 1);

                    int commaIndex = portionRead.indexOf(",");
                    String attachedVertex = portionRead.substring(0, commaIndex);
                    String edgeWeight = portionRead.substring(commaIndex + 2);

                    int vtx = Integer.valueOf(attachedVertex);
                    int weight = Integer.valueOf(edgeWeight);

                    if (!graph.contains(vtx)) {
                        graph.addToGraph(graph.get(vertexNum).addEdges(new int[]{vtx}));
                    } else if (graph.get(vertexNum).getEdge(vtx) == null) {
                        graph.addToGraph(graph.get(vertexNum).addEdges
                                (new GraphVertex[]{graph.get(vtx)}));
                    }
                    graph.get(vertexNum).getEdge(vtx).setWeight(weight);
                }
            }
        }

        scan.close();
        return graph;
    }

    public static void setRandEdgeWeights(GraphObject graph) {
        graph.addEdgesToGraph();

        for (GraphEdge e : graph.graphEdges) {
            if (e.getWeight() == 0) {
                int randWeight = (int) (Math.random() * 15);
                e.setWeight(randWeight);
                e.getVertex().getEdge(e.getNode().getData()).setWeight(randWeight);
            }
        }
    }

}
