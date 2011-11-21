package mirroruniverse.g2.astar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import mirroruniverse.g2.Map;
import mirroruniverse.g2.Position;
import mirroruniverse.sim.MUMap;

public class MirrorUniverseAStar extends AStar<State> {
	Map leftMap;
	Map rightMap;

	public MirrorUniverseAStar(Map leftMap, Map rightMap) {
		this.leftMap = leftMap;
		this.rightMap = rightMap;
	}

	@Override
	public boolean isGoal(State node) {
		// if we haven't found the exits yet
		if (leftMap.exitPos == null || rightMap.exitPos == null)
			return false;

		// now we know the position of the exits
		Position posLeft = node.posLeft;
		Position posRight = node.posRight;
		Position exitLeft = leftMap.exitPos;
		Position exitRight = rightMap.exitPos;

		// if they are the same, both left and right
		if (posLeft.x == exitLeft.x && posLeft.y == exitLeft.y
				&& posRight.x == exitRight.x && posRight.y == exitRight.y)
			return true;
		return false;
	}

	@Override
	protected Double g(State from, State to) {
		return 1.0;
	}

	@Override
	protected Double h(State from, State to) {

		// TODO Auto-generated method stub
		double x1,x2,y1,y2,deltaX,deltaY,diagonal;
		
		int[][] leftMap = this.leftMap.map;
		Position leftPos = this.leftMap.playerPos;
		Position exit = this.leftMap.exitPos;
		
		
		
		
		
		

		x1 = from.posLeft.x;
		y1 = from.posLeft.y;
		x2 = to.posLeft.x;
		y2 = to.posLeft.y;
		deltaX = Math.abs(x1 - x2);
		deltaY = Math.abs(y1 - y2);
		diagonal = Math.max(deltaX, deltaY);
		// double orthogonal = Math.abs(deltaX - deltaY);
		double distanceLeft = diagonal;// + orthogonal;

		x1 = from.posRight.x;
		y1 = from.posRight.y;
		x2 = to.posRight.x;
		y2 = to.posRight.y;
		deltaX = Math.abs(x1 - x2);
		deltaY = Math.abs(y1 - y2);
		diagonal = Math.max(deltaX, deltaY);
		// orthogonal = Math.abs(deltaX - deltaY);
		double distanceRight = diagonal;// + orthogonal;

		return Math.max(distanceLeft, distanceRight);
	}

	@Override
	protected List<State> generateSuccessors(State node) {
		List<State> successors = new LinkedList<State>();

		Position posLeft = node.posLeft;
		Position posRight = node.posRight;

		for (int i = 0; i != MUMap.aintDToM.length; ++i) {
			int[] aintMove = MUMap.aintDToM[i];
			int deltaX = aintMove[0];
			int deltaY = aintMove[1];

			Position newPosLeft;
			// if it's on the exit, don't move
			if (leftMap.isExit(posLeft))
				newPosLeft = new Position(posLeft.x, posLeft.y);
			else {
				newPosLeft = new Position(posLeft.x + deltaX, posLeft.y
						+ deltaY);
				if (!leftMap.isValid(newPosLeft)) {
					newPosLeft.x -= deltaX;
					newPosLeft.y -= deltaY;
				}
			}

			Position newPosRight;
			if (rightMap.isExit(posRight))
				newPosRight = new Position(posLeft.x, posLeft.y);
			else {
				newPosRight = new Position(posRight.x + deltaX, posRight.y
						+ deltaY);
				if (!rightMap.isValid(newPosRight)) {
					newPosRight.x -= deltaX;
					newPosRight.y -= deltaY;
				}
			}
		}
		return successors;
	}
}
