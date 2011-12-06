package mirroruniverse.g2.astar2;

import java.util.LinkedList;
import java.util.List;

import mirroruniverse.g2.Map;
import mirroruniverse.g2.Position;
import mirroruniverse.sim.MUMap;

public class BFSUnknown extends AStar<Position> {
	
	Map map;
	
	public BFSUnknown(Map map) {
		this.map = map;
	}

	public Position search(Position start) {
		this.fringe.clear();
		this.closedStates.clear();
		this.expandedCounter = 0;
		
		Node<Position> root = new Node<Position>(start, h(start));
		fringe.offer(root);

		while (true) {
			Node<Position> n = fringe.poll();

			if (n == null)
				break;

			if (isGoal(n.state))
				return n.parent.state;

			expand(n);
			closedStates.add(n.state);
		}

		return null;
	}
	
	@Override
	protected List<Position> generateSuccessors(Node<Position> node) {
		List<Position> successors = new LinkedList<Position>();

		if (closedStates.contains(node.state))
			return successors;

		int x = node.state.x;
		int y = node.state.y;
		
		int[] aintMove;
		int deltaX, deltaY, newX, newY;

		for (int i = 1; i != MUMap.aintDToM.length; ++i) {
			aintMove = MUMap.aintDToM[i];
			deltaX = aintMove[0];
			deltaY = aintMove[1];
			
			
			newX = x + deltaX;
			newY = y + deltaY;
			// if there is a position that is unknown on the left map
			if (map.isObstacle(newX, newY) || map.isExit(newX, newY)) {
				// if it's not valid, roll back
				newX -= deltaX;
				newY -= deltaY;
			}

			Position newPos = new Position(newY, newX);
			if (!newPos.equals(node.state)
					&& (!closedStates.contains(newPos)))
				successors.add(newPos);
		}
		return successors;
	}
	
	@Override
	protected boolean isGoal(Position p) {
		return this.map.isUnknown(p);
	}

	@Override
	protected int h(Position state) {
		return 0;
	}

	@Override
	protected int stepCost(Position from, Position to) {
		return 1;
	}


}
