package mirroruniverse.g2;

import java.util.Random;

import mirroruniverse.sim.MUMap;
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
		Random rdmTemp = new Random();
		int nextX = rdmTemp.nextInt(3);
		int nextY = rdmTemp.nextInt(3);
		int d = MUMap.aintMToD[nextX][nextY];
		rightMap.updatePlayer(MUMap.aintDToM[d]);
		leftMap.updatePlayer(MUMap.aintDToM[d]);
		return d;
	}

}
