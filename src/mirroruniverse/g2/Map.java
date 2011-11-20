
package mirroruniverse.g2;

import mirroruniverse.sim.MUMap;

public class Map {

	public String name;
	public Position playerPos;
	public Position exitPos;
	public int[][] map;
	public final int GUARANTEED_SIZE = 100;
	public final int MAX_SIZE = GUARANTEED_SIZE * 2 + 1;
	public final int RADIUS = 3;
	public enum Tile {
		UNKNOWN (8), BARRIER (1), EMPTY (0), END(2);
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



	public void updateView(int[][] view) {
		int center = view.length / 2;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				map[playerPos.x + j][playerPos.y + i] = view[center + j][center + i];
			}
		}
		System.out.println(name + " has map\n"+printMap());
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
		String ret = "";
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				ret += map[i][j] + " ";
			}
			ret += "\n";
		}
		return ret;
	}

}
