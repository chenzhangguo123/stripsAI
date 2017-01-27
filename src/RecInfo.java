
public class RecInfo {

	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private int edge1;
	private int edge2;
	private String id;
	private RecInfo targed;

/* ---------------------------- Constant Values ---------------------------- */

	public static final String DUMMY_ID = "ID:[DUMMY]";

/* ---------------------------- DEBUG Environment -------------------------- */

	private static final String DEBUG_TAG = "RecInfo";
	private static final int DEBUG_ALL = 0;
	private static final int DEBUG_CLASS = 1;
	private static final int DEBUG_FUNCTION = 2;
	private static final int DEBUG_SPECIFIC = 3;
	private static final int CURRENT_DEBUG_LEVEL = DEBUG_FUNCTION;

/* ---------------------------- Object Construction ------------------------ */

	public RecInfo(int x1, int x2, int y1, int y2){		
		this.x1=x1;
		this.y1=y1;
		this.x2=x2;
		this.y2=y2;
		edge1=this.x2-this.x1;
		edge2=this.y2-this.y1;
		this.id = DUMMY_ID;
		this.targed = null;
	}	

		public RecInfo(int x1, int x2, int y1, int y2, String id){		
		this.x1=x1;
		this.y1=y1;
		this.x2=x2;
		this.y2=y2;
		edge1=this.x2-this.x1;
		edge2=this.y2-this.y1;
		this.id = id;
		this.targed = null;
	}	


/* ----------------------------- Public Methods ---------------------------- */

	public RecInfo copy(){
		RecInfo copy = new RecInfo(x1,x2,y1,y2);
		copy.setId(this.id);
		copy.setTarged(this.targed);
		return copy;
	}
	
	public int getEdge1() {
		return edge1;
	}
	
	public int getEdge2() {
		return edge2;
	}
	
	public boolean checkEdges(int edge1,int edge2) {
		//System.out.println(edge1+" "+this.edge1+" "+edge2+" "+this.edge2);
		if(this.edge1==edge1){
			if(this.edge2==edge2)
				return true;			
		}
		else
			if(this.edge1==edge2)
				if(this.edge2==edge1)
					return true;	
		System.out.println("damn");
		return false;
	}
	public double calDis(int x3, int y3){
		//System.out.println(x+" "+x2+" , "+y+" "+y2);
		double dis=Math.sqrt((x1-x3)*(x1-x3)+(y1-y3)*(y1-y3));
		return dis;
	}
	
	public int getX1(){
		return x1;
	}
	
	public int getY1(){
		return y1;
	}
	
	public int getX2(){
		return x2;
	}
	
	public int getY2(){
		return y2;
	}

	public void update(RecInfo newInfo){
		this.x1 = newInfo.x1;
		this.x2 = newInfo.x2;
		this.y1 = newInfo.y1;
		this.y2 = newInfo.y2;
		this.edge1 = newInfo.edge1;
		this.edge2 = newInfo.edge2;
	}

	public void setCor(int x1, int x2, int y1, int y2){
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		edge1=this.x2-this.x1;
		edge2=this.y2-this.y1;
	}

	public String getId(){
		return this.id;
	}

	public void setId(String id){
		this.id = id;
	}

	public void setTarged(RecInfo targed){
		if(targed == null){
			debugPrint(DEBUG_FUNCTION,"setTarged(): Warning, null pointer set!");
		}
		this.targed = targed;
	}

	public RecInfo getTarged(){
		return this.targed;
	}
	
	public void setDummy(){
		this.id = DUMMY_ID;
	}

	public boolean isDummy(){
		return this.id.equals(DUMMY_ID);
	}

	public boolean equalsById(RecInfo rec){
		return this.id.equals(rec.id);
	}

	public boolean equalsByCoords(RecInfo rec){
		return  (x1 == rec.x1) && 
				(x2 == rec.x2) &&
				(y1 == rec.y1) &&
				(y2 == rec.y2);
	}


	public static String generateID(int x1, int x2, int y1, int y2){
		return "ID:[" + x1 + "#" + x2 + "#" + y1 + "#" + y2 + "]";
	}

/* ----------------------------- Object Methods ---------------------------- */

	@Override
	public String toString(){
		return "(" + x1 + "," + x2 + "," + y1 + "," + y2 + ")" + id;
	}
	
/* ---------------------------- Private Methods ---------------------------- */

	private static void debugPrint(int debugLevel, String debugText){
		if(debugLevel == CURRENT_DEBUG_LEVEL || CURRENT_DEBUG_LEVEL == DEBUG_ALL){
			System.out.println("Debug print: "+DEBUG_TAG);
			System.out.println(debugText);
		}
	}


 } // End of Class RecInfo ------------------------------------------------ //
