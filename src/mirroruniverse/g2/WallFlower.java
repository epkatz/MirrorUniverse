package mirroruniverse.g2;

import mirroruniverse.sim.Player;

public class WallFlower implements Player {

	private Explorer explorer;
	private Info info;
	private RouteFinder routeFinder;
	
	public WallFlower() {
		this.info = new Info();
		this.explorer = new Explorer(this.info);
		this.routeFinder = new RouteFinder(this.info);	
	}

	@Override
	public int lookAndMove(int[][] aintViewL, int[][] aintViewR) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
