import java.util.Stack;
import java.util.ArrayList;

public class StripsEngine {

/* -------------------------------- fields *-------------------------------- */

	private GameGraphics game;
	private StripsAPI api;
	private Stack<Condition> goalStack
	private ArrayList<Action> plan;
	private ArrayList<Problem> problems;

	
/* ---------------------------- Constant Values ---------------------------- */

/* ---------------------------- DEBUG Environment -------------------------- */

/* ---------------------------- Object Construction ------------------------ */

	public StripsEngine(GameGraphics game){
		this.game = game;
		this.api = new StripsAPI(game);
		this.goalStack = new Stack<Condition>();
		this.plan = new ArrayList<Action>();
		this.problems = getProblems();
	}
	
/* ----------------------------- Public Methods ---------------------------- */

	public void solve(){
		prepareGoalStack();

		/* We will continue to solve problems until our goalStack is empty */
		while(goalStack.empty() != true){

			// Lets look at our top Goal, if it is satisfied, just pop it out
			Condition currentGoal goalStack.peek();
			if(currentGoal.isSatisfied()){
				goalStack.pop();

			/* If the new Goal is IN_PLACE type and it is not satisfied,
			 * Then we will add SubGoals to our GoalStack
			 */
			}else{
				switch(currentGoal.getName){
					case IN_PLACE:
						/* Adding 6 new Conditions to the GoalStack
							1.  ROTATED(rect1,targed1) = false - Both must not 
							    be rotated one related to another
							2.	IS_TO_THE_LEFT(rect1,targed1) = false
							3.	IS_TO_THE_RIGHT(rect1,targed1) = false
							4.	IS_LOWER(rect1,targed1) = false
							5.	IS_HIGHER(rect1,targed1) = false
							6.  IN_SPACE(source,ROOM(targed)) = true - They must
								in the same room
						 */
						ArrayList<RecInfo>() furniture = currentGoal.getArgs();
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
						/*
						 * - We define the Action we want to preform
						 * - Check if the preconditions are satisfied, 
						 * 	 - if true, we apply the action
						 * 	 - else we add them to the goal stack
						 */
					case ROTATED: 
						RecInfo source = currentGoal.getArgs.get(0);
						Action newAction = new Action(api,
													  Action.ROTATE_RIGHT,
													  source);
						if(newAction.CheckPreconditions()){
							newAction.Apply();
							plan.add(newAction);
						}else{
							for (Condition precondition : 
								 newAction.getPreconditions()){
					        	goalStack.add(precondition);
					      	}											
						}
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
				}   //switch
			}	//else
		}
	}

/* ----------------------------- Object Methods ---------------------------- */

/* ---------------------------- Private Methods ---------------------------- */

	private void prepareGoalStack(){

	}

 } // End of Class StripsEngine ----------------------------------------------- //