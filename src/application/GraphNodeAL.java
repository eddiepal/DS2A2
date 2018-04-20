package application;
import java.util.ArrayList;
import java.util.List;

public class GraphNodeAL<T> {
	public T data;
	
	public List<GraphNodeAL<T>> adjList=new ArrayList<>();
	
	public GraphNodeAL(T data) {
		this.data=data;
	}
	
	public void connectToNodeDirected(GraphNodeAL<T> destNode) {
		adjList.add(destNode);
	}
	
	public void connectToNodeUndirected(GraphNodeAL<T> destNode) {
		adjList.add(destNode);
		destNode.adjList.add(this);
	}
	
	//Regular recursive depth-first graph traversal
	public static void traverseGraphDepthFirst(GraphNodeAL<?> from, List<GraphNodeAL<?>> encountered ){
	System.out.println(from.data);
	if(encountered==null) encountered=new ArrayList<>(); //First node so create new (empty) encountered list
	encountered.add(from);
	for(GraphNodeAL<?> adjNode : from.adjList)
	if(!encountered.contains(adjNode)) traverseGraphDepthFirst(adjNode, encountered );
	}
	//Agenda list based breadth-first graph traversal (tail recursive)
	public static void traverseGraphBreadthFirst(List<GraphNodeAL<?>> agenda, List<GraphNodeAL<?>> encountered ){
	if(agenda.isEmpty()) return;
	GraphNodeAL<?> next=agenda.remove(0);
	System.out.println(next.data);
	if(encountered==null) encountered=new ArrayList<>(); //First node so create new (empty) encountered list
	encountered.add(next);
	for(GraphNodeAL<?> adjNode : next.adjList)
	if(!encountered.contains(adjNode) && !agenda.contains(adjNode)) agenda.add(adjNode); //Add children to
	//end of agenda
	traverseGraphBreadthFirst(agenda, encountered ); //Tail call
	}
	//Agenda list based depth-first graph traversal (tail recursive)
	public static <T> void traverseGraphDepthFirst(List<GraphNodeAL<?>> agenda, List<GraphNodeAL<?>> encountered ){
	if(agenda.isEmpty()) return;
	GraphNodeAL<?> next=agenda.remove(0);
	System.out.println(next.data);
	if(encountered==null) encountered=new ArrayList<>(); //First node so create new (empty) encountered list
	encountered.add(next);
	for(GraphNodeAL<?> adjNode : next.adjList)
	if(!encountered.contains(adjNode) && !agenda.contains(adjNode)) agenda.add( 0 ,adjNode); //Add children to
	//front of agenda (order unimportant here)
	traverseGraphDepthFirst(agenda, encountered ); //Tail call
	}

	
	public static void main(String[] args) {
		
	//Create undirected graph (Adjacency List)
	GraphNodeAL<String> a=new GraphNodeAL<>("Cherry");
	GraphNodeAL<String> b=new GraphNodeAL<>("Apple");
	GraphNodeAL<String> c=new GraphNodeAL<>("Plum");
	GraphNodeAL<String> d=new GraphNodeAL<>("Mango");
	GraphNodeAL<String> e=new GraphNodeAL<>("Kiwi");
	GraphNodeAL<String> f=new GraphNodeAL<>("Coconut");
	GraphNodeAL<String> g=new GraphNodeAL<>("Pear");
	GraphNodeAL<String> h=new GraphNodeAL<>("Orange");
	//Pairs either way to connect undirected
	a.connectToNodeUndirected(b); 
	a.connectToNodeUndirected(c);
	b.connectToNodeUndirected(c);
	b.connectToNodeUndirected(g);
	c.connectToNodeUndirected(d);
	g.connectToNodeUndirected(e);
	d.connectToNodeUndirected(e);
	f.connectToNodeUndirected(e);
	e.connectToNodeUndirected(h);
	//Printing of list
	System.out.println("Recursive depth first traversal starting at Orange");
	System.out.println("-------------------------------------------------");
	traverseGraphDepthFirst(h,null);
	System.out.println("\nAgenda based depth first traversal starting at Coconut");
	System.out.println("-------------------------------------------------");
	List<GraphNodeAL<?>> agenda=new ArrayList<>();
	agenda.add(f);
	traverseGraphDepthFirst(agenda,null);
	System.out.println("\nAgenda based breadth first traversal starting at Coconut");
	System.out.println("-------------------------------------------------");
	agenda.add(f);
	traverseGraphBreadthFirst(agenda,null);
	}
}

