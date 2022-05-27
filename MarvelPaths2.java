package hw6;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import hw4.Edge;
import hw4.Graph;
import hw5.MarvelParser;

public class MarvelPaths2 {
	private Graph<String, Double> weightedGraph = new Graph<String, Double>();
	
	public void importGraph(Graph<String, Double> g) {
		weightedGraph = g;
	}
	
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
				weightedGraph.addNode(name);
			}
		}
		// add books to graph as edges
		HashMap<String, Integer> edges = new HashMap<String, Integer>();
		for(String char1 : chars) {
			for(String book : charsInBooks.keySet()) {
				if (charsInBooks.get(book).contains(char1)) {
					for(String char2 : charsInBooks.get(book)) {
						if (!char1.equals(char2)) {
							if (edges.containsKey(char1 + "$" + char2)) {
								edges.put(char1 + "$" + char2, edges.get(char1 + "$" + char2) + 1);
							}
							else {
								edges.put(char1 + "$" + char2, 1);
							}
						}
					}
				}
			}
		}
		for (Map.Entry<String, Integer> element : edges.entrySet()) {
			Edge<String, Double> e = new Edge<String, Double>(element.getKey().substring(0, element.getKey().indexOf("$")), element.getKey().substring(element.getKey().indexOf("$") + 1), 1.0/element.getValue());
			weightedGraph.addEdge(e);
		}
	}
	public int numNodes() {
		return weightedGraph.numNodes();
	}
	public int numEdges() {
		return weightedGraph.numEdges();
	}
	public ArrayList<Double> getWeights() {
		ArrayList<Double> ans = new ArrayList<Double>();
		for (Edge<String,Double> e: weightedGraph.getEdges()) {
			ans.add(e.getLabel());
		}
		return ans;
	}
	
	public String findPath(String char1, String char2) {
		ArrayList<String> c = new ArrayList<String>(weightedGraph.getNodes().stream().collect(Collectors.toList()));
		if (!c.contains(char1) && !c.contains(char2) && !char1.equals(char2)) {
			return "unknown character " + char1 + "\nunknown character " + char2 + "\n";
		}
		else if (!c.contains(char1)) {
			return "unknown character " + char1 + "\n";
		}
		else if (!c.contains(char2)) {
			return "unknown character " + char2 + "\n";
		}
		String start = char1;
		String dest = char2;
		PriorityQueue<ArrayList<Edge<String, Double>>> active = new PriorityQueue<ArrayList<Edge<String, Double>>>(10, new Comparator<ArrayList<Edge<String, Double>>>() {
			public int compare(ArrayList<Edge<String, Double>> path1, ArrayList<Edge<String, Double>> path2)  {
				Edge<String, Double> dest1 = path1.get(path1.size() - 1);
				Edge<String, Double> dest2 = path2.get(path2.size() - 1);
				if (!(dest1.getLabel().equals(dest2.getLabel())))
					return dest1.getLabel().compareTo(dest2.getLabel());
				return path1.size() - path2.size();
			}
		});
		Set<String> finished = new HashSet<String>();
		ArrayList<Edge<String, Double>> startNode = new ArrayList<Edge<String, Double>>();
		Edge<String, Double> edge = new Edge<String, Double>(start, start, 0.0);
		startNode.add(edge);
		active.add(startNode);
		while(!active.isEmpty()) {
			ArrayList<Edge<String, Double>> minPath = active.poll();
			String minDest = minPath.get(minPath.size() - 1).getChild();
			double minCost = minPath.get(minPath.size() - 1).getLabel();
			if (minDest.equals(dest)) {
				String ans = "path from " + start + " to " + dest + ":\n";
				Iterator<Edge<String, Double>> it = minPath.iterator();
	        	List<Edge<String, Double>> l = new ArrayList<Edge<String, Double>>();
	        	while(it.hasNext())
	        		l.add(it.next());
	        	double totalCost = 0;
	        	for(int i = 1; i < l.size(); i++)
	        	{
	        		String parent = l.get(i).getParent();
	        		String child = l.get(i).getChild();
	        		double edgeWeight = l.get(i).getLabel() - l.get(i - 1).getLabel();
					ans += parent + " to " + child + " with weight " + String.format("%.3f", edgeWeight) + "\n";
					totalCost += edgeWeight;
				}
	        	ans += "total cost: " + String.format("%.3f", totalCost) + "\n";
	            return(ans);
			}
			if (finished.contains(minDest)) {
				continue;
			}
			Set<Edge<String, Double>> children = weightedGraph.listChildrenSorted(minDest);
			for (Edge<String,Double> e: children) {
				if (!finished.contains(e.getChild())) {
					double newCost = minCost + e.getLabel();
					ArrayList<Edge<String, Double>> newPath = new ArrayList<Edge<String, Double>>();
					for (Edge<String, Double> element: minPath) {
						newPath.add(element);
					}
					newPath.add(new Edge<String, Double>(e.getParent(), e.getChild(), newCost));
					active.add(newPath);
				}
			}
			finished.add(minDest);
		}
		return "path from " + start + " to " + dest + ":\nno path found\n";
	}
}