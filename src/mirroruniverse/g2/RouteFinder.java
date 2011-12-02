package mirroruniverse.g2;

import java.util.List;

import mirroruniverse.g2.astar.MirrorUniverseAStar;
import mirroruniverse.g2.astar.State;
import mirroruniverse.g2.astar2.ActionConverter;
import mirroruniverse.g2.astar2.Encoder;
import mirroruniverse.g2.astar2.ExitPathSearcher;
import mirroruniverse.sim.Player;

public class RouteFinder {
	Map leftMap;
	Map rightMap;
	ExitPathSearcher searcher;
	List<Integer> actions;

	public RouteFinder(Map leftMap, Map rightMap) {
		this.leftMap = leftMap;
		this.rightMap = rightMap;
		this.searcher = new ExitPathSearcher(leftMap, rightMap);
	}
	
	public boolean pathFound() {
		return actions != null && actions.size() != 0;
	}
	
	public int getMove() {
		return actions.remove(0);
	}

	public List<Integer> searchPath() {
		if (leftMap.exitPos == null || rightMap.exitPos == null)
			return null;
		// the initial state is the current position of the player
		int start = Encoder.encode(leftMap.playerPos.x, leftMap.playerPos.y, rightMap.playerPos.x, rightMap.playerPos.y);
		int goal = Encoder.encode(leftMap.exitPos.x, leftMap.exitPos.y, rightMap.exitPos.x, rightMap.exitPos.y);

		List<Integer> path = searcher.search(start, goal);
		actions = ActionConverter.convert(path);
		
		return actions;
	}

	public static int computeMove(State from, State to) {
		double x1, x2, y1, y2;
		x1 = from.posLeft.x;
		y1 = from.posLeft.y;
		x2 = to.posLeft.x;
		y2 = to.posLeft.y;
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

		x1 = from.posRight.x;
		y1 = from.posRight.y;
		x2 = to.posRight.x;
		y2 = to.posRight.y;
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
