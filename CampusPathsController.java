package hw7;

import java.util.Scanner;

public class CampusPathsController {
	public static String takeInput(String input, CampusPathsModel m, Scanner in) {
		if (input.equals("b")) {
			return m.listBuildings();
		}
		else if (input.equals("r")) {
			System.out.print("First building id/name, followed by Enter: ");
			String first = in.nextLine();
			System.out.print("Second building id/name, followed by Enter: ");
			String second = in.nextLine();
			if (isNumeric(first) && Integer.parseInt(first) < 92) {
				if (!(m.IDtoName(Integer.parseInt(second)) == null)) {
					first = m.IDtoName(Integer.parseInt(first));
				}
			}
			if (isNumeric(second) && Integer.parseInt(second) < 92) {
				if (!(m.IDtoName(Integer.parseInt(second)) == null)) {
					second = m.IDtoName(Integer.parseInt(second));
				}
			}
			return m.findPath(first, second);
		}
		else if (input.equals("m")) {
			return	"b lists all buildings\n" +
					"r prints directions for the shortest route between any two buildings\n" +
					"q quits the program\n" +
					"m prints a menu of all commands\n";
		}
		else {
			return "Unknown option\n";
		}
	}
	
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        Double.parseDouble(strNum);
	    } catch (NumberFormatException n) {
	        return false;
	    }
	    return true;
	}
}
