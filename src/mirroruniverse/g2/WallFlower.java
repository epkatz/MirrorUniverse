package mirroruniverse.g2;

import mirroruniverse.sim.Player;

public class WallFlower implements Player {

	private Explorer explorer;
	private Info info;
	private RouteFinder routeFinder;
	Map rightMap;
	Map leftMap;
	
	public WallFlower() {
		this.info = new Info();
		this.explorer = new Explorer(this.info);
		this.routeFinder = new RouteFinder(this.info);	
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
	
}
