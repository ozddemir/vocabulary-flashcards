package flash.ui.card.level;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CardLoad extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
	//	Parent root = FXMLLoader.load(getClass().getResource("card.fxml"));
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("card.fxml"));
		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("card.css").toExternalForm());
		scene.getStylesheets().add(getClass().getClassLoader().getResource("css/card.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
