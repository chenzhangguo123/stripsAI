
public class StripsAPI {
/* -------------------------------- fields *-------------------------------- */
	
	private GameGraphics game;

/* ---------------------------- Constant Values ---------------------------- */

	// Rooms definition:  in the format RecInfo(x1,x2,y1,y2)
	public static final RecInfo ROOM1 = new RecInfo(0,7,0,4);
	public static final RecInfo ROOM2 = new RecInfo(0,7,5,11);
	public static final RecInfo ROOM3 = new RecInfo(8,19,0,11);
	public static final RecInfo DOORWAY_ROOMS12 = new RecInfo(2,5,4,5);
	public static final RecInfo DOORWAY_ROOMS13 = new RecInfo(7,8,1,3);
	public static final RecInfo DOORWAY_ROOMS23 = new RecInfo(7,8,6,10);
	
	
/* ---------------------------- DEBUG Environment -------------------------- */

	private static final String DEBUG_TAG = "StripsAPI";
	private static final int DEBUG_ALL = 0;
	private static final int DEBUG_CLASS = 1;
	private static final int DEBUG_FUNCTION = 2;
	private static final int DEBUG_SPECIFIC = 3;
	private static final int CURRENT_DEBUG_LEVEL = DEBUG_ALL;

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
		debugPrint(DEBUG_FUNCTION,"In function getRoom");
		return ROOM1;
	}

	/**
	 * Method returns the obstacle that prevets "rect" to allpy the action
	 * @param rect - the rectangle we want to move
	 * @param actionName - the name of the action we want to preform
	 * @return - Obstacle info
	 */
	public RecInfo getObstacle(RecInfo rect,String actionName){
		switch(actionName){
			case MOVE_LEFT:		
				// TODO
				return;
			case MOVE_RIGHT:
				// TODO
				return;		
			case MOVE_UP:
				// TODO				
				return;			
			case MOVE_DOWN:
				// TODO
				return;		
			case ROTATE_LEFT:
				// TODO
				return;		
			case ROTATE_RIGHT:
				// TODO
				return;
			default: 
				return;		
		}
	}

	/**
	 * Method calculates temprary place, in the same room, where we can 
	 * move the Obstacle, so we will be able to preform the action
	 */
	public RecInfo findTempObstaclePlace(RecInfo rect, 
									     RecInfo obstacle,
									     String actionName){
			switch(actionName){
			case MOVE_LEFT:		
				// TODO
				return;
			case MOVE_RIGHT:
				// TODO
				return;		
			case MOVE_UP:
				// TODO				
				return;			
			case MOVE_DOWN:
				// TODO
				return;		
			case ROTATE_LEFT:
				// TODO
				return;		
			case ROTATE_RIGHT:
				// TODO
				return;
			default: 
				return;		
		}
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
	public RecInfo findSpotNearDorway(RecInfo furniture, RecInfo currentRoom
												RecInfo targedRoom){
		// TODO
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
		return false;
	}

	/**
	 * @param
	 * @return
	 */
	public boolean InPlace(RecInfo rect,RecInfo place){
		debugPrint(DEBUG_FUNCTION,"In function InPlace");
		return false;
	}

	/**
	 * @param
	 * @return
	 */
	public boolean CanMoveUp(RecInfo rect){
		return false;
	} // rect can move one step up
	
	/**
	 * @param
	 * @return
	 */
	public boolean CanMoveDown(RecInfo rect){
		return false;
	} // rect can move one step down

	/**
	 * @param
	 * @return
	 */	
	public boolean CanMoveLeft(RecInfo rect){
		return false;
	} // rect can move one step left
	
	/**
	 * @param
	 * @return
	 */	
	public boolean CanMoveRight(RecInfo rect){
		return false;
	} // rect can move one step right
	
	/**
	 * @param
	 * @return
	 */
	public boolean CanRotateRight(RecInfo rect){
		return false;
	} // there is enough space to make right rotation
	
	/**
	 * @param
	 * @return
	 */
	public boolean CanRotateLeft(RecInfo rect){
		return false;
	} // there is enough space to make left rotation
	
	/**
	 * @param
	 * @return
	 */
	public boolean Rotated(RecInfo rect1, RecInfo rect2){
		return false;
	} // rect1 is rotated related to rect2
	
	/**
	 * @param
	 * @return
	 */
	public boolean IsLower(RecInfo rect1, RecInfo rect2){
		return false;
	} // rect1 is lower then rect2
	
	/**
	 * @param
	 * @return
	 */
	public boolean IsHigher(RecInfo rect1, RecInfo rect2){
		return false;
	} // rect1 is higher then rect2
	
	/**
	 * @param
	 * @return
	 */
	public boolean IsToTheLeft(RecInfo rect1, RecInfo rect2){
		return false;
	} // rect1 is to the left of rect2
	
	/**
	 * @param
	 * @return
	 */
	public boolean IsToTheRight(RecInfo rect1, RecInfo rect2){
		return false;
	} // rect1 is to the right of rect2


	/* ------ Actions -------*/
	
	/**
	 * @param
	 */
	public void RotateRight(RecInfo rect){

	}
	
	/**
	 * @param
	 */
	public void RotateLeft(RecInfo rect){
	
	}

	/**
	 * @param
	 */	
	public void MoveRight(RecInfo rec){
		debugPrint(DEBUG_FUNCTION,"In function MoveRight");
	}
	
	/**
	 * @param
	 */	
	public void MoveLeft(RecInfo rec){
		debugPrint(DEBUG_FUNCTION,"In function MoveLeft");
	}

	/**
	 * @param
	 */	
	public void MoveUp(RecInfo rec){
		debugPrint(DEBUG_FUNCTION,"In function MoveUp");
	}

	/**
	 * @param
	 */	
	public void MoveDown(RecInfo rec){
		debugPrint(DEBUG_FUNCTION,"In function MoveDown");
	}

/* ----------------------------- Object Methods ---------------------------- */

/* ---------------------------- Private Methods ---------------------------- */

	private static void debugPrint(int debugLevel, String debugText){
		if(debugLevel == CURRENT_DEBUG_LEVEL || debugLevel == DEBUG_ALL){
			System.out.println("Debug print: "+DEBUG_TAG);
			System.out.println(debugText);
		}
	}

 } // End of Class stripsAPI ----------------------------------------------- //