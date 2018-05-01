package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;

public class GraphNodeAL2<T> {
    public T data;
    public List<GraphLinkAL> adjList = new ArrayList<>(); // Adjacency list now contains link objects = key!
    // Could use any concrete List implementation
    public int nodeValue = Integer.MAX_VALUE;
    private Scanner input;
    private String mapdata = "src/application/mapdata.csv";
    private Scanner mapread;
    private int id;
    private String name;
    private ArrayList<GraphNodeAL2<String>> listOfLocations;
    private List<GraphNodeAL2> list = new ArrayList<GraphNodeAL2>();


    public GraphNodeAL2() {

    }

    public GraphNodeAL2(int i, String name) {

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

    // Regular recursive depth-first graph traversal with total distance
    public static void traverseGraphDepthFirstShowingTotalDistance(GraphNodeAL2<?> from,
                                                                   List<GraphNodeAL2<?>> encountered, int totalDistance) {
        System.out.println(from.data + " (Total distance of reaching node: " + totalDistance + ")");
        if (encountered == null)
            encountered = new ArrayList<>(); // First node so create new (empty) encountered list
        encountered.add(from);
        // Could sort adjacency list here based on distance ï¿½ see next slide for more
        // info!
        for (GraphLinkAL adjLink : from.adjList)
            if (!encountered.contains(adjLink.destNode))
                traverseGraphDepthFirstShowingTotalDistance(adjLink.destNode, encountered,
                        totalDistance + adjLink.distance);
    }

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
        traverseGraphDepthFirstShowingTotalDistance(a2, null, 0);

        System.out.println("The shortest path from Q to Y is:");
        System.out.println("");
        System.out.println("The shortest path from New Ross to Kilmuckridge");
        System.out.println("using Dijkstra's algorithm:");
        System.out.println("-------------------------------------");
        DistancedPath cpa = findShortestPathDijkstra(a1, "Kilkenny");
        for (GraphNodeAL2<?> n : cpa.pathList)
            System.out.println(n.data);
        System.out.println("\nThe total path distance is: " + cpa.pathDistance + "km");

        GraphNodeAL2 rh = new GraphNodeAL2();
		/*rh.addMapLocation();
		rh.addLocationLink();*/
        rh.loadMap();


    }

    public void addMapLocation() {
        for (int i = 0; i < 4; i++) {
            System.out.println("ENTER NAME");
            Scanner addName = new Scanner(System.in);
            String name = (addName.nextLine());

            System.out.println(name);
            list.add(new GraphNodeAL2(name));
            System.out.println(list + name);


            //listOfLocations.add(new GraphNodeAL2<String>(name));

/*			GraphNodeAL2<String> a1 = new GraphNodeAL2<>("New Ross");
			GraphNodeAL2<String> index = new GraphNodeAL2<>("New Ross");*/
        }
    }

    public void addLocationLink() {

        try {
            for (int i = 0; i < 1; i++) {
                System.out.println("Enter First Location Link");
                Scanner addFirstLocation = new Scanner(System.in);
                int firstLocation = (addFirstLocation.nextInt());

                System.out.println("Enter Second Location Link");
                Scanner addSecondLocation = new Scanner(System.in);
                int secondLocation = (addSecondLocation.nextInt());

                System.out.println("Enter the road name between the two locations");
                Scanner addRoad = new Scanner(System.in);
                String road = (addRoad.nextLine());

                System.out.println("Enter the distance between the two links");
                Scanner addDistance = new Scanner(System.in);
                int distance = (addDistance.nextInt());

                list.get(firstLocation).connectToNodeUndirected(list.get(secondLocation),road, distance);


            }
        } catch (Exception e) {
            System.out.println("Error: Make sure to enter whole numbers only and that the location index exists");
        }
        try {
            System.out.println("The shortest path from New Ross to Kilmuckridge");
            System.out.println("using Dijkstra's algorithm:");
            System.out.println("-------------------------------------");
            DistancedPath cpa = findShortestPathDijkstra(list.get(0), "Cork");
            for (GraphNodeAL2<?> n : cpa.pathList)
                System.out.println(n.data);
            System.out.println("\nThe total path distance is: " + cpa.pathDistance + "km");
        } catch (Exception e) {
            System.out.println("Error: Please make sure only whole numbers were entered and that the location index exits.");
        }
    }
    int test = 0;
    private void loadMap() {
        input = new Scanner(System.in);
        //create file from student file
        File mapfile = new File(mapdata);
        //try catch incase file doesnt exist
        System.out.println("");
        System.out.println("");
        System.out.println("CSV MAP DATA");
        System.out.println("--------------------------------");
        try {
            //use scanner to read file
            mapread = new Scanner(mapfile);
            //while there is still next item in the file
            int index = 1;
            while(mapread.hasNext()) {
                //create a string called data that = value from file
                String data = mapread.next();
                //create string array of data separated at comma
                String[] parts = data.split(",");
                //set name to the string at the 0 column
                String name = parts[0];




                if(test==1) {
                    System.out.println(name);
                    list.add(new GraphNodeAL2(name));
                    System.out.println(list + "     Name: " + name);
                }



                //System.out.println(data);
					/*set gender to string at 1 column
					String gender = parts[1];
					boolean car = false;
					// set id to string at column 3
					String id = parts[3];
					//if column 2 is true set car to true, else its false
					if(parts[2].equals("true"))
					{
						 car = true;
					} */
                //create student using these  attributes from the file
                //Student student = new Student(name,gender,car,id);
                //add the students
                //GraphNodeAL2<String> dasr = new GraphNodeAL2<>(id,name);

                //int test = dasr.nodeValue;
//					studentlist.addStudent(student);

            }

            System.out.println();
            System.out.println("Map data loaded...");
            //close the read
            mapread.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}