
public class RecInfo {
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private int edge1;
	private int edge2;
	
	public RecInfo(int x1, int x2, int y1, int y2){		
		this.x1=x1;
		this.y1=y1;
		this.x2=x2;
		this.y2=y2;
		edge1=this.x2-this.x1;
		edge2=this.y2-this.y1;
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

	@Override
	public String toString(){
		return "(" + x1 + "," + x2 + "," + y1 + "," + y2 + ")";
	}
	
}
