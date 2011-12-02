package mirroruniverse.g2;

import java.util.Random;

import mirroruniverse.g2.Map.Tile;
import mirroruniverse.g2.astar.State;
import mirroruniverse.sim.MUMap;
import mirroruniverse.sim.Player;

public class Explorer {
	Map leftMap;
	Map rightMap;
	private Position nextTarget;
	private boolean trackMode = false;
	Map currentMap;
	Backtracker backtrack;
	boolean finish = false;
	
	public Explorer(Map leftMap, Map rightMap) {
		this.leftMap = leftMap;
		this.rightMap = rightMap;
		nextTarget = new Position();
		this.currentMap = leftMap;
	}
	
	// random move
	public int getMove() {
//		Random rdmTemp = new Random();
//		int nextX = rdmTemp.nextInt(3);
//		int nextY = rdmTemp.nextInt(3);

		//int d = MUMap.aintMToD[nextX][nextY];
		int d = 0;
		
		if(currentMap.playerPos.x == nextTarget.x && currentMap.playerPos.y == nextTarget.y){
			trackMode = false;
			_updateExploreMapValue();
		}
		
		if(!trackMode){
			d = getBlindMove();
		}
		
		
			if( d == 0){ // first  wall walk complete , look for exit in left over spaces
							
				if(nextTarget.x == 0 ||nextTarget.y == 0){// after first wall walk, calculate the expected dimension
					finish = _syncExploredMap(); //with the actual explored area
					if(finish){
						_findMapDimension();
						_findVacantSpace();
					}
					
					
				}
					Position from = new Position(currentMap.playerPos.y , currentMap.playerPos.x);
					Position to = new Position(nextTarget.y , nextTarget.x);
					generateBackTrack();
					if(backtrack.pathFound()){
						d = backtrack.getMove();
					}
					
					//d = computeMove(from,to);
					//move to target
			
				
			}else{
				if (Config.DEBUG) {
				System.out.println("performing blind walk");
				}
				
				//nextTarget.x = nextTarget.y = 0;
			}
			
	
		
		if (Config.DEBUG) {
			System.out.println("Next move is :" + MUMap.aintDToM[d][0] + " "
					+ MUMap.aintDToM[d][1]);
		}
		
		return d;
	}
	
	
	
