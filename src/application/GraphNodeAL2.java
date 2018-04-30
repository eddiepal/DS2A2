package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.Slider;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

public class GraphNodeAL2<T> implements Initializable {
	public T data;
	public List<GraphLinkAL> adjList = new ArrayList<>(); // Adjacency list now contains link objects = key!
	// Could use any concrete List implementation
	public int nodeValue = Integer.MAX_VALUE;
	
	public GraphNodeAL2() {
		
	}

	public GraphNodeAL2(T data) {
		this.data = data;
	}

	public void connectToNodeDirected(GraphNodeAL2<T> destNode, int distance) {
		adjList.add(new GraphLinkAL(destNode, distance)); // Add new link object to source adjacency list
	}

	public void connectToNodeUndirected(GraphNodeAL2<T> destNode, int distance) {
		adjList.add(new GraphLinkAL(destNode, distance)); // Add new link object to source adjacency list
		destNode.adjList.add(new GraphLinkAL(this, distance)); // Add new link object to destination adjacency list
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	// Regular recursive depth-first graph traversal with total distance
	public static void traverseGraphDepthFirstShowingTotalDistance(GraphNodeAL2<?> from,
			List<GraphNodeAL2<?>> encountered, int totalDistance) {
		System.out.println(from.data + " (Total distance of reaching node: " + totalDistance + ")");
		if (encountered == null)
			encountered = new ArrayList<>(); // First node so create new (empty) encountered list
		encountered.add(from);
		// Could sort adjacency list here based on distance � see next slide for more
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

	public void testRun() {

/*		GraphNodeAL2<String> a1 = new GraphNodeAL2<>("Wexford");
		GraphNodeAL2<String> a2 = new GraphNodeAL2<>("Wicklow");
		GraphNodeAL2<String> a3 = new GraphNodeAL2<>("Carlow");
		GraphNodeAL2<String> a4 = new GraphNodeAL2<>("Kilkenny");
		GraphNodeAL2<String> a5 = new GraphNodeAL2<>("Dublin");
		GraphNodeAL2<String> a6 = new GraphNodeAL2<>("Kildare");*/
/*		GraphNodeAL2<String> a7 = new GraphNodeAL2<>("New Ross");*/
/*		GraphNodeAL2<String> a8 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a9 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a10 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a11 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a12 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a13 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a14 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a15 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a16 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a17 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a18 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a19 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a20 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a21 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a22 = new GraphNodeAL2<>("New tes");
		GraphNodeAL2<String> a23 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a24 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a25 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a26 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a27 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a28 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a29 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a30 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a31 = new GraphNodeAL2<>("New Ross");
		GraphNodeAL2<String> a32 = new GraphNodeAL2<>("New Ross");*/

		/*a1.connectToNodeUndirected(a2, 53);
		a1.connectToNodeUndirected(a3, 3);
		a1.connectToNodeUndirected(a4, 10);
		a2.connectToNodeUndirected(a5, 1000);
		a2.connectToNodeUndirected(a3, 10);
		a2.connectToNodeUndirected(a6, 10);
		traverseGraphDepthFirstShowingTotalDistance(a2, null, 0);

		System.out.println("The shortest path from Q to Y is:");
		System.out.println("");
		System.out.println("The shortest path from New Ross to Kilmuckridge");
		System.out.println("using Dijkstra's algorithm:");
		System.out.println("-------------------------------------");
		DistancedPath cpa = findShortestPathDijkstra(a1, "Kilkenny");
		for (GraphNodeAL2<?> n : cpa.pathList)
			System.out.println(n.data);
		System.out.println("\nThe total path distance is: " + cpa.pathDistance + "km");*/
	}
}