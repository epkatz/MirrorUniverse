package mirroruniverse.g2.astar2;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import mirroruniverse.g2.Map;
import mirroruniverse.sim.MUMap;

public class MinDiffCalculator extends MUAStar {

	Set<Integer> reachable;
	
	public MinDiffCalculator(Map leftMap, Map rightMap, Set<Integer> reachable) {
		super(leftMap, rightMap);
		this.reachable = reachable;
	}
	
	@Override
	protected boolean isGoal(Integer state) {
		return reachable.contains(state);
	}

	@Override
	protected List<Integer> generateSuccessors(Node<Integer> node) {
		List<Integer> successors = new LinkedList<Integer>();

		if (closedStates.contains(node.state))
			return successors;

		int[] coordinates = Encoder.decode(node.state);
		int x1 = coordinates[0];
		int y1 = coordinates[1];
		int x2 = coordinates[2];
		int y2 = coordinates[3];

		int[] aintMove;
		int deltaX, deltaY, newX1, newY1, newX2, newY2;

		// now none of the player is on the exit
		for (int i = 1; i != MUMap.aintDToM.length; ++i) {
			aintMove = MUMap.aintDToM[i];
			deltaX = aintMove[0];
			deltaY = aintMove[1];

			newX1 = x1 + deltaX;
			newY1 = y1 + deltaY;
			if (leftMap.isExit(newX1, newY1))
				continue;
			if (!leftMap.isValid(newX1, newY1)) {
				// if it's not valid, roll back
				newX1 -= deltaX;
				newY1 -= deltaY;
			}

			newX2 = x2 + deltaX;
			newY2 = y2 + deltaY;
			if (rightMap.isExit(newX2, newY2))
				continue;
			if (!rightMap.isValid(newX2, newY2)) {
				// if it's not valid, roll back
				newX2 -= deltaX;
				newY2 -= deltaY;
			}

			Integer newState = Encoder.encode(newX1, newY1, newX2, newY2);
			if (!newState.equals(node.state)
					&& (!closedStates.contains(newState)))
				successors.add(newState);
		}
		return successors;
	}

}
