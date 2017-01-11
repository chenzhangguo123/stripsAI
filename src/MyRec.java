import javafx.scene.shape.Rectangle;

public class MyRec extends Rectangle{
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private int edge1;
	private int edge2;
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
		edge1=this.x2-this.x1;
		edge2=this.y2-this.y1;
	}
	public MyRec(){	
		super();
		this.x1=0;
		this.y1=0;
		this.x2=0;
		this.y2=0;
		edge1=0;
		edge2=0;
	}
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
		edge1=this.x2-this.x1;
		edge2=this.y2-this.y1;
		//System.out.println(x1+" "+x2+" "+y1+" "+y2);
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
	public String toString(){
		return super.toString()+" "+x1+" "+x2+" "+y1+" "+y2;		
	}
}
