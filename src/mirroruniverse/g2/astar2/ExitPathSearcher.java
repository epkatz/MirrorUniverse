package mirroruniverse.g2.astar2;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import mirroruniverse.g2.Config;
import mirroruniverse.g2.Map;
import mirroruniverse.sim.MUMap;

public class ExitPathSearcher extends MUAStar {

	protected Queue<Node<Integer>> hangingNodes;
	protected int hangingNodesExpandedCounter;
	protected boolean needExplored;

	public ExitPathSearcher(Map leftMap, Map rightMap) {
		super(leftMap, rightMap);
		HeuristicValueComparator cmp = new HeuristicValueComparator();
		this.hangingNodes = new PriorityQueue<Node<Integer>>(
				Config.DEFAULT_QUEUE_SIZE, cmp);
		this.needExplored = false;
	}

	public class HeuristicValueComparator implements Comparator<Node<Integer>> {
		@Override
		public int compare(Node<Integer> o1, Node<Integer> o2) {
			return o1.h - o2.h;
		}
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
		
		// if one of the players has reached the exit
		if (leftMap.isExit(x1, y1) || rightMap.isExit(x2, y2)) {
			// put it in hanging queue
			hangingNodes.add(node);
			// do not expand it, return directly
			return successors;
		}

		// now none of the player is on the exit
		for (int i = 1; i != MUMap.aintDToM.length; ++i) {
			aintMove = MUMap.aintDToM[i];
			deltaX = aintMove[0];
			deltaY = aintMove[1];
			
			
			newX1 = x1 + deltaX;
			newY1 = y1 + deltaY;
			// if there is a position that is unknown on the left map
			if (leftMap.isUnknown(newX1, newY1))
				this.needExplored = true;
			if (!leftMap.isValid(newX1, newY1)) {
				// if it's not valid, roll back
				newX1 -= deltaX;
				newY1 -= deltaY;
			}

			newX2 = x2 + deltaX;
			newY2 = y2 + deltaY;
			// if there is a position that is unknown on the left map
			if (rightMap.isUnknown(newX2, newY2))
				this.needExplored = true;
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

		Node<Integer> root = new Node<Integer>(start, h(start));
		fringe.offer(root);

		while (true) {
			Node<Integer> n = fringe.poll();

			if (n == null) {
				if (this.needExplored == true)
					break;

				ExitSubPathSearcher esps = new ExitSubPathSearcher(leftMap,
						rightMap, hangingNodes);
				return esps.search(null, goal);
			}

			if (isGoal(n.state))
				return constructSolution(n);

			expand(n);
			closedStates.add(n.state);
		}
		return null;
	}

	protected void reset() {
		this.closedStates.clear();
		this.fringe.clear();
		this.hangingNodes.clear();
		this.expandedCounter = 0;
		this.hangingNodesExpandedCounter = 0;
		this.needExplored = false;
	}
}
