package mirroruniverse.g2.astar2;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import mirroruniverse.g2.Map;
import mirroruniverse.sim.MUMap;

public class ExitSubPathSearcher extends MUAStar {

	protected Queue<Node<Integer>> hangingNodes;
	protected int minDiff;

	public ExitSubPathSearcher(Map leftMap, Map rightMap,
			Queue<Node<Integer>> hangingNodes, int minDiff) {
		super(leftMap, rightMap);
		this.hangingNodes = hangingNodes;
		this.minDiff = minDiff;
	}

	@Override
	protected List<Integer> generateSuccessors(Node<Integer> node) {
		List<Integer> successors = new LinkedList<Integer>();

		if (closedStates.contains(node.state))
			return successors;

		int[] coordinates = Encoder.decode(node.state);
		final int x1 = coordinates[0];
		final int y1 = coordinates[1];
		final int x2 = coordinates[2];
		final int y2 = coordinates[3];
		
		int[] aintMove;
		int deltaX, deltaY, newX1, newY1, newX2, newY2;

		for (int i = 1; i != MUMap.aintDToM.length; ++i) {
			aintMove = MUMap.aintDToM[i];
			deltaX = aintMove[0];
			deltaY = aintMove[1];
			
			newX1 = x1;
			newY1 = y1;
			newX2 = x2;
			newY2 = y2;

			if (!leftMap.isExit(x1, y1)) {
				newX1 = x1 + deltaX;
				newY1 = y1 + deltaY;

				if (!leftMap.isValid(newX1, newY1)) {
					// if it's not valid, roll back
					newX1 -= deltaX;
					newY1 -= deltaY;
				}
			}

			if (!rightMap.isExit(x2, y2)) {
				newX2 = x2 + deltaX;
				newY2 = y2 + deltaY;

				if (!rightMap.isValid(newX2, newY2)) {
					// if it's not valid, roll back
					newX2 -= deltaX;
					newY2 -= deltaY;
				}
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
		this.goal = goal;
		List<Integer> bestSubSolution = null;
		int minSubCost = this.minDiff == Integer.MAX_VALUE ? Integer.MAX_VALUE : this.minDiff + 1;
		int minCost = Integer.MAX_VALUE;
		Node<Integer> bestCandidate = null;

		while (!hangingNodes.isEmpty()) {
			Node<Integer> candidate = hangingNodes.poll();
			
			List<Integer> subSolution = compute(candidate.state, minSubCost);
			if (subSolution == null)
				continue;
			int subCost = subSolution.size();
			if (subCost <= minSubCost) {
				minSubCost = subCost;
				if (candidate.g + subCost < minCost) {
					bestCandidate = candidate;
					bestSubSolution = subSolution;
				}
			}
		}
		
		if (bestCandidate == null)
			return null;
		List<Integer> bestSolution = this.constructSolution(bestCandidate);
		bestSubSolution.remove(0);
		int diff = bestSubSolution.size();
		bestSolution.addAll(bestSubSolution);
		return bestSolution;
	}

	public List<Integer> compute(Integer candidate, int depthLimit) {
		this.closedStates.clear();
		this.fringe.clear();
		Node<Integer> root = new Node<Integer>(candidate, h(candidate));
		fringe.offer(root);
		while (true) {
			Node<Integer> node = fringe.poll();
			if (node == null)
				return null;

			int[] c = Encoder.decode(node.state);
			if (node.g > depthLimit)
				return null;

			if (this.isGoal(node.state))
				return constructSolution(node);

			expand(node);
			closedStates.add(node.state);
		}
	}
}
