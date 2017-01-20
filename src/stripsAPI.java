

public class StripsAPI { //SingleTon

/* -------------------------------- fields *-------------------------------- */
	
	//For Singleton definition
	private static StripsAPI ourInstance = new StripsAPI();

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

	// Empty private constructor for singleTon
	private StripsAPI(){
		debugPrint(DEBUG_CLASS,"In constructor");
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
	public boolean InSpaceCondition(RecInfo rec, RecInfo space){
		return false;
	}

	/**
	 * @param
	 * @return
	 */
	public boolean InPlaceCondition(RecInfo rect,RecInfo place){
		debugPrint(DEBUG_FUNCTION,"In function InPlaceCondition");
		return false;
	}

	/**
	 * @param
	 * @return
	 */
	public boolean CanMoveUpCondition(RecInfo rect){
		return false;
	} // rect can move one step up
	
	/**
	 * @param
	 * @return
	 */
	public boolean CanMoveDownCondition(RecInfo rect){
		return false;
	} // rect can move one step down

	/**
	 * @param
	 * @return
	 */	
	public boolean CanMoveLeftCondition(RecInfo rect){
		return false;
	} // rect can move one step left
	
	/**
	 * @param
	 * @return
	 */	
	public boolean CanMoveRightCondition(RecInfo rect){
		return false;
	} // rect can move one step right
	
	/**
	 * @param
	 * @return
	 */
	public boolean CanRotateRightCondition(RecInfo rect){
		return false;
	} // there is enough space to make right rotation
	
	/**
	 * @param
	 * @return
	 */
	public boolean CanRotateLeftCondition(RecInfo rect){
		return false;
	} // there is enough space to make left rotation
	
	/**
	 * @param
	 * @return
	 */
	public boolean RotatedCondition(RecInfo rect1, RecInfo rect2){
		return false;
	} // rect1 is rotated related to rect2
	
	/**
	 * @param
	 * @return
	 */
	public boolean IsLowerCondition(RecInfo rect1, RecInfo rect2){
		return false;
	} // rect1 is lower then rect2
	
	/**
	 * @param
	 * @return
	 */
	public boolean IsHigherCondition(RecInfo rect1, RecInfo rect2){
		return false;
	} // rect1 is higher then rect2
	
	/**
	 * @param
	 * @return
	 */
	public boolean IsToTheLeftCondition(RecInfo rect1, RecInfo rect2){
		return false;
	} // rect1 is to the left of rect2
	
	/**
	 * @param
	 * @return
	 */
	public boolean IsToTheRightCondition(RecInfo rect1, RecInfo rect2){
		return false;
	} // rect1 is to the right of rect2


	/* ------ Actions -------*/
	
	/**
	 * @param
	 */
	public void RotateRightAction(RecInfo rect){

	}
	
	/**
	 * @param
	 */
	public void RotateLeftAction(RecInfo rect){
	
	}

	/**
	 * @param
	 */	
	public void MoveRightAction(RecInfo rec){
		debugPrint(DEBUG_FUNCTION,"In function MoveRightAction");
	}
	
	/**
	 * @param
	 */	
	public void MoveLeftAction(RecInfo rec){
		debugPrint(DEBUG_FUNCTION,"In function MoveLeftAction");
	}

	/**
	 * @param
	 */	
	public void MoveUpAction(RecInfo rec){
		debugPrint(DEBUG_FUNCTION,"In function MoveUpAction");
	}

	/**
	 * @param
	 */	
	public void MoveDownAction(RecInfo rec){
		debugPrint(DEBUG_FUNCTION,"In function MoveDownAction");
	}
	
	
/* ----------------------------- Object Methods ---------------------------- */

/* ---------------------------- Private Methods ---------------------------- */

	private void debugPrint(int debugLevel, String debugText){
		if(debugLevel == CURRENT_DEBUG_LEVEL || debugLevel == DEBUG_ALL){
			System.out.println("Debug print: "+DEBUG_TAG);
			System.out.println(debugText);
		}
	}

 } // End of Class stripsAPI ----------------------------------------------- //