package com.taosdata.jdbc;

import com.taosdata.jdbc.utils.SpecifyAddress;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;
import java.util.Properties;

import static org.junit.Assert.*;

public class TSDBDriverTest {

    private static String[] validURLs;
    private Connection conn;

    @Test
    public void connectWithJdbcURL() throws SQLException {
        String url = SpecifyAddress.getInstance().getJniWithoutUrl();
        if (url == null) {
            url = "jdbc:TAOS://localhost:6030/?user=root&password=taosdata";
        } else {
            url += "?user=root&password=taosdata";
        }
        conn = DriverManager.getConnection(url);
        assertNotNull("failure - connection should not be null", conn);
    }

    @Test
    public void connectWithProperties() throws SQLException {
        String url = SpecifyAddress.getInstance().getJniWithoutUrl();
        if (url == null) {
            url = "jdbc:TAOS://localhost:6030/?user=root&password=taosdata";
        } else {
            url += "?user=root&password=taosdata";
        }
        Properties connProps = new Properties();
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");
        conn = DriverManager.getConnection(url, connProps);
        assertNotNull("failure - connection should not be null", conn);
    }

    @Test
    public void connectWithConfigFile() throws SQLException {
        String url = SpecifyAddress.getInstance().getJniWithoutUrl();
        if (url == null) {
            url = "jdbc:TAOS://:/?user=root&password=taosdata";
        } else {
            url += "?user=root&password=taosdata";
        }
        Properties connProps = new Properties();
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");
        conn = DriverManager.getConnection(url, connProps);
        assertNotNull("failure - connection should not be null", conn);
    }

    @Test
    public void testParseURL() {
        TSDBDriver driver = new TSDBDriver();

        String url = SpecifyAddress.getInstance().getJniWithoutUrl();
        if (url == null) {
            url = "jdbc:TAOS://127.0.0.1:0/db?user=root&password=taosdata&charset=UTF-8";
        } else {
            url += "db?user=root&password=taosdata&charset=UTF-8";
        }
        String host = SpecifyAddress.getInstance().getHost();
        if (host == null) {
            host = "127.0.0.1";
        }
        String port = SpecifyAddress.getInstance().getJniPort();
        if (port == null) {
            port = "0";
        }
        Properties config = new Properties();
        Properties actual = driver.parseURL(url, config);
        assertEquals("failure - host should be " + host, host, actual.get("host"));
        assertEquals("failure - port should be " + port, port, actual.get("port"));
        assertEquals("failure - dbname should be db", "db", actual.get("dbname"));
        assertEquals("failure - user should be root", "root", actual.get("user"));
        assertEquals("failure - password should be taosdata", "taosdata", actual.get("password"));
        assertEquals("failure - charset should be UTF-8", "UTF-8", actual.get("charset"));

        url = SpecifyAddress.getInstance().getJniWithoutUrl();
        if (url == null) {
            url = "jdbc:TAOS://127.0.0.1:0";
        }
        host = SpecifyAddress.getInstance().getHost();
        if (host == null) {
            host = "127.0.0.1";
        }
        port = SpecifyAddress.getInstance().getJniPort();
        if (port == null) {
            port = "0";
        }
        config = new Properties();
        actual = driver.parseURL(url, config);
        assertEquals("failure - host should be " + host, host, actual.getProperty("host"));
        assertEquals("failure - port should be " + port, port, actual.get("port"));
        assertNull("failure - dbname should be null", actual.get("dbname"));

        url = SpecifyAddress.getInstance().getJniWithoutUrl();
        if (url == null) {
            url = "jdbc:TAOS://127.0.0.1:0/db";
        } else {
            url += "db";
        }
        host = SpecifyAddress.getInstance().getHost();
        if (host == null) {
            host = "127.0.0.1";
        }
        port = SpecifyAddress.getInstance().getJniPort();
        if (port == null) {
            port = "0";
        }
        config = new Properties();
        actual = driver.parseURL(url, config);
        assertEquals("failure - host should be " + host, host, actual.getProperty("host"));
        assertEquals("failure - port should be " + port, port, actual.get("port"));
        assertEquals("failure - dbname should be db", "db", actual.get("dbname"));

        url = "jdbc:TAOS://:/?";
        config = new Properties();
        config.setProperty(TSDBDriver.PROPERTY_KEY_USER, "root");
        config.setProperty(TSDBDriver.PROPERTY_KEY_PASSWORD, "taosdata");
        actual = driver.parseURL(url, config);
        assertEquals("failure - user should be root", "root", actual.getProperty("user"));
        assertEquals("failure - password should be taosdata", "taosdata", actual.getProperty("password"));
        assertNull("failure - host should be null", actual.getProperty("host"));
        assertNull("failure - port should be null", actual.getProperty("port"));
        assertNull("failure - dbname should be null", actual.getProperty("dbname"));
    }