	public int getBlindMove(){
		
		
//		int lx = leftMap.playerPos.x;
//		int ly = leftMap.playerPos.y;
//		int[][] lmap = leftMap.map;
//		int[][] rmap = rightMap.map;
//		int[][] exp = leftMap.exploredMap;
//		int nextDirection = 0;
//		
//		if(leftMap.exitPos == null){
//			lmap = leftMap.map;
//			 exp = leftMap.exploredMap;
//		}else if(rightMap.exitPos == null){
//			System.out.println("Left Known -----------------------------------");
//			lmap = rightMap.map;
//			 exp = rightMap.exploredMap;
//		}else{
//			System.out.println("  Both Known -----------------------------------------------------------");
//		}
		int lx , ly;
		int[][] lmap;
		int[][] exp;
		int nextDirection = 0;
		nextTarget.x = nextTarget.y = 0;
		
		if( leftMap.exitPos == null ){//|| !found()
			//System.out.println("  Left Not  Known -----------------------------------------------------------");
			currentMap = leftMap;
		}else if(rightMap.exitPos == null){
			System.out.println("Left Known -----------------------------------");
			currentMap = rightMap;
		}else{
			//go to exit
			System.out.println("  Both Known -----------------------------------------------------------");
			if(leftMap.exitPos.x != leftMap.playerPos.x || leftMap.exitPos.y != leftMap.exitPos.y){
				System.out.println("target exit" + leftMap.exitPos.x + " "  + leftMap.exitPos.y);
				currentMap = leftMap;
				nextTarget.x = leftMap.exitPos.x;
				nextTarget.y = leftMap.exitPos.y;
				trackMode = true;
				return 0;
			}
			else if (rightMap.exitPos.x != rightMap.playerPos.x || rightMap.exitPos.y != rightMap.exitPos.y){
				currentMap = leftMap;
				nextTarget.x = rightMap.exitPos.x;
				nextTarget.y = rightMap.exitPos.y;
				trackMode = true;
				return 0;
			}else{
				System.out.println("HURRAYyyyyyyyyyyyyyyyy!!!!!!!!!!!");
			}
			
		}
		
//		if(leftMap.exitPos == null){
//			currentMap = leftMap;
//		}else if(rightMap.exitPos == null){
//			System.out.println("Left Known -----------------------------------");
//			currentMap = rightMap;
//		}else{
//			System.out.println("  Both Known -----------------------------------------------------------");
//		}
		
		lx = currentMap.playerPos.x;
		ly = currentMap.playerPos.y;
		lmap = currentMap.map;
		
		exp = currentMap.exploredMap;
		
	
					if(lmap[ly][lx+1] != Map.Tile.BARRIER.getValue() && exp[ly][lx+1] != 5 && (exp[ly][lx+1] != 6 || !finish)  && _ifExplore() && _checkEntropy(ly, lx+1, Constants.EAST)){
						nextDirection = Constants.EAST;
					}else if(lmap[ly+1][lx] != Map.Tile.BARRIER.getValue() && exp[ly+1][lx] != 5 && (exp[ly+1][lx] != 6 || !finish) && _checkEntropy(ly+1, lx, Constants.SOUTH)){
						nextDirection = Constants.SOUTH;
					}else if(lmap[ly][lx-1] != Map.Tile.BARRIER.getValue() && exp[ly][lx-1] != 5 && (exp[ly][lx-1] != 6 || !finish) && _checkEntropy(ly, lx-1, Constants.WEST)){
						nextDirection = Constants.WEST;
					}else if(lmap[ly-1][lx] != Map.Tile.BARRIER.getValue() && exp[ly-1][lx] != 5 && (exp[ly-1][lx] != 6 || !finish) && _checkEntropy(ly-1, lx, Constants.NORTH)){
						nextDirection = Constants.NORTH;
			        }else if(lmap[ly+1][lx+1] != Map.Tile.BARRIER.getValue() && exp[ly+1][lx+1] != 5 && (exp[ly+1][lx+1] != 6 || !finish) && _checkEntropy(ly+1, lx+1, Constants.SOUTHEAST)){
			        	nextDirection = Constants.SOUTHEAST;
			        }
			        else if(lmap[ly+1][lx-1] != Map.Tile.BARRIER.getValue() && exp[ly+1][lx-1] != 5 && (exp[ly+1][lx-1] !=6 || !finish) && _checkEntropy(ly+1, lx-1, Constants.SOUTHWEST)){
			        	nextDirection = Constants.SOUTHWEST;
			        }else if(lmap[ly-1][lx-1] != Map.Tile.BARRIER.getValue() && exp[ly-1][lx-1] != 5 && (exp[ly-1][lx-1] != 6 || !finish) && _checkEntropy(ly-1, lx-1, Constants.NORTHWEST)){
			        	nextDirection = Constants.NORTHWEST;
			        }else if(lmap[ly-1][lx+1] != Map.Tile.BARRIER.getValue() && exp[ly-1][lx+1] != 5 && (exp[ly-1][lx+1] != 6 || !finish) && _checkEntropy(ly-1, lx+1, Constants.NORTHEAST)){
			        	nextDirection = Constants.NORTHEAST;
			        }
			
					if(nextDirection == 0){
						System.out.println("I am blocked  here X = " + lx + "  Y = " + ly);
					}
		return nextDirection;
		
	}
	
	private boolean _checkEntropy(int y , int x, int direction){
		int[][] lmap = currentMap.exploredMap;
		int sight = 2;
		for(int i =1; i<=sight; i++){
//			if(lmap[y][x+i] == 5 && direction == Constants.EAST){
//				return false;
//			}else if (lmap[y][x-i] == 5 && direction == Constants.WEST){
//				return false;
//			}else if(lmap[y + i][x] == 5 && direction == Constants.SOUTH){
//				return false;
//			}else if(lmap[y - i][x] == 5 && direction == Constants.NORTH){
//				return false;
//			}
			if(lmap[y][x+i] == 5 && direction != Constants.WEST && direction!=Constants.NORTH){
				return false;
			}else if (lmap[y][x-i] == 5 && direction != Constants.EAST && direction!=Constants.NORTH){
				return false;
			}else if(lmap[y + i][x] == 5 && direction != Constants.NORTH){
				return false;
			}else if(lmap[y - i][x] == 5 && direction != Constants.SOUTH){
				return false;
			}else if(lmap[y - i][x - i] == 5 && direction != Constants.SOUTHEAST){
				
			}else if(lmap[y - i][x + i] == 5 && direction != Constants.SOUTHWEST){
				
			}else if(lmap[y + i][x - i] == 5 && direction != Constants.NORTHEAST){
				
			}else if(lmap[y + i][x + i] == 5 && direction != Constants.NORTHWEST){
				
			}
			
//			else if(lmap[ly+1][lx+1] != Map.Tile.BARRIER.getValue() && exp[ly+1][lx+1] != 5){
//	        	nextDirection = Constants.SOUTHEAST;
//	        }
//	        else if(lmap[ly+1][lx-1] != Map.Tile.BARRIER.getValue() && exp[ly+1][lx-1] != 5){
//	        	nextDirection = Constants.SOUTHWEST;
//	        }else if(lmap[ly-1][lx-1] != Map.Tile.BARRIER.getValue() && exp[ly-1][lx-1] != 5){
//	        	nextDirection = Constants.NORTHWEST;
//	        }else if(lmap[ly-1][lx+1] != Map.Tile.BARRIER.getValue() && exp[ly-1][lx+1] != 5){
//	        	nextDirection = Constants.NORTHEAST;
//	        }
			
		}
		
		
		return true;
	}
	
