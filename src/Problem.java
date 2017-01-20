

/**
 * A small class to describe a simple problem:
 * The class contains a pair of rectangles source and targed
 */
 public class Problem {
/* -------------------------------- fields --------------------------------- */

	private RecInfo source;
	private RecInfo targed;

/* ---------------------------- Constant Values ---------------------------- */

/* ---------------------------- DEBUG Environment -------------------------- */

/* ---------------------------- Object Construction ------------------------ */

	public Problem(RecInfo source, RecInfo targed){
		this.source = source;
		this.targed = targed;
	}

/* ----------------------------- Public Methods ---------------------------- */

	public RecInfo getSource(){
		return source;
	}

	public RecInfo getTarged(){
		return targed;
	}

/* ----------------------------- Object Methods ---------------------------- */

/* ---------------------------- Private Methods ---------------------------- */

} // End of Class Problem -------------------------------------------------- //
