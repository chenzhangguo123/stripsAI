import javafx.scene.shape.Rectangle;

public class MyRec extends Rectangle{

/* -------------------------------- fields --------------------------------- */	
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private int edge1;
	private int edge2;
	private String id;
	private RecInfo info;
	private RecInfo targed;

/* ---------------------------- Constant Values ---------------------------- */

/* ---------------------------- DEBUG Environment -------------------------- */

	private static final String DEBUG_TAG = "MyRec";
	private static final int DEBUG_ALL = 0;
	private static final int DEBUG_CLASS = 1;
	private static final int DEBUG_FUNCTION = 2;
	private static final int DEBUG_SPECIFIC = 3;
	private static final int CURRENT_DEBUG_LEVEL = DEBUG_ALL;

/* ---------------------------- Object Construction ------------------------ */
	
	public MyRec(int x1, int x2, int y1, int y2){
		super();
		if(x2<x1){
			int tX1=x1;
			x1=x2;
			x2=tX1;
		}
		if(y2<y1){
			int tY1=y1;
			y1=y2;
			y2=tY1;
		}		
		this.x1=x1;
		this.y1=y1;
		this.x2=x2;
		this.y2=y2;
		edge1=Math.abs(this.x2-this.x1);
		edge2=Math.abs(this.y2-this.y1);
		this.id = RecInfo.generateID(x1,x2,y1,y2);
		this.targed = null;
		this.info = new RecInfo(x1,x2,y1,y2,this.id);
	}

	public MyRec(){	
		super();
		this.x1=0;
		this.y1=0;
		this.x2=0;
		this.y2=0;
		edge1=0;
		edge2=0;
		this.id = RecInfo.DUMMY_ID;
		this.targed = null;
		this.info = new RecInfo(x1,x2,y1,y2,id);
	}


/* ----------------------------- Public Methods ---------------------------- */

	public void setCor(int x1, int x2, int y1, int y2){	
		if(x2<x1){
			int tX1=x1;
			x1=x2;
			x2=tX1;
		}
		if(y2<y1){
			int tY1=y1;
			y1=y2;
			y2=tY1;
		}
		this.x1=x1;
		this.y1=y1;
		this.x2=x2;
		this.y2=y2;
		edge1=Math.abs(this.x2-this.x1);
		edge2=Math.abs(this.y2-this.y1);
		this.info.setCor(x1,x2,y1,y2);
	}
	
	public int getX1(){
		return this.x1;
	}
	
	public int getX2(){
		return this.x2;
	}
	
	public int getY1(){
		return this.y1;
	}
	
	public int getY2(){
		return this.y2;
	}
	
	public void moveToRight(){
		this.x1++;
		this.x2++;
	}
	
	public void moveToLeft(){
		this.x1--;
		this.x2--;
	}
	
	public void moveUp(){
		this.y1++;
		this.y2++;
	}
	
	public void moveDown(){
		this.y1--;
		this.y2--;
	}
	
	public boolean checkEdges(MyRec rec) {
		//System.out.println(rec.edge1+" "+this.edge1+" "+rec.edge2+" "+this.edge2);
		if(this.edge1==rec.edge1){
			if(this.edge2==rec.edge2)
				return true;			
		}
		else
			if(this.edge1==rec.edge2)
				if(this.edge2==rec.edge1)
					return true;	
		return false;
	}
	public double calDis(MyRec rec){
		return Math.sqrt((x1-rec.x1)*(x1-rec.x1)+(y1-rec.y1)*(y1-rec.y1));
	}
	
	// public String toString(){
	// 	return super.toString()+" "+x1+" "+x2+" "+y1+" "+y2;		
	// }
	
	public RecInfo getRecInfo(){
		return info;
	}

	public void setTarged(RecInfo targed){
		if(targed == null){
			debugPrint(DEBUG_FUNCTION,"setTarged(): Warning, null pointer set!");
		}
		this.targed = targed;
		this.info.setTarged(targed);
	}

	public String getRecttId(){
		return this.id;
	}

	public boolean equalsById(MyRec rec){
		return this.id.equals(rec.id);
	}

	public boolean equalsById(RecInfo rec){
		return this.id.equals(rec.getId());
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

 } // End of Class MyRec --------------------------------------------------- //
