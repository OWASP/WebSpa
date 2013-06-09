package net.seleucus.wsp.db;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class WSDatabaseAdaptorH2 implements WSDatabaseAdaptor {

    private static final String DRIVER = "org.h2.Driver";
    private static Statement wsStatement;

    public WSDatabaseAdaptorH2(String dbPath) throws ClassNotFoundException, SQLException, IOException {

        Class.forName(DRIVER);
        Connection wsConnection = DriverManager.getConnection("jdbc:h2:" + dbPath + ";");

        URL schemaLocation = ClassLoader.getSystemResource("data/web-spa-schema.sql");
        File schemaFile = new File(schemaLocation.getFile());
        if (schemaFile.canRead() == false) {
            throw new IOException("Web-Spa Database Schema Cannot Be Read");
        }

        String schemaText = FileUtils.readFileToString(schemaFile);
        wsStatement = wsConnection.createStatement();
        wsStatement.execute(schemaText);

    }

    @Override
    public void dropAllObjects() throws SQLException {
        wsStatement.execute("DROP ALL OBJECTS");
    }

    @Override
    public void initDB(String dbPath) throws ClassNotFoundException, SQLException, IOException {
        Class.forName(DRIVER);
        Connection wsConnection = DriverManager.getConnection("jdbc:h2:" + dbPath + ";");

        URL schemaLocation = ClassLoader.getSystemResource("data/web-spa-schema.sql");
        File schemaFile = new File(schemaLocation.getFile());
        if (schemaFile.canRead() == false) {
            throw new IOException("Web-Spa Database Schema Cannot Be Read");
        }

        String schemaText = FileUtils.readFileToString(schemaFile);
        wsStatement = wsConnection.createStatement();
        wsStatement.execute(schemaText);

    }

    @Override
    public void loadDB(String dbPath) throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        Connection wsConnection = DriverManager.getConnection("jdbc:h2:" + dbPath + ";");

    }

}
