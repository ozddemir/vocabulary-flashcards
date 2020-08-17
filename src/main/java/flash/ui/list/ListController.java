package flash.ui.list;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ResourceBundle;

import flash.database.DatabaseHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ListController implements Initializable {

	ObservableList<Vocabulary> vocabs = FXCollections.observableArrayList();

	@FXML
	private TableView<Vocabulary> tableView;

	@FXML
	private TableColumn<Vocabulary, String> level;

	@FXML
	private TableColumn<Vocabulary, String> wordCol;

	@FXML
	private TableColumn<Vocabulary, String> definationCol;

	DatabaseHandler databaseHandler;

	public void initialize(URL location, ResourceBundle resources) {
		databaseHandler = DatabaseHandler.getInstance();
		initCol();
		loadData();

	}

	private void initCol() {
		level.setCellValueFactory(new PropertyValueFactory<>("level"));
		wordCol.setCellValueFactory(new PropertyValueFactory<>("word"));
		definationCol.setCellValueFactory(new PropertyValueFactory<>("defination"));
	}

	private void loadData() {

		String query = "SELECT * FROM VOCAB";
		ResultSet rs = databaseHandler.execQuery(query);

		try {
			while (rs.next()) {
				String level = rs.getString("level");
				String word = rs.getString("word");
				String defination = rs.getString("meaning");

				vocabs.add(new Vocabulary(level, word, defination));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		tableView.getItems().setAll(vocabs);
	}

	public static class Vocabulary {
		private SimpleStringProperty level;
		private SimpleStringProperty word;
		private SimpleStringProperty defination;

		public Vocabulary(String level, String word, String defination) {
			this.level = new SimpleStringProperty(level);
			this.word = new SimpleStringProperty(word);
			this.defination = new SimpleStringProperty(defination);
		}

		public String getLevel() {
			return level.get();
		}

		public String getWord() {
			return word.get();
		}

		public String getDefination() {
			return defination.get();
		}

	}

}
