package net.seleucus.wsp.db;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.CharBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;

public class WSDatabase {

	private static final String DB_PATH = "web-spa-db";

	private Connection wsConnection;

	public WSDatabase() throws ClassNotFoundException, SQLException,
			IOException {

		// Check if the database properties file is present, if not create it
		// from the bundled template...
		File propsFile = new File(DB_PATH + ".properties");
		if (!propsFile.exists()) {
			URL bundledPropsLocation = ClassLoader
					.getSystemResource("data/bundled-web-spa-db.properties");
			FileUtils.copyURLToFile(bundledPropsLocation, propsFile);
		}

		// Check if the database script file is present, if not create it from
		// the bundled template...
		File scriptFile = new File(DB_PATH + ".script");
		if (!scriptFile.exists()) {
			URL bundledScriptlocation = ClassLoader
					.getSystemResource("data/bundled-web-spa-db.script");
			FileUtils.copyURLToFile(bundledScriptlocation, scriptFile);
		}

		Class.forName("org.hsqldb.jdbcDriver");
		wsConnection = DriverManager.getConnection("jdbc:hsqldb:" + DB_PATH);

	}

	public synchronized void shutdown() throws SQLException {

		Statement st = wsConnection.createStatement();
		// db writes out to files and performs clean shuts down
		// otherwise there will be an unclean shutdown
		// when program ends
		st.execute("SHUTDOWN");
		wsConnection.close();
	}

	protected synchronized void initialise() {

	}

	// use for SQL command SELECT
	public synchronized void query(String expression) throws SQLException {

		Statement st = null;
		ResultSet rs = null;

		st = wsConnection.createStatement(); // statement objects can be reused
												// with

		// repeated calls to execute but we
		// choose to make a new one each time
		rs = st.executeQuery(expression); // run the query

		// do something with the result set.
		// dump(rs);
		st.close(); // NOTE!! if you close a statement the associated ResultSet
					// is

		// closed too
		// so you should copy the contents to some other object.
		// the result set is invalidated also if you recycle an Statement
		// and try to execute some other query before the result set has been
		// completely examined.
	}

	// use for SQL commands CREATE, DROP, INSERT and UPDATE
	private synchronized void update(String expression) throws SQLException {

		Statement st = null;

		st = wsConnection.createStatement(); // statements

		int i = st.executeUpdate(expression); // run the query

		if (i == -1) {
			System.out.println("db error : " + expression);
		}

		st.close();
	} // void update()

	protected synchronized void deleteAllDatabaseFiles() throws SQLException {

		this.shutdown();

		String[] extensions = { ".properties", ".script", ".log", ".data",
				".backup" };
		for (String extension : extensions) {
			File dbFile = new File(DB_PATH + extension);
			if (dbFile.exists()) {
				dbFile.delete();
			}
		}

	}

	public synchronized void addUser(String fullName, CharSequence passSeq, String eMail,
			String phone) {
		
		String sqlPassPhrase = "INSERT INTO PASSPHRASES (PASSPHRASE, CREATED) VALUES (?, CURRENT_TIMESTAMP);";
		
		String sqlUsers = "INSERT INTO PUBLIC.USERS (PPID, FULLNAME, EMAIL, PHONE, CREATED, MODIFIED) VALUES " +
				"(SELECT PPID FROM PUBLIC.PASSPHRASES WHERE PASSPHRASE = ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);";
		
        try {
			
        	PreparedStatement psPassPhrase = wsConnection.prepareStatement(sqlPassPhrase);
        	psPassPhrase.setString(1, passSeq.toString());
        	psPassPhrase.executeUpdate();
        	
        	psPassPhrase.close();
        	
        	PreparedStatement psUsers = wsConnection.prepareStatement(sqlUsers);
        	psUsers.setString(1, passSeq.toString());
        	psUsers.setString(2, fullName);
        	psUsers.setString(3, eMail);
        	psUsers.setString(4, phone);
        	psUsers.executeUpdate();
        	
        	psUsers.close();
        	
		} catch (SQLException ex) {
			
			 throw new RuntimeException(ex);
			 
		}

	}

	public synchronized String showUsers() {
		
		StringBuffer resultsBuffer = new StringBuffer();
		resultsBuffer.append("ID\tActive\tFull Name\tModified\n");
		
		String sqlPassUsers = "SELECT PPID, ACTIVE, FULLNAME, MODIFIED FROM PASSPHRASES JOIN USERS ON PASSPHRASES.PPID = USERS.PPID;";
		try {
			Statement stmt = wsConnection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlPassUsers);

			while (rs.next()) {
				resultsBuffer.append(rs.getString(1));
				resultsBuffer.append('\t');
				resultsBuffer.append(rs.getString(2));
				resultsBuffer.append('\t');
				resultsBuffer.append(rs.getString(3));
				resultsBuffer.append('\t');
				resultsBuffer.append(rs.getString(4));
				resultsBuffer.append('\n');
			}

			rs.close();
			stmt.close();

		} catch (SQLException ex) {
			
			 throw new RuntimeException(ex);

		}
		
		return resultsBuffer.toString();
	}
	
	public synchronized boolean isPasswordInUse(CharSequence passSeq) {
		
		boolean passExists = false;
		
		String sqlPassPhrases = "SELECT PASSPHRASE FROM PASSPHRASES;";
		try {
			Statement stmt = wsConnection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlPassPhrases);

			while (rs.next()) {
				char[] dbPassPhraseArray = rs.getString(1).toCharArray();
				CharSequence dbPassSeq = CharBuffer.wrap(dbPassPhraseArray);
				
				if(dbPassSeq.equals(passSeq)) {
					passExists = true;
					break;
				}
			}

			rs.close();
			stmt.close();

		} catch (SQLException ex) {
			
			throw new RuntimeException(ex);
			
		}
		
		return passExists;
		
	}

}
