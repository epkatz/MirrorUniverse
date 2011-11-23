package mirroruniverse.g2.astar;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import mirroruniverse.g2.Config;
import mirroruniverse.g2.Map;
import mirroruniverse.g2.Position;
import mirroruniverse.g2.astar.AStar.Node;
import mirroruniverse.sim.MUMap;

public class ImperfectSolutionAStar extends MirrorUniverseAStar {

	PriorityQueue<Node> hangingNodes;
	int threshold;

	public ImperfectSolutionAStar(Map leftMap, Map rightMap,
			PriorityQueue<Node> hangingNodes) {
		super(leftMap, rightMap);
		this.hangingNodes = hangingNodes;
		threshold = Integer.MAX_VALUE;
	}

	@Override
	protected List<State> generateSuccessors(Node node) {
		List<State> successors = new LinkedList<State>();

		if (closedStates.contains(node.state))
			return successors;

		Position posLeft = node.state.posLeft;
		Position posRight = node.state.posRight;

		if (Config.DEBUG)
			System.out.println("Expand:\n" + node.state);

		// one of the players is on the exit

		for (int i = 1; i != MUMap.aintDToM.length; ++i) {
			int[] aintMove = MUMap.aintDToM[i];
			int deltaX = aintMove[0];
			int deltaY = aintMove[1];

			Position newPosLeft;
			if (leftMap.isExit(posLeft))
				newPosLeft = posLeft;
			else {
				newPosLeft = new Position(posLeft.y + deltaY, posLeft.x
						+ deltaX);
				if (!leftMap.isValid(newPosLeft)) {
					// if it's not valid, roll back
					newPosLeft.x -= deltaX;
					newPosLeft.y -= deltaY;
				}
			}

			Position newPosRight;
			if (rightMap.isExit(posRight))
				newPosRight = posRight;
			else {
				newPosRight = new Position(posRight.y + deltaY, posRight.x
						+ deltaX);
				if (!rightMap.isValid(newPosRight)) {
					// if it's not valid, roll back
					newPosRight.x -= deltaX;
					newPosRight.y -= deltaY;
				}
			}
			State newState = new State(newPosLeft, newPosRight);
			if (!newState.equals(node.state)
					&& (!closedStates.contains(newState)))
				successors.add(newState);
		}

		return successors;
	}

	public List<State> getBestSolution() {
		List<State> bestSolution = null;
		Node bestNode = null;

		while (!hangingNodes.isEmpty()) {
			this.closedStates.clear();
			Node node = hangingNodes.poll();
			List<State> solution = this.compute(node.state);
			if (solution != null) {
				int totalCost = solution.size();
				if (totalCost < threshold) {
					threshold = totalCost;
					bestNode = node;
					bestSolution = solution;
				}
			}
		}

		assert (bestSolution != null);
		List<State> globalSolution = constructSolution(bestNode);
		bestSolution.remove(0);
		globalSolution.addAll(bestSolution);
		return globalSolution;
	}

	public List<State> compute(State start) {
		try {
			Node root = new Node();
			root.setState(start);

			expand(root);

			for (;;) {
				Node p = fringe.poll();

				if (p == null) {
					return null;
				}

				State last = p.getState();

				if (isGoal(last)) {
					return constructSolution(p);
				}

				// prune if there is a better solution
				if (p.f >= threshold)
					return null;

				expand(p);
				closedStates.add(p.state);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
