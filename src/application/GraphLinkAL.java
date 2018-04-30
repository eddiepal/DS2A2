package application;
public class GraphLinkAL {
	public GraphNodeAL2<?> destNode; // Could also store source node if required
	public int distance; // Other link attributes could be similarly stored

	public GraphLinkAL(GraphNodeAL2<?> destNode, int distance) {
		this.destNode = destNode;
		this.distance = distance;
	}
}
