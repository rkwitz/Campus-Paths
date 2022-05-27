package hw5;

import hw4.Graph;
import hw4.Edge;
import java.io.IOException;
import java.util.*;


public class MarvelPaths {
	
	private Graph<String, String> graph = new Graph<String, String>();
	
	public void createNewGraph(String filename) {
		Map<String, Set<String>> charsInBooks = new HashMap<String, Set<String>>();
		Set<String> chars = new HashSet<String>();
		try {
			MarvelParser.readData(filename, charsInBooks, chars);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		// add chars to graph as nodes
		Iterator<String> itr = chars.iterator();
		while(itr.hasNext()) {
			String name = itr.next();
			if (!name.equals("")) {
				graph.addNode(name);
			}
		}
		// add books to graph as edges
		for(String char1 : chars) {
			for(String book : charsInBooks.keySet()) {
				if (charsInBooks.get(book).contains(char1)) {
					for(String char2 : charsInBooks.get(book)) {
						if (!char1.equals(char2)) {
							Edge<String, String> e = new Edge<String, String>(char1, char2, book);
							graph.addEdge(e);
						}
					}
				}
			}
		}
	}
	
	public String findPath(String node1, String node2) {
		if (!graph.getNodes().contains(node1) && !graph.getNodes().contains(node2) && !node1.equals(node2)) {
			return "unknown character " + node1 + "\nunknown character " + node2 + "\n";
		}
		else if (!graph.getNodes().contains(node1)) {
			return "unknown character " + node1 + "\n";
		}
		else if (!graph.getNodes().contains(node2)) {
			return "unknown character " + node2 + "\n";
		}
		String start = node1;
		String dest = node2;
		Queue<String> q = new LinkedList<>();
		Map<String, ArrayList<String>> m = new HashMap<String, ArrayList<String>>();
		q.add(start);
		m.put(start, new ArrayList<String>());
		while(!q.isEmpty()) {
			String curr = q.poll();
			if (curr.equals(dest)) {
				String answer = "";
				for (String edge : m.get(curr)) {
					answer += edge;
				}
				return "path from " + start + " to " + dest +":" + "\n" + answer; 
			}
			TreeSet<Edge<String,String>> edges = graph.listChildrenSorted(curr);
			for(Edge<String,String> edge : edges) {
				if (!m.containsKey(edge.getChild())) {
					ArrayList<String> p = new ArrayList<String>(m.get(curr));
					p.add(curr + " to " + edge.getChild() + " via " + edge.getLabel() + "\n");
					m.put(edge.getChild(), p);
					q.add(edge.getChild());
				}
			}
		}
		return "path from " + start + " to " + dest +":" + "\n" + "no path found\n";
	}
	public int numNodes() {
		return graph.numNodes();
	}
	public int numEdges() {
		return graph.numEdges();
	}
}