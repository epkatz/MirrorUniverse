
package mirroruniverse.g2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import mirroruniverse.sim.MUMap;

public class Explorer {
	Map leftMap;
	Map rightMap;
	LinkedList<Position> leftOpenList = new LinkedList<Position>();
	LinkedList<Position> rightOpenList = new LinkedList<Position>();
	public int r = -1;
	public boolean allExplored = false;
	Backtracker backtrack;

	public Explorer(Map leftMap, Map rightMap) {
		this.leftMap = leftMap;
		this.rightMap = rightMap;
	}

	public int getMove(int[][] aintViewL, int[][] aintViewR) {
		if (r == -1) {
			r = aintViewL.length / 2;
		}
		addToFrontier(leftOpenList, aintViewL, leftMap);
		addToFrontier(rightOpenList, aintViewR, rightMap);
		int d = nextBestSearch();
		System.out.println(d);
		return d;
	}

	public void addToFrontier(LinkedList<Position> openList, int[][] view, Map myMap) {
		Iterator<Position> itr = openList.iterator();
		while (itr.hasNext()) {
			Position p = itr.next();
			if (myMap.map[p.y][p.x] != Map.Tile.UNKNOWN.getValue()) {
				itr.remove();
			}
		}
		for (int i = 0; i < view.length; i++) {
			for (int j = 0; j < view.length; j++) {
				if (myMap.map[myMap.playerPos.y + i][myMap.playerPos.x + i] == Map.Tile.EMPTY.getValue()) {
					for (int k = 1; k <= 8; k++) {
						int[] diff = MUMap.aintDToM[k];
						if (myMap.map[myMap.playerPos.y + i + diff[1]][myMap.playerPos.x + j + diff[0]] == Map.Tile.UNKNOWN.getValue()) {

							openList.add(myMap.playerPos.newPosFromOffset(i + diff[1], j + diff[0]));
						}
					}
				}
			}
		}
	}

	public int nextBestSearch() {
		int d = -1;
		int bestCount = 0;
		for (int i = 0; i <= 8; i++) {
			int[] diff = MUMap.aintDToM[i];
			int leftCount = countNewSpacesOpened(diff, leftMap, leftMap.playerPos);
			int rightCount = countNewSpacesOpened(diff, rightMap, rightMap.playerPos);
			if (leftCount + rightCount > bestCount) {
				bestCount = leftCount + rightCount;
				d = i;
			}
		}
		if (bestCount != 0) {
			return d;
		} else {
			generateBackTrack();
		}
		while (d == -1) {
			int tempD;
			if (!backtrack.pathFound()) {
				tempD = randomness();
			} else {
				tempD = backtrack.getMove();
			}
			int[] diff = MUMap.aintDToM[tempD];
			if (leftMap.map[leftMap.playerPos.y + diff[1]][rightMap.playerPos.x + diff[0]] != Map.Tile.EXIT.getValue()
					&& rightMap.map[rightMap.playerPos.y + diff[1]][rightMap.playerPos.x + diff[0]] != Map.Tile.EXIT.getValue()) {
				d = tempD;
				break;
			}
			System.out.println("loop");
		}
		return d;
	}

	public void generateBackTrack() {
		Position leftPos;
		Position rightPos;
		if (!leftOpenList.isEmpty()) {
			leftPos = leftOpenList.peek();
		} else {
			leftPos = new Position(0, 0);
		}
		if (!rightOpenList.isEmpty()) {
			rightPos = rightOpenList.peek();
		} else {
			rightPos = new Position(0, 0);
		}
		backtrack = new Backtracker(leftMap, rightMap, leftPos, rightPos);
	}

	public int countNewSpacesOpened(int[] diff, Map myMap, Position pos) {
		int ret = 0;
		Position newPos = pos.newPosFromOffset(diff[1], diff[0]);
		if (myMap.map[newPos.y][newPos.x] == Map.Tile.EMPTY.getValue()) {
			for (int i = -((r / 2) + 1); i <= (r / 2) + 1; i++) {
				for (int j = -((r / 2) + 1); j <= (r / 2) + 1; j++) {
					if (myMap.map[newPos.y + i][newPos.x + j] == Map.Tile.UNKNOWN.getValue()) {
						ret++;
					}
				}
			}
		}
		return ret;
	}

	public int randomness() {
		Random rdmTemp = new Random();
		int nextX = rdmTemp.nextInt(3);
		int nextY = rdmTemp.nextInt(3);
		System.out.println("RANDOM");
		return MUMap.aintMToD[nextX][nextY];
	}

}