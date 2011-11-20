package mirroruniverse.g2.astar;

import mirroruniverse.g2.Position;

public class State {
	Position posLeft;
	Position posRight;

	public State(Position posLeft, Position posRight) {
		this.posLeft = posLeft;
		this.posRight = posRight;
	}
}
