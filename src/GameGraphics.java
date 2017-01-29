import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.ResourceBundle;
import java.util.Stack;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameGraphics implements Initializable{

/* -------------------------------- fields --------------------------------- */
	
	Stack<Paint> cL=new Stack<>();
	Paint lastInCl;
	
	private double tileWidth;
    private double tileHeight;
    
    int[] preTile=new int[2];
    
    MyRec hoverRec = new MyRec();
    
    boolean pressed=false;
    boolean hovered=false;
    
    List<MyRec> furL=new LinkedList<>();
    List<MyRec> furR=new LinkedList<>();
    Map<Paint,MyRec> recsColor = new LinkedHashMap<>();
    
    boolean locked=false;
    
//    @FXML
//    private Text movesMade;

    @FXML
    private Button simBegin;
    
    @FXML
    private Button help;

    @FXML
    private Button showStack;
    
    @FXML
    private GridPane board1;

    @FXML
    private GridPane board2;
    
    //Showing the stack (HW2_v2)
    private Stack<Text> logicStack = new Stack<>();
	private ListProperty<Text> graphicStack = new SimpleListProperty<>(FXCollections.observableList(logicStack)); 
		
	private ListView<Text> movesMade;
    private Scene stackScene;
    private Stage stackStage;
    
    //Showing the final plan
    private ArrayList<Text> planList = new ArrayList<Text>();
	private ListProperty<Text> graphicPlanList = new SimpleListProperty<>(FXCollections.observableList(planList));
    private ListView<Text> planListView;
    private Scene planListScene;
    private Stage planListStage;
    

    //Timing
    private long msPerMove;
    
    @FXML 
    private TextField msMove;
    
    //Threading - needed to view every move and not only the final result
    public Thread solveThread;
    public Task<Void> solveTask;


/* ---------------------------- Constant Values ---------------------------- */

	final static double sizePre=0.99;

    //Time between each move in milliseconds
    private static final long GRAPHIC_STACK_MS = 50;
    private static final long MS = 1000 - GRAPHIC_STACK_MS;
    
    //graphic stack size:
    private static final double GRAPHIC_STACK_H = 700;
    private static final double GRAPHIC_STACK_W = 400;

/* ---------------------------- DEBUG Environment -------------------------- */

	private static final String DEBUG_TAG = "GameGraphics";
	private static final int DEBUG_ALL = 0;
	private static final int DEBUG_CLASS = 1;
	private static final int DEBUG_FUNCTION = 2;
	private static final int DEBUG_SPECIFIC = 3;
	private static final int DEBUG_NONE = -1;
	private static final int CURRENT_DEBUG_LEVEL = DEBUG_NONE;

/* --------------------------- Graphics Initialize ------------------------- */    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	help.setOnAction(
    	        new EventHandler<ActionEvent>() {
    	            @Override
    	            public void handle(ActionEvent event) {
    	                final Stage dialog = new Stage();
    	                dialog.setTitle("Help");
    	                dialog.initModality(Modality.APPLICATION_MODAL);
    	                VBox dialogVbox = new VBox(20);
    	                dialogVbox.getChildren().add(new Text(" Left click and drag the mouse to create a \"furniture\" "
    	                		+ "\n\n Right click to delete a \"furniture\" "
    	                		+ "\n\n Middle click to change color of a \"furniture\" in the right board "
    	                		+ "\n\n If \"furniture\" is black, it means there were no \"furniture\" in the same "
    	                		+ "\n size in the left board. "
    	                		+ "\n If you have putten one after you can use the middle click to update it. "
    	                		+ "\n\n Also, you should have only one \"furniture\" of each color in the right side "
    	                		+ "\n (on the left side it is already like that). "
    	                		+ "\n"));
    	                Scene dialogScene = new Scene(dialogVbox);
    	                dialog.setScene(dialogScene);
    	                dialog.show();
    	            }
    	         });
    	for(int i=0; i<360*19;i+=19) {
    		double sI=(double)(209%360);
    		double sISB=(sI/360*30+70)/100;
    		cL.add(Color.hsb(sI, sISB, sISB));		
    	}
    	lastInCl=cL.pop();
    	tileWidth=(board1.getPrefWidth())/20;
    	tileHeight=(board1.getPrefHeight())/12;
    	board1.setOnMousePressed(e->getPressed(e, true));
    	board2.setOnMousePressed(e->getPressed(e, false));
    	board1.setOnMouseReleased(e->{
    		if(!pressed)
    			return; 
    		pressed=false;
    		hovered=false;
    		int y1=preTile[0];
    		int x1=preTile[1];
    		int y2=getYfromSc(e.getSceneY(), true);
    		int x2=getXfromSc(e.getSceneX(), true);
    		if(!legalPlace(x1,x2,y1,y2, true)){
    			board1.getChildren().remove(hoverRec);
    			return;
    		}
    		MyRec rec = new MyRec(x1,x2,y1,y2);
            try{
            	setRec(rec,x1,x2,y1,y2,true);  
            	lastInCl=cL.pop();
            	board1.getChildren().addAll(rec);
            	furL.add(rec);
                recsColor.put(rec.getFill(), rec);
            } catch(Exception ex){}
            finally{
            	board1.getChildren().remove(hoverRec);
            }     
    	});
    	board2.setOnMouseReleased(e->{
    		if(!pressed)
    			return; 
    		pressed=false;
    		hovered=false;
    		int y1=preTile[0];
    		int x1=preTile[1];
    		int y2=getYfromSc(e.getSceneY(), false);
    		int x2=getXfromSc(e.getSceneX(), false); 
    		if(!legalPlace(x1,x2,y1,y2, false)){
    			board2.getChildren().remove(hoverRec);
    			return;
    		}
    		MyRec rec = new MyRec(x1,x2,y1,y2);
            try{
            	setRec(rec,x1,x2,y1,y2,false);  
            	lastInCl=cL.pop();
            	board2.getChildren().addAll(rec);
            	furR.add(rec);
            } catch(Exception ex){}
            finally{
            	board2.getChildren().remove(hoverRec);
            }         
    	});
    	board1.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
    		if(!pressed)
    			return;  		
    		int y1=preTile[0];
    		int x1=preTile[1];
    		int y2=getYfromSc(e.getSceneY(), true);
    		int x2=getXfromSc(e.getSceneX(), true);
    		hoverRec.setCor(x1,x2,y1,y2);
    		try {
				setRec(hoverRec,x1,x2,y1,y2,true);
				cL.push(lastInCl);
				if(!hovered)
	    			board1.getChildren().addAll(hoverRec);
	    		hovered=true;
			} catch (Exception ex) {}
    		
        });
    	board2.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
    		if(!pressed)
    			return;  		
    		int y1=preTile[0];
    		int x1=preTile[1];
    		int y2=getYfromSc(e.getSceneY(), false);
    		int x2=getXfromSc(e.getSceneX(), false);
    		hoverRec.setCor(x1,x2,y1,y2);
    		try {
				setRec(hoverRec,x1,x2,y1,y2,false);
				if(!hovered)
	    			board2.getChildren().addAll(hoverRec);
	    		hovered=true;
			} catch (Exception ex) {}
    		
        });

       	// The graphic stack code:
			movesMade=new ListView<>();
			movesMade.itemsProperty().bind(graphicStack);
			movesMade.setRotate(180);
			stackStage = new Stage();
			stackStage.setHeight(GRAPHIC_STACK_H);
            stackStage.setWidth(GRAPHIC_STACK_W);
			stackStage.setTitle("Goal Stack");
			stackStage.initModality(Modality.NONE);
			stackScene = new Scene(movesMade);
			stackStage.setOnCloseRequest(r->stackStage.hide());
			stackStage.setScene(stackScene);
			showStack.setOnAction(a->showGraphicStack());
			// the graphic stack code       	

            //The planList code
            planListView=new ListView<>();
            planListView.itemsProperty().bind(graphicPlanList);
            planListStage = new Stage();
            planListStage.setHeight(GRAPHIC_STACK_H);
            planListStage.setWidth(GRAPHIC_STACK_W);
            planListStage.setTitle("Final Plan");
            planListStage.initModality(Modality.NONE);
            planListScene = new Scene(planListView);
            planListStage.setOnCloseRequest(r->planListStage.hide());
            planListStage.setScene(planListScene);
