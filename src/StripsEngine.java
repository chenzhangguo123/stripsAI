import java.util.Stack;
import java.util.ArrayList;

public class StripsEngine {

/* -------------------------------- fields *-------------------------------- */

	private GameGraphics game;
	private StripsAPI api;
	private Stack<Condition> goalStack;
	private ArrayList<Action> plan;
	private ArrayList<Problem> problems;
	private Stack<Condition> problemStack;

	
/* ---------------------------- Constant Values ---------------------------- */

/* ---------------------------- DEBUG Environment -------------------------- */

/* ---------------------------- Object Construction ------------------------ */

	public StripsEngine(GameGraphics game){
		this.game = game;
		this.api = new StripsAPI(game);
		this.goalStack = new Stack<Condition>();
		this.plan = new ArrayList<Action>();
		this.problems = game.getProblems();
		this.problemStack = new Stack<Condition>();
	}
	
/* ----------------------------- Public Methods ---------------------------- */

	public void Solve(){
		prepareGoalStack();

		/* We will continue to solve problems until our goalStack is empty */
		while(goalStack.empty() != true){

			// Lets look at our top Goal, if it is satisfied, just pop it out
			Condition currentGoal = goalStack.peek();
			Condition currentProblem = problemStack.peek();
			if(currentGoal.isSatisfied()){
				if(currentGoal == currentProblem){
					problemStack.pop();
				}
				goalStack.pop();
			}else{
				switch(currentGoal.getName){
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
							1.  ROTATED(rect1,targed1) = false - Both must not 
							    be rotated one related to another
							2.	IS_TO_THE_LEFT(rect1,targed1) = false
							3.	IS_TO_THE_RIGHT(rect1,targed1) = false
							4.	IS_LOWER(rect1,targed1) = false
							5.	IS_HIGHER(rect1,targed1) = false
							6.  IN_SPACE(source,ROOM(targsed)) = true - They must
								in the same room
						 */
						ArrayList<RecInfo> furniture = currentGoal.getArgs();
						Condition newGoal = new Condition(api,
														  Condition.ROTATED,
														  furniture,false);
						goalStack.push(newGoal);
						newGoal = new Condition(api,
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
						handleActionCase(Action.MOVE_LEFT);
						break;
					case Condition.IS_TO_THE_RIGHT: 
						handleActionCase(Action.MOVE_RIGHT);
						break;
					default:
						break;
				}   //switch
			}	//else
		}
	}

/* ----------------------------- Object Methods ---------------------------- */

/* ---------------------------- Private Methods ---------------------------- */


	/**
	 * Method prepares the initial GoalStack and ProblemStack, using the info
	 * in the ProblemsList
	 */
	private void prepareGoalStack(){
		for (Problem Problem : problems){
			ArrayList<RecInfo> args = new ArrayList<RecInfo>();
			args.add(problem.getSource);
			args.add(problem.getTarged);
        	Condition newGoal = new Condition(api,
        									  Condition.IN_PLACE,
        									  args,true);
        	goalStack.push(newGoal);
        	problemStack.push(newGoal);
      	}		
	}

	private void moveBetweenRooms(){

	}


	/** Helper method for Solve()
	 * - We define the Action we want to preform
	 * - Check if the preconditions are satisfied, 
	 * 	 - if true, we apply the action
	 * 	 - else we add them to the goal stack
	 */
	private void handleActionCase(String action){
		currentGoal = goalStack.peek();
		RecInfo source = currentGoal.getArgs.get(0);
		Action newAction = new Action(api,
									  action,
									  source);
		if(newAction.CheckPreconditions()){
			newAction.Apply();
			plan.add(newAction);
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
	 * ST-2. IN_PLACE(tmpPlace,obstacle) = true : Return the 
	 *	obstacle to it's original place
	 * ST-1. IN_PLACE(source, targed) = true :
	 *	move source to targed
	 * ST. IN_PLACE(obstacle,tmpPlace) = true : Move the 
	 *	obstacle to temprary place
	 *
	 */
	private void handleObstacleCase(String action){
		Condition currentProblem = problemStack.peek();
		RecInfo source = currentProblem.getArgs().get(0);
		RecInfo targed = currentProblem.getArgs().get(1);
		RecInfo obstacle = api.getObstacle(source,
										action);
		RecInfo tmpPlace = api.findTempObstaclePlace(source,
										obstacle,
										action);
		ArrayList<RecInfo> args = new ArrayList<RecInfo>();
		args.add(tmpPlace);
		args.add(obstacle);
		Condition newGoal = new Condition(api,
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
		args = new ArrayList<RecInfo>();
		args.add(obstacle);
		args.add(tmpPlace);
		newGoal = new Condition(api,
										  Condition.IN_PLACE,
										  args,true);
		goalStack.push(newGoal);
	}

 } // End of Class StripsEngine ----------------------------------------------- //