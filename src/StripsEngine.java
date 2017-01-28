import java.util.Stack;
import java.util.ArrayList;

public class StripsEngine {

/* -------------------------------- fields *-------------------------------- */

	private GameGraphics game;
	private StripsAPI api;
	private Stack<Condition> goalStack;
	private ArrayList<Action> plan;
	private ArrayList<Problem> problems;

	//aux
	private Stack<Condition> problemStack;

	//Loop prevention
	private int iterationsSinceLastMove;
	private int movesMade;
	
/* ---------------------------- Constant Values ---------------------------- */

	//Infinite Loop prevention
	private static final int ALLOWED_ITERATIONS_UNTIL_MOVE = 200;
	private static final int MAX_MOVES_FOR_EACH_PROBLEM = 100;
	private static final int MAX_STACK_SIZE_PER_PROBLEM = 100;

/* ---------------------------- DEBUG Environment -------------------------- */

	private static final String DEBUG_TAG = "StripsEngine";
	private static final int DEBUG_ALL = 0;
	private static final int DEBUG_CLASS = 1;
	private static final int DEBUG_FUNCTION = 2;
	private static final int DEBUG_SPECIFIC = 3;
	private static final int DEBUG_NONE = -1;
	private static final int CURRENT_DEBUG_LEVEL = DEBUG_ALL;


/* ---------------------------- Object Construction ------------------------ */

	public StripsEngine(GameGraphics game){
		this.game = game;
		this.api = new StripsAPI(game);
		this.goalStack = new Stack<Condition>();
		this.plan = null;
		this.problems = game.getProblems();
		this.problemStack = new Stack<Condition>();
	}
	
/* ----------------------------- Public Methods ---------------------------- */

	public void Solve(){
		debugPrint(DEBUG_FUNCTION,"reached Solve");
		iterationsSinceLastMove = 0;
		movesMade = 0;
		prepareGoalStack();

		/* We will continue to solve problems until our goalStack is empty */
		while(goalStack.empty() != true){
			//Check if we are not in an infinite loop:
			if(isInfiniteLoop()){
				break;
			}

			// Lets look at our top Goal, if it is satisfied, just pop it out
			iterationsSinceLastMove++;
			Condition currentGoal = goalStack.peek();
			Condition currentProblem = problemStack.peek();
			debugPrint(DEBUG_FUNCTION,
				"currentGoal is "+currentGoal.toString() + "\n" +
				"currentProblem is "+currentProblem.toString());
			if(currentGoal.isSatisfied()){
				debugPrint(DEBUG_FUNCTION,"The Goal:"+currentGoal.toString() + 
						"poped out of the GoalStack");
				if(currentGoal == currentProblem){
					problemStack.pop();
				}
				goalStack.pop();
			}else{
				switch(currentGoal.getName()){
					/* If the new Goal is IN_PLACE type and it is not satisfied,
					 * Then we will add SubGoals to our GoalStack
					 */
					case Condition.IN_PLACE:
						/* If we face a new problem that we never seen before
						 * add it to the problemStack
						 */
						if(currentGoal != currentProblem){
							problemStack.push(currentGoal);
						}
						/* Adding 6 new Conditions to the GoalStack
							ST-5.	IS_TO_THE_LEFT(rect1,targed1) = false
							ST-4.	IS_TO_THE_RIGHT(rect1,targed1) = false
							ST-3.	IS_LOWER(rect1,targed1) = false
							ST-2.	IS_HIGHER(rect1,targed1) = false
							ST-1.  ROTATED(rect1,targed1) = false - Both must not 
							    be rotated one related to another							
							ST.  IN_SPACE(source,ROOM(targsed)) = true - They must
								in the same room
						 */
						ArrayList<RecInfo> furniture = currentGoal.getArgs();
						Condition newGoal = new Condition(api,
												Condition.IS_TO_THE_LEFT,
														  furniture,false);
						goalStack.push(newGoal);
						newGoal = new Condition(api,
												Condition.IS_TO_THE_RIGHT,
														  furniture,false);
						goalStack.push(newGoal);
						newGoal = new Condition(api,
												Condition.IS_LOWER,
														  furniture,false);
						goalStack.push(newGoal);	
						newGoal = new Condition(api,
												Condition.IS_HIGHER,
														  furniture,false);
						goalStack.push(newGoal);
						newGoal = new Condition(api,
														  Condition.ROTATED,
														  furniture,false);
						goalStack.push(newGoal);						
						RecInfo targed = furniture.get(1);
						RecInfo desiredRoom = api.getRoom(targed);
						ArrayList<RecInfo> args = new ArrayList<RecInfo>();
						args.add(furniture.get(0));
						args.add(desiredRoom);
						newGoal = new Condition(api,
												Condition.IN_SPACE,
														  args,true);	
						goalStack.push(newGoal);			
						break;

					/* If we reach the following cases, it means we try to  
					 * apply an Action, but we ran in to an Obstacle 
					 * see handleObstacleCase() bellow
					 */
					case Condition.CAN_MOVE_UP:
						handleObstacleCase(Action.MOVE_UP);
						break;
					case Condition.CAN_MOVE_DOWN:
						handleObstacleCase(Action.MOVE_DOWN);
						break;
					case Condition.CAN_MOVE_LEFT:
						handleObstacleCase(Action.MOVE_LEFT);
						break;
					case Condition.CAN_MOVE_RIGHT:
						handleObstacleCase(Action.MOVE_RIGHT);
						break;
					case Condition.CAN_ROTATE_RIGHT:
						handleObstacleCase(Action.ROTATE_RIGHT);
						break;
					case Condition.CAN_ROTATE_LEFT:
						handleObstacleCase(Action.ROTATE_LEFT);
						break;
					case Condition.IN_SPACE: 
						moveBetweenRooms();
						break;

					/* Here we start to deal with Actions
					 * see handleActionCase() below
					 */
					case Condition.ROTATED: 
						handleActionCase(Action.ROTATE_LEFT);
						break;
					case Condition.IS_LOWER: 
						handleActionCase(Action.MOVE_UP);
						break;
					case Condition.IS_HIGHER: 
						handleActionCase(Action.MOVE_DOWN);
						break;
					case Condition.IS_TO_THE_LEFT: 
						handleActionCase(Action.MOVE_RIGHT);
						break;
					case Condition.IS_TO_THE_RIGHT: 
						handleActionCase(Action.MOVE_LEFT);
						break;
					default:
						break;
				}   //switch
			}	//else
		} // while
		printPlan();
	}

/* ----------------------------- Object Methods ---------------------------- */

/* ---------------------------- Private Methods ---------------------------- */