    @Test(expected = SQLException.class)
    public void acceptsURL() throws SQLException {
        Driver driver = new TSDBDriver();
        for (String url : validURLs) {
            assertTrue("failure - acceptsURL(\" " + url + " \") should be true", driver.acceptsURL(url));
        }
        driver.acceptsURL(null);
        fail("acceptsURL throws exception when parameter is null");
    }

    @Test
    public void getPropertyInfo() throws SQLException {
        Driver driver = new TSDBDriver();
        String url = SpecifyAddress.getInstance().getJniWithoutUrl();
        if (url == null) {
            url = "jdbc:TAOS://localhost:6030/information_schema?user=root&password=taosdata";
        } else {
            url += "information_schema?user=root&password=taosdata";
        }
        String host = SpecifyAddress.getInstance().getHost();
        if (host == null) {
            host = "localhost";
        }
        String port = SpecifyAddress.getInstance().getJniPort();
        if (port == null) {
            port = "6030";
        }
        Properties connProps = new Properties();
        DriverPropertyInfo[] propertyInfo = driver.getPropertyInfo(url, connProps);
        for (DriverPropertyInfo info : propertyInfo) {
            if (info.name.equals(TSDBDriver.PROPERTY_KEY_HOST))
                assertEquals("failure - host should be " + host, host, info.value);
            if (info.name.equals(TSDBDriver.PROPERTY_KEY_PORT))
                assertEquals("failure - port should be " + port, port, info.value);
            if (info.name.equals(TSDBDriver.PROPERTY_KEY_DBNAME))
                assertEquals("failure - dbname should be information_schema", "information_schema", info.value);
            if (info.name.equals(TSDBDriver.PROPERTY_KEY_USER))
                assertEquals("failure - user should be root", "root", info.value);
            if (info.name.equals(TSDBDriver.PROPERTY_KEY_PASSWORD))
                assertEquals("failure - password should be root", "taosdata", info.value);
        }
    }

    @Test
    public void getMajorVersion() {
        assertEquals(3, new TSDBDriver().getMajorVersion());
    }

    @Test
    public void getMinorVersion() {
        assertEquals(0, new TSDBDriver().getMinorVersion());
    }

    @Test
    public void jdbcCompliant() {
        assertFalse(new TSDBDriver().jdbcCompliant());
    }

    @Test
    public void getParentLogger() {
        assertNull(new TSDBDriver().getParentLogger());
    }

    @BeforeClass
    public static void beforeClass() {
        String specifyHost = SpecifyAddress.getInstance().getHost();
        if (specifyHost == null) {
            specifyHost = "localhost";
        }
        String port = SpecifyAddress.getInstance().getJniPort();
        if (port == null) {
            port = "6030";
        }
        validURLs = new String[]{
                "jdbc:TAOS://" + specifyHost + ":0",
                "jdbc:TAOS://" + specifyHost,
                "jdbc:TAOS://" + specifyHost + ":" + port + "/test",
                "jdbc:TAOS://" + specifyHost + ":" + port,
                "jdbc:TAOS://" + specifyHost + ":" + port + "/",
                "jdbc:TSDB://" + specifyHost + ":" + port,
                "jdbc:TSDB://" + specifyHost + ":" + port + "/",
                "jdbc:TAOS://" + specifyHost + ":0/db?user=root&password=taosdata",
                "jdbc:TAOS://:",
                "jdbc:TAOS://:/",
                "jdbc:TAOS://:/test",
                "jdbc:TAOS://" + specifyHost + ":0/?user=root&password=taosdata"
        };
    }

    @Test
    public void oo() throws SQLException {
        int count = 1000000;
        long startTime = System.nanoTime();
        for (int i = 0; i < count; i++) {
            System.currentTimeMillis();
        }
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        double averageTime = (double) totalTime / count;
        System.out.println("调用 " + count + " 次 System.currentTimeMillis() 总共花费 " + totalTime + " 纳秒");
        System.out.println("平均每次调用花费 " + averageTime + " 纳秒");
        double callsPerSecond = 1000000000 / averageTime;
        System.out.println("每秒大约可以调用 " + callsPerSecond + " 次");
    }

}