package net.seleucus.wsp.db;

import org.junit.Test;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertTrue;

public class WSDatabaseAdaptorTest {

    @Test
    public final void shouldCreateADatabaseIfAFileDoesNotExist01() throws Exception {

        DateFormat dateFormat = new SimpleDateFormat("-yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();

        final String dbName = "src/test/resources/data/01" + dateFormat.format(date);
        final WSDatabaseAdaptorYiannis myDB = new WSDatabaseAdaptorYiannis(dbName);

        File dbDataFile = new File(dbName + ".properties");
        final boolean fileExists = dbDataFile.exists();

        myDB.deleteAllDatabaseFiles();

        assertTrue(fileExists);

    }

}
