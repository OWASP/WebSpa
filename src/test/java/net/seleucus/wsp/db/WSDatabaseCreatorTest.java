package net.seleucus.wsp.db;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeThat;

public class WSDatabaseCreatorTest {

    private static final String DB_PATH = "data/web-spa-server-database.db";

    @Ignore
    @Test
    public final void shouldBeAbleToInstantiateAWSDBLoaderIfTheFileExists() throws Exception {
        WSDatabaseAdaptorH2 myLoader = new WSDatabaseAdaptorH2(DB_PATH);
        myLoader.dropAllObjects();
    }

    @Ignore
    @Test(expected = FileNotFoundException.class)
    public final void shouldThrowAnExceptionIfTheFileDoesNotExist() throws Exception {
        WSDatabaseAdaptorH2 myLoader = new WSDatabaseAdaptorH2("data/doesnotexist.db");
        myLoader.dropAllObjects();
    }

    @Ignore
    @Test(expected = IOException.class)
    public final void shouldThrowAnExceptionIfTheFileCannotBeRead() throws Exception {
        assumeThat(System.getProperty("os.name").toLowerCase(), not(containsString("win")));

        String pathToUnreadable = "data/unreadable.db";
        URL rootLocation = ClassLoader.getSystemResource(pathToUnreadable);
        File unreadable = new File(rootLocation.getFile());
        unreadable.setReadable(false);

        WSDatabaseAdaptorH2 myLoader = new WSDatabaseAdaptorH2("data/unreadable.db");
        myLoader.dropAllObjects();
    }

    @Ignore
    @Test
    public final void shouldLoadAValidDBIfTheFileExists() throws Exception {
        WSDatabaseAdaptorH2 myLoader = new WSDatabaseAdaptorH2(DB_PATH);
        String dbStatus = null; // myLoader.getStatus();
        assertEquals("Table-1", dbStatus);
        myLoader.dropAllObjects();
    }

}
