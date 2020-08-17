package flash.ui.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXToggleButton;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController implements Initializable {

	@FXML
	private JFXToggleButton isListRandom; // Give option to make lists random or not

	public static boolean isRandom; // static is used for common memory reach -> Passing data for make the list
									// random or not
	public static String title; // Differentiate levels by title variable

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		isListRandom.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				if (isListRandom.isSelected()) {
					isListRandom.setText("ON");
					isRandom = true;
				} else {
					isListRandom.setText("OFF");
					isRandom = false;
				}
			}
		});
	}

	@FXML
	void listVocab(ActionEvent event) {
		loadWindow("list.fxml", " List Vocabulary", "css/list.css");
		// second data is for title
	}

	@FXML
	void addCardAction(ActionEvent event) {
		loadWindow("AddCard.fxml", " Add New.. ", "css/AddCard.css");
	}

	/* Four levels will be displayed down below */

	@FXML
	void level1(ActionEvent event) {
		title = "Level 1"; // I am doing this because I don't like to create different package for almost
							// exact levels. Only change is one line to get level from database

		loadWindow("card.fxml", title, "css/card.css");
	}

	@FXML
	void level2(ActionEvent event) {
		title = "Level 2";
		loadWindow("card.fxml", title, "css/card.css");
	}

	@FXML
	void level3(ActionEvent event) {
		title = "Level 3";
		loadWindow("card.fxml", title, "css/card.css");
	}

	@FXML
	void level4(ActionEvent event) {
		title = "Level 4";
		loadWindow("card.fxml", title, "css/card.css");
	}

	@FXML
	void learnVocab(ActionEvent event) {
		title = "Learn";
		loadWindow("cardLearn.fxml", title, "css/card-learn.css");
	}

	@FXML
	void learnedVocab(ActionEvent event) {
		title = "Learned";
		loadWindow("cardLearn.fxml", title, "css/card-learn.css");
	}

	private void loadWindow(String direction, String theTitle, String styleSheet) {
		try {
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(direction));
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getClassLoader().getResource(styleSheet).toExternalForm());
			stage.setScene(scene);

			stage.setTitle(theTitle);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
