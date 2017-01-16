
public class StripsEngine {

/* -------------------------------- fields *-------------------------------- */

	private stripsAPI api;
	
/* ---------------------------- Constant Values ---------------------------- */

/* ---------------------------- DEBUG Environment -------------------------- */

/* ---------------------------- Object Construction ------------------------ */

	public StripsEngine(StripsApi myApi){
		api = myApi;
	}
	
/* ----------------------------- Public Methods ---------------------------- */

	public void solve(ArrayList<MyRec> start, ArrayList<MyRec> finish){
		api.rotateRight();
		api.moveOneStepRight();
		api.rotateRight();
	}

/* ----------------------------- Object Methods ---------------------------- */

/* ---------------------------- Private Methods ---------------------------- */

 } // End of Class StripsEngine ----------------------------------------------- //