package codesquad.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DataBaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DataBaseManager.class);
    private static final String CSV_DATABASE_JDBC_URL = "jdbc:csvdatabase:";
    private static Connection connection;

    static {
        try {
            Class.forName("codesquad.database.jdbc.CsvDataBaseDriver");
            initDatabase();
        } catch (Exception e) {
            logger.error("Failed to initialize database", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(CSV_DATABASE_JDBC_URL);
            } catch (SQLException e) {
                logger.error("Failed to connect to database", e);
            }
        }
        return connection;
    }

    public static void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeQuery("CREATE TABLE users (" +
                    "user_id , " +
                    "user_name , " +
                    "password , " +
                    "email , " +
                    "user_image_url )");
            logger.info("Initializing database: posts");
            stmt.executeQuery("CREATE TABLE posts (" +
                    "id , " +
                    "title , " +
                    "user_id , " +
                    "user_name , " +
                    "image_url , " +
                    "content )");
            logger.info("Initializing database: users");
            stmt.executeQuery("CREATE TABLE comments (" +
                    "id , " +
                    "post_id , " +
                    "user_id , " +
                    "user_name , " +
                    "comment )");
            logger.info("Initializing database: comments");
        } catch (SQLException e) {
            logger.error("Failed to initialize database", e);
        }
    }
}
