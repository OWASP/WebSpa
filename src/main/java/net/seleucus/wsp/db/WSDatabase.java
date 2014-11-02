package net.seleucus.wsp.db;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WSDatabase {

	private final static Logger LOGGER = LoggerFactory.getLogger(WSDatabase.class);    

	protected static final String DB_PATH = "webspa-db";

	private Connection wsConnection;

	public WSActionsAvailable actionsAvailable;
	public WSActionsReceived actionsReceived;
	public WSPassPhrases passPhrases;
	public WSUsers users;

	public WSDatabase() throws ClassNotFoundException, SQLException,
			IOException {

		// Check if the database properties file is present, if not create it
		// from the bundled template...
		File propsFile = new File(DB_PATH + ".properties");
		if (!propsFile.exists()) {
			
			LOGGER.debug("Database properties file {} not found.", propsFile.getName());
			
			URL bundledPropsLocation = ClassLoader
					.getSystemResource("data/bundled-webspa-db.properties");
			
			LOGGER.debug("Copying default database properties file from bundle");
			
			FileUtils.copyURLToFile(bundledPropsLocation, propsFile);
			
		} else {
			
			LOGGER.debug("Database properties file {} found in current directory.", propsFile.getName());
			
		}

		// Check if the database script file is present, if not create it from
		// the bundled template...
		File scriptFile = new File(DB_PATH + ".script");
		if (!scriptFile.exists()) {
			
			LOGGER.debug("Database script file {} not found.", scriptFile.getName());
			
			URL bundledScriptlocation = ClassLoader
					.getSystemResource("data/bundled-webspa-db.script");
			
			LOGGER.debug("Copying default database script file from bundle");
			
			FileUtils.copyURLToFile(bundledScriptlocation, scriptFile);
		
		} else {
			
			LOGGER.debug("Database script file {} found in current directory.", scriptFile.getName());
			
		}

		LOGGER.debug("Establishing the database connection to: {}", DB_PATH);
		
		Class.forName("org.hsqldb.jdbcDriver");
		wsConnection = DriverManager.getConnection("jdbc:hsqldb:" + DB_PATH);
		
		actionsAvailable = new WSActionsAvailable(wsConnection);
		actionsReceived = new WSActionsReceived(wsConnection);
		passPhrases = new WSPassPhrases(wsConnection);
		users = new WSUsers(wsConnection);

	}

	public synchronized void shutdown() {

		Statement st;
		
		try {

			st = wsConnection.createStatement();
			LOGGER.info("Beginning database shutdown..."); 
			st.execute("SHUTDOWN");
			wsConnection.close();
			LOGGER.info("Database shutdown complete.");
			
		} catch (SQLException ex) {
			
			 LOGGER.error("A Database exception has occured: {}.", ex.getMessage());

		}

	}

}
