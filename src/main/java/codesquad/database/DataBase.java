package codesquad.database;

import codesquad.domain.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static final Logger logger = LoggerFactory.getLogger(DataBase.class);

    static {
        DatabaseManager.initDatabase();
    }

    private DataBase() {
    }

    public static void addUser(User user) {
        String sql = "INSERT INTO users (userId, name, password, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.userId());
            pstmt.setString(2, user.name());
            pstmt.setString(3, user.password());
            pstmt.setString(4, user.email());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.info("Failed to add user: {}", user);
        }
    }

    public static User findUserByUserId(String userId) {
        String sql = "SELECT * FROM users WHERE userId = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getString("name"),
                            rs.getString("password"),
                            rs.getString("userId"),
                            rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            logger.info("Failed to find user, userId: {}", userId);
        }
        return null;
    }

    public static List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("userId"),
                        rs.getString("email")));
            }
        } catch (SQLException e) {
            logger.error("Failed to find users", e);
        }
        return users;
    }
}
