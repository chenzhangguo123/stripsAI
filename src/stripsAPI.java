

public class stripsAPI {

/* -------------------------------- fields *-------------------------------- */

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

/* ----------------------------- Public Methods ---------------------------- */

	/* ------ Check for possible moves -------*/
	
	/**
	 * 
	 * @param the current furniture
	 * @return one of the Rooms or dorway defined above 
	 * under Rooms definition
	 */
	public RecInfo getRoom(MyRec rect){
		return ROOM1;
	}
	
	/**
	 * The function check if given rectangle is inside a space
	 * @param rec - the given rectangle
	 * @param space - the space we want to check 
	 * @return : true if the rectangle is fully covered by the space 
	 */
	public boolean isInside(RecInfo rec, RecInfo space){
		return false;
	}
	
	/**
	 * The function check if given rectangle intersects with the space
	 * @param rec - the given rectangle
	 * @param space - the space we want to check 
	 * @return : true if the rectangle has an intersection with the space 
	 */
	public boolean isIntersects(RecInfo rec, RecInfo space){
		return false
	}
	
	/* ------ Make one step change -------*/
	
	public void rotateRight(RecInfo rect){
		
	}
	
	public void rotateLeft(RecInfo rect){
		
	}
	
	public void moveRight(RecInfo rec){
		
	}
	
	public void moveLeft(RecInfo rec){
		
	}
	
	public void moveUp(RecInfo rec){
		
	}
	
	public void moveDown(RecInfo rec){
		
	}
	
	
/* ----------------------------- Object Methods ---------------------------- */

/* ---------------------------- Private Methods ---------------------------- */

 } // End of Class stripsAPI ----------------------------------------------- //