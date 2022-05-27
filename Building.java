package hw7;

public class Building {
	private String name;
	private int x_coord;
	private int y_coord;
	private int id;
	
	public Building(String name, int id, int x_coord, int y_coord) {
		if(name.equals("")) {
			this.name = "Intersection " + String.valueOf(id);
		}
		else {
			this.name = name;
		}
		this.x_coord = x_coord;
		this.y_coord = y_coord;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getX() {
		return x_coord;
	}
	
	public int getY() {
		return y_coord;
	}
	
	public int getID() {
		return id;
	}
	
}
