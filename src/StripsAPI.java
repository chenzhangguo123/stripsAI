

public class StripsAPI {
/* -------------------------------- fields *-------------------------------- */
	
	private GameGraphics game;

/* ---------------------------- Constant Values ---------------------------- */

	// Rooms definition:  in the format RecInfo(x1,x2,y1,y2)
	public final RecInfo ROOM1 = new RecInfo(0,7,0,4);
	public final RecInfo ROOM2 = new RecInfo(0,7,5,11);
	public final RecInfo ROOM3 = new RecInfo(8,19,0,11);
	public final RecInfo DOORWAY_ROOMS12 = new RecInfo(2,5,4,5);
	public final RecInfo CORRIDOR12 = new RecInfo(2,5,0,11);
	public final RecInfo DOORWAY_ROOMS13 = new RecInfo(7,8,1,3);
	public final RecInfo CORRIDOR13 = new RecInfo(0,19,1,3);
	public final RecInfo DOORWAY_ROOMS23 = new RecInfo(7,8,6,10);
	public final RecInfo CORRIDOR23 = new RecInfo(0,19,6,10);
	
	
/* ---------------------------- DEBUG Environment -------------------------- */

	private static final String DEBUG_TAG = "StripsAPI";
	private static final int DEBUG_ALL = 0;
	private static final int DEBUG_CLASS = 1;
	private static final int DEBUG_FUNCTION = 2;
	private static final int DEBUG_SPECIFIC = 3;
	private static final int DEBUG_NONE = -1;	
	private static final int CURRENT_DEBUG_LEVEL = DEBUG_NONE;

/* ---------------------------- Object Construction ------------------------ */

	public StripsAPI(GameGraphics game){
		debugPrint(DEBUG_CLASS,"In constructor");
		this.game = game;
	}

/* ----------------------------- Public Methods ---------------------------- */

	/* ------ Auxiliary methods -------*/
	
	/**
	 * 
	 * @param the current furniture
	 * @return one of the Rooms or dorway defined above 
	 * under Rooms definition
	 */
	public RecInfo getRoom(RecInfo rect){
		if(rect.getX2() <= this.ROOM1.getX2()){
			if(rect.getY2() <= this.ROOM1.getY2()){
				return this.ROOM1;
			}
			return this.ROOM2;
		}
		return this.ROOM3;
	}

	/**
	 * Method returns the obstacle that prevets "rect" to allpy the action
	 * @param rect - the rectangle we want to move
	 * @param actionName - the name of the action we want to preform
	 * @return - Obstacle info
	 */
	public RecInfo getObstacle(RecInfo rect,String actionName){
		int x1 = rect.getX1();
		int x2 = rect.getX2();
		int y1 = rect.getY1();
		int y2 = rect.getY2();
		
		int edge1 = rect.getEdge1();
		int edge2 = rect.getEdge2();
		
		int maxLength = (int) (y2 - Math.round( Math.sqrt( 
				   Math.pow(edge1+1,2)
				+  Math.pow(edge2+1,2))));
		
		
		RecInfo info = null;
		switch(actionName){
			case Action.MOVE_LEFT:		
				return game.getRectangleByYAxis(x1-1, y1, y2);
			case Action.MOVE_RIGHT:
				return game.getRectangleByYAxis(x2+1, y1, y2);
			case Action.MOVE_UP:
				return game.getRectangleByXAxis(x1, x2, y1-1);
			case Action.MOVE_DOWN:
				return game.getRectangleByXAxis(x1, x2, y2+1);
			case Action.ROTATE_LEFT:
				System.out.println("Rotate Left");
				for(int i = x1-1-edge2; i <= x1-1; i++){
					info = game.getRectangleByYAxis(i, y2-maxLength+1, y2);
					if(info != null){
						return info;
					}
				}
				for(int i = x1; i <= x2; i++){
					info = game.getRectangleByYAxis(i, y2-maxLength+1, y1-1);
					if(info != null){
						return info;
					}
				}
				debugPrint(DEBUG_SPECIFIC,"Null Pointer Received");
				return null;
			case Action.ROTATE_RIGHT:
				System.out.println("Rotate Right");
				for(int i = x2+1; i <= x2+1+edge2; i++){
					info = game.getRectangleByYAxis(i, y2-maxLength+1, y2);
					if(info != null){
						return info;
					}
				}
				for(int i = x1; i <= x2; i++){
					info = game.getRectangleByYAxis(i, y2-maxLength+1, y1-1);
					if(info != null){
						return info;
					}
				}
				debugPrint(DEBUG_SPECIFIC,"Null Pointer Received");
				return null;	
			default: 
				debugPrint(DEBUG_SPECIFIC,"Null Pointer Received");
				return null;		
		}
	}

