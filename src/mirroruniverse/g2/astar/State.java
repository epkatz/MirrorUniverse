package mirroruniverse.g2.astar;

import mirroruniverse.g2.Map.Position;

public class State {
	Position pos1;
	Position pos2;

	public State(Position pos1, Position pos2) {
		this.pos1 = pos1;
		this.pos2 = pos2;
	}
}
