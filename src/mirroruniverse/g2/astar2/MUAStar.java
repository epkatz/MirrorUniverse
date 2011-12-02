package mirroruniverse.g2.astar2;

import java.util.List;

import mirroruniverse.g2.Map;

public abstract class MUAStar extends AStar<Integer> {
	Map leftMap;
	Map rightMap;
	
	public MUAStar(Map leftMap, Map rightMap) {
		this.leftMap = leftMap;
		this.rightMap = rightMap;
	}
	
	@Override
	protected int stepCost(Integer from, Integer to) {
		return 1;
	}

	@Override
	protected int h(Integer state) {
		int[] c1 = Encoder.decode(state);
		int[] c2 = Encoder.decode(this.goal);
		
		int x1, x2, y1, y2, deltaX, deltaY;
		
		x1 = c1[0];
		y1 = c1[1];
		x2 = c2[0];
		y2 = c2[1];
		deltaX = Math.abs(x1 - x2);
		deltaY = Math.abs(y1 - y2);
		int distanceLeft = Math.max(deltaX, deltaY);

		x1 = c1[2];
		y1 = c1[3];
		x2 = c2[2];
		y2 = c2[3];
		deltaX = Math.abs(x1 - x2);
		deltaY = Math.abs(y1 - y2);
		int distanceRight = Math.max(deltaX, deltaY);

		return Math.max(distanceLeft, distanceRight);
	}

}