	/**
	 * Method calculates temprary place, in the same room, where we can 
	 * move the Obstacle, so we will be able to preform the action
	 */
	private RecInfo findTempObstaclePlaceMovingAside(RecInfo rect, 
									     RecInfo obstacle,
									     String actionName){
		System.out.println("Obstacle aside");
		RecInfo currentRoom = getRoom(rect);
		int distanceToBottomWall = currentRoom.getY2() - rect.getY2();
		int distanceToTopWall = rect.getY1() - currentRoom.getY1();
		int theLowestPick = obstacle.getY2();
		boolean found = false;
		
		int additionalFactor = 0;
		int searchLocation = -1;
		switch(actionName){
			case Action.MOVE_LEFT:
				additionalFactor = -1;
				searchLocation = obstacle.getX1()-1;
				break;
			case Action.MOVE_RIGHT:
				additionalFactor = 1;
				searchLocation = obstacle.getX2()+1;
				break;
		}
		
		if (distanceToBottomWall >= obstacle.getEdge2()+1){
			System.out.println("Checking down");
			for(int i = obstacle.getY2()+1; i <= rect.getY2()+obstacle.getEdge2()+1; i++){
				if (game.getRectangleByXAxis(obstacle.getX1(), obstacle.getX2(), i) != null){
					System.out.println("Not Found obstacle place " + i);
					theLowestPick = i -1;
					found = true;
					break;
				}
			}
			if (!found){
				System.out.println("Found obstacle place");
				return new RecInfo(obstacle.getX1(), obstacle.getX2(), 
						rect.getY2() + 1, rect.getY2()+obstacle.getEdge2()+1);
			}
			found = false;	
		}
		else{
			theLowestPick = rect.getY2()+distanceToBottomWall;
			for(int i = obstacle.getY2()+1; i <= rect.getY2()+distanceToBottomWall; i++){
				if (game.getRectangleByXAxis(obstacle.getX1(), obstacle.getX2(), i) != null){
					theLowestPick = i - 1;
					break;
				}
			}
		}
		distanceToTopWall = rect.getY1() - currentRoom.getY1();
		int theHighestPick = obstacle.getY1();
		if (distanceToTopWall >= obstacle.getEdge2()+1){
			for(int i = obstacle.getY1()-1; i >= rect.getY1()-obstacle.getEdge2()-1; i--){
				if (game.getRectangleByXAxis(obstacle.getX1(), obstacle.getX2(), i) != null){
					theHighestPick = i + 1;
					found = true;
					break;
				}
			}
			if (!found){
				return new RecInfo(obstacle.getX1(), obstacle.getX2(), 
						rect.getY1()-obstacle.getEdge2()-1, rect.getY1() - 1);
			}
			found = false;	
		}
		else{
			theHighestPick = rect.getY1()-distanceToTopWall;
			for(int i = obstacle.getY1()-1; i >= rect.getY1()-distanceToTopWall; i--){
				if (game.getRectangleByXAxis(obstacle.getX1(), obstacle.getX2(), i) != null){
					theHighestPick = i + 1;
					break;
				}
			}
		}

		
		if(!(actionName == Action.MOVE_LEFT && obstacle.getX1() == currentRoom.getX1()) 
				&& !(actionName == Action.MOVE_RIGHT && obstacle.getX2() == currentRoom.getX2())){
			RecInfo tempRec = game.getRectangleByYAxis(searchLocation, theHighestPick, theLowestPick);
			while (tempRec != null){
				if(tempRec.getY1() - theHighestPick >= obstacle.getEdge2() + 1){
					return new RecInfo(obstacle.getX1() + additionalFactor, obstacle.getX2() + additionalFactor, 
																theHighestPick, theHighestPick + obstacle.getEdge2());
				}
				theHighestPick = tempRec.getY2()+1;
				tempRec = game.getRectangleByYAxis(searchLocation, theHighestPick, theLowestPick);
			}
			if(theHighestPick+obstacle.getEdge2() <= theLowestPick){
				return new RecInfo(obstacle.getX1() + additionalFactor, obstacle.getX2() + additionalFactor,
															theHighestPick, theHighestPick+obstacle.getEdge2());
			}
		}
		
		if(distanceToTopWall >= distanceToBottomWall){
			System.out.println("Distance to top wall is greater");
			System.out.println("Distance to wall is " + distanceToTopWall 
					+ " obstacle length " + (obstacle.getEdge2() + 1));
			if(distanceToTopWall >= obstacle.getEdge2() + 1){
				System.out.println("before returnig, obstacle is not null");
				return new RecInfo(obstacle.getX1(), obstacle.getX2(), 
						rect.getY1() - obstacle.getEdge2() - 1, rect.getY1()-1);
			}
		}
		else{ 
			System.out.println("Distance to top wall is greater");
			System.out.println("Distance to wall is " + distanceToTopWall 
					+ " obstacle length " + (obstacle.getEdge2() + 1));
			if(distanceToBottomWall >= obstacle.getEdge2() + 1){
				return new RecInfo(obstacle.getX1(), obstacle.getX2(), 
						rect.getY2() + 1, rect.getY2()+ 1 + obstacle.getEdge2());
			}
		}
		return null;
	}
	
	
	/**
	 * @param rect
	 * @param obstacle
	 * @param actionName
	 * @return
	 */
	private RecInfo findTempObstaclePlaceUpDown(RecInfo rect, 
									     RecInfo obstacle,
									     String actionName){
		System.out.println("Obstacle up down");
		
		RecInfo currentRoom = getRoom(rect);
		int distanceToRightWall = currentRoom.getX2() - rect.getX2();
		int distanceToLeftWall = rect.getX1() - currentRoom.getX1();
		int theMostRightPick = obstacle.getX2();
		int theMostLeftPick = obstacle.getX1();
		boolean found = false;
		
		int additionalFactor = 0;
		int searchLocation = -1;
		switch(actionName){
			case Action.MOVE_UP:
				additionalFactor = -1;
				searchLocation = obstacle.getY1()-1;
				break;
			case Action.MOVE_DOWN:
				additionalFactor = 1;
				searchLocation = obstacle.getY2()+1;
				break;
		}
		
		if (distanceToRightWall >= obstacle.getEdge1()+1){
			System.out.println("Distance to right wall");
			for(int i = obstacle.getX2()+1; i <= rect.getX2()+obstacle.getEdge1()+1; i++){
				if (game.getRectangleByYAxis(i, obstacle.getY1(), obstacle.getY2()) != null){
					theMostRightPick = i -1;
					found = true;
					break;
				}
			}
			if (!found){
				return new RecInfo(rect.getX2()+1, rect.getX2()+obstacle.getEdge1()+1, 
						obstacle.getY1(), obstacle.getY2());
			}
			found = false;	
		}
		else{
			theMostRightPick = rect.getX2()+distanceToRightWall;
			for(int i = obstacle.getX2()+1; i <= rect.getX2()+distanceToRightWall; i++){
				if (game.getRectangleByYAxis(i, obstacle.getY1(), obstacle.getY2()) != null){
					theMostRightPick = i - 1;
					break;
				}
			}
		}


		if (distanceToLeftWall >= obstacle.getEdge1()+1){
			System.out.println("Distance to left wall");
			for(int i = obstacle.getX1()-1; i >= rect.getX1()-obstacle.getEdge1()-1; i--){
				if (game.getRectangleByYAxis(i, obstacle.getY1(), obstacle.getY2()) != null){
					theMostLeftPick = i + 1;
					found = true;
					break;
				}
			}
			if (!found){
				return new RecInfo(rect.getX1()-obstacle.getEdge1()-1, rect.getX1()-1,
						obstacle.getY1(), obstacle.getY2());
			}
			found = false;	
		}
		else{
			theMostLeftPick = rect.getX1()-distanceToLeftWall;
			for(int i = obstacle.getX1()-1; i >= rect.getX1()-distanceToLeftWall; i--){
				if (game.getRectangleByYAxis(i, obstacle.getY1(), obstacle.getY2()) != null){
					theMostLeftPick = i + 1;
					break;
				}
			}
		}
		
		if(!(actionName == Action.MOVE_UP && obstacle.getY1() == currentRoom.getY1())
				&& !(actionName == Action.MOVE_DOWN && obstacle.getY2() == currentRoom.getY2())){
			System.out.println("up or down");
			RecInfo tempRec = game.getRectangleByXAxis(theMostLeftPick, theMostRightPick, searchLocation);
			while (tempRec != null){
				if(tempRec.getX1() - theMostLeftPick >= obstacle.getEdge1() + 1){
					return new RecInfo(theMostLeftPick, theMostLeftPick+obstacle.getEdge1(), 
							obstacle.getY1()+additionalFactor, obstacle.getY2()+additionalFactor);
				}
				theMostLeftPick = tempRec.getX2()+1;
				tempRec = game.getRectangleByXAxis(theMostLeftPick, theMostRightPick, searchLocation);
			}
			if(theMostLeftPick+obstacle.getEdge1() <= theMostRightPick){
				return new RecInfo(theMostLeftPick,theMostLeftPick+obstacle.getEdge1(),
						obstacle.getY1()+additionalFactor, obstacle.getY2()+additionalFactor);
			}
		}
		
		
		if(distanceToLeftWall >= distanceToRightWall){
			System.out.println("Distance to left wall is greater");
			System.out.println("Distance to wall is " + distanceToLeftWall 
					+ " obstacle length " + (obstacle.getEdge1() + 1));
			if(distanceToLeftWall >= obstacle.getEdge1() + 1){
				return new RecInfo(rect.getX1() - obstacle.getEdge1()-1, rect.getX1()-1,
											obstacle.getY1(), obstacle.getY2());
			}
		}
		else if(distanceToRightWall >= obstacle.getEdge1() + 1){
			System.out.println("Distance to right wall is greater");
			System.out.println("Distance to wall is " + distanceToRightWall 
					+ " obstacle length " + (obstacle.getEdge1() + 1));
			return new RecInfo(rect.getX2() +1, rect.getX2() + obstacle.getEdge1() + 1, 
					obstacle.getY1(), obstacle.getY2());
			}
		return null;
	}
	
	
	private int findLowestPickBelow(int distanceToBottomWall, RecInfo rect, 
		     												RecInfo obstacle){
		int theLowestPick = obstacle.getY2();
		if (distanceToBottomWall >= obstacle.getEdge2()+1){
			System.out.println("Checking down");
			for(int i = obstacle.getY2()+1; i <= rect.getY2()+obstacle.getEdge2()+1; i++){
				if (game.getRectangleByXAxis(obstacle.getX1(), obstacle.getX2(), i) != null){
					System.out.println("Not Found obstacle place " + i);
					theLowestPick = i -1;
					break;
				}
				else theLowestPick++;
			}
		}
		else{
			theLowestPick = rect.getY2()+distanceToBottomWall;
			for(int i = obstacle.getY2()+1; i <= rect.getY2()+distanceToBottomWall; i++){
				if (game.getRectangleByXAxis(obstacle.getX1(), obstacle.getX2(), i) != null){
					theLowestPick = i - 1;
					break;
				}
			}
		}
		return theLowestPick;
	}
	
