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

/* ---------------------------- Object Construction ------------------------ */

	// Empty private constructor for singleTon
	private StripsAPI(){}

/* ----------------------------- Public Methods ---------------------------- */

	/* ------ Auxiliary methods -------*/
	
	/**
	 * 
	 * @param the current furniture
	 * @return one of the Rooms or dorway defined above 
	 * under Rooms definition
	 */
	public RecInfo getRoom(RecInfo rect){
		return ROOM1;
	}
	
	/* In our implementation of STRIPS, We use a set of Conditions and 
		and Actions. Each set will be implemented as a group of methods.*/

	/* ------ Conditions -------*/

	/**
	 * The function check if given rectangle is inside a space
	 * @param rec - the given rectangle
	 * @param space - the space we want to check 
	 * @return : true if the rectangle is fully covered by the space 
	 */
	public static final boolean InSpace(RecInfo rec, RecInfo space){
		return false;
	}

	/* ------ Actions -------*/
	
	public void RotateRight(RecInfo rect){
		
	}
	
	public void RotateLeft(RecInfo rect){
		
	}
	
	public void MoveRight(RecInfo rec){
		
	}
	
	public void MoveLeft(RecInfo rec){
		
	}
	
	public void MoveUp(RecInfo rec){
		
	}
	
	public void MoveDown(RecInfo rec){
		
	}
	
	
/* ----------------------------- Object Methods ---------------------------- */

/* ---------------------------- Private Methods ---------------------------- */

 } // End of Class stripsAPI ----------------------------------------------- //