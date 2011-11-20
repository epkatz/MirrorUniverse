package mirroruniverse.g2;

import mirroruniverse.sim.Player;

public class RouteFinder{
	Info info;
	Explorer explorer;
	RouteFinder routeFinder;
	Map rightMap;
	Map leftMap;
	
	//This hasn't been tested
	public enum Direction {
		NORTH (1), NORTHEAST(2), NORTHWEST(3), SOUTH(4), SOUTHEAST(5), 
		SOUTHWEST(6), EAST(7), WEST(8);
		private int value;
		private Direction (int value) {
			this.value = value;
		}
	};
	
	public RouteFinder () {
		rightMap = new Map("Right");
		leftMap = new Map("Left");
		
	}
	
	
	
	@Override
	public int lookAndMove(int[][] aintViewL, int[][] aintViewR) {
		leftMap.updateView(aintViewL);
		rightMap.updateView(aintViewR);
		return move();
	}
	
	public int move() {
		return 0;
	}
	
	public RouteFinder(Info info){
		this.info = info;
	}
}