	private int findHighestPickAbove(int distanceToTopWall, int maxLength, RecInfo rect, 
		     												RecInfo obstacle){
		int theHighestPick = obstacle.getY1();
		if (distanceToTopWall >= obstacle.getEdge2()+1){
			for(int i = obstacle.getY1()-1; i >= rect.getY2()- maxLength-obstacle.getEdge2()-1; i--){
				if (game.getRectangleByXAxis(obstacle.getX1(), obstacle.getX2(), i) != null){
					theHighestPick = i + 1;
					break;
				}
				else theHighestPick--;
			}
		}
		else{
			theHighestPick = rect.getY2() - maxLength - distanceToTopWall;
			for(int i = obstacle.getY1()-1; i >= rect.getY2() - maxLength - distanceToTopWall; i--){
				if (game.getRectangleByXAxis(obstacle.getX1(), obstacle.getX2(), i) != null){
					theHighestPick = i + 1;
					break;
				}
			}
		}
		return theHighestPick;
	}
	
	private RecInfo findTempObstaclePlaceRotations(RecInfo rect, 
									     RecInfo obstacle,
									     String actionName){
		System.out.println("Obstacle rotations");
		RecInfo currentRoom = getRoom(rect);
		int distanceToBottomWall = currentRoom.getY2() - rect.getY2();
		int distanceToTopWall;
		int theLowestPick = obstacle.getY2();
		int maxLength = (int) (rect.getY2() - Math.round( Math.sqrt( 
				   Math.pow(rect.getEdge1()+1,2)
				+  Math.pow(rect.getEdge2()+1,2)))-1);
		boolean found = false;
		
		int additionalFactor = 0;
		int searchLocation = -1;
		boolean sideway = false;
		switch(actionName){
			case Action.ROTATE_LEFT:
				if(obstacle.getX2() < rect.getX1()) sideway = true;
				additionalFactor = -(obstacle.getEdge1()+1);
				searchLocation = obstacle.getX1()-obstacle.getEdge1()-1;
				break;
			case Action.ROTATE_RIGHT:
				if(obstacle.getX1() > rect.getX2()) sideway = true;
				additionalFactor = obstacle.getEdge1()+1;
				searchLocation = obstacle.getX2()+1;
				break;
		}
		
		if(sideway){
		
			theLowestPick = findLowestPickBelow(distanceToBottomWall, rect, obstacle);
			if(theLowestPick == rect.getY2()+obstacle.getEdge2()+1){
				return new RecInfo(obstacle.getX1(), obstacle.getX2(), 
						rect.getY2() + 1, rect.getY2()+obstacle.getEdge2()+1);
			}
			
			distanceToTopWall = rect.getY2() - maxLength - currentRoom.getY1();
			int theHighestPick = findHighestPickAbove(distanceToTopWall, maxLength,
																		rect, obstacle);
			if(theHighestPick == rect.getY2()- maxLength-obstacle.getEdge2()-1){
				return new RecInfo(obstacle.getX1(), obstacle.getX2(), 
						rect.getY2()- maxLength-obstacle.getEdge2()-1, 
						rect.getY2()- maxLength - 1);
			}
			
			if		((actionName == Action.ROTATE_LEFT 
						&& obstacle.getX1() - obstacle.getEdge1() - 1 >= currentRoom.getX1()) 
				||  (actionName == Action.ROTATE_RIGHT 
						&& obstacle.getX2() + obstacle.getEdge1() + 1 <= currentRoom.getX2())){
					int counter = 0;
					for (int i = theLowestPick; i <= theHighestPick; i++){
						if (game.getRectangleByXAxis(searchLocation, searchLocation +obstacle.getEdge1(),i) != null){
							counter = 0;
						}
						else{
							counter++;
							if(counter == obstacle.getEdge2()+1){
								return new RecInfo(searchLocation, searchLocation +obstacle.getEdge1(),
												i,i+obstacle.getEdge2());
							}
						}
					}
				}
				
			if(distanceToTopWall >= distanceToBottomWall){
			System.out.println("Distance to top wall is greater");
			System.out.println("Distance to wall is " + distanceToTopWall 
					+ " obstacle length " + (obstacle.getEdge2() + 1));
			if(distanceToTopWall >= obstacle.getEdge2() + 1){
				System.out.println("before returnig, obstacle is not null");
				return new RecInfo(obstacle.getX1(), obstacle.getX2(), 
						rect.getY1() - obstacle.getEdge2() - 1, rect.getY1()-1);
				}	
			}
			else{ 
				System.out.println("Distance to top wall is greater");
				System.out.println("Distance to wall is " + distanceToTopWall 
						+ " obstacle length " + (obstacle.getEdge2() + 1));
				if(distanceToBottomWall >= obstacle.getEdge2() + 1){
					return new RecInfo(obstacle.getX1(), obstacle.getX2(), 
							rect.getY2() + 1, rect.getY2()+ 1 + obstacle.getEdge2());
				}
			}
			return null;
			}
			
		else{
			int distanceToRightWall = -1;
			int distanceToLeftWall = -1;
			int rightLimit = -1;
			int leftLimit = -1;
			switch(actionName){
				case Action.ROTATE_LEFT:
					distanceToRightWall = currentRoom.getX2() - rect.getX2();
					distanceToLeftWall = (rect.getX1()-rect.getEdge2()-1) - currentRoom.getX1();
					rightLimit = rect.getX2() + obstacle.getEdge1() + 1;
					leftLimit = rect.getX1() - rect.getEdge2() - obstacle.getEdge1() - 2;
					break;
				case Action.ROTATE_RIGHT:
					distanceToRightWall = currentRoom.getX2() - (rect.getX2()+rect.getEdge2()+1);
					distanceToLeftWall = rect.getX1() - currentRoom.getX1();
					rightLimit = rect.getX2() + rect.getEdge2() + obstacle.getEdge1() + 2;
					leftLimit = rect.getX1()  - obstacle.getEdge1() - 1;
			}

			int theMostRightPick = obstacle.getX2();
			int theMostLeftPick = obstacle.getX1();
			
			if (distanceToRightWall >= obstacle.getEdge1()+1){
			System.out.println("Distance to right wall");
			for(int i = obstacle.getX2()+1; i <= rightLimit; i++){
				if (game.getRectangleByYAxis(i, obstacle.getY1(), obstacle.getY2()) != null){
					theMostRightPick = i -1;
					found = true;
					break;
				}
			}
			if (!found){
				return new RecInfo(rightLimit-obstacle.getEdge1(), rightLimit, 
						obstacle.getY1(), obstacle.getY2());
			}
			found = false;	
		}
		else{
			theMostRightPick = rect.getX2()+distanceToRightWall;
			for(int i = obstacle.getX2()+1; i <= currentRoom.getX2(); i++){
				if (game.getRectangleByYAxis(i, obstacle.getY1(), obstacle.getY2()) != null){
					theMostRightPick = i - 1;
					break;
				}
			}
		}


		if (distanceToLeftWall >= obstacle.getEdge1()+1){
			System.out.println("Distance to left wall");
			for(int i = obstacle.getX1()-1; i >= leftLimit; i--){
				if (game.getRectangleByYAxis(i, obstacle.getY1(), obstacle.getY2()) != null){
					theMostLeftPick = i + 1;
					found = true;
					break;
				}
			}
			if (!found){
				return new RecInfo(leftLimit, leftLimit + obstacle.getEdge1(),
						obstacle.getY1(), obstacle.getY2());
			}
			found = false;	
		}
		else{
			theMostLeftPick = rect.getX1()-distanceToLeftWall;
			for(int i = obstacle.getX1()-1; i >= currentRoom.getX1(); i--){
				if (game.getRectangleByYAxis(i, obstacle.getY1(), obstacle.getY2()) != null){
					theMostLeftPick = i + 1;
					break;
				}
			}
		}
		
		if	((rect.getY2() - maxLength -1) - obstacle.getEdge2() >= currentRoom.getY1()){
					int counter = 0;
					searchLocation = (rect.getY2() - maxLength -1) - obstacle.getEdge2();
					for (int i = theMostLeftPick; i <= theMostRightPick; i++){
						if (game.getRectangleByYAxis(i, searchLocation, searchLocation +obstacle.getEdge2()) != null){
							counter = 0;
						}
						else{
							counter++;
							if(counter == obstacle.getEdge1()+1){
								return new RecInfo(i, i + obstacle.getEdge1(), 
										searchLocation,searchLocation+obstacle.getEdge2());
							}
						}
					}
				}
			if(distanceToLeftWall >= distanceToRightWall){

			if(distanceToLeftWall >= obstacle.getEdge1() + 1){
				System.out.println("before returnig, obstacle is not null");
				return new RecInfo(leftLimit, leftLimit + obstacle.getEdge1(),
						obstacle.getY1(), obstacle.getY2());
				}	
			}
			else{ 
				if(distanceToRightWall >= obstacle.getEdge1() + 1){
					return new RecInfo(rightLimit-obstacle.getEdge1(), rightLimit, 
												obstacle.getY1(), obstacle.getY2());
				}
			}
			}
			return null;
		}
			


