package mirroruniverse.g2.astar;

import java.util.List;

import mirroruniverse.g2.Map;

public class MirrorUniverseAStar extends AStar<State> {
	Map leftMap;
	Map rightMap;
	
	public MirrorUniverseAStar(Map leftMap, Map rightMap) {
		this.leftMap = leftMap;
		this.rightMap = rightMap;
	}

	@Override
	protected boolean isGoal(State node) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Double g(State from, State to) {
		// TODO Auto-generated method stub
		
		return 1.0;
	}

	@Override
	protected Double h(State from, State to) {
		// TODO Auto-generated method stub
		double x1,x2,y1,y2,deltaX,deltaY,diagonal;
		
		
		x1 = from.posLeft.x;
		y1 = from.posLeft.y;
		x2 = to.posLeft.x;
		y2 = to.posLeft.y;
		deltaX = Math.abs(x1 - x2);
		deltaY = Math.abs(y1 - y2);
		diagonal = Math.max(deltaX, deltaY);
		//double orthogonal = Math.abs(deltaX - deltaY);
		double distanceLeft = diagonal;//+ orthogonal;
		
		x1 = from.posRight.x;
		y1 = from.posRight.y;
		x2 = to.posRight.x;
		y2 = to.posRight.y;
		deltaX = Math.abs(x1 - x2);
		deltaY = Math.abs(y1 - y2);
		diagonal = Math.max(deltaX, deltaY);
		//orthogonal = Math.abs(deltaX - deltaY);
		double distanceRight = diagonal;//+ orthogonal;
		
		return Math.max(distanceLeft, distanceRight);
	}

	@Override
	protected List<State> generateSuccessors(State node) {
		// TODO Auto-generated method stub
		return null;
	}

}
