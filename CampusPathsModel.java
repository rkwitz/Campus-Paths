package hw7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import hw4.Edge;
import hw4.Graph;
import hw6.MarvelPaths2;

public class CampusPathsModel {
	private static Building[] buildings;
	private static Graph<String, Double> g = new Graph<String, Double>();
	
	public void readData(String edgesFilename, String nodesFilename) throws IOException {
		/*	requires: valid filenames
		 * 	modifies: g, buildings
		 * 	effects: populates graph and adds buildings to buildings array
		 * 	returns: none
		 */
		try (BufferedReader reader = new BufferedReader(new FileReader(nodesFilename))) {
			String line = null;
			ArrayList<Building> temp = new ArrayList<Building>();
			int max = 0;
			while ((line = reader.readLine()) != null) {
				String[] info = line.split(",");
				if (info.length != 4) {
					throw new IOException("File " + nodesFilename + " wrong format.");
				}
				Building b = new Building(info[0], Integer.parseInt(info[1]), Integer.parseInt(info[2]), Integer.parseInt(info[3]));
				g.addNode(b.getName());
				temp.add(b);
				if (b.getID() > max) {
					max = b.getID();
				}
			}
			buildings = new Building[max + 1];
			for (Building b : temp) {
				buildings[b.getID()] = b;
			}
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(edgesFilename))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] info = line.split(",");
				if (info.length != 2) {
					throw new IOException("File " + edgesFilename + " wrong format.");
				}
				Double weight = Math.pow(Math.pow(buildings[Integer.parseInt(info[1])].getX() - buildings[Integer.parseInt(info[0])].getX(), 2) + Math.pow(buildings[Integer.parseInt(info[1])].getY() - buildings[Integer.parseInt(info[0])].getY(), 2) , 0.5);
				Edge<String, Double> e = new Edge<String, Double>(buildings[Integer.parseInt(info[0])].getName(), buildings[Integer.parseInt(info[1])].getName(), weight);
				Edge<String, Double> f = new Edge<String, Double>(buildings[Integer.parseInt(info[1])].getName(), buildings[Integer.parseInt(info[0])].getName(), weight);
				g.addEdge(e);
				g.addEdge(f);
			}
		}
		checkRep();
	}
	
	public String listBuildings() {
		/*	requires: populated building array
		 * 	modifies: nothing
		 * 	effects: none
		 * 	returns: list of buildings as string
		 */
		checkRep();
		String ans = "";
		ArrayList<Building> buildingsSorted = new ArrayList<Building>();
		for (Building b : buildings) {
			if (!(b == null) && !b.getName().contains("Intersection")) {
				buildingsSorted.add(b);
			}
		}
		Collections.sort(buildingsSorted, new Comparator<Building>() {
		    public int compare(Building b1, Building b2) {
		        return b1.getName().compareTo(b2.getName());
		    }
		});
		for (Building b : buildingsSorted) {
			ans += b.getName() + "," + b.getID() + "\n";
		}
		checkRep();
		return ans;
	}
	
	public String IDtoName(int ID) {
		/*	requires: ID, populated building array
		 * 	modifies: none
		 * 	effects: none
		 * 	returns: name of given ID
		 */
		checkRep();
		if (buildings[ID] == null) {
			return null;
		}
		else {
			return buildings[ID].getName();
		}
	}
	
	public String findPath(String building1, String building2) {
		/*	requires: populated building array and graph g, two string inputs
		 * 	modifies: none
		 * 	effects: none
		 * 	returns: path from given building1 to given building2 
		 */
		checkRep();
		MarvelPaths2 m = new MarvelPaths2();
		m.importGraph(g);
		String Path = m.findPath(building1, building2);
		String output = "";
		Scanner scanner = new Scanner(Path);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.substring(0, 7).equals("unknown")) {
				output += "Unknown building: [" + line.substring(18, line.length()) + "]\n";
			}
			else if (building1.equals(building2)) {
				scanner.close();
				return "Path from " + building1 + " to " + building2 + ":\nTotal distance: 0.000 pixel units.\n";
			}
			else if (line.substring(0, 5).equals("path ")) {
				output += "Path from " + building1 + " to " + building2 + ":\n";
			}
			else if (line.substring(0, 10).equals("total cost")) {
				output += "Total distance: " + line.substring(12, line.length()) + " pixel units.\n";
			}
			else if (line.substring(0, 13).equals("no path found")) {
				scanner.close();
				return "There is no path from " + building1 + " to " + building2 + ".\n";
			}
			else if (!line.equals("")) {
				String start = line.substring(0, line.indexOf(" to "));
				String end = line.substring(line.indexOf(" to ") + 4, line.indexOf(" with "));
				Building s = new Building("",0,0,0);
				Building e = new Building("",0,0,0);
				for (Building b: buildings) {
					if (!(b == null ) && b.getName().equals(start)) {
						s = b;
					}
					else if (!(b == null ) && b.getName().equals(end)){
						e = b;
					}
				}
				String direction;
				Double degree = 0.0;
				if (s.getX() == e.getX())
				{
					if (s.getY() < e.getY()) {
						degree = 0.0;
					}
					else {
						degree = 180.0;
					}
				}
				else if (s.getY() == e.getY()) {
					if (s.getX() < e.getX()) {
						degree = 90.0;
					}
					else {
						degree = 270.0;
					}
				}
				else {
					degree = (180.0*Math.atan((double)(s.getY() - e.getY()) / (double)(e.getX() - s.getX()))) / Math.PI;
					if ((((double)(s.getY() - e.getY()) > 0) && ((double)(e.getX() - s.getX()) < 0)) ||  (((double)(s.getY() - e.getY()) < 0) && ((double)(e.getX() - s.getX()) < 0))) {
						if (degree > 0) {
							degree = 180.0 + degree;
						}
						else {
							degree = degree - 180;
						}
					}
					degree = 90 - degree;
					if (degree < 0) {
						degree += 360;
					}
				}
				if ((337.5 <= degree) || (degree < 22.5)) {
					direction = "North"; 
				}
				else if ((22.5 <= degree) && (degree < 67.5)) {
					direction = "NorthEast"; 
				}
				else if ((67.5 <= degree) && (degree < 112.5)) {
					direction = "East"; 
				}
				else if ((112.5 <= degree) && (degree < 157.5)) {
					direction = "SouthEast"; 
				}
				else if ((157.5 <= degree) && (degree < 202.5)) {
					direction = "South"; 
				}
				else if ((202.5 <= degree) && (degree < 247.5)) {
					direction = "SouthWest"; 
				}
				else if ((247.5 <= degree) && (degree < 292.5)) {
					direction = "West"; 
				}
				else {
					direction = "NorthWest"; 
				}
				output += "\tWalk " + direction + " to (" + end + ")\n";
			}
		}
		scanner.close();
		checkRep();
		return output;
	}
	
	private void checkRep() throws RuntimeException {
		if (buildings == null) {
			throw new RuntimeException("buildings is null");
		}
		if (g == null) {
			throw new RuntimeException("graph is null");
		}
	}
}
