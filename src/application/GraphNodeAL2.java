package application;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class GraphNodeAL2<T> {
    public T data;
    public List<GraphLinkAL> adjList = new ArrayList<>(); // Adjacency list now contains link objects = key!
    // Could use any concrete List implementation
    public int nodeValue = Integer.MAX_VALUE;
    private Scanner input;
    private String name = "";
    @FXML
    private List<GraphNodeAL2> nodeList = new ArrayList<GraphNodeAL2>();
    @FXML
    ComboBox locationStartCb;
    @FXML
    ComboBox locationDestCb;
    Scanner scanner = new Scanner(System.in);
    @FXML
    TextArea detailsArea;

    public GraphNodeAL2() {
    }

    public GraphNodeAL2(T data) {
        this.data = data;
    }

    public void connectToNodeDirected(GraphNodeAL2<T> destNode, String road, int distance) {
        adjList.add(new GraphLinkAL(destNode, road, distance)); // Add new link object to source adjacency list
    }

    public void connectToNodeUndirected(GraphNodeAL2<T> destNode, String road, int distance) {
        adjList.add(new GraphLinkAL(destNode, road, distance)); // Add new link object to source adjacency list
        destNode.adjList.add(new GraphLinkAL(this, road, distance)); // Add new link object to destination adjacency list
    }


    //

    public static <T> DistancedRoute findShortestRouteDijkstra(GraphNodeAL2<?> startNode, T lookingfor) {
        DistancedRoute cp = new DistancedRoute(); // Create result object for shortest route
        List<GraphNodeAL2<?>> encountered = new ArrayList<>(), unencountered = new ArrayList<>(); // Create
        // encountered/unencountered
        // lists
        startNode.nodeValue = 0; // Set the starting node value to zero
        unencountered.add(startNode); // Add the start node as the only value in the unencountered list to start
        GraphNodeAL2<?> currentNode;
        do { // Loop until unencountered list is empty
            currentNode = unencountered.remove(0); // Get the first unencountered node (sorted list, so will have lowest
            // value)
            encountered.add(currentNode); // Record current node in encountered list
            if (currentNode.data.equals(lookingfor)) { // Found goal - assemble route list back to start and return it
                cp.routeList.add(currentNode); // Add the current (goal) node to the result list (only element)
                cp.routeDistance = currentNode.nodeValue; // The total shortest route distance is the node value of the
                // current/goal node
                while (currentNode != startNode) { // While we're not back to the start node...
                    boolean foundPrevRouteNode = false; // Use a flag to identify when the previous route node is
                    // identified
                    for (GraphNodeAL2<?> n : encountered) { // For each node in the encountered list...
                        for (GraphLinkAL e : n.adjList) // For each edge from that node...
                            if (e.destNode == currentNode && currentNode.nodeValue - e.distance == n.nodeValue) { // If
                                // that edge links to the current node and the difference in node values is the distance of the edge -> found route node!
                                cp.routeList.add(0, n); // Add the identified route node to the front of the result list
                                currentNode = n; // Move the currentNode reference back to the identified route node
                                foundPrevRouteNode = true; // Set the flag to break the outer loop
                                break; // We've found the correct previous route node and moved the currentNode
                                // reference
                                // back to it so break the inner loop
                            }
                        if (foundPrevRouteNode)
                            break; // We've identified the previous route node, so break the inner loop to continue
                    }

                }
                // Reset the node values for all nodes to (effectively) infinity so we can
                // search again (leave no footprint!)
                for (GraphNodeAL2<?> n : encountered)
                    n.nodeValue = Integer.MAX_VALUE;
                for (GraphNodeAL2<?> n : unencountered)
                    n.nodeValue = Integer.MAX_VALUE;
                return cp; // The distanceed (shortest) route has been assembled, so return it!
            }
            // We're not at the goal node yet, so...
            for (GraphLinkAL e : currentNode.adjList) // For each edge/link from the current node...
                if (!encountered.contains(e.destNode)) { // If the node it leads to has not yet been encountered (i.e.
                    // processed)
                    e.destNode.nodeValue = Integer.min(e.destNode.nodeValue, currentNode.nodeValue + e.distance); // Update
                    // the node value at the end of the edge to the minimum of its current value or the total of the current node's value plus the distance of the edge
                    unencountered.add(e.destNode);
                }
            Collections.sort(unencountered, (n1, n2) -> n1.nodeValue - n2.nodeValue); // Sort in ascending node value
            // order
        } while (!unencountered.isEmpty());
        return null; // No route found, so return null
    }

    public void start() {
        try {
            loadMap();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Regular recursive depth-first graph traversal with total distance
    public void traverseGraphDepthFirstShowingTotalDistance(GraphNodeAL2<?> from,
                                                            List<GraphNodeAL2<?>> encountered, int totalDistance) {
        detailsArea.appendText("\n" + from.data + ": " + totalDistance + "km");
        if (encountered == null)
            encountered = new ArrayList<>(); // First node so create new (empty) encountered list
        encountered.add(from);
        // Could sort adjacency list here based on distance Ã¯Â¿Â½ see next slide for more
        // info!
        Collections.sort(from.adjList, (a, b) -> a.distance - b.distance);
        for (GraphLinkAL adjLink : from.adjList)
            if (!encountered.contains(adjLink.destNode))
                traverseGraphDepthFirstShowingTotalDistance(adjLink.destNode, encountered,
                        totalDistance + adjLink.distance);
    }

    //Recursive depth-first search of graph (all paths identified returned)
    public static <T> List<List<GraphNodeAL2<?>>> findAllPathsDepthFirst(GraphNodeAL2<?> from, List<GraphNodeAL2<?>> encountered, T lookingfor){
        List<List<GraphNodeAL2<?>>> result=null, temp2;
        if(from.data.equals(lookingfor)) { //Found it
            List<GraphNodeAL2<?>> temp=new ArrayList<>(); //Create new single solution path list
            temp.add(from); //Add current node to the new single path list
            result=new ArrayList<>(); //Create new "list of lists" to store path permutations
            result.add(temp); //Add the new single path list to the path permutations list
            return result; //Return the path permutations list
        }
        if(encountered==null) encountered=new ArrayList<>(); //First node so create new (empty) encountered list
        encountered.add(from); //Add current node to encountered list
        for(GraphNodeAL2<?> adjNode : from.adjList){
            if(!encountered.contains(adjNode)) {
                temp2=findAllPathsDepthFirst(adjNode,new ArrayList<>(encountered),lookingfor); //Use clone of encountered list
//for recursive call!
                if(temp2!=null) { //Result of the recursive call contains one or more paths to the solution node
                    for(List<GraphNodeAL2<?>> x : temp2) //For each partial path list returned
                        x.add(0,from); //Add the current node to the front of each path list
                    if(result==null) result=temp2; //If this is the first set of solution paths found use it as the result
                    else result.addAll(temp2); //Otherwise append them to the previously found paths
                }
            }
        }
        return result;
    }


    public static void main(String[] args) {
        GraphNodeAL2<String> a1 = new GraphNodeAL2<>("Wexford");
        a1.connectToNodeUndirected(a1, "esfsw", 53);
    }


    public void shortestRoute() {

            detailsArea.clear();
            detailsArea.appendText("hiiiiiiiiiiiiiiiii");
            detailsArea.appendText(("\nThe shortest route from " + locationStartCb.getValue() + " to " + locationDestCb.getValue() + "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"));
            System.out.println();
        System.out.println("hiiii");
            DistancedRoute cpa = findShortestRouteDijkstra(nodeList.get(locationStartCb.getSelectionModel().getSelectedIndex()), locationDestCb.getValue());
        System.out.println((locationStartCb.getSelectionModel().getSelectedIndex()));
        System.out.println(locationDestCb.getValue());
            int i = 0;

            for (GraphNodeAL2<?> n : cpa.routeList) {
                detailsArea.appendText(i + ": " + n.data + "\n");
            }
            detailsArea.appendText("\nThe total route distance is: " + cpa.routeDistance + "km");


    }

    public void allRoutes() {
        try {
            detailsArea.setText("\nAll shortest routes from " + locationStartCb.getValue() + " to other locations"+"\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            traverseGraphDepthFirstShowingTotalDistance((nodeList.get(locationStartCb.getSelectionModel().getSelectedIndex())), null, 0);
        } catch (Exception e) {
            detailsArea.setText("Please enter a start and end location.");

        }
    }

    public void multibleRoutes() {
        try {
            System.out.println("All solution paths from Coconut (object f)");
            System.out.println("to node containing Plum");
            System.out.println("------------------------------------------");
            List<List<GraphNodeAL2<?>>> allPaths=findAllPathsDepthFirst((nodeList.get(locationStartCb.getSelectionModel().getSelectedIndex())),null,locationDestCb.getValue());
            int pCount=1;
            for(List<GraphNodeAL2<?>> p : allPaths) {
                System.out.println("\nPath "+(pCount++)+"\n--------");
                for(GraphNodeAL2<?> n : p)
                    System.out.println(n.data);
            }
            System.out.println("testmeeeeeee");


        } catch (Exception e) {
            e.printStackTrace();
            detailsArea.setText("Please enter a start and end location.");
        }
    }


    int test = 0;

    public void loadMap () throws IOException {

        String mapdata = "src/application/mapdata.csv";
        File mapfile = new File(mapdata);
        System.out.println("\n");
        System.out.println("CSV MAP DATA");
        System.out.println("--------------------------------");

        //use scanner to read file
        Scanner mapread = new Scanner(mapfile);
        //while there is still next item in the file
        String line;

        //create a string called data that = value from file

        while (mapread.hasNext()) {
            String data = mapread.next();
            //create string array of data separated at comma
            String[] parts = data.split(",");
            //set name to the string at the 0 column

            for (int i = 0; i < parts.length; i++) {
                GraphNodeAL2<String> test = new GraphNodeAL2<>(parts[i]);

                // test.ID = id;
                //id++;
                nodeList.add(test);
            }
            //list.add(new GraphNodeAL2(name, road, distance));
        }

        String linkdata = "src/application/links.csv";
        File linkfile = new File(linkdata);
        Scanner linkread = new Scanner(linkfile);

        while (linkread.hasNext()) {
            String data = linkread.next();
            //create string array of data separated at comma
            String[] parts = data.split(",");
            //set name to the string at the 0 column

            for (int i = 0; i < parts.length; i++) {
                int firstLocation = Integer.parseInt(parts[0]);
                int secondLocation = Integer.parseInt(parts[1]);
                String road = parts[2];
                int distance = Integer.parseInt(parts[3]);
 /*               GraphNodeAL2<String> test = new GraphNodeAL2<>(parts[i]);
                nodeLis.add(test);*/
                nodeList.get(firstLocation).connectToNodeUndirected(nodeList.get(secondLocation), road, distance);
            }
        }
        locationStartCb.getItems().clear();
        locationDestCb.getItems().clear();
        System.out.println();
        System.out.println("Map data loaded...");
        mapread.close();

        for (int i = 0; i < nodeList.size(); i++) {
            locationStartCb.getItems().add(nodeList.get(i).data);
            locationDestCb.getItems().add(nodeList.get(i).data);
        }
    }

    public void exitMenu () {
        System.exit(0);
    }

}