	/**
	 * Method calculates temprary place, in the same room, where we can 
	 * move the Obstacle, so we will be able to preform the action
	 */
	public RecInfo findTempObstaclePlace(RecInfo rect, 
									     RecInfo obstacle,
									     String actionName){
		System.out.println("Find Temp Obstacle Function");
			switch(actionName){
			case Action.MOVE_LEFT:		
			case Action.MOVE_RIGHT:
				return findTempObstaclePlaceMovingAside(rect, obstacle, actionName);
			case Action.MOVE_UP:
			case Action.MOVE_DOWN:
				return findTempObstaclePlaceUpDown(rect, obstacle, actionName);
			case Action.ROTATE_LEFT:
			case Action.ROTATE_RIGHT:
				return findTempObstaclePlaceRotations(rect, obstacle, actionName);
			default: 
				return null;		
		} //Rotations done only for the case obstacle is on the rotate destination area
	}

	/**
	 * Method returns aמ appropriate spot near the doorway between the 
	 * the currentRoom and the targedRoom, INSIDE THE CURRENT ROOM!
	 * e.g : if we have 
	 * 			furniture(0,2,0,1)
	 *			currentRoom(0,7,0,4)
	 * 			targedRoom(8,19,5,11)
	 * 		Then the appropriate spot near the doorway will be: 
	 * 			doorWay(5,7,1,2)
	 */ 
	
