
package mirroruniverse.g2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import mirroruniverse.g2.Map.Tile;
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
		for (int i = 0; i < view.length; i++) {
			for (int j = 0; j < view.length; j++) {
				if (myMap.map[myMap.playerPos.y + i][myMap.playerPos.x + i] == Map.Tile.EMPTY.getValue()) {
					for (int k = 1; k <= 8; k++) {
						int[] diff = MUMap.aintDToM[k];
						if (myMap.map[myMap.playerPos.y + i + diff[1]][myMap.playerPos.x + j + diff[0]] == Map.Tile.UNKNOWN.getValue()) {
							openList.push(myMap.playerPos.newPosFromOffset(i + diff[1], j + diff[0]));
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
			System.out.println("Best Count");
			return d;
		}
		generateBackTrack();
		d = backtrack.getMove();
		return d;
	}
	
	public Position getFrontier(LinkedList<Position> list, Map myMap) {
		Iterator<Position> itr = list.iterator();
		while (itr.hasNext()) {
			Position p = itr.next();
			if (myMap.map[p.y][p.x] != Map.Tile.UNKNOWN.getValue()) {
				itr.remove();
			} else {
				return p;
			}
		}
		return null;
	}
	
	public Position closestUnknown(Map myMap, Position current) {
		if (myMap.map[current.y][current.x] == Map.Tile.UNKNOWN.getValue()) {
			//System.out.println(current + " is unknown");
			return current;
		} else {
			myMap.map[current.y][current.x] = Map.Tile.MARKED.getValue();
			//System.out.println(current + " is marked");
		}
		for (int j = 1; j <= 8; j++) {
			int[] diff = MUMap.aintDToM[j];
			if (current.y + diff[1] >= myMap.map.length || current.y + diff[1] < 0) {
				continue;
			}
			if (current.x + diff[0] >= myMap.map.length || current.x + diff[0] < 0) {
				continue;
			}
			if (myMap.map[current.y + diff[1]][current.x + diff[0]] == Map.Tile.EMPTY.getValue() || 
					myMap.map[current.y + diff[1]][current.x + diff[0]] == Map.Tile.UNKNOWN.getValue()) {
				//System.out.println("looking at " + new Position(current.y + diff[1], current.x + diff[0]));
				Position newPos = closestUnknown(myMap, new Position(current.y + diff[1], current.x + diff[0]));
				//System.out.println("returned " + newPos);
				if (newPos != null) {
					//System.out.println("****************** " + newPos);
					return newPos;
				}
			}
		}
		return null;
	}
	
	public void generateBackTrack() {
		Map newLeft = new Map("left", leftMap);
		Map newRight = new Map("right", rightMap);
		Position leftPos = closestUnknown(newLeft, leftMap.playerPos);
		Position rightPos = closestUnknown(newRight, rightMap.playerPos);
		System.out.println("Returned " + leftPos);
		System.out.println("Returned " + rightPos);
		//System.exit(0);
		backtrack = new Backtracker(leftMap, rightMap, leftPos, rightPos);
	}
	

/*	public void generateBackTrack() {
		Position leftPos = getFrontier(leftOpenList, leftMap);
		Position rightPos = getFrontier(rightOpenList, rightMap);
		if (leftPos == null) {
			leftPos = leftOpenList.peek();
		}
		if (rightPos == null) {
			rightPos = rightOpenList.peek();
		}
		backtrack = new Backtracker(leftMap, rightMap, leftPos, rightPos);
	}*/

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
		System.exit(0);
		return MUMap.aintMToD[nextX][nextY];
	}

}