package net.seleucus.wsp.db;

import java.sql.SQLException;


public class WSDatabaseManager extends WSDatabaseAdaptor {

	public WSDatabaseManager(String dbPath) throws ClassNotFoundException, SQLException {
		super(dbPath);
	}

}
