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

	public void solve(ArrayList<Problem>){
		prepareGoalStack();
		while(goalStack.empty() != true){
			Condition currentGoal goalStack.peek();
			if(currentGoal.getName() == IN_PLACE){
				if(currentGoal.Check() == true){
					goalStack.pop();
				}else{
					// TODO ... 
				}
			}
		}
	}

/* ----------------------------- Object Methods ---------------------------- */

/* ---------------------------- Private Methods ---------------------------- */

	private void prepareGoalStack(){

	}

 } // End of Class StripsEngine ----------------------------------------------- //