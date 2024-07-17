package codesquad.database;

import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static final JdbcConnectionPool jdbcConnectionPool;
    private static final String JDBC_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("org.h2.Driver");
            jdbcConnectionPool = JdbcConnectionPool.create(JDBC_URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("H2 JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return jdbcConnectionPool.getConnection();
    }

    public static void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "user_id VARCHAR(50) PRIMARY KEY, " +
                    "user_name VARCHAR(50), " +
                    "password VARCHAR(50), " +
                    "email VARCHAR(50))");

            stmt.execute("CREATE TABLE IF NOT EXISTS posts (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "title VARCHAR(255) NOT NULL,"+
                    "user_id VARCHAR(255) NOT NULL, " +
                    "user_name VARCHAR(255) NOT NULL, " +
                    "image_url VARCHAR(1000) NOT NULL, " +
                    "content CLOB NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(), " +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP())");

            stmt.execute("CREATE TABLE IF NOT EXISTS comments (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "post_id BIGINT NOT NULL," +
                    "user_id VARCHAR(255) NOT NULL, " +
                    "user_name VARCHAR(255) NOT NULL, " +
                    "comment CLOB NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(), " +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP())");
        } catch (SQLException e) {
            logger.error("Failed to initialize database", e);
        }
    }
}
