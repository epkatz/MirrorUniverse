
package mirroruniverse.g2;

import mirroruniverse.sim.MUMap;

public class Map {

	public String name;
	public Position playerPos;
	public Position exitPos;
	public int[][] map;
	public final int GUARANTEED_SIZE = 5;
	public final int MAX_SIZE = GUARANTEED_SIZE * 2 + 1;
	public enum Tile {
		UNKNOWN (8), BARRIER (1), EMPTY (0), EXIT(2);
		private int value;
		private Tile (int value) {
			this.value = value;
		}
	};
	
	public boolean isExit(Position pos) {
		if (exitPos != null)
			if (exitPos.x == pos.x && exitPos.y == pos.y)
				return true;
		return false;
	}
	
	public boolean isValid(Position pos) {
		if (map[pos.y][pos.x] == Tile.EMPTY.value)
			return true;
		return false;
	}

	public Map(String name) {
		this.name = name;
		playerPos = new Position(MAX_SIZE / 2, MAX_SIZE / 2);
		exitPos = null;

		map = new int[MAX_SIZE][MAX_SIZE];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				map[i][j] = Tile.UNKNOWN.value;
			}
		}
	}
	
	public void updatePlayer(int[] newPos) {

		
		if (map[(playerPos.y+newPos[1])][(playerPos.x+newPos[0])] == Tile.EMPTY.value ||
				map[(playerPos.y+newPos[1])][(playerPos.x+newPos[0])] == Tile.EXIT.value) {
			
			if (exitPos != null) {
				if (playerPos.x == exitPos.x && playerPos.y == exitPos.y) {
					return;
				}
			}
					
			if(name.equals("Right")){
				System.out.println("Player Right: " + playerPos.x + " " + playerPos.y);
				System.out.println("Next Move Right: " + map[(playerPos.x+newPos[0])][(playerPos.y+newPos[1])] );
				playerPos.x += newPos[0];
				playerPos.y += newPos[1];
				System.out.println("Player Right Next Position: " + playerPos.x+ " " + playerPos.y);
			}else{
				//System.out.println("Player Left: " + playerPos.x + " " + playerPos.y);
				//System.out.println("Next Move Left: " + map[(playerPos.x+newPos[0])][(playerPos.y+newPos[1])] );
				playerPos.x += newPos[0];
				playerPos.y += newPos[1];
				//System.out.println("Player Left Next Position: " + playerPos.x + " " + playerPos.y);
			}
			
		}
	}



	public void updateView(int[][] view) {
		int center = view.length / 2;
		
		for (int i = -view.length/2; i <= view.length/2; i++) {
			for (int j = -view.length/2; j <= view.length/2; j++) {
				if (!isLegalPosition(playerPos.y, i) || !isLegalPosition(playerPos.x, j)) {
					continue;
				}
				if (map[playerPos.y + i][playerPos.x + j] == Tile.UNKNOWN.value) {
					map[playerPos.y + i][playerPos.x + j] = view[center + i][center + j];
				}
				if (view[center + i][center + j] == 2) {
					if (exitPos == null) {
						exitPos = new Position(center + i, center + j);
					}
				}
			}
		}
		if (name.equals("Right")) {
			System.out.println(name + " has map\n" + printMap());
			System.out.println(whatIsee(view));
		}
		
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

				if (playerPos.x == j && playerPos.y == i) {
					ret += "X" + "  ";
				} else {
					ret += map[i][j] + "  ";
				}
			}
			ret += "\n";
		}
		return ret;
	}
	
	public boolean isLegalPosition(int v, int add) {
		if (v + add < 0 || v + add >= MAX_SIZE) {
			return false;
		}
		return true;
	}

}