	private RecInfo findSpotNearVerticalDoorway(RecInfo door, int edge, int x1, int x2){
		int counter = -1;
		for (int i = door.getY1(); i <= door.getY2(); i++){
			if (game.getRectangleByXAxis(x1, x2, i) != null){
				counter = -1;
			}
			else{
				counter ++;
				if(counter == edge){
					return new RecInfo(x1,x2, i-edge, i);
				}
			}
		}
		return null;
	}
	
	
	private RecInfo findSpotNearHorizontalDoorway(RecInfo door, int edge, int y1, int y2){
		int counter = -1;
		for (int i = door.getX1(); i <= door.getX2(); i++){
			if (game.getRectangleByYAxis(i, y1, y2) != null){
				counter = -1;
			}
			else{
				counter++;
				if(counter == edge){
					return new RecInfo(i-edge, i, y1,y2);
				}
			}
		}
		return null;
	}
	
/*	public static final RecInfo DOORWAY_ROOMS12 = new RecInfo(2,5,4,5);
	public static final RecInfo DOORWAY_ROOMS13 = new RecInfo(7,8,1,3);
	public static final RecInfo DOORWAY_ROOMS23 = new RecInfo(7,8,6,10);*/
	public RecInfo findSpotNearDorway(RecInfo furniture, RecInfo currentRoom,
												RecInfo targetRoom){//TO ADD CHECK ALONG THE WALL
		RecInfo door;
		int x1, x2, y1, y2;
		int counter;
		if(currentRoom == this.ROOM1 && targetRoom == this.ROOM3){
			door = this.DOORWAY_ROOMS13;
			x1 = door.getX1() - furniture.getEdge1() - 1;
			x2 = door.getX1() - 1;
			return findSpotNearVerticalDoorway(door, furniture.getEdge2(), x1, x2);
		}
		
		if(currentRoom == this.ROOM1 && targetRoom == this.ROOM2){
			door = this.DOORWAY_ROOMS12;
			y1 = door.getY1() - furniture.getEdge2() - 1;
			y2 = door.getY1() - 1;
			return findSpotNearHorizontalDoorway(door, furniture.getEdge1(), y1, y2);
		}
		
		
		if(currentRoom == this.ROOM2 && targetRoom == this.ROOM1){
			door = this.DOORWAY_ROOMS12;
			y1 = door.getY2() + 1;
			y2 = door.getY2() +furniture.getEdge2() + 1;
			return findSpotNearHorizontalDoorway(door, furniture.getEdge1(), y1, y2);
		}
		
		
		
		if(currentRoom == this.ROOM2 && targetRoom == this.ROOM3){
			door = this.DOORWAY_ROOMS23;
			x1 = door.getX1() - furniture.getEdge1() - 1;
			x2 = door.getX1() - 1;
			return findSpotNearVerticalDoorway(door, furniture.getEdge2(), x1, x2);
		}
		
		if(currentRoom == this.ROOM3 && targetRoom == this.ROOM1){
			door = this.DOORWAY_ROOMS13;
			x1 = door.getX2() + 1; 
			x2 = door.getX2() + furniture.getEdge1() + 1;
			return findSpotNearVerticalDoorway(door, furniture.getEdge2(), x1, x2);
		}
		
		if(currentRoom == this.ROOM3 && targetRoom == this.ROOM2){
			door = this.DOORWAY_ROOMS23;
			x1 = door.getX2() + 1; 
			x2 = door.getX2() + furniture.getEdge1() + 1;
			return findSpotNearVerticalDoorway(door, furniture.getEdge2(), x1, x2);
		}
		return null;
		
	}
	
