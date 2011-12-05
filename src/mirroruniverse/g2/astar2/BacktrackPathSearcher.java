package mirroruniverse.g2.astar2;

import java.util.LinkedList;
import java.util.List;

import mirroruniverse.g2.Map;
import mirroruniverse.sim.MUMap;

public class BacktrackPathSearcher extends MUAStar {

	protected Node<Integer> bestNode;
	protected int minCost;
	protected int minDiff;

	public BacktrackPathSearcher(Map leftMap, Map rightMap) {
		super(leftMap, rightMap);
		bestNode = null;
		minCost = Integer.MAX_VALUE;
		minDiff = Integer.MAX_VALUE;
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

	@Override
	public List<Integer> search(Integer start, Integer goal) {
		this.reset();
		this.goal = goal;

		int[] goalCoordinates = Encoder.decode(goal);
		int gx1 = goalCoordinates[0];
		int gy1 = goalCoordinates[1];
		int gx2 = goalCoordinates[2];
		int gy2 = goalCoordinates[3];

		int[] coordinates;
		int x1, y1, x2, y2;

		Node<Integer> root = new Node<Integer>(start, h(start));
		fringe.offer(root);

		while (true) {
			Node<Integer> n = fringe.poll();

			if (n == null) {
				if (bestNode != null)
					return this.constructSolution(bestNode);
				return null;
			}

			if (isGoal(n.state)) {
				return constructSolution(n);
			}
			coordinates = Encoder.decode(n.state);
			x1 = coordinates[0];
			y1 = coordinates[1];
			x2 = coordinates[2];
			y2 = coordinates[3];
			
//			System.out.println("Expand:");
//			for (int c : coordinates)
//				System.out.println(c);
//			System.out.println("Closed:" + closedStates.size());

			if ((x1 == gx1 && y1 == gy1) || (x2 == gx2 && y2 == gy2)) {
				if (this.minDiff > n.h) {
					this.minDiff = n.h;
					if (this.minCost > n.f) {
						this.minCost = n.f;
						this.bestNode = n;
						// short cut
						return constructSolution(n);
					}
				}
			}

			expand(n);
			closedStates.add(n.state);
		}
	}

	protected void reset() {
		this.bestNode = null;
		this.closedStates.clear();
		this.expandedCounter = 0;
		this.fringe.clear();
		this.goal = null;
		this.minCost = Integer.MAX_VALUE;
		this.minDiff = Integer.MAX_VALUE;
	}
}
