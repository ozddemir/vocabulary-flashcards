package flash.ui.list;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("list.fxml"));
		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("list.css").toExternalForm());
		scene.getStylesheets().add(getClass().getClassLoader().getResource("css/list.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
