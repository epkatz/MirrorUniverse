package mirroruniverse.g2;

import java.util.List;
import java.util.Random;

import mirroruniverse.sim.MUMap;
import mirroruniverse.sim.Player;
import mirroruniverse.g2.astar.State;

;

public class WallFlower implements Player {

	private Explorer explorer;
	private RouteFinder routeFinder;
	Map rightMap;
	Map leftMap;
	List<State> route;

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

		if (leftMap.exitPos != null && rightMap.exitPos != null)
			route = routeFinder.searchPath();
		if (route != null) {
			System.out.println("Path Found");
			// follow the path
		}
		return move();
	}

	public int move() {
		Random rdmTemp = new Random();
		int nextX = rdmTemp.nextInt(3);
		int nextY = rdmTemp.nextInt(3);

		int d = MUMap.aintMToD[nextX][nextY];
		System.out.println("Next move is :" + MUMap.aintDToM[d][0] + " "
				+ MUMap.aintDToM[d][1]);

		rightMap.updatePlayer(MUMap.aintDToM[d]);
		leftMap.updatePlayer(MUMap.aintDToM[d]);
		return d;
	}

}
