package mirroruniverse.g2.astar;

import mirroruniverse.g2.Position;

public class State {
	Position posLeft;
	Position posRight;

	public State(Position posLeft, Position posRight) {
		this.posLeft = posLeft;
		this.posRight = posRight;
	}

	public boolean equals(State rhs) {
		if (posLeft.x == rhs.posLeft.x && posLeft.y == rhs.posLeft.y
				&& posRight.x == rhs.posRight.x && posRight.y == rhs.posRight.y )
			return true;
		return false;
	}
}