	/************************************************************************ 
	 *   In our implementation of STRIPS, We use a set of Conditions and 	*
	 *   and Actions. Each set will be implemented as a group of methods.	*
	 ************************************************************************/

	/* ------ Conditions -------*/

	/**
	 * The function check if given rectangle is inside a space
	 * @param rec - the given rectangle
	 * @param space - the space we want to check 
	 * @return : true if the rectangle is fully covered by the space 
	 */
	public boolean InSpace(RecInfo rec, RecInfo space){
		return rec.getX1() >= space.getX1() &&
			rec.getX2() <= space.getX2() &&
			rec.getY1() >= space.getY1() &&
			rec.getY2() <= space.getY2();
	}

	/**
	 * @param
	 * @return
	 */
	public boolean InPlace(RecInfo rect,RecInfo place){
		return rect.getX1() == place.getX1() && rect.getX2() == place.getX2() 
				&& rect.getY1() == place.getY1() && rect.getY2() == place.getY2();
	}

	/**
	 * @param
	 * @return
	 */
	public boolean CanMoveUp(RecInfo rect){
		int x1 = rect.getX1();
		int x2 = rect.getX2();
		int y1 = rect.getY1();
		
		if (y1 == this.ROOM1.getY1()) { //cannot move into wall; maybe it has to be 0
			return false;
		}
		
		if (y1 == this.ROOM2.getY1()){
			if(x1 >= this.DOORWAY_ROOMS12.getX1() 
			&& x2 <= this.DOORWAY_ROOMS12.getX2()){ //Door
				if (game.getRectangleByXAxis(x1, x2, y1-1) == null){
					return true;
				}
				
				return false;
			}
			else if (x1 <2 || (x2 > 5 && x2 <= 7)){//cannot move into wall
				return false;
			}
		}
		
		if (game.getRectangleByXAxis(x1, x2, y1-1) == null){
			return true;
		}
		
		return false;
		
	} // rect can move one step up
	
	/**
	 * @param
	 * @return
	 */
	public boolean CanMoveDown(RecInfo rect){
		int x1 = rect.getX1();
		int x2 = rect.getX2();
		int y2 = rect.getY2();
		
		if (y2 == this.ROOM2.getY2()) { //cannot move into wall; 
			return false;
		}
		
		if (y2 == this.ROOM1.getY2()){
			if(x1 >= this.DOORWAY_ROOMS12.getX1() && x2 <= this.DOORWAY_ROOMS12.getX2()){ //Door
				if (game.getRectangleByXAxis(x1, x2, y2+1) == null){
					return true;
				}
				
				return false;
			}
			else if (x1 <this.DOORWAY_ROOMS12.getX1() 
					|| (x2 > this.DOORWAY_ROOMS12.getX2() 
					&& x2 <= this.ROOM2.getX2())){//cannot move into wall
				return false;
			}
		}
		
		if (game.getRectangleByXAxis(x1, x2, y2+1) == null){
			return true;
		}
		
		return false;
	} // rect can move one step down

	/**
	 * @param
	 * @return
	 */	
	public boolean CanMoveLeft(RecInfo rect){
		int x1 = rect.getX1();
		int y1 = rect.getY1();
		int y2 = rect.getY2();
		
		if (x1 == this.ROOM1.getX1()) { //cannot move into wall; 
			return false;
		}
		
		if (x1 == this.ROOM3.getX1()) { //cannot move into wall; 
			return false;
		}
		
		/*if (x1 == this.DOORWAY_ROOMS13.getX2()+1){
			if(y1 >= this.DOORWAY_ROOMS13.getY1() && y2 <= this.DOORWAY_ROOMS13.getY2()){ //Door
				if (game.getRectangleByYAxis(x1-1, this.DOORWAY_ROOMS13.getY1(), this.DOORWAY_ROOMS13.getY2()) == null){
					return true;
				}
				return false;
			}
			if(y1 >= this.DOORWAY_ROOMS23.getY1() && y2 <= this.DOORWAY_ROOMS23.getY2()){ //Door
				if (game.getRectangleByYAxis(x1-1, this.DOORWAY_ROOMS23.getY1(), this.DOORWAY_ROOMS23.getY2()) == null){
					return true;
				}
				return false;
			}
			return false;
		}*/
		
		if (x1 == this.DOORWAY_ROOMS12.getX1() && y1 <= this.DOORWAY_ROOMS12.getY1() && y2 >= this.DOORWAY_ROOMS12.getY2()){
			return false;
		}
		
		if (game.getRectangleByYAxis(x1-1, y1, y2) == null){
			return true;
		}
		
		return false;
	} // rect can move one step left
	
	/**
	 * @param
	 * @return
	 */	
	public boolean CanMoveRight(RecInfo rect){
		int x2 = rect.getX2();
		int y1 = rect.getY1();
		int y2 = rect.getY2();
		
		if (x2 == this.ROOM3.getX2()) { //cannot move into wall; maybe it has to be 0
			return false;
		}
		
		if (x2 == this.DOORWAY_ROOMS13.getX1()-1){
			if(y1 >= this.DOORWAY_ROOMS13.getY1() && y2 <= this.DOORWAY_ROOMS13.getY2()){ //Door
				if (game.getRectangleByYAxis(x2+1, this.DOORWAY_ROOMS13.getY1(), this.DOORWAY_ROOMS13.getY2()) == null){
					return true;
				}
				return false;
			}
			if(y1 >= this.DOORWAY_ROOMS23.getY1() && y2 <= this.DOORWAY_ROOMS23.getY2()){ //Door
				if (game.getRectangleByYAxis(x2+1, this.DOORWAY_ROOMS23.getY1(), this.DOORWAY_ROOMS23.getY2()) == null){
					return true;
				}
				return false;
			}
			return false;
		}
		
		if (x2 == this.DOORWAY_ROOMS12.getX2() && y1 <= this.DOORWAY_ROOMS12.getY1() && y2 >= this.DOORWAY_ROOMS12.getY2()){
			return false;
		}
		
		if (game.getRectangleByYAxis(x2+1, y1, y2) == null){
			return true;
		}
		
		return false;
	} // rect can move one step right
	
