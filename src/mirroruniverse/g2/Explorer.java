
package mirroruniverse.g2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import mirroruniverse.sim.MUMap;

public class Explorer {
	Map leftMap;
	Map rightMap;
	LinkedList<Position> leftOpenList = new LinkedList<Position>();
	LinkedList<Position> rightOpenList = new LinkedList<Position>();
	public int r = -1;
	public boolean allExplored = false;
	List<Integer> path;
	Backtracker2 backtrack;

	public Explorer(Map leftMap, Map rightMap) {
		this.leftMap = leftMap;
		this.rightMap = rightMap;
		backtrack = new Backtracker2(leftMap, rightMap);
	}
	
	/* Interface for seeing how many new spaces were found
	 * Just call the get to find how many and then reset when you decide to execute A*
	 */

	public int getMove(int[][] aintViewL, int[][] aintViewR) {
		if (r == -1) {
			r = aintViewL.length / 2;
		}
		int d = nextBestSearch();
		int[] diff = MUMap.aintDToM[d];
		while (leftMap.steppingOnExit(diff) || rightMap.steppingOnExit(diff)) {
			d = fallback();
			diff = MUMap.aintDToM[d];
			if (path != null) {
				path.clear();
			}
			System.out.println("Hit Random");
		}
		System.out.println("in get move");
		return d;
	}

