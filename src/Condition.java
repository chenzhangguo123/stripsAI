import java.util.ArrayList;

/**
 * Note: in this code we assume that the input is legal
 */
public class Condition {

/* -------------------------------- fields --------------------------------- */

	private StripsAPI api;
	private String name;
	private boolean desiredValue; // When we set a condition, we want to 
								  // what value we want it to be true or false
	private ArrayList<RecInfo> args;

/* ---------------------------- Constant Values ---------------------------- */

	public static final String IN_PLACE = "IN_PLACE";
	public static final String CAN_MOVE_UP =  "CAN_MOVE_UP";
	public static final String CAN_MOVE_DOWN = "CAN_MOVE_DOWN";
	public static final String CAN_MOVE_LEFT = "CAN_MOVE_LEFT";
	public static final String CAN_MOVE_RIGHT = "CAN_MOVE_RIGHT";
	public static final String CAN_ROTATE_RIGHT = "CAN_ROTATE_RIGHT";
	public static final String CAN_ROTATE_LEFT = "CAN_ROTATE_RIGHT";
	public static final String ROTATED = "ROTATED";
	public static final String IN_SPACE = "IN_SPACE";
	public static final String IS_LOWER = "IS_LOWER";
	public static final String IS_HIGHER = "IS_HIGHER";
	public static final String IS_TO_THE_LEFT = "IS_TO_THE_LEFT";
	public static final String IS_TO_THE_RIGHT = "IS_TO_THE_RIGHT";

/* ---------------------------- DEBUG Environment -------------------------- */

	private static final String DEBUG_TAG = "class Condition";
	private static final int DEBUG_ALL = 0;
	private static final int DEBUG_CLASS = 1;
	private static final int DEBUG_FUNCTION = 2;
	private static final int DEBUG_SPECIFIC = 3;
	private static final int CURRENT_DEBUG_LEVEL = DEBUG_ALL;

/* ---------------------------- Object Construction ------------------------ */

	public Condition(StripsAPI api, String name, ArrayList<RecInfo> args, 
						boolean desiredValue){
		this.api = api;
		this.name = name;
		this.args = args;
		this.desiredValue = desiredValue;
	}

/* ----------------------------- Public Methods ---------------------------- */

	public boolean Check(){
		switch(naem){
			case IN_PLACE:
				return api.InPlace(args.get(0),args.get(1));
			case CAN_MOVE_UP:
				return api.CanMoveUp(args.get(0));
			case CAN_MOVE_DOWN:
				return api.CanMoveDown(args.get(0));
			case CAN_MOVE_LEFT:
				return api.CanMoveLeft(args.get(0));
			case CAN_MOVE_RIGHT:
				return api.CanMoveRight(args.get(0));
			case CAN_ROTATE_RIGHT:
				return api.CanRotateRight(args.get(0));
			case CAN_ROTATE_LEFT:
				return api.CanRotateLeft(args.get(0));
			case ROTATED: 
				return api.Rotated(args.get(0),args.get(1));
			case IN_SPACE: 
				return api.InSpace(args.get(0),args.get(1));
			case IS_LOWER: 
				return api.IsLower(args.get(0),args.get(1));
			case IS_HIGHER: 
				return api.IsHigher(args.get(0),args.get(1));
			case IS_TO_THE_LEFT: 
				return api.IsToTheLeft(args.get(0),args.get(1));
			case IS_TO_THE_RIGHT: 
				return api.IsToTheRight(args.get(0),args.get(1));
			default:
				debugPrint(DEBUG_FUNCTION,"BUG invalid Condition");
				return false;
		}
	}

	/**
	 * Method Checks if the condition is satisfied according to our 
	 * desired value. 
	 * @return Check() AND desired value 	
	 */ 
	public boolean isSatisfied(){
		return (Check() && desiredValue);
	}

	/* --- Getters --- */

	public boolean getDesiredValue(){
		return this.desiredValue;
	}

	public String getName(){
		return this.name;
	}

	public ArrayList<RecInfo> getArgs(){
		return args;
	}

	/* --- Setters --- */

	public void setDesiredValue(boolean desiredValue){
		this.desiredValue = desiredValue;
	}

/* ----------------------------- Object Methods ---------------------------- */

    @Override 
    public String toString() {
    	String s = "";
    	s += name;
    	s += "( ";
    	for(int i=0,i<args.size(),i++){
    		s += args.get(i).toString();
    		if(i+1 != args.size()){
    			s+= ",";
    		}
    	}
    	s += " )";
    	return s;
    }

/* ---------------------------- Private Methods ---------------------------- */

	private static void debugPrint(int debugLevel, String debugText){
		if(debugLevel == CURRENT_DEBUG_LEVEL || debugLevel == DEBUG_ALL){
			System.out.println("Debug print: "+DEBUG_TAG);
			System.out.println(debugText);
		}
	}

 } // End of Class Condition ----------------------------------------------- //
