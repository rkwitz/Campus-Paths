package hw7;

import java.io.*;
import java.util.*;

public class CampusPaths {
	public static void main(String[] arg) {
		try {
			CampusPathsModel m = new CampusPathsModel();
			m.readData("data/RPI_map_data_Edges.csv", "data/RPI_map_data_Nodes.csv");
			Scanner in = new Scanner(System.in);
			Boolean active = true;
			while(active) {
				String input = in.nextLine();
				if (input.equals("q")) {
					active = false;
				}
				else {
					System.out.print(CampusPathsController.takeInput(input, m, in));
				}
			}
			in.close();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}