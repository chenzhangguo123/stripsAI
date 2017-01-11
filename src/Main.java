import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application
{
	public static void main(String[] args )
	{
		Application.launch(args);	
	}
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		Pane mainPane = (Pane) FXMLLoader.load(Main.class.getResource("gameGraphics.fxml"));
		primaryStage.setScene(new Scene(mainPane));
		primaryStage.setTitle("Furniture moving simulator 2017");
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
		});
	}
}