	public int nextBestSearch() {
		int d = -1;
		int bestCount = 0;
		int leftCount = 0;
		int rightCount = 0;
		for (int i = 0; i <= 8; i++) {
			int[] diff = MUMap.aintDToM[i];
			leftCount = countNewSpacesOpened(diff, leftMap, leftMap.playerPos);
			rightCount = countNewSpacesOpened(diff, rightMap, rightMap.playerPos);
			if (rightMap.exitPos != null && leftMap.exitPos == null) {
				if (leftCount > bestCount) {
					bestCount = leftCount;
					d = i;
				}
			} else if (leftMap.exitPos != null && rightMap.exitPos == null) {
				if (rightCount > bestCount) {
					bestCount = rightCount;
					d = i;
				}
			} else {
				if (leftCount + rightCount > bestCount) {
					bestCount = leftCount + rightCount;
					d = i;
				}
			}
		}
		if (bestCount != 0) {
			if (leftCount > 1) {
				leftOpenList.push(new Position(leftMap.playerPos.y, leftMap.playerPos.x));
				for (int i = -((r / 2) + 1); i <= (r / 2) + 1; i++) {
					for (int j = -((r / 2) + 1); j <= (r / 2) + 1; j++) {
						if (leftMap.map[leftMap.playerPos.y+i][leftMap.playerPos.x+j] == Map.Tile.EMPTY.getValue()) { 
							leftOpenList.push(new Position(leftMap.playerPos.y+i, leftMap.playerPos.x+j));
						}
					}
				}
			}
			if (rightCount > 1) {
				rightOpenList.push(new Position(rightMap.playerPos.y, rightMap.playerPos.x));
				for (int i = -((r / 2) + 1); i <= (r / 2) + 1; i++) {
					for (int j = -((r / 2) + 1); j <= (r / 2) + 1; j++) {
						if (rightMap.map[rightMap.playerPos.y+i][rightMap.playerPos.x+j] == Map.Tile.EMPTY.getValue()) { 
							rightOpenList.push(new Position(rightMap.playerPos.y+i, rightMap.playerPos.x+j));
						}
					}
				}
			}
			if (path != null) {
				path.clear();
			}
			return d;
		}
		if (path == null || path.isEmpty()) {
			try {
				path = generateBackTrack();
			} catch (Exception e) {
				return 0;
			}
		}
		d = path.remove(0);
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
		myMap.map[current.y][current.x] = Map.Tile.MARKED.getValue();
		//System.out.println(current + " is marked");
		Random r = new Random();
		int[] directions = {1, 2, 3, 4, 5, 6, 7, 8};
		for (int i = 0; i < 7; i++) {
			int slot1 = r.nextInt(8);
			int slot2 = r.nextInt(8);
			int temp = directions[slot1];
			directions[slot1] = directions[slot2];
			directions[slot2] = temp;
		}
		for (int i = 0; i < directions.length; i++) {
			int j = directions[i];
			int[] diff = MUMap.aintDToM[j];
			if (current.y + diff[1] >= myMap.map.length || current.y + diff[1] < 0) {
				continue;
			}
			if (current.x + diff[0] >= myMap.map.length || current.x + diff[0] < 0) {
				continue;
			}
			if (myMap.map[current.y + diff[1]][current.x + diff[0]] == Map.Tile.UNKNOWN.getValue()) {
				return current;
			}
			if (myMap.map[current.y + diff[1]][current.x + diff[0]] == Map.Tile.EMPTY.getValue()) {
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
	
/*	public Position closestUnknown(Map myMap, Position myPos) {
		System.out.println(myMap.printMap());
		int[][] moves = {{0, 1}, {1, 0}, {1, 1}, {0, -1}, {-1, 0}, {-1, -1}, {-1, 1}, {1, -1}};
		LinkedList<Position> openList = new LinkedList<Position>();
		openList.add(myPos);
		while (!openList.isEmpty()) {
			Position current = openList.removeFirst();
			System.out.println(current);
			for (int[] mv: moves) {
				if (myMap.map[current.y + mv[1]][current.x + mv[0]] == Map.Tile.EMPTY.getValue()) {
					Position newP = new Position(current.y + mv[1], current.x + mv[0]);
					if (newP.y < myMap.map.length && newP.y >= 0
							&& newP.x < myMap.map.length && newP.x >= 0) {
						if (myMap.map[newP.y][newP.x] == Map.Tile.UNKNOWN.getValue()) {
							return current;
						}
						if (!openList.contains(newP)) {
							openList.addLast(newP);
						}
					}
				}
			}
			myMap.map[current.y][current.x] = Map.Tile.MARKED.getValue();
		}
		System.exit(0);
		return null;
	}*/


	public List<Integer> generateBackTrack() {
		Position leftPos;
		Position rightPos;
		//System.out.println("Start Backtracker++");
		if (!leftOpenList.isEmpty()) {
			leftPos = pullPosition(leftOpenList, leftMap);
		} else {
			Map newLeft = new Map("left", leftMap);
			leftPos = closestUnknown(newLeft, leftMap.playerPos);
		}
		if (!rightOpenList.isEmpty()) {
			rightPos =pullPosition(rightOpenList, rightMap);
		} else {
			Map newRight = new Map("right", rightMap);
			rightPos = closestUnknown(newRight, rightMap.playerPos);
		}
		List<Integer> ret = null;
		if (rightPos == null && leftPos != null) {
			Backtracker2 newBacktrack = new Backtracker2(leftMap, leftMap);
			ret = newBacktrack.search(leftPos, leftPos);
			if (ret == null || ret.isEmpty()) {
				System.out.println("Cannot explore map");
				return generateOneSidedBackTrack(leftMap, leftMap.playerPos, leftOpenList);
			}
		} else if (rightPos != null && leftPos == null) {
			Backtracker2 newBacktrack = new Backtracker2(rightMap, rightMap);
			ret = newBacktrack.search(rightPos, rightPos);
			if (ret == null || ret.isEmpty()) {
				return generateOneSidedBackTrack(rightMap, rightMap.playerPos, rightOpenList);
			}
		} else {
			ret = backtrack.search(leftPos, rightPos);
			if (ret == null || ret.isEmpty()) {
				return generateBackTrack();
			}
		}
		return ret;
	}
	
	public List<Integer> generateOneSidedBackTrack(Map myMap, Position pos, LinkedList<Position> list) {
		Position tempPos;
		if (!list.isEmpty()) {
			tempPos = list.pop();
		} else {
			Map newMap = new Map("one-sided", myMap);
			tempPos = closestUnknown(newMap, myMap.playerPos);
		}
		List<Integer> ret = null;
		Backtracker2 newBacktrack = new Backtracker2(myMap, myMap);
		ret = newBacktrack.search(tempPos, tempPos);
		if (ret == null || ret.isEmpty()) {
			return generateBackTrack();
		}
		return ret;
	}
	
	public Position pullPosition(LinkedList<Position> list, Map myMap) {
		Position ret = null;
		while (ret == null && !list.isEmpty()) {
			Position newPos = list.peek();
			if (myMap.map[newPos.y][newPos.x] == Map.Tile.EMPTY.getValue()) {
				for (int i = -r; i <= r; i++) {
					for (int j = -r; j <= r; j++) {
						if (myMap.map[newPos.y + i][newPos.x + j] == Map.Tile.UNKNOWN.getValue()) {
							ret = newPos;
							return ret;
						}
					}
				}
			}
			list.pop();
		}
		return ret;
	}

	public int countNewSpacesOpened(int[] diff, Map myMap, Position pos) {
		int ret = 0;
		Position newPos = pos.newPosFromOffset(diff[1], diff[0]);
		if (myMap.map[newPos.y][newPos.x] == Map.Tile.EMPTY.getValue()) {
			for (int i = -r; i <= r; i++) {
				for (int j = -r; j <= r; j++) {
					if (myMap.map[newPos.y + i][newPos.x + j] == Map.Tile.UNKNOWN.getValue()) {
						ret++;
					}
				}
			}
		}
		return ret;
	}
	
	public int countSpacesOpen(Map myMap, Position pos) {
		int ret = 0;
		if (myMap.map[pos.y][pos.x] == Map.Tile.EMPTY.getValue()) {
			for (int i = -((r / 2) + 1); i <= (r / 2) + 1; i++) {
				for (int j = -((r / 2) + 1); j <= (r / 2) + 1; j++) {
					if (myMap.map[pos.y + i][pos.x + j] == Map.Tile.UNKNOWN.getValue()) {
						ret++;
					}
				}
			}
		}
		return ret;
	}

	public int fallback() {
		Random rdmTemp = new Random();
		int nextX = rdmTemp.nextInt(3);
		int nextY = rdmTemp.nextInt(3);
		return MUMap.aintMToD[nextX][nextY];
	}

}