package mirroruniverse.g2;

import java.util.List;

import mirroruniverse.g2.astar.BacktrackingAStar;
import mirroruniverse.g2.astar.State;
import mirroruniverse.g2.astar2.ActionConverter;
import mirroruniverse.g2.astar2.BacktrackPathSearcher;
import mirroruniverse.g2.astar2.Encoder;

public class Backtracker2 {
	private BacktrackPathSearcher searcher;
	Map leftMap;
	Map rightMap;

	public Backtracker2(Map leftMap, Map rightMap) {
		this.leftMap = leftMap;
		this.rightMap = rightMap;
		this.searcher = new BacktrackPathSearcher(leftMap, rightMap);
	}

	public List<Integer> search(Position left, Position right) {
		int start = Encoder.encode(leftMap.playerPos.x, leftMap.playerPos.y,
				rightMap.playerPos.x, rightMap.playerPos.y);
		int goal = Encoder.encode(left.x, left.y, right.x, right.y);
		
		List<Integer> statesList = searcher.search(start, goal);
		List<Integer> actions = ActionConverter.convert(statesList);
		
		return actions;
	}
}
