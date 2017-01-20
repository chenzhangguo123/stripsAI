 

 /**
  * Note: In this code we assume that we get the right input
  */
 public class Action {

/* -------------------------------- fields --------------------------------- */

	private StripsAPI api;
	private String name;
	private RecInfo furniture;
	private ArrayList<Condition> preconditions;

/* ---------------------------- Constant Values ---------------------------- */

	public static final String MOVE_LEFT = "Move Left";
	public static final String MOVE_RIGHT = "Move Right";
	public static final String MOVE_UP = "Move Up";
	public static final String MOVE_DOWN = "Move Down";
	public static final String ROTATE_LEFT = "Rotate Left";
	public static final String ROTATE_RIGHT = "Rotate Right";

/* ---------------------------- DEBUG Environment -------------------------- */

	private static final String DEBUG_TAG = "class Action";
	private static final int DEBUG_ALL = 0;
	private static final int DEBUG_CLASS = 1;
	private static final int DEBUG_FUNCTION = 2;
	private static final int DEBUG_SPECIFIC = 3;
	private static final int CURRENT_DEBUG_LEVEL = DEBUG_ALL;

/* ---------------------------- Object Construction ------------------------ */

	public Action(StripsAPI api, String name, RecInfo furniture){
		this.api = api;
		this.name = name;
		this.furniture = furniture;
		this preconditions = new ArrayList<Condition>();
		setPreconditions();
	}

/* ----------------------------- Public Methods ---------------------------- */

	public void Apply(){
		switch(name){
			case MOVE_LEFT:
				api.MoveLeft(furniture);
				return;
			case MOVE_RIGHT:
				api.MoveRight(furniture);
				return;		
			case MOVE_UP:
				api.MoveUp(furniture);
				return;			
			case MOVE_DOWN:
				api.MoveDown(furniture);
				return;		
			case ROTATE_LEFT:
				api.RotateLeft(furniture);
				return;		
			case ROTATE_RIGHT:
				api.RotateRight(furniture);
				return;
			default: 
				return;		
		}
	}

	public ArrayList<Condition> getPreconditions(){
		return preconditions;
	}

/* ----------------------------- Object Methods ---------------------------- */

    @Override 
    public String toString() {
    	return name + "(" + furniture.toString + ")";
    }

/* ---------------------------- Private Methods ---------------------------- */

	private void setPreconditions(){
		ArrayList<RecInfo> args = new ArrayList<RecInfo>();
		args.add(furniture);
		switch(name){
			case MOVE_LEFT:
				Condition condition = new Condition(api,
													Condition.CAN_MOVE_LEFT,
													args,
													true);
				preconditions.add(condition);
				return;
			case MOVE_RIGHT:
				Condition condition = new Condition(api,
													Condition.CAN_MOVE_RIGHT,
													args,
													true);
				preconditions.add(condition);	
				return;		
			case MOVE_UP:
				Condition condition = new Condition(api,
													Condition.CAN_MOVE_UP,
													args,
													true);
				preconditions.add(condition);	
				return;			
			case MOVE_DOWN:
				Condition condition = new Condition(api,
													Condition.CAN_MOVE_DOWN,
													args,
													true);
				preconditions.add(condition);		
				return;		
			case ROTATE_LEFT:
				Condition condition = new Condition(api,
													Condition.CAN_ROTATE_LEFT,
													args,
													true);
				preconditions.add(condition);		
				return;		
			case ROTATE_RIGHT:
				Condition condition = new Condition(api,
													Condition.CAN_ROTATE_RIGHT,
													args,
													true);
				preconditions.add(condition);		
				return;
			default: 
				return;		
		}
	}

	private static void debugPrint(int debugLevel, String debugText){
		if(debugLevel == CURRENT_DEBUG_LEVEL || debugLevel == DEBUG_ALL){
			System.out.println("Debug print: "+DEBUG_TAG);
			System.out.println(debugText);
		}
	}

 } // End of Class X ------------------------------------------------------- //