	private boolean notEncountersWalls(RecInfo rect){
		int x1 = rect.getX1();
		int x2 = rect.getX2();
		int y1 = rect.getY1();
		int y2 = rect.getY2();
		if (x1 < ROOM1.getX1()){ // under upper wall
			return false;
		}
		if (y1 < ROOM1.getY1()){ //under bottom wall
			return false;
		}
		if (x2 > ROOM3.getX2()){ // under upper wall
			return false;
		}
		
		if (y2 > ROOM2.getY2()){ //under bottom wall
			return false;
		}
		if (y1 < DOORWAY_ROOMS13.getY1() && x1 <= DOORWAY_ROOMS13.getX1() && x2 >= DOORWAY_ROOMS13.getX2()){ //between door and upper wall
			return false;
		}
		if (y1 > DOORWAY_ROOMS13.getY2() && y1 < DOORWAY_ROOMS23.getY1() 
				&& x1 <= DOORWAY_ROOMS13.getX1() && x2 >= DOORWAY_ROOMS13.getX2()){ //between doors
			return false;
		}
		if (y2 > DOORWAY_ROOMS13.getY2() && y2 < DOORWAY_ROOMS23.getY1() 
				&& x1 <= DOORWAY_ROOMS13.getX1() && x2 >= DOORWAY_ROOMS13.getX2()){ //between doors
			return false;
		}
		if (y2 > DOORWAY_ROOMS23.getY2() && x1 <= DOORWAY_ROOMS23.getX1() && x2 >= DOORWAY_ROOMS23.getX2()){ //between door and bottom wall
			return false;
		}
		if (x1 < DOORWAY_ROOMS12.getX1() && y1 <= DOORWAY_ROOMS12.getY1() && y2 >= DOORWAY_ROOMS12.getY2()){
			return false;
		}
		if (x1 > DOORWAY_ROOMS12.getX2() && x1 < DOORWAY_ROOMS23.getX1() && y1 <= DOORWAY_ROOMS12.getY1() && y2 >= DOORWAY_ROOMS12.getY2()){
			return false;
		}
		if (x2 > DOORWAY_ROOMS12.getX2() && x2 <= DOORWAY_ROOMS23.getX1() && y1 <= DOORWAY_ROOMS12.getY1() && y2 >= DOORWAY_ROOMS12.getY2()){
			return false;
		}
		return true;
	}
	
	
	/**
	 * @param
	 * @return
	 */
	public boolean CanRotateRight(RecInfo rect){
		int x1 = rect.getX1();
		int x2 = rect.getX2();
		int y1 = rect.getY1();
		int y2 = rect.getY2();
		int edge1 = rect.getEdge1();
		int edge2 = rect.getEdge2();
		
		
		int newX1 = x2+1;
		int newX2 = newX1 + edge2;
		int newY2 = y2;
		int newY1 = (int) (y2 - Math.round( Math.sqrt( 
				   Math.pow(edge1+1,2)
				+  Math.pow(edge2+1,2))));
		

		if (notEncountersWalls(new RecInfo(newX1, newX2, newY1, newY2)) == false){
			return false;
		}
		
		if (!game.IsFree(new RecInfo(newX1, newX2, newY1, newY2))){
			return false;
		}
		
		newY2 = y1-1;
		
		if (notEncountersWalls(new RecInfo(x1, x2, newY1, newY2)) == false){
			return false;
		}
		
		if (!game.IsFree(new RecInfo(x1, x2, newY1, newY2))){
			return false;
		}
		
		return true;

	} // there is enough space to make right rotation
	
	/**
	 * @param
	 * @return
	 */
	public boolean CanRotateLeft(RecInfo rect){
		int x1 = rect.getX1();
		int x2 = rect.getX2();
		int y1 = rect.getY1();
		int y2 = rect.getY2();
		int edge1 = rect.getEdge1();
		int edge2 = rect.getEdge2();
		
		int newX1 = x1 - edge2 - 1;
		int newX2 = x1-1;

		
		int newY2 = y2;
		int newY1 = (int) (y2 - Math.round( Math.sqrt( 
				   Math.pow(edge1+1,2)
				+  Math.pow(edge2+1,2))));
		

		if (notEncountersWalls(new RecInfo(newX1, newX2, newY1, newY2)) == false){
			return false;
		}
		
		if (!game.IsFree(new RecInfo(newX1, newX2, newY1, newY2))){
			return false;
		}
		
		newY2 = y1-1;
		
		if (notEncountersWalls(new RecInfo(x1, x2, newY1, newY2)) == false){
			return false;
		}
		
		if (!game.IsFree(new RecInfo(x1, x2, newY1, newY2))){
			return false;
		}
		
		return true;
	} // there is enough space to make left rotation
	
	/**
	 * @param
	 * @return
	 */
	public boolean Rotated(RecInfo rect1, RecInfo rect2){
		debugPrint(DEBUG_SPECIFIC,"Rotated(): \n"+
			"rect1 = "+rect1 + "\n" +			
			"rect2 = "+rect2 + "\n" +			
			"rect1.getEdge1() = "+rect1.getEdge1() + "\n" +
			"rect2.getEdge1() = "+rect2.getEdge1() + "\n" +
			"rect1.getEdge2() = "+rect1.getEdge2() + "\n" +
			"rect2.getEdge2() = "+rect2.getEdge2() + "\n"
			);
		return !(rect1.getEdge1() == rect2.getEdge1() && rect1.getEdge2() == rect2.getEdge2());
	} // rect1 is rotated related to rect2
	
