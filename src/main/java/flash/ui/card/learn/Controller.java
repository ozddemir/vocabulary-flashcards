package flash.ui.card.learn;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXToggleNode;

import flash.database.DatabaseHandler;
import flash.model.VocabModel;
import flash.ui.main.MainController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

public class Controller implements Initializable {

	@FXML
	private Text frontFace;

	@FXML
	private JFXCheckBox isLearned;

	@FXML
	private JFXToggleNode flipCard;

	private final List<VocabModel> vocabsList = new LinkedList<>();
	private ListIterator<VocabModel> vocabIterator;
	private VocabModel iterate;

	DatabaseHandler databaseHandler;
	private boolean forward = true; // Play list Problem..
	private boolean isFlipped = true; // ChangeListener oldValue and newValue could not
										// prevent the double click

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		databaseHandler = DatabaseHandler.getInstance();
		getData();
		/*avoiding from nul exception*/
		isLearned.setVisible(false);
		flipCard.setVisible(false);
		
		Collections.shuffle(vocabsList);
		vocabIterator = vocabsList.listIterator();

		/* That green check box */
		isLearned.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (isLearned.isSelected()) {
					String word = frontFace.getText();
					String query = "UPDATE VOCAB SET isLearned = true WHERE word = '" + word + "'";
					databaseHandler.execAction(query);
				} else {
					String word = frontFace.getText();
					String query = "UPDATE VOCAB SET isLearned = false WHERE word = '" + word + "'";
					databaseHandler.execAction(query);
				}
			}
		});

		/* Back Face */
		flipCard.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (isFlipped) {

					frontFace.setText(iterate.getMeaning());
					isFlipped = false;
				} else if (!isFlipped) {
					frontFace.setText(iterate.getWord());
					isFlipped = true;
				}
			}
		});
	}

	@FXML
	void nextItem(ActionEvent event) {
		
		if(!isLearned.isVisible() || !flipCard.isVisible()) {
			isLearned.setVisible(true);
			flipCard.setVisible(true);
		}
		
		if (!forward) {
			if (vocabIterator.hasNext()) {
				iterate = vocabIterator.next();
				forward = true;
			}
		}

		if (vocabIterator.hasNext()) {
			iterate = vocabIterator.next();
			frontFace.setText(iterate.getWord());
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
			isFlipped = true;
			/* End of the green learned checkBox code */
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("End of the row");
			alert.show();

		}
	}

	@FXML
	void previousItem(ActionEvent event) {

		if (forward) {
			if (vocabIterator.hasPrevious()) {
				iterate = vocabIterator.previous();
				forward = false;
			}
		}
		if (vocabIterator.hasPrevious()) {

			iterate = vocabIterator.previous();
			frontFace.setText(iterate.getWord());
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
			isFlipped = true;

		} else {
			Alert alert = new Alert(AlertType.INFORMATION);

			alert.setContentText(null);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("End of the row!");
			alert.show();
		}
	}

	private void getData() {
		String title = MainController.title;
		boolean isLearned;
		if (title.equals("Learn")) {
			isLearned = false;
		} else {
			isLearned = true;
		}
		String query = "SELECT * FROM VOCAB WHERE isLearned = " + isLearned + "";

		try (ResultSet rs = databaseHandler.execQuery(query)) {
			while (rs.next()) {
				int vocabID = rs.getInt("id");
				String word = rs.getString("word");
				String meaning = rs.getString("meaning");

				VocabModel vocab = new VocabModel(vocabID, word, meaning);
				vocabsList.add(vocab);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
