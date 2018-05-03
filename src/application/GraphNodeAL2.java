package application;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.FlowPane;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class GraphNodeAL2<T> {
    public T data;
    @FXML
    private FlowPane container;
    public List<GraphLinkAL> locationStartList = new ArrayList<>();
    private List<ComboBox> comboBoxes;
    public List<GraphLinkAL> adjList = new ArrayList<>(); // Adjacency list now contains link objects = key!
    // Could use any concrete List implementation
    public int nodeValue = Integer.MAX_VALUE;
    private Scanner input;
    private String mapdata = "src/application/mapdata.csv";
    private Scanner mapread;
    private String linkdata = "src/application/links.csv";
    private Scanner linkread;
    private int id;
    private String name = "";
    private ArrayList<GraphNodeAL2<String>> listOfLocations;
    @FXML
    private List<GraphNodeAL2> nodeList = new ArrayList<GraphNodeAL2>();
    @FXML
    private List<GraphNodeAL2> linkList = new ArrayList<GraphNodeAL2>();
    @FXML
    Slider slider;
    @FXML
    ComboBox locationStartCb;
    @FXML
    ComboBox locationDestCb;
    @FXML
    private List<ComboBox> combobox;
    Scanner scanner = new Scanner(System.in);

    public GraphNodeAL2() {

    }


    public GraphNodeAL2(int i, String name) {

    }

    public GraphNodeAL2(T data) {
        this.data = data;
    }

    public GraphNodeAL2(String name, String road2, String distance) {
    }

    public void connectToNodeDirected(GraphNodeAL2<T> destNode, String road, int distance) {
        adjList.add(new GraphLinkAL(destNode, road, distance)); // Add new link object to source adjacency list
    }

    public void connectToNodeUndirected(GraphNodeAL2<T> destNode, String road, int distance) {
        adjList.add(new GraphLinkAL(destNode, road, distance)); // Add new link object to source adjacency list
        destNode.adjList.add(new GraphLinkAL(this, road, distance)); // Add new link object to destination adjacency list
    }

    // Regular recursive depth-first graph traversal with total distance
    public static void traverseGraphDepthFirstShowingTotalDistance(GraphNodeAL2<?> from,
                                                                   List<GraphNodeAL2<?>> encountered, int totalDistance) {
        System.out.println(from.data + " (Total distance of reaching node: " + totalDistance + ")");
        if (encountered == null)
            encountered = new ArrayList<>(); // First node so create new (empty) encountered list
        encountered.add(from);
        // Could sort adjacency list here based on distance Ã¯Â¿Â½ see next slide for more
        // info!
        for (GraphLinkAL adjLink : from.adjList)
            if (!encountered.contains(adjLink.destNode))
                traverseGraphDepthFirstShowingTotalDistance(adjLink.destNode, encountered,
                        totalDistance + adjLink.distance);
    }
    //

    public static <T> DistancedPath findShortestPathDijkstra(GraphNodeAL2<?> startNode, T lookingfor) {
        DistancedPath cp = new DistancedPath(); // Create result object for shortest path
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
            if (currentNode.data.equals(lookingfor)) { // Found goal - assemble path list back to start and return it
                cp.pathList.add(currentNode); // Add the current (goal) node to the result list (only element)
                cp.pathDistance = currentNode.nodeValue; // The total shortest path distance is the node value of the
                // current/goal node
                while (currentNode != startNode) { // While we're not back to the start node...
                    boolean foundPrevPathNode = false; // Use a flag to identify when the previous path node is
                    // identified
                    for (GraphNodeAL2<?> n : encountered) { // For each node in the encountered list...
                        for (GraphLinkAL e : n.adjList) // For each edge from that node...
                            if (e.destNode == currentNode && currentNode.nodeValue - e.distance == n.nodeValue) { // If
                                // that
                                // edge
                                // links
                                // to
                                // the
                                // current node and the difference in node values is the distance of the edge ->
                                // found path node!
                                cp.pathList.add(0, n); // Add the identified path node to the front of the result list
                                currentNode = n; // Move the currentNode reference back to the identified path node
                                foundPrevPathNode = true; // Set the flag to break the outer loop
                                break; // We've found the correct previous path node and moved the currentNode
                                // reference
                                // back to it so break the inner loop
                            }
                        if (foundPrevPathNode)
                            break; // We've identified the previous path node, so break the inner loop to continue
                    }

                }
                // Reset the node values for all nodes to (effectively) infinity so we can
                // search again (leave no footprint!)
                for (GraphNodeAL2<?> n : encountered)
                    n.nodeValue = Integer.MAX_VALUE;
                for (GraphNodeAL2<?> n : unencountered)
                    n.nodeValue = Integer.MAX_VALUE;
                return cp; // The distanceed (shortest) path has been assembled, so return it!
            }
            // We're not at the goal node yet, so...
            for (GraphLinkAL e : currentNode.adjList) // For each edge/link from the current node...
                if (!encountered.contains(e.destNode)) { // If the node it leads to has not yet been encountered (i.e.
                    // processed)
                    e.destNode.nodeValue = Integer.min(e.destNode.nodeValue, currentNode.nodeValue + e.distance); // Update
                    // the
                    // node
                    // value
                    // at
                    // the
                    // end
                    // of the edge to the minimum of its current value or the total of the current
                    // node's value plus the distance of the edge
                    unencountered.add(e.destNode);
                }
            Collections.sort(unencountered, (n1, n2) -> n1.nodeValue - n2.nodeValue); // Sort in ascending node value
            // order
        } while (!unencountered.isEmpty());
        return null; // No path found, so return null
    }

    public void start() {
        try {
            loadMap();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setData() {


    }


    public static void main(String[] args) {
        GraphNodeAL2<String> a1 = new GraphNodeAL2<>("Wexford");
        GraphNodeAL2<String> a2 = new GraphNodeAL2<>("Wicklow");
        GraphNodeAL2<String> a3 = new GraphNodeAL2<>("Carlow");
        GraphNodeAL2<String> a4 = new GraphNodeAL2<>("Kilkenny");
        GraphNodeAL2<String> a5 = new GraphNodeAL2<>("Dublin");
        GraphNodeAL2<String> a6 = new GraphNodeAL2<>("Kildare");

        a1.connectToNodeUndirected(a2, "esfsw", 53);
        a1.connectToNodeUndirected(a3, "wwa", 3);
        a1.connectToNodeUndirected(a4, "fser", 10);
        a2.connectToNodeUndirected(a5, "fssd", 1000);
        a2.connectToNodeUndirected(a3, "sfsg", 10);
        a2.connectToNodeUndirected(a6, "sfsd", 10);
        // traverseGraphDepthFirstShowingTotalDistance(a2, null, 0);

        /*System.out.println("The shortest path from Q to Y is:");
        System.out.println("");
        System.out.println("The shortest path from New Ross to Kilmuckridge");
        System.out.println("using Dijkstra's algorithm:");
        System.out.println("-------------------------------------");
        DistancedPath cpa = findShortestPathDijkstra(a1, "Kilkenny");
        for (GraphNodeAL2<?> n : cpa.pathList)
            System.out.println(n.data);
        System.out.println("\nThe total path distance is: " + cpa.pathDistance + "km");

        GraphNodeAL2 rh = new GraphNodeAL2();
        //rh.addMapLocation();
        //rh.addLocationLink();
        rh.setData();
        System.out.println("@@@@@@@@@@");
        rh.loadMap();*/


    }

    public void addMapLocation() {
        for (int i = 0; i < 4; i++) {
            System.out.println("ENTER NAME");
            Scanner scanner = new Scanner(System.in);
            String name1 = (scanner.nextLine());
            //System.out.println(name);
            nodeList.add(new GraphNodeAL2(nodeList));
            //System.out.println(list + name);
            //listOfLocations.add(new GraphNodeAL2<String>(name));
            System.out.println(new GraphNodeAL2(name));
            System.out.println(data);
        }
        addLocationLink();

    }

    public void shortestRoute() {
        try {
            System.out.println("The shortest path from New Ross to Kilmuckridge");
            System.out.println("using Dijkstra's algorithm:");
            System.out.println("-------------------------------------");
            DistancedPath cpa = findShortestPathDijkstra(nodeList.get(locationStartCb.getSelectionModel().getSelectedIndex()), locationDestCb.getValue());
            for (GraphNodeAL2<?> n : cpa.pathList)
                System.out.println(n.data);
            System.out.println("\nThe total path distance is: " + cpa.pathDistance + "km");
        } catch (Exception e) {
            System.out.println("Error: Please make sure only whole numbers were entered and that the location index exits.");
        }
    }

    public void test8() {
        locationStartCb.getButtonCell();
        System.out.println(locationStartCb.getValue());
        System.out.println(locationDestCb.getValue());
        //String convertedToString = String.valueOf(locationStartCb.getVisibleRowCount());
    }

    public void addLocationLink() {


        try {
            for (int i = 0; i < 1; i++) {
                System.out.println("Enter First Location Link(int)");
                int firstLocation = (scanner.nextInt());
                System.out.println("Enter Second Location Link(int)");
                int secondLocation = (scanner.nextInt());
                System.out.println("Enter the road name between the two locations");
                String road = (scanner.nextLine());
                scanner.nextLine();
                System.out.println("Enter the distance between the two links");
                int distance = (scanner.nextInt());

                nodeList.get(firstLocation).connectToNodeUndirected(nodeList.get(secondLocation), road, distance);
            }
        } catch (Exception e) {
            System.out.println("Error: Make sure to enter whole numbers only and that the location index exists");
        }
        try {
            System.out.println("Dijkstra's algorithm:");
            System.out.println("-------------------------------------");
            System.out.println("Enter the source Location Link(INT INDEX)");
            int sourceLocation = (scanner.nextInt());
            System.out.println("Enter the destination(STRING)");
            String dest = (scanner.nextLine());

            System.out.println("The shortest path from New Ross to Kilmuckridge");
            System.out.println("using Dijkstra's algorithm:");
            System.out.println("-------------------------------------");
            DistancedPath cpa = findShortestPathDijkstra(nodeList.get(sourceLocation), locationDestCb.getValue());
            for (GraphNodeAL2<?> n : cpa.pathList)
                System.out.println(n.data);
            System.out.println("\nThe total path distance is: " + cpa.pathDistance + "km");
        } catch (Exception e) {
            System.out.println("Error: Please make sure only whole numbers were entered and that the location index exits.");
        }
    }

    int test = 0;

    public void loadMap() throws IOException {

        File mapfile = new File(mapdata);
        System.out.println("\n");
        System.out.println("CSV MAP DATA");
        System.out.println("--------------------------------");

        //use scanner to read file
        mapread = new Scanner(mapfile);
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

        File linkfile = new File(linkdata);
        linkread = new Scanner(linkfile);

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
                System.out.println(firstLocation);
 /*               GraphNodeAL2<String> test = new GraphNodeAL2<>(parts[i]);
                nodeLis.add(test);*/
                nodeList.get(firstLocation).connectToNodeUndirected(nodeList.get(secondLocation), road, distance);
            }
            //list.add(new GraphNodeAL2(name, road, distance));
        }


        // String test = "hey";
        //locationStartCb.getItems().clear();
        System.out.println();
        System.out.println("Map data loaded...");
        mapread.close();

        for (int i = 0; i < nodeList.size(); i++) {
            System.out.println(nodeList.get(i).data);
            locationStartCb.getItems().add(nodeList.get(i).data);
            locationDestCb.getItems().add(nodeList.get(i).data);
        }


        //setData();
    }
  
    /* public void readIn() throws IOException {

        BufferedReader br = null;

        br = new BufferedReader(new FileReader(new File("src/application/.csv")));
        String line;
        while ((line = br.readLine()) != null) {
            String[] entries = line.split(",");
            for (int i = 0; i < entries.length; i++) {
                GraphNodeAL<String> test = new GraphNodeAL<>(entries[i]);
                test.ID = id;
                id++;
                list.add(test);

            }
        }
        

        br = new BufferedReader(new FileReader(new File("src/files/links.csv")));

        while ((line = br.readLine()) != null) {

            String[] entries = line.split(",");
            int src = parseInt(entries[0]);
            int dest = parseInt(entries[1]);
            int dist = parseInt(entries[2]);
            list.get(src).connectToNodeUndirected(list.get(dest), dist);
        }

    }

    public void exitMenu(ActionEvent event) {
        System.exit(0);
    }*/

}