	/**
	 * @param
	 * @return
	 */
	public boolean IsLower(RecInfo rect1, RecInfo rect2){
		return rect1.getY1() > rect2.getY1();
	} // rect1 is lower then rect2
	
	/**
	 * @param
	 * @return
	 */
	public boolean IsHigher(RecInfo rect1, RecInfo rect2){
		return rect1.getY1() < rect2.getY1();
	} // rect1 is higher then rect2
	
	/**
	 * @param
	 * @return
	 */
	public boolean IsToTheLeft(RecInfo rect1, RecInfo rect2){
		return rect1.getX1() < rect2.getX1();
	} // rect1 is to the left of rect2
	
	/**
	 * @param
	 * @return
	 */
	public boolean IsToTheRight(RecInfo rect1, RecInfo rect2){
		return rect1.getX1() > rect2.getX1();
	} // rect1 is to the right of rect2


	/* ------ Actions -------*/
	
	/**
	 * @param
	 */
	public void RotateRight(RecInfo source){
		MyRec rectangle = game.findRectangle(source);
		if(rectangle==null){
			debugPrint(DEBUG_FUNCTION,"RotateRight() : BUG rectangle is not found!");
		}
		RecInfo rect = rectangle.getRecInfo();		
		RecInfo destinationInfo = new RecInfo(rect.getX2()+1, 
											rect.getX2()+rect.getEdge2()+1, 
											rect.getY2()-rect.getEdge1(), 
											rect.getY2());
		game.Move(rect,  destinationInfo);
		// rect.update(destinationInfo);		
	}
	
	/**
	 * @param
	 */
	public void RotateLeft(RecInfo source){
		MyRec rectangle = game.findRectangle(source);
		if(rectangle==null){
			debugPrint(DEBUG_FUNCTION,"RotateLeft() : BUG rectangle is not found!");
		}
		RecInfo rect = rectangle.getRecInfo();		
		RecInfo destinationInfo = new RecInfo(rect.getX1()-rect.getEdge2()-1, 
								rect.getX1()-1, 
								rect.getY2()-rect.getEdge1(), 
								rect.getY2());
		game.Move(rect,  destinationInfo);
		// rect.update(destinationInfo);		
	}

	/**
	 * @param
	 */	
	public void MoveRight(RecInfo source){
		MyRec rectangle = game.findRectangle(source);
		if(rectangle==null){
			debugPrint(DEBUG_FUNCTION,"MoveRight() : BUG rectangle is not found!");
		}
		RecInfo rect = rectangle.getRecInfo();
		RecInfo destinationInfo = new RecInfo(rect.getX1()+1, rect.getX2()+1, 
							rect.getY1(), rect.getY2());
		game.Move(rect,  destinationInfo);
		// rect.update(destinationInfo);		
	}
	
	/**
	 * @param
	 */	
	public void MoveLeft(RecInfo source){
		MyRec rectangle = game.findRectangle(source);
		if(rectangle==null){
			debugPrint(DEBUG_FUNCTION,"MoveLeft() : BUG rectangle is not found!");
		}
		RecInfo rect = rectangle.getRecInfo();		
		RecInfo destinationInfo = new RecInfo(rect.getX1()-1, rect.getX2()-1, 
				rect.getY1(), rect.getY2());	
		game.Move(rect,  destinationInfo);
		// rect.update(destinationInfo);	
	}

	/**
	 * @param
	 */	
	public void MoveUp(RecInfo source){
		MyRec rectangle = game.findRectangle(source);
		if(rectangle==null){
			debugPrint(DEBUG_FUNCTION,"MoveUp() : BUG rectangle is not found!");
		}
		RecInfo rect = rectangle.getRecInfo();		
		RecInfo destinationInfo = new RecInfo(rect.getX1(), rect.getX2(), 
				rect.getY1()-1, rect.getY2()-1);		
		game.Move(rect,  destinationInfo);
		// rect.update(destinationInfo);
	}

	/**
	 * @param
	 */	
	public void MoveDown(RecInfo source){
		MyRec rectangle = game.findRectangle(source);
		if(rectangle==null){
			debugPrint(DEBUG_FUNCTION,"MoveDown() : BUG rectangle is not found!");
		}
		RecInfo rect = rectangle.getRecInfo();		
		RecInfo destinationInfo = new RecInfo(rect.getX1(), rect.getX2(), 
				rect.getY1()+1, rect.getY2()+1);
		game.Move(rect,  destinationInfo);
		// rect.update(destinationInfo);
	}

	public boolean validateRectInfo(RecInfo rect){
		if(rect == null){
			Global.InvokeAssert(true,"RectInfo validation Assert: null pointer received!");
			return false;
		}
		if (!InSpace(rect,ROOM1) &&
			!InSpace(rect,ROOM2) &&
			!InSpace(rect,ROOM3) &&
			!InSpace(rect,CORRIDOR13) &&
			!InSpace(rect,CORRIDOR12) &&
			!InSpace(rect,CORRIDOR23)){
			Global.InvokeAssert(true,"RectInfo validation Assert: The RectInfo: "+ rect + " is Ilegal!");
			return false;
		}
		return true;
	}

/* ----------------------------- Object Methods ---------------------------- */

/* ---------------------------- Private Methods ---------------------------- */

	private static void debugPrint(int debugLevel, String debugText){
		if(debugLevel == CURRENT_DEBUG_LEVEL || CURRENT_DEBUG_LEVEL == DEBUG_ALL){
			System.out.println("Debug print: "+DEBUG_TAG);
			System.out.println(debugText);
		}
	}

 } // End of Class stripsAPI ----------------------------------------------- //