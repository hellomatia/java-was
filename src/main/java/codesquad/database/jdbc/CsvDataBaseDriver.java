package codesquad.database.jdbc;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class CsvDataBaseDriver implements Driver {
    static {
        try {
            DriverManager.registerDriver(new CsvDataBaseDriver());
        } catch (SQLException e) {
            throw new RuntimeException("Can't register driver!");
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (!acceptsURL(url)) return null;
        return new CsvDataBaseConnection();
    }

    @Override
    public boolean acceptsURL(String url) {
        return url.startsWith("jdbc:csvdatabase:");
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
