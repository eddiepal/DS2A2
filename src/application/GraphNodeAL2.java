package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GraphNodeAL2<T> {
	public T data;
	public List<GraphLinkAL> adjList = new ArrayList<>(); // Adjacency list now contains link objects = key!
	// Could use any concrete List implementation
	public int nodeValue = Integer.MAX_VALUE;

	public GraphNodeAL2(T data) {
		this.data = data;
	}

	public void connectToNodeDirected(GraphNodeAL2<T> destNode, int cost) {
		adjList.add(new GraphLinkAL(destNode, cost)); // Add new link object to source adjacency list
	}

	public void connectToNodeUndirected(GraphNodeAL2<T> destNode, int cost) {
		adjList.add(new GraphLinkAL(destNode, cost)); // Add new link object to source adjacency list
		destNode.adjList.add(new GraphLinkAL(this, cost)); // Add new link object to destination adjacency list
	}

	// Regular recursive depth-first graph traversal with total cost
	public static void traverseGraphDepthFirstShowingTotalCost(GraphNodeAL2<?> from, List<GraphNodeAL2<?>> encountered,
			int totalCost) {
		System.out.println(from.data + " (Total cost of reaching node: " + totalCost + ")");
		if (encountered == null)
			encountered = new ArrayList<>(); // First node so create new (empty) encountered list
		encountered.add(from);
		// Could sort adjacency list here based on cost – see next slide for more info!
		for (GraphLinkAL adjLink : from.adjList)
			if (!encountered.contains(adjLink.destNode))
				traverseGraphDepthFirstShowingTotalCost(adjLink.destNode, encountered, totalCost + adjLink.cost);
	}

	public static class CostedPath {
		public int pathCost = 0;
		public List<GraphNodeAL2<?>> pathList = new ArrayList<>();
	}

		public static <T> CostedPath findCheapestPathDijkstra(GraphNodeAL2<?> startNode, T lookingfor) {
			CostedPath cp = new CostedPath(); // Create result object for cheapest path
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
					cp.pathCost = currentNode.nodeValue; // The total cheapest path cost is the node value of the
															// current/goal node
					while (currentNode != startNode) { // While we're not back to the start node...
						boolean foundPrevPathNode = false; // Use a flag to identify when the previous path node is
															// identified
						for (GraphNodeAL2<?> n : encountered) { // For each node in the encountered list...
							for (GraphLinkAL e : n.adjList) // For each edge from that node...
								if (e.destNode == currentNode && currentNode.nodeValue - e.cost == n.nodeValue) { // If that
																													// edge
																													// links
																													// to
																													// the
									// current node and the difference in node values is the cost of the edge ->
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
					return cp; // The costed (cheapest) path has been assembled, so return it!
				}
				// We're not at the goal node yet, so...
				for (GraphLinkAL e : currentNode.adjList) // For each edge/link from the current node...
					if (!encountered.contains(e.destNode)) { // If the node it leads to has not yet been encountered (i.e.
																// processed)
						e.destNode.nodeValue = Integer.min(e.destNode.nodeValue, currentNode.nodeValue + e.cost); // Update
																													// the
																													// node
																													// value
																													// at
																													// the
																													// end
						// of the edge to the minimum of its current value or the total of the current
						// node's value plus the cost of the edge
						unencountered.add(e.destNode);
					}
				Collections.sort(unencountered, (n1, n2) -> n1.nodeValue - n2.nodeValue); // Sort in ascending node value
																							// order
			} while (!unencountered.isEmpty());
			return null; // No path found, so return null
		}
	

	public static void main(String[] args) {
		GraphNodeAL2<String> a=new GraphNodeAL2<>("Silver");
		GraphNodeAL2<String> b=new GraphNodeAL2<>("Bronze");
		GraphNodeAL2<String> c=new GraphNodeAL2<>("Lead");
		GraphNodeAL2<String> d=new GraphNodeAL2<>("Tin");
		GraphNodeAL2<String> e=new GraphNodeAL2<>("Copper");
		GraphNodeAL2<String> f=new GraphNodeAL2<>("Brass");
		GraphNodeAL2<String> g=new GraphNodeAL2<>("Iron");
		GraphNodeAL2<String> h=new GraphNodeAL2<>("Gold");
		a.connectToNodeUndirected(b, 5);
		a.connectToNodeUndirected(c, 9);
		b.connectToNodeUndirected(c, 2);
		b.connectToNodeUndirected(d, 6);
		c.connectToNodeUndirected(e, 5);
		d.connectToNodeUndirected(h, 8);
		d.connectToNodeUndirected(g, 9);
		e.connectToNodeUndirected(g, 3);
		e.connectToNodeUndirected(f, 7);
		f.connectToNodeUndirected(g, 6);
		g.connectToNodeUndirected(h, 2);
		traverseGraphDepthFirstShowingTotalCost(b, null, 0);
		
		System.out.println("The cheapest path from Q to Y is:");
		System.out.println("");
		System.out.println("The cheapest path from Silver to Gold");
		System.out.println("using Dijkstra's algorithm:");
		System.out.println("-------------------------------------");
		CostedPath cpa=findCheapestPathDijkstra(a,"Gold");
		for(GraphNodeAL2<?> n : cpa.pathList)
		System.out.println(n.data);
		System.out.println("\nThe total path cost is: "+cpa.pathCost);

	}
}