//            showStack.setOnAction(a->planListStage.show());
            // the graphic planList code

            msPerMove = MS;
    }

/* ---------------------------- Start Simulator ---------------------------- */  
    @FXML
    private void startSim() {
    	GameGraphics game = this;
    	initTiming();
		System.out.println("started");
		printFurnitureLists();
		StripsEngine engine = new StripsEngine(game);
		solveTask = new Task<Void>() {
            @Override 
            public Void call() throws Exception {
            	System.out.println("inside new thread");
        		engine.Solve();
                return null;
            }
        };
        logicStack.clear();
        graphicStack.clear();
        solveThread = new Thread(solveTask);
        solveThread.setDaemon(true);
        solveThread.start();
	}

/* ----------------------------- Public Methods ---------------------------- */
    
    public ArrayList<Problem> getProblems(){
		ArrayList<Problem> problems = new ArrayList<Problem>();
		
		MyRec rectangle = null;
		RecInfo info = null;
		for(int i =0; i < furL.size(); i++){
			rectangle = furL.get(i);
			info = findRectangleMatch(rectangle);
			if (info == null){
				System.out.println("Destination rectangle is missing");
			} else{
				rectangle.setTarged(info);
				RecInfo sourceInfo = rectangle.getRecInfo();
				problems.add(new Problem(sourceInfo, info));
			}
		}

		return problems;
	}

    public boolean IsFree(RecInfo space){
    	MyRec rectangle = null;
    	for(int x = space.getX1(); x <= space.getX2(); x ++){
    		for(int y = space.getY1(); y <= space.getY2(); y++){
    			rectangle = getRectangleByPoint(x, y, furL);
    			if (rectangle != null){
    				return false;
    			}
    		}
    	}
    	return true;
    }
    
    public void Move(RecInfo source, RecInfo dest){
    	System.out.println("Game.move() : Source="+source+" Dest="+dest);
    	MyRec rectangle = findRectangle(source);

    	// TODO : Add here alert (assert ?) for trying to move out of the board
    	rectangle.setCor(dest.getX1(), dest.getX2(), dest.getY1(), dest.getY2());
    	makeMove(rectangle);
    }

    public MyRec findRectangle(RecInfo info){
    	MyRec rectangle;
    	if(!info.isDummy()){
 			rectangle = getRectangleById(info,furL);
    	}else{
    		rectangle = getRectangleByPoint(info.getX1(),
    									    info.getY1(),furL);
    	}
    	if(rectangle == null){
    		debugPrint(DEBUG_FUNCTION,"Game.Move() : BUG null pointer returned");
    	}
    	return rectangle;
    }

    
    public RecInfo getRectangleByYAxis(int x, int y1, int y2){
    	RecInfo info = null;
    	MyRec rectangle = null;
    	for(int i = y1; i <= y2; i++){
    		rectangle = getRectangleByPoint(x, i, furL);
    		if (rectangle != null){
    			info = rectangle.getRecInfo();
    			break;
    		}
    	}
    	return info;
    }

    public RecInfo getRectangleByXAxis(int x1, int x2, int y){
    	RecInfo info = null;
    	MyRec rectangle = null;
    	for(int i = x1; i <= x2; i++){
    		rectangle = getRectangleByPoint(i, y, furL);
    		if (rectangle != null){
    			info = rectangle.getRecInfo();
    			break;
    		}
    	}
    	return info;
    }


	//adds a string to the stack
	public void PushToGraphicStack(String str) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {	
				Text t=new Text(str);
				t.setRotate(180);
				graphicStack.add(t);
                logicStack.push(t);
	        }
	    });
        try {
            Thread.sleep(GRAPHIC_STACK_MS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}

	//removes the first string from the stack
	public void PopFromGraphicStack() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if(!logicStack.empty()){
					graphicStack.remove(logicStack.peek());
					logicStack.pop();
				}
	        }
	    });		
        try {
            Thread.sleep(GRAPHIC_STACK_MS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }				
	}
    
    public void showPlanList(ArrayList<Action> plan){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	planList.clear();
            	graphicPlanList.clear();
                for(Action action : plan){
                    Text t=new Text(action.toString());
                    planList.add(t);
                    graphicPlanList.add(t);
                }
                planListStage.show();
            }
        });     
    }

/* ---------------------------- Private Methods ---------------------------- */

    private void initTiming(){
        String textFeild = msMove.getText();
        if(textFeild != ""){
        	long num = MS;
            try {
            	num = Long.parseLong(textFeild);
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                msPerMove = MS;            	
                e.printStackTrace();
                return;
            }
            if(num>GRAPHIC_STACK_MS){
                msPerMove = num - GRAPHIC_STACK_MS;
            }else{
            	msPerMove = GRAPHIC_STACK_MS;
            }
        }else{
            msPerMove = MS;
        }
    }

    private void showGraphicStack(){
    	stackStage.show();
    	stackStage.setAlwaysOnTop(true);
    }
    
    private void hideGraphicStack(){
    	stackStage.hide();
    }
    
	private boolean legalPlace(int x1, int x2, int y1, int y2, boolean isBoard1) {
		List<MyRec> tFur=null;
		if(isBoard1)
			tFur=furL;
		else
			tFur=furR;
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
		if(x2>19||y2>11)
			return false;
		if(x1<=7&&x2<=7)
		{
			if(x1>=2&&x2<=5){}
			else if(y1<=4&&y2<=4){}
			else if(y1>4&&y2>4){}
			else return false;
		}
		else if(x1>7&&x2>7){}
		else if((y1>=1&&y2<=3)||(y1>=6&&y2<=10)){}
		else return false;				
		for(int i=x1;i<=x2;i++)
			for(int j=y1;j<=y2;j++)
				if(getRecInPoint(getScFromY(j, isBoard1),getScFromX(i, isBoard1),tFur, isBoard1)!=null)
					return false;	
		return true;
	}

	private void setRec(MyRec rec, int x1, int x2, int y1, int y2, boolean isBoard1) throws Exception {
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
		rec.setHeight((y2-y1+1)*tileHeight*sizePre);
        rec.setWidth((x2-x1+1)*tileWidth*sizePre);  
        GridPane.setRowIndex(rec, y1);
        GridPane.setColumnIndex(rec, x1);  
        rec.setTranslateY((y2-y1)*rec.getHeight()/((y2-y1+1)*2));
        rec.setTranslateX((x2-x1)*rec.getWidth()/((x2-x1+1)*2));
        if(isBoard1){	
        	lastInCl=cL.pop();
        	rec.setFill(lastInCl);	  	
        }
        else{
        	Paint recPaint=Paint.valueOf("BLACK");
        	double minDis=999999;        	
        	for(Entry<Paint, MyRec> en:recsColor.entrySet()){
        		if(en.getValue().checkEdges(rec)){
        			double thisDis=en.getValue().calDis(rec);
        			if(thisDis<minDis){
        				minDis=thisDis;
        				recPaint=en.getKey();
        			}	
        		}
        	}
        	rec.setFill(recPaint);
        }
	}

	private void getPressed(MouseEvent e, boolean isBoard1) {
		if(e.getButton().equals(MouseButton.PRIMARY)){
			pressed=true;
			preTile[0]=getYfromSc(e.getSceneY(), isBoard1);
			preTile[1]=getXfromSc(e.getSceneX(), isBoard1);	
		}
		else
			if(e.getButton().equals(MouseButton.SECONDARY)){
				double cY=e.getSceneY();
				double cX=e.getSceneX();
				List<MyRec> tFur=null;
				if(isBoard1)
					tFur=furL;
				else
					tFur=furR;
				Rectangle rec=getRecInPoint(cY,cX,tFur, isBoard1);
				if(rec!=null){
					if(isBoard1){
						Paint recColor=rec.getFill();
						cL.push(recColor);
						recsColor.remove(recColor);
						board1.getChildren().remove(rec);					
					}
					else{
						board2.getChildren().remove(rec);
					}
					tFur.remove(rec);	
				}
			}
			else
				if(e.getButton().equals(MouseButton.MIDDLE)){				
					double cY=e.getSceneY();
					double cX=e.getSceneX();
					List<MyRec> tFur=null;
					if(isBoard1)
						tFur=furL;
					else
						tFur=furR;					
					MyRec rec=getRecInPoint(cY,cX,tFur, isBoard1);
					if(rec==null)
						return;					
					Paint currPaint=rec.getFill();
					System.out.println(rec);
					if(isBoard1)
						return;
					Paint recPaint=rec.getFill();		    
		        	boolean foundColor=recPaint.equals(Paint.valueOf("BLACK"));
		        	//finds next legal paint
		        	for(Entry<Paint, MyRec> en:recsColor.entrySet()){
		        		if(en.getValue().checkEdges(rec)){
		        			Paint thisPaint=en.getKey();
		        			if(foundColor){
		        				recPaint=thisPaint;
		        				break;
		        			}
		        			foundColor=thisPaint.equals(currPaint);		        			
		        		}
		        	}
		        	if(recPaint.equals(currPaint)){
		        		//finds first legal paint
		        		for(Entry<Paint, MyRec> en:recsColor.entrySet()){
			        		if(en.getValue().checkEdges(rec)){
			        			Paint thisPaint=en.getKey();
			        			if(!thisPaint.equals(currPaint)){
			        				recPaint=thisPaint;
			        				break;
			        			}		        			
			        		}
			        	}
		        	}
		        	rec.setFill(recPaint);
				}
	}

	private MyRec getRecInPoint(double cY, double cX, List<MyRec> tFur, boolean isBoard1) {
		for(MyRec rec:tFur){
			double x=getRecX(rec,isBoard1);
			double y=getRecY(rec,isBoard1);
			if(cX>=x&&cX<=(x+rec.getWidth())&&cY>=y&&cY<=(y+rec.getHeight()))
				return rec;
		}
		return null;
	}
	
	private MyRec getRectangleByPoint(int xPoint, int yPoint, List<MyRec> boardList){
		for(MyRec rec:boardList){
			int x1 = rec.getX1();
			int x2 = rec.getX2();
			int y1 = rec.getY1();
			int y2 = rec.getY2();
			if (xPoint <= x2 && xPoint >= x1 && yPoint <= y2 && yPoint >= y1){
				return rec;
			}
		}
		return null;
	}
	

	private double getRecY(Rectangle rec, boolean isBoard1) {
		double y=rec.getLayoutY()+rec.getTranslateY();
		if(isBoard1)
			y+=board1.getLayoutY();	
		else
			y+=board2.getLayoutY();
		return y;
	}

	private double getRecX(Rectangle rec, boolean isBoard1) {
		double x=rec.getLayoutX()+rec.getTranslateX();
		if(isBoard1)
			x+=board1.getLayoutX();
		else
			x+=board2.getLayoutX();
		return x;
	}

	private int getXfromSc(double sceneX, boolean isBoard1) {
		if(isBoard1)
			return (int)(Math.ceil(sceneX-board1.getLayoutX())/tileWidth);
		return (int)(Math.ceil(sceneX-board2.getLayoutX())/tileWidth);
	}
	private int getYfromSc(double sceneY, boolean isBoard1) {
		if(isBoard1)
			return (int)(Math.ceil(sceneY-board1.getLayoutY())/tileHeight);
		return (int)(Math.ceil(sceneY-board2.getLayoutY())/tileHeight);
	}
	private double getScFromX(int x, boolean isBoard1) {
		if(isBoard1)
			return x*tileWidth+board1.getLayoutX();
		return  x*tileWidth+board2.getLayoutX();
	}
	private double getScFromY(int y, boolean isBoard1) {
		if(isBoard1)
			return  y*tileHeight+board1.getLayoutY();
		return y*tileHeight+board2.getLayoutY();
	}

	private void printFurnitureLists(){
		System.out.println("FurL list:");
		for(MyRec rec : furL){
			System.out.println(rec.toString());
		}
		System.out.println("FurR list:");
		for(MyRec rec : furR){
			System.out.println(rec.toString());
		}		
	}

    private RecInfo findRectangleMatch(MyRec rectangle) {
    	MyRec destRectangle = null;
		for(int index = 0; index < furR.size(); index++){
			destRectangle = furR.get(index);
			if (rectangle.checkEdges(destRectangle)){
				return new RecInfo(destRectangle.getX1(), destRectangle.getX2(), destRectangle.getY1(), destRectangle.getY2());
			}
		}
		return null;
	}

    
	private MyRec getRectangleById(RecInfo source,List<MyRec> boardList){
		for(MyRec rec : boardList){
			if (rec.equalsById(source)){
				return rec;
			}
		}
		return null;
	}

    private void makeMove(MyRec rectangle){
	      Platform.runLater(new Runnable() {
	          @Override
	          public void run() {
	      		//Make Move code
	            int x1 = rectangle.getX1();
	           	int x2 = rectangle.getX2();
	           	int y1 = rectangle.getY1();
	           	int y2 = rectangle.getY2();
	       		board1.getChildren().remove(rectangle);
	       		try{
	       			setRec(rectangle,x1,x2,y1,y2,true);
	       	    	board1.getChildren().addAll(rectangle);
	       	    	System.out.println("moved");	    	
	       		}
	       		catch(Exception ex){ System.out.println("Something is wrong");}
	               finally{
	               }     
	           	//end of Make Move code
	          }
	        });
	    	try {
				Thread.sleep(msPerMove);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }

	private static void debugPrint(int debugLevel, String debugText){
		if(debugLevel == CURRENT_DEBUG_LEVEL || CURRENT_DEBUG_LEVEL == DEBUG_ALL){
			System.out.println("Debug print: "+DEBUG_TAG);
			System.out.println(debugText);
		}
	}


 } // End of Class GameGraphics  ------------------------------------------ //

 /* ------------------------------- Old Code ------------------------------- */

   /* public void moveRight(RecInfo source){
		MyRec rectangle = getRectangleByPoint(source.getX1(), source.getY1(), furL);
    	rectangle.moveToRight();
    	makeMove(rectangle);
    }
    
    public void moveLeft(RecInfo source){
    	MyRec rectangle = getRectangleByPoint(source.getX1(), source.getY1(), furL);
    	rectangle.moveToLeft();
    	makeMove(rectangle);
    }
    
    public void moveUp(RecInfo source){
    	MyRec rectangle = getRectangleByPoint(source.getX1(), source.getY1(), furL);
    	rectangle.moveUp();
    	makeMove(rectangle);
    }
    
    public void moveDown(RecInfo source){
    	MyRec rectangle = getRectangleByPoint(source.getX1(), source.getY1(), furL);
    	rectangle.moveDown();
    	makeMove(rectangle);
    }
    */