	/**
	 * Method prepares the initial GoalStack and ProblemStack, using the info
	 * in the ProblemsList
	 */
	private void prepareGoalStack(){
		plan = new ArrayList<Action>();
		for (Problem Problem : problems){
			ArrayList<RecInfo> args = new ArrayList<RecInfo>();
			args.add(Problem.getSource());
			args.add(Problem.getTarged());
        	Condition newGoal = new Condition(api,
        									  Condition.IN_PLACE,
        									  args,true);
        	goalStack.push(newGoal);
        	problemStack.push(newGoal);
      	}		
	}

	/**
	 * Method adds Goals for moving the source furniture to the specified room
	 * - currentDoorway: we find a spot near the doorway in current room
	 * - targedDoorway: we find a spot near the doorway in the target room
	 * - we add the following to the GoalStack:
	 * ST-4 : IS_TO_THE_LEFT(source,targedDoorway) = false
	 * ST-3 : IS_TO_THE_RIGHT(source,targedDoorway) = false
	 * ST-2	: IS_LOWER(source,targedDoorway) = false
	 * ST-1	: IS_HIGHER(source,targedDoorway) = false
	 * ST : IN_PLACE(source,currentDoorway)
	 */
	private void moveBetweenRooms(){
		
		Condition currentGoal = goalStack.peek();
		RecInfo source = currentGoal.getArgs().get(0);
		RecInfo currentRoom = api.getRoom(source);
		RecInfo targedRoom = currentGoal.getArgs().get(1);
		RecInfo currentDoorway = api.findSpotNearDorway(source,
														currentRoom,
														targedRoom);
		RecInfo targedDoorway = api.findSpotNearDorway(source,
														targedRoom,
														currentRoom);
		ArrayList<RecInfo> args = new ArrayList<RecInfo>();
		args.add(source);
		args.add(targedDoorway);
		Condition newGoal = new Condition(api,
										Condition.IS_TO_THE_LEFT,args,false);
		goalStack.push(newGoal);
		newGoal = new Condition(api,Condition.IS_TO_THE_RIGHT,args,false);
		goalStack.push(newGoal);
		newGoal = new Condition(api,Condition.IS_LOWER,args,false);
		goalStack.push(newGoal);	
		newGoal = new Condition(api,Condition.IS_HIGHER,args,false);
		goalStack.push(newGoal);

		args = new ArrayList<RecInfo>();
		args.add(source);
		args.add(currentDoorway);
		newGoal = new Condition(api,Condition.IN_PLACE,args,true);
		goalStack.push(newGoal);
	}

	/** Helper method for Solve()
	 * - We define the Action we want to preform
	 * - Check if the preconditions are satisfied, 
	 * 	 - if true, we apply the action
	 * 	 - else we add them to the goal stack
	 */
	private void handleActionCase(String action){
		Condition currentGoal = goalStack.peek();
		RecInfo source = currentGoal.getArgs().get(0);
		Action newAction = new Action(api,
									  action,
									  source);
		if(newAction.CheckPreconditions()){
			RecInfo printRec = source.copy();
			Action actionPrint = new Action(api,action,printRec);
			newAction.Apply();
			plan.add(actionPrint);
			iterationsSinceLastMove = 0;
			movesMade++;
		}else{
			for (Condition precondition : newAction.getPreconditions()){
	        	goalStack.add(precondition);
	      	}											
		}		
	}

	/**
	 * Method to handle an Obstacle case
	 * If we reached thease cases, means that we want to
	 * apply an action, but we have an Obstacle. In the 
	 * current engine, we choose to move the Obstacle aside
	 * to let us apply the action and after we apply it, we 
	 * return the obstacle to it's original place
	 * so we add the following to the GoalStack:
	 * ST-2. IN_PLACE(obstacle,oldObstaclePlace) = true : Return the 
	 *	obstacle to it's original place
	 * ST-1. IN_PLACE(source, targed) = true :
	 *	move source to targed
	 * ST. IN_PLACE(obstacle,tmpPlace) = true : Move the 
	 *	obstacle to temprary place
	 */	
	private void handleObstacleCase(String action){
		Condition currentProblem = problemStack.peek();
		RecInfo source = currentProblem.getArgs().get(0);
		RecInfo targed = currentProblem.getArgs().get(1);
		RecInfo obstacle = api.getObstacle(source,
										action);
		debugPrint(DEBUG_SPECIFIC,"getObstacle returned "+obstacle);
		RecInfo oldObstaclePlace = obstacle.copy();
		oldObstaclePlace.setDummy();
		RecInfo tmpPlace = api.findTempObstaclePlace(source,
										obstacle,
										action);
		debugPrint(DEBUG_SPECIFIC,"findTempObstaclePlace returned "+tmpPlace);
		ArrayList<RecInfo> args;
		Condition newGoal;
		boolean needToReturnObstacle = doWeneedToReturnObstacle(oldObstaclePlace,obstacle);

		if(needToReturnObstacle){
			args = new ArrayList<RecInfo>();
			args.add(obstacle);
			args.add(oldObstaclePlace);
			newGoal = new Condition(api,
											  Condition.IN_PLACE,
											  args,true);
			goalStack.push(newGoal);
			args = new ArrayList<RecInfo>();
			args.add(source);
			args.add(targed);
			newGoal = new Condition(api,
											  Condition.IN_PLACE,
											  args,true);
			goalStack.push(newGoal);
		}

		args = new ArrayList<RecInfo>();
		args.add(obstacle);
		args.add(tmpPlace);
		newGoal = new Condition(api,
										  Condition.IN_PLACE,
										  args,true);
		goalStack.push(newGoal);
	}

	private void printPlan(){
		for(Action action : plan){
			System.out.println(action.toString());
		}
	}

	private boolean doWeneedToReturnObstacle(RecInfo oldObstaclePlace, 
		RecInfo obstacle){
		boolean result = false;
		RecInfo obstacleTarged = obstacle.getTarged();
		if(obstacleTarged != null){
			if (obstacleTarged.equalsByCoords(oldObstaclePlace)) {
				result = true;
			}
		}
		debugPrint(DEBUG_SPECIFIC,"Obstacle is"+obstacle.toString()+"\n" +
			"obstacle.targed = "+ obstacleTarged + "\n" +
			"obstacleOldPlace = "+ oldObstaclePlace + "\n" +
			"Need to return = "+ result);
		return result;
	}


	private boolean isInfiniteLoop(){
		if(iterationsSinceLastMove > ALLOWED_ITERATIONS_UNTIL_MOVE){
			debugPrint(DEBUG_FUNCTION,"Infinite Loop: iterationsSinceLastMove = "+iterationsSinceLastMove);
			return true;
		}
		if(movesMade > MAX_MOVES_FOR_EACH_PROBLEM*problems.size()){
			debugPrint(DEBUG_FUNCTION,"Infinite Loop: movesMade = "+movesMade);			
			return true;
		}
		if(goalStack.size() > MAX_STACK_SIZE_PER_PROBLEM*problems.size()){
			debugPrint(DEBUG_FUNCTION,"Infinite Loop: goalStack Size = "+goalStack.size());	
			return true;
		}
		return false;
	}

	private static void debugPrint(int debugLevel, String debugText){
		if(debugLevel == CURRENT_DEBUG_LEVEL || CURRENT_DEBUG_LEVEL == DEBUG_ALL){
			System.out.println("Debug print: "+DEBUG_TAG);
			System.out.println(debugText);
		}
	}




 } // End of Class StripsEngine -------------------------------------------- //