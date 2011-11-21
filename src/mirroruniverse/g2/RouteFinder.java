package mirroruniverse.g2;

import java.util.List;

import mirroruniverse.g2.astar.MirrorUniverseAStar;
import mirroruniverse.g2.astar.State;
import mirroruniverse.sim.Player;

public class RouteFinder {
	Map leftMap;
	Map rightMap;
	MirrorUniverseAStar Astar;

	public RouteFinder(	Map leftMap, Map rightMap) {
		this.leftMap = leftMap;
		this.rightMap = rightMap;
		this.Astar = new MirrorUniverseAStar(leftMap, rightMap);
	}
	
	public List<State> searchPath() {
		// the initial state is the current position of the player
		return Astar.compute(new State(leftMap.playerPos, rightMap.playerPos));
	}
}
