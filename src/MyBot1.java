import java.util.ArrayList;
import java.util.Collections;

import tron.utils.Map;
import tron.utils.TronBot;

public class MyBot1 implements TronBot {

	@Override
	public String makeMove(Map m) {

		int mx = m.myX();
		int my = m.myY();

		ArrayList<String> al = new ArrayList<String>();

		if (!m.isWall(mx, my - 1)) {
			al.add("North");
			// return "North";
		}

		if (!m.isWall(mx, my + 1)) {
			al.add("South");
			// return "South";
		}

		if (!m.isWall(mx - 1, my)) {
			al.add("West");
			// return "West";
		}

		if (!m.isWall(mx + 1, my)) {
			al.add("East");
			// return "East";
		}

		if (al.size() > 0) {
			Collections.shuffle(al);
			return al.get(0);
		}
		return "South";

	}

}
