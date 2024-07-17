package codesquad.database;

import codesquad.database.jdbc.CsvDataBaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    static {
        try {
            initDatabase();
        } catch (Exception e) {
            logger.error("Failed to initialize database", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return new CsvDataBaseConnection();
    }

    public static void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            logger.info("Initializing database: users");
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
                    "content , " +
                    "created_at , " +
                    "updated_at )");

            logger.info("Initializing database: comments");
            stmt.executeQuery("CREATE TABLE comments (" +
                    "id , " +
                    "post_id , " +
                    "user_id , " +
                    "user_name , " +
                    "comment , " +
                    "created_at , " +
                    "updated_at )");
        } catch (SQLException e) {
            logger.error("Failed to initialize database", e);
        }
    }
}
