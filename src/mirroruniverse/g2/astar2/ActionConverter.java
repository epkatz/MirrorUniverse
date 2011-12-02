package mirroruniverse.g2.astar2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import mirroruniverse.g2.astar.State;

public class ActionConverter {
	public static List<Integer> convert(List<Integer> states) {
		if (states == null)
			return null;
		LinkedList<Integer> actions = new LinkedList<Integer>();

		Iterator<Integer> iter = states.iterator();
		Integer last = iter.next();
		Integer current;
		while (iter.hasNext()) {
			current = iter.next();
			int move = computeMove(last, current);
			if(move != 0)
				actions.add(move);
			last = current;
		}
		
		return actions;
	}

	public static int computeMove(Integer from, Integer to) {
		int[] c1;
		int[] c2;
		int x1, y1, x2, y2;
		c1 = Encoder.decode(from);
		c2 = Encoder.decode(to);
		x1 = c1[0];
		y1 = c1[1];
		x2 = c2[0];
		y2 = c2[1];
		
//		int[] coordinates;
//		System.out.println("From:");
//		coordinates = Encoder.decode(from);
//		for (int i = 0; i != coordinates.length; ++i)
//			System.out.println(coordinates[i]);
//
//		System.out.println("To:");
//		coordinates = Encoder.decode(to);
//		for (int i = 0; i != coordinates.length; ++i)
//			System.out.println(coordinates[i]);

		
		int directionLeft = -1;

		if (x1 == x2 && y1 == y2) {
			directionLeft = 0; // stay put
		} else if (x1 == x2 && y1 < y2) {
			directionLeft = 7; // move down
		} else if (x1 == x2 && y1 > y2) {
			directionLeft = 3; // move up
		} else if (y1 == y2 && x1 < x2) {
			directionLeft = 1; // move right
		} else if (y1 == y2 && x1 > x2) {
			directionLeft = 5; // move left
		} else if (x1 < x2 && y1 < y2) {
			directionLeft = 8; // move down- right (south east)
		} else if (x1 > x2 && y1 > y2) {
			directionLeft = 4; // up left ( north west)
		} else if (x1 > x2 && y1 < y2) {
			directionLeft = 6; // down left (south west)
		} else if (x1 < x2 && y1 > y2) {
			directionLeft = 2; // up right (north east)
		}

		x1 = c1[2];
		y1 = c1[3];
		x2 = c2[2];
		y2 = c2[3];
		int directionRight = -1;

		if (x1 == x2 && y1 == y2) {
			directionRight = 0; // stay put
		} else if (x1 == x2 && y1 < y2) {
			directionRight = 7; // move down
		} else if (x1 == x2 && y1 > y2) {
			directionRight = 3; // move up
		} else if (y1 == y2 && x1 < x2) {
			directionRight = 1; // move right
		} else if (y1 == y2 && x1 > x2) {
			directionRight = 5; // move left
		} else if (x1 < x2 && y1 < y2) {
			directionRight = 8; // move down- right (south east)
		} else if (x1 > x2 && y1 > y2) {
			directionRight = 4; // up left ( north west)
		} else if (x1 > x2 && y1 < y2) {
			directionRight = 6; // down left (south west)
		} else if (x1 < x2 && y1 > y2) {
			directionRight = 2; // up right (north east)
		}

		return directionLeft != 0 ? directionLeft : directionRight; // return
																	// the
																	// none-zero
																	// one
	}
}
