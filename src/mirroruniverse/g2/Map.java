
package mirroruniverse.g2;

import mirroruniverse.sim.MUMap;

public class Map {

	public String name;
	public Position playerPos;
	public Position exitPos;
	public int[][] map;
	public final int GUARANTEED_SIZE = 5;
	public final int MAX_SIZE = GUARANTEED_SIZE * 2 + 1;
	public final int RADIUS = 3;
	public enum Tile {
		UNKNOWN (8), BARRIER (1), EMPTY (0), EXIT(2);
		private int value;
		private Tile (int value) {
			this.value = value;
		}
	};

	public Map(String name) {
		this.name = name;
		playerPos = new Position();
		playerPos.x = MAX_SIZE / 2;
		playerPos.y = MAX_SIZE / 2;
		exitPos = null;

		map = new int[MAX_SIZE][MAX_SIZE];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				map[i][j] = Tile.UNKNOWN.value;
			}
		}
	}
	
	public void updatePlayer(int[] newPos) {
		if (map[(playerPos.x+newPos[0])][(playerPos.y+newPos[1])] == Tile.EMPTY.value ||
				map[(playerPos.x+newPos[0])][(playerPos.y+newPos[1])] == Tile.EXIT.value) {
			playerPos.x += newPos[0];
			playerPos.y += newPos[1];
		}
	}

	public class Position {
		int x;
		int y;
	}

	public void updateView(int[][] view) {
		int center = view.length / 2;
		for (int i = -view.length/2; i <= view.length/2; i++) {
			for (int j = -view.length/2; j <= view.length/2; j++) {
				map[playerPos.x + i][playerPos.y + j] = view[center + i][center + j];
				if (view[center + i][center + j] == 2) {
					if (exitPos == null) {
						exitPos = new Position();
						exitPos.x = center + i;
						exitPos.y = center + j;
					}
				}
			}
		}
		if (name.equals("Right"))
			System.out.println(name + " has map\n" + printMap());
	}

	private String whatIsee(int[][] view) {
		String ret = "";
		for (int i = 0; i < view.length; i++) {
			for (int j = 0; j < view.length; j++) {
				ret += view[i][j] + " ";
			}
			ret += "\n";
		}
		return ret;
	}
	
	private String printMap() {
		String ret = "   ";
		for (int i = 0; i < map.length; i++) {
			if (i < 10)
				ret += i + "  ";
			else
				ret += i + " ";
		}
		ret += "\n";
		for (int i = 0; i < map.length; i++) {
			if (i < 10)
				ret += i + "  ";
			else
				ret += i + " ";
			for (int j = 0; j < map.length; j++) {
				if (playerPos.x == i && playerPos.y == j) {
					ret += "X" + "  ";
				} else {
					ret += map[i][j] + "  ";
				}
			}
			ret += "\n";
		}
		return ret;
	}

}
