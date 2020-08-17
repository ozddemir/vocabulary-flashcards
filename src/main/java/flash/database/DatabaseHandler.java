package flash.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public final class DatabaseHandler {

	private static DatabaseHandler instance = null;

	private static final String DB_URL = "jdbc:derby:database;create=true";
	private static Connection conn = null;
	private static Statement stmt = null;

	private DatabaseHandler() {
		createConnection();
		setupVocabTable();

	}

	public static DatabaseHandler getInstance() {
		if (instance == null) {
			instance = new DatabaseHandler();
		}
		return instance;
	}

	@SuppressWarnings("deprecation")
	public void createConnection() {

		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			conn = DriverManager.getConnection(DB_URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ResultSet execQuery(String query) {
		ResultSet result;
		try {
			stmt = conn.createStatement();
			result = stmt.executeQuery(query);
		} catch (SQLException ex) {
			System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
			return null;
		} finally {
		}
		return result;
	}

	public boolean execAction(String qu) {
		try {
			stmt = conn.createStatement();
			stmt.execute(qu);
			return true;
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
			System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
			return false;
		} finally {
		}
	}

	void setupVocabTable() {
		String TABLE_NAME = "VOCAB";
		try {
			stmt = conn.createStatement();

			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

			if (tables.next()) {
				System.out.println("Table " + TABLE_NAME + " already exists. Ready for go!");
			} else {
				stmt.execute("CREATE TABLE " + TABLE_NAME + "("
						+ "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n"
						+ "word varchar(200),\n" 
						+ "meaning varchar(200),\n" 
						+ "level varchar(200),\n"
						+ "expo varchar(200),\n"
						+ "isLearned boolean default false,\n"
						+ "CONSTRAINT primary_key PRIMARY KEY (id)"
						+ " )");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage() + " --- setupDatabase");
		} finally {
		}
	}
}
