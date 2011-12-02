package mirroruniverse.g2.astar2;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import mirroruniverse.g2.Config;

public abstract class AStar<T> {
	protected T goal;

	protected Queue<Node<T>> fringe;
	protected Set<T> closedStates;
	protected int expandedCounter;

	protected abstract int stepCost(T from, T to);

	protected abstract int h(T state);

	protected abstract List<T> generateSuccessors(Node<T> node);

	public AStar() {
		//this.fringe = new PriorityQueue<Node<T>>(Config.DEFAULT_QUEUE_SIZE);
		this.fringe = new LinkedList<Node<T>>();
		this.closedStates = new HashSet<T>();
		this.expandedCounter = 0;
	}

	protected boolean isGoal(T state) {
		return this.goal.equals(state);
	}

	protected void expand(Node<T> node) {
		List<T> successors = generateSuccessors(node);

		for (T s : successors) {
			Node<T> newNode = new Node<T>(s, stepCost(node.state, s), h(s), node);
			fringe.offer(newNode);
		}

		expandedCounter++;
	}

	public List<T> search(T start, T goal) {
		this.goal = goal;
		this.fringe.clear();
		this.closedStates.clear();
		this.expandedCounter = 0;
		
		Node<T> root = new Node<T>(start, h(start));
		fringe.offer(root);

		while (true) {
			Node<T> n = fringe.poll();

			if (n == null)
				break;

			if (isGoal(n.state))
				return constructSolution(n);

			expand(n);
			closedStates.add(n.state);
		}

		return null;
	}

	protected List<T> constructSolution(Node<T> leave) {
		LinkedList<T> path = new LinkedList<T>();

		for (Node<T> n = leave; n != null; n = n.parent) {
			path.addFirst(n.state);
		}

		return path;
	}

	public int getExpandedCounter() {
		return expandedCounter;
	}
}
