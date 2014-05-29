package org.opengis.cite.iso19136;

import java.util.logging.Level;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.geotoolkit.factory.Hints;
import org.opengis.cite.iso19136.util.TestSuiteLogger;
import org.testng.IExecutionListener;

/**
 * A listener that is invoked before and after a test run. It is often used to
 * configure a shared fixture that exists for the duration of the entire test
 * run.
 * 
 * <p>
 * A shared fixture should be used with caution in order to avoid undesirable
 * test interactions. In general, it should be populated with "read-only"
 * objects that are not modified during the test run.
 * </p>
 * 
 * @see com.occamlab.te.spi.executors.FixtureManager FixtureManager
 * 
 */
public class TestRunListener implements IExecutionListener {

    /**
     * Notifies the listener that a test run is about to start; it looks for a
     * JNDI DataSource named "jdbc/EPSG" that provides access to a database
     * containing the official EPSG geodetic parameters. If one is found, it is
     * set as a {@link org.geotoolkit.factory.Hints#EPSG_DATA_SOURCE hint} when
     * initializing the EPSG factory. An embedded database will be created if
     * necessary.
     */
    @Override
    public void onExecutionStart() {
        DataSource dataSource = null;
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/EPSG");
        } catch (NamingException nx) {
            TestSuiteLogger
                    .log(Level.CONFIG,
                            "JNDI DataSource 'jdbc/EPSG' not found. An embedded database will be created if necessary.");
        }
        if (null != dataSource) {
            Hints.putSystemDefault(Hints.EPSG_DATA_SOURCE, dataSource);
        }
    }

    @Override
    public void onExecutionFinish() {
    }
}
