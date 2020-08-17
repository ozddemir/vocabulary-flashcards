package flash.ui.card.level;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;

import flash.database.DatabaseHandler;
import flash.model.VocabModel;
import flash.ui.main.MainController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class CardController implements Initializable {

	@FXML 
	private JFXCheckBox isLearned;
	@FXML
	private  JFXButton deleteItem;

	@FXML
	private AnchorPane rootPane; //bu bir iþi yaramýyor sil kontrolden sonra

	@FXML
	private Text wordText; // The word that user would like to learn.

	@FXML
	private Text meaningText; // The meaning of the text in user native language.

	@FXML
	private Text expoText; // Explanation or a sample sentence of the world.

	private final List<VocabModel> vocabsList = new LinkedList<>();
	private ListIterator<VocabModel> vocabIterator;

	private VocabModel iterate;
	private boolean forward = true; // iterating list and next and previous methods need flag to iterate properly.

	DatabaseHandler databaseHandler; // Calling singleton database class

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		databaseHandler = DatabaseHandler.getInstance();
		getData();
		
		/*avoiding from null exceptions*/
		isLearned.setVisible(false); 
		deleteItem.setVisible(false);

		if (MainController.isRandom) { // shuffling the list
			Collections.shuffle(vocabsList);
		}
		vocabIterator = vocabsList.listIterator();

		/* check box isLearned */
		isLearned.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (isLearned.isSelected()) {
					String word = wordText.getText();
					String query = "UPDATE VOCAB SET isLearned = true WHERE word = '" + word + "'";
					databaseHandler.execAction(query);
				} else {
					String word = wordText.getText();
					String query = "UPDATE VOCAB SET isLearned = false WHERE word = '" + word + "'";
					databaseHandler.execAction(query);
				}
			}
		});
	}

	@FXML
	private void nextItem(ActionEvent event) {

		/* set check box visible  */
		if(!isLearned.isVisible() || !deleteItem.isVisible()) {
			isLearned.setVisible(true);
			deleteItem.setVisible(true);
		}
		
		if (!forward) {
			if (vocabIterator.hasNext()) {
				iterate = vocabIterator.next();
				forward = true;
			}
		}

		if (vocabIterator.hasNext()) {
			iterate = vocabIterator.next();
			wordText.setText(iterate.getWord());
			meaningText.setText(iterate.getMeaning());
			expoText.setText(iterate.getExpo());
			int id = iterate.getVocabID();

			/* Attempting Better Solution */
			String query = "SELECT isLearned FROM VOCAB WHERE id = " + id + ""; // No single quotes=> id is an integer

			try (ResultSet rs = databaseHandler.execQuery(query)) {
				while (rs.next()) {
					boolean isLearnedValue = rs.getBoolean("isLearned");
					isLearned.setSelected(isLearnedValue);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			/* Dumb code but somehow it is working */

			/* Shit code */
			/* isLearned.setSelected(iterate.getIsLearned()); */
			// This is not a good way to display the check state. The reason is data comes
			// from an out-dated list which is already filled before code reach here. The
			// application needs a real-time set up to display
			// up-to-date state of the check box
			/* End of the shit code */

		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("End of the row");
			alert.show();

		}
	}

	@FXML
	private void previousItem(ActionEvent event) {

		if (forward) {
			if (vocabIterator.hasPrevious()) {
				iterate = vocabIterator.previous();
				forward = false;
			}
		}
		if (vocabIterator.hasPrevious()) {

			iterate = vocabIterator.previous();
			wordText.setText(iterate.getWord());
			meaningText.setText(iterate.getMeaning());
			expoText.setText(iterate.getExpo());
			int id = iterate.getVocabID();

			/* Attempting Better Solution */
			String query = "SELECT isLearned FROM VOCAB WHERE id = " + id + ""; // No single quotes=> id is an integer

			try (ResultSet rs = databaseHandler.execQuery(query)) {
				while (rs.next()) {
					boolean isLearnedValue = rs.getBoolean("isLearned");
					isLearned.setSelected(isLearnedValue);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			/* Dumb code but somehow it is working */

			/* Shit code */
			/* isLearned.setSelected(iterate.getIsLearned()); */
			// This is not a good way to display the check state. The reason is data comes
			// from an out-dated list which is already filled before code reach here. The
			// application needs a real-time set up to display
			// up-to-date state of the check box
			/* End of the shit code */
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);

			alert.setContentText(null);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("End of the row!");
			alert.show();
		}
	}

	@FXML
	private void deleteItem(ActionEvent event) {

		String word = wordText.getText();
		String actionQuery = "DELETE FROM VOCAB WHERE word = '" + word + "'";

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirm Delete Operation");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure want to delete the item ");
		Optional<ButtonType> response = alert.showAndWait();

		if (response.get() == ButtonType.OK) {
			if (databaseHandler.execAction(actionQuery)) {

				vocabIterator.remove();

				wordText.setText("This card has been deleted");
				meaningText.setText("");
				expoText.setText("");

			} else {
				Alert alert1 = new Alert(AlertType.ERROR);
				alert1.setContentText(null);
				alert1.setTitle(null);
				alert1.setHeaderText(null);
				alert1.setContentText("Delete operation is canceled");
			}
		} else {
			Alert alert1 = new Alert(Alert.AlertType.ERROR);
			alert1.setTitle("Cancelled");
			alert1.setHeaderText(null);
			alert1.setContentText("Delete operation is cancelled");
			alert1.show();
		}
	}

	/* Getting data from database and put it into map */
	private void getData() {

		/* Get the level */
		String level;
		switch (MainController.title) {
		case "Level 1":
			level = "Level A1";
			break;
		case "Level 2":
			level = "Level A2";
			break;
		case "Level 3":
			level = "Level B1";
			break;
		case "Level 4":
			level = "Level B2";
			break;
		default:
			level = "Level A1";
			break;
		}

		String query = "SELECT * FROM VOCAB WHERE level = '" + level + "'";

		try (ResultSet rs = databaseHandler.execQuery(query)) {
			while (rs.next()) {
				int vocabID = rs.getInt("id");
				String word = rs.getString("word");
				String meaning = rs.getString("meaning");
				String expo = rs.getString("expo");

				VocabModel vocab = new VocabModel(vocabID, word, meaning, expo);
				vocabsList.add(vocab);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
