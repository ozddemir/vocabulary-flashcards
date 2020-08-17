package flash.ui.addcard;

import java.net.URL;

import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import flash.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AddCardController implements Initializable {

	@FXML
	private JFXTextField firstWord;

	@FXML
	private JFXTextField secondWord;

	@FXML
	private JFXTextArea expoWord;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private JFXComboBox<String> levelBox;

	DatabaseHandler databaseHandler;

	public void initialize(URL arg0, ResourceBundle arg1) {
		databaseHandler = DatabaseHandler.getInstance();
		levelBox.getItems().addAll("Level A1", "Level A2", "Level B1", "Level B2");
	}

	@FXML
	void cancelCard(ActionEvent event) {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();
	}

	/* add new flash card */
	@FXML
	void saveCard(ActionEvent event) {
		String word = firstWord.getText();
		String meaning = secondWord.getText();
		String expo = expoWord.getText();
		String level = levelBox.getValue();

		if (word.isEmpty() || meaning.isEmpty() || level==null || expo.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setHeaderText("Please fill all rows");
			alert.showAndWait();
			clearRows();
			return;
		}

		String query = "INSERT INTO VOCAB(word" + "," + "meaning" + "," + "level" + "," + "expo) VALUES (" + "'" + word
				+ "'," + "'" + meaning + "'," + "'" + level + "'," + "'" + expo + "'" + ")";

		if (databaseHandler.execAction(query)) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText(null);
			alert.setContentText("The data is stored.");
			alert.showAndWait();
			clearRows();
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("An issue has been occured.");
			alert.showAndWait();
			clearRows();
		}
	}

	/* Reset the rows for the next addition */
	private void clearRows() {
		firstWord.setText("");
		secondWord.setText("");
		expoWord.setText("");
	}
}
