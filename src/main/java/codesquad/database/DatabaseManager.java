package codesquad.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static final String JDBC_URL = "jdbc:h2:~/userdb";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("H2 JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public static void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "userId VARCHAR(50) PRIMARY KEY, " +
                    "name VARCHAR(50), " +
                    "password VARCHAR(50), " +
                    "email VARCHAR(50))");
        } catch (SQLException e) {
            logger.error("Failed to initialize database", e);
        }
    }
}
