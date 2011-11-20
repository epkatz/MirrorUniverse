package mirroruniverse.g2;

import mirroruniverse.g2.astar.MirrorUniverseAStar;
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
}
