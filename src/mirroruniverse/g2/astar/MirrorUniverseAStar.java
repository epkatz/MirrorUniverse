package mirroruniverse.g2.astar;

import java.util.List;

import mirroruniverse.g2.Map;
import mirroruniverse.g2.Position;

public class MirrorUniverseAStar extends AStar<State> {
	Map leftMap;
	Map rightMap;
	
	public MirrorUniverseAStar(Map leftMap, Map rightMap) {
		this.leftMap = leftMap;
		this.rightMap = rightMap;
	}

	@Override
	protected boolean isGoal(State node) {
		// if we haven't found the exits yet
		if (leftMap.exitPos == null || rightMap.exitPos == null)
			return false;
		
		// now we know the position of the exits
		Position posLeft = node.posLeft;
		Position posRight = node.posRight;
		Position exitLeft = leftMap.exitPos;
		Position exitRight = rightMap.exitPos;
		
		// if they are the same, both left and right
		if(posLeft.x == exitLeft.x && posLeft.y == exitLeft.y
				&& posRight.x == exitRight.x && posRight.y == exitRight.y)
			return true;
		return false;
	}

	@Override
	protected Double g(State from, State to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Double h(State from, State to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<State> generateSuccessors(State node) {
		// TODO Auto-generated method stub
		return null;
	}

}
