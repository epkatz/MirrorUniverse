package mirroruniverse.g2.astar;

import java.util.List;

import mirroruniverse.g2.Map;

public class MirrorUniverseAStar extends AStar<State> {
	Map leftMap;
	Map rightMap;
	
	public MirrorUniverseAStar(Map leftMap, Map rightMap) {
		this.leftMap = leftMap;
		this.rightMap = rightMap;
	}

	@Override
	protected boolean isGoal(State node) {
		// TODO Auto-generated method stub
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
