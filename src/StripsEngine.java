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
		// TODO ...
	}

/* ----------------------------- Object Methods ---------------------------- */

/* ---------------------------- Private Methods ---------------------------- */

 } // End of Class StripsEngine ----------------------------------------------- //