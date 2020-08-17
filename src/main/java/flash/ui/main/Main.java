package flash.ui.main;

import java.io.IOException;

import flash.database.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Main.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getClassLoader().getResource("css/main.css").toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("FLASH CARD");
		primaryStage.show();
	
		new Thread(() -> DatabaseHandler.getInstance()).start();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
		
	}
}

