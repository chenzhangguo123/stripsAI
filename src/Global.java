

/**
 * class Global: Contains global variables and static methods to apply 
 * in all project classes
 */
public class Global {

/* -------------------------------- fields *-------------------------------- */



/* ---------------------------- Constant Values ---------------------------- */


/* ---------------------------- DEBUG Environment -------------------------- */

	public static final boolean ENABLE_DEBUG = true;
	// Assertions
	private static final boolean ENABLE_ASSERT = true;

/* ---------------------------- Object Construction ------------------------ */

/* ----------------------------- Public Methods ---------------------------- */

	public static final void InvokeAssert(boolean expression, String text){
		if(ENABLE_ASSERT){
			System.out.println(text);
			assert !expression;
		}
	}

/* ----------------------------- Object Methods ---------------------------- */

/* ---------------------------- Private Methods ---------------------------- */

} // End of Class Global --------------------------------------------------- //