	private void _findMapDimension(){
		_updateExploreMapValue();
		int[][] lmap = currentMap.exploredMap;
		int xmin = 0 ,xmax = 0 ,ymin = 0,ymax =0;
		
		
		for(int j = 0; j< lmap.length ; j++){
			
			for(int i = 0 ; i < lmap.length ; i++){
				
				if(lmap[j][i] == 6){
					if(xmin ==0){
						xmin = i;
						ymin = j;
					}else{
						xmax = i;
						ymax= j;
					}
				}
			}
		}
		
		currentMap.xmin = xmin;
		currentMap.xmax = xmax;
		currentMap.ymax = ymax;
		currentMap.ymin = ymin;
		
	}
	
	private void _findVacantSpace(){
		_updateExploreMapAfterFinish();
		int[][] lmap = currentMap.map;
		int xmin = currentMap.xmin+1 ,xmax = currentMap.xmax-1 ,ymin = currentMap.ymin+1,ymax = currentMap.ymax-1;
		
		
			while(true){
				for(int j=ymin; j<=ymax; j++)
				for(int i=xmin; i<=xmax; i++){
					
					if(lmap[j][i+1] == Tile.UNKNOWN.getValue()){
						nextTarget.x = i;
						nextTarget.y = j;
						trackMode = true;
						System.out.println("theval1234");
						return;
					}
				}
				
//				for(int i=xmin; i<=xmax; i++){
//					
//					if(lmap[ymax][i+1] == Tile.UNKNOWN.getValue()){
//						nextTarget.x = i;
//						nextTarget.y = ymax;
//						trackMode = true;
//						return;
//					}
//				}
//			
//				for(int i=ymin; i<=ymax; i++){
//					
//					if(lmap[i+1][xmin] == Tile.UNKNOWN.getValue()){
//						nextTarget.x = xmin;
//						nextTarget.y = i;
//						trackMode = true;
//						return;
//					}
//				}
//				
//				for(int i=ymin; i<=ymax; i++){
//					
//					if(lmap[i+1][xmax] == Tile.UNKNOWN.getValue()){
//						nextTarget.x = xmax;
//						nextTarget.y = i;
//						trackMode = true;
//						return;
//					}	
//				}
//				ymin =  ymin+1;
//				xmin = xmin+1;
//				ymax = ymax-1;
//				xmax = xmax-1;
//				
			
			}
			
			
	}
	
	private boolean _ifExplore(){
		int[][] lmap = currentMap.exploredMap;
		int lx = currentMap.playerPos.x;
		int ly = currentMap.playerPos.y;
		
		if(nextTarget.x !=0 || nextTarget.y != 0){
			//exploring empty spaces, first wall walk over
			return true;
		}
		//if we know the extreme end of this row , no ned to explore
		for(int i = lx+1 ;  i < lmap.length ; i++){
			if(lmap[ly][i] == 5){
				return false;
			}
		}
		return true;
	}
	
	public int computeMove(Position from, Position to) {
		double x1, x2, y1, y2, deltaX, deltaY, diagonal;
//		x1 = from.posLeft.x;
//		y1 = from.posLeft.y;
//		x2 = to.posLeft.x;
//		y2 = to.posLeft.y;
		x1 = from.x;
		y1 = from.y;
		x2 = to.x;
		y2 = to.y;
		int directionLeft = -1;

		if (x1 == x2 && y1 == y2) {
			directionLeft = 0; // stay put
		} else if (x1 == x2 && y1 < y2) {
			directionLeft = 7; // move down
		} else if (x1 == x2 && y1 > y2) {
			directionLeft = 3; // move up
		} else if (y1 == y2 && x1 < x2) {
			directionLeft = 1; // move right
		} else if (y1 == y2 && x1 > x2) {
			directionLeft = 5; // move left
		} else if (x1 < x2 && y1 < y2) {
			directionLeft = 8; // move down- right (south east)
		} else if (x1 > x2 && y1 > y2) {
			directionLeft = 4; // up left ( north west)
		} else if (x1 > x2 && y1 < y2) {
			directionLeft = 6; // down left (south west)
		} else if (x1 < x2 && y1 > y2) {
			directionLeft = 2; // up right (north east)
		}

//		x1 = from.posRight.x;
//		y1 = from.posRight.y;
//		x2 = to.posRight.x;
//		y2 = to.posRight.y;
//		int directionRight = -1;
//
//		if (x1 == x2 && y1 == y2) {
//			directionRight = 0; // stay put
//		} else if (x1 == x2 && y1 < y2) {
//			directionRight = 7; // move down
//		} else if (x1 == x2 && y1 > y2) {
//			directionRight = 3; // move up
//		} else if (y1 == y2 && x1 < x2) {
//			directionRight = 1; // move right
//		} else if (y1 == y2 && x1 > x2) {
//			directionRight = 5; // move left
//		} else if (x1 < x2 && y1 < y2) {
//			directionRight = 8; // move down- right (south east)
//		} else if (x1 > x2 && y1 > y2) {
//			directionRight = 4; // up left ( north west)
//		} else if (x1 > x2 && y1 < y2) {
//			directionRight = 6; // down left (south west)
//		} else if (x1 < x2 && y1 > y2) {
//			directionRight = 2; // up right (north east)
//		}

		//assert (directionLeft != -1 && directionRight != -1);
		//return directionLeft != 0 ? directionLeft : directionRight; // return
																	// the
																	// none-zero
																	// one
		return directionLeft;
	}
	
	private boolean _syncExploredMap(){
		int[][] lmap = currentMap.exploredMap;
		int[][] actualmap = currentMap.map;
		
		
		int xmin = 0 ,xmax = 0 ,ymin = 0,ymax =0;
		
		
		
		for(int j = 0; j< actualmap.length ; j++){
			
			for(int i = 0 ; i < actualmap.length ; i++){
				
				if(actualmap[j][i] == 0){
					if(xmin ==0){
						xmin = i;
						ymin = j;
					}else{
						xmax = i;
						ymax= j;
					}
				}
			}
		}
//		Position memory = new Position();
//		for(int i = 0; i<=xmax ; i++){
//			if(actualmap[ymin][i] == Tile.BARRIER.getValue() && actualmap[ymin][i-1] == Tile.UNKNOWN.getValue()){
//				
//				nextTarget.x = i-1;
//				nextTarget.y = ymin;
//				trackMode = true;
//				
//			}
//		}
		for(int j = 0; j< lmap.length ; j++){
			
			for(int i = 0 ; i < lmap.length ; i++){
				
				if(lmap[j][i] ==5){
					lmap[j][i] = 6;
				}
			}
		}
		if(lmap[ymin][xmin] != 6){ //handle if this boundary
			nextTarget.x = xmin;
			nextTarget.y = ymin;
			trackMode = true;
		}
		if(nextTarget.x != 0 || nextTarget.y != 0){
			return false;
		}
//		for(int j = 0; j< lmap.length ; j++){
//			
//			for(int i = 0 ; i < lmap.length ; i++){
//				
//				if(actualmap[j][i] == Tile.BARRIER.getValue() || actualmap[j][i] == Tile.EMPTY.getValue()){
//					lmap[j][i] = 5;
//				}
//			}
//		}
		return true;
	
	}
	
	private void _updateExploreMapValue(){
		int[][] lmap = currentMap.exploredMap;
		
		for(int j = 0; j< lmap.length ; j++){
			
			for(int i = 0 ; i < lmap.length ; i++){
				
				if(lmap[j][i] ==5){
					lmap[j][i] = 6;
				}
			}
		}	
	}
	
	private void _updateExploreMapAfterFinish(){
		int[][] lmap = currentMap.exploredMap;
		int[][] actualmap = currentMap.map;
		for(int j = 0; j< lmap.length ; j++){
		
		for(int i = 0 ; i < lmap.length ; i++){
			
			if(actualmap[j][i] == Tile.EMPTY.getValue()){
				lmap[j][i] = 6;
			}
		}
	}
	}
	
	private boolean found(){
		int[][] lmap = currentMap.map;
		for(int j = 26; j< 31 ; j++){
			
			for(int i = 18 ; i < 35 ; i++){
				
				if(lmap[j][i] == Tile.UNKNOWN.getValue()){
					return false;
				}
			}
		}
		 return true;
	}
	private boolean found1(){
		int[][] lmap = rightMap.map;
		for(int j = 12; j< 14 ; j++){
			
			for(int i = 18 ; i < 37 ; i++){
				
				if(lmap[j][i] == Tile.UNKNOWN.getValue()){
					return false;
				}
			}
		}
		 return true;
	}
	public void generateBackTrack() {
		Position from = new Position(currentMap.playerPos.y , currentMap.playerPos.x);
		Position to = new Position(nextTarget.y , nextTarget.x);
		//Position leftPos = closestUnknown(newLeft, leftMap.playerPos);
		//Position rightPos = closestUnknown(newRight, rightMap.playerPos);
		//System.out.println("Returned " + leftPos);
		//System.out.println("Returned " + rightPos);
		//System.exit(0);
		backtrack = new Backtracker(currentMap, currentMap, to, to);
	}
}

