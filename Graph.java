package hw4;
import java.util.*;


public class Graph<N extends Comparable<N>, L extends Comparable<L>> {
	private HashMap<N, HashSet<Edge<N,L>>> graph;
	public Graph() {
		graph = new HashMap<N, HashSet<Edge<N,L>>>();
	}
	public void addNode(N node){
		/*	requires: name is not ' ', node is new
		 * 	modifies: graph
		 * 	effects: adds node to graph
		 * 	returns: none
		 */
		graph.put(node, new HashSet<Edge<N,L>>());
		checkRep();
	}
	public void addEdge(Edge<N,L> edge) {
		/*	requires: parent != ' ', child != ' ', and weight not 0, parent and child in graph
		 * 	modifies: graph
		 * 	effects: adds edge to graph by changing node data
		 * 	returns: none
		 */
		checkRep();
		HashSet<Edge<N,L>> val = graph.get(edge.getParent());
		if(!val.contains(edge)) {
			val.add(edge);
		}
		checkRep();
	}
	
	public HashSet<Edge<N,L>> getEdges() {
		HashSet<Edge<N,L>> g = new HashSet<Edge<N,L>>();
		for (N key : graph.keySet()) {
			g.addAll(graph.get(key));
		}
		return g;
	}
	
	public void clear() {
		graph.clear();
	}
	
	public Set<N> getNodes() {
		return graph.keySet();
	}
	
	public int numEdges() {
		int edges = 0;
		for (N key : graph.keySet()) {
			edges += graph.get(key).size();
		}
		return edges;
	}
	
	public int numNodes() {
		return graph.size();
	}
	
	public boolean contains(N n){
		return graph.keySet().contains(n);
	}
	
	public TreeSet<Edge<N,L>> listChildrenSorted(N n) {
		return new TreeSet<Edge<N,L>>(graph.get(n));
	}
	
	private void checkRep() throws RuntimeException {
		if (this.graph == null) {
			throw new RuntimeException("graph is null");
		}
		if (this.graph.keySet() == null) {
			throw new RuntimeException("keyset is null");
		}
		if(this.graph.containsKey(null)) {
			throw new RuntimeException("keyset contains null");
		}
	}
}
