package net.seleucus.wsp.db;

import java.io.IOException;
import java.sql.SQLException;

public interface WSDatabaseAdaptor {
    public void dropAllObjects() throws SQLException;

    public void initDB(String dbPath) throws ClassNotFoundException, SQLException, IOException;

    public void loadDB(String dbPath) throws ClassNotFoundException, SQLException;
}
