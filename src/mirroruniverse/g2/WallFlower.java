package mirroruniverse.g2;

import mirroruniverse.sim.Player;

public class WallFlower implements Player {

	private Explorer explorer;
	private RouteFinder routeFinder;
	Map rightMap;
	Map leftMap;

	public WallFlower() {
		rightMap = new Map("Right");
		leftMap = new Map("Left");
		this.explorer = new Explorer(this.leftMap, this.rightMap);
		this.routeFinder = new RouteFinder(this.leftMap, this.rightMap);
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

}
