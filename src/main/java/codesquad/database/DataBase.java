package codesquad.database;

import codesquad.domain.article.model.Post;
import codesquad.domain.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataBase {
    private static final Logger logger = LoggerFactory.getLogger(DataBase.class);

    static {
        DatabaseManager.initDatabase();
        initializeData();
    }

    private DataBase() {
    }

    private static void initializeData() {
        List<User> initialUsers = Arrays.asList(
                new User("Admin", "admin", "admin", "admin@example.com"),
                new User("John Doe", "password123", "john", "john@example.com"),
                new User("Jane Smith", "pass456", "jane", "jane@example.com"),
                new User("Bob Johnson", "bobpass", "bob", "bob@example.com")
        );

        String sql = "INSERT INTO users (userId, name, password, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (User user : initialUsers) {
                pstmt.setString(1, user.userId());
                pstmt.setString(2, user.name());
                pstmt.setString(3, user.password());
                pstmt.setString(4, user.email());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("failed to initialize users", e);
        }
    }

    public static void addUser(User user) {
        String sql = "MERGE INTO users KEY(userId) VALUES (?, ?, ?, ?)";
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

    public static void addPost(Post post) {
        String sql = "INSERT INTO posts (account_id, account_nickname, image_url, content, likes_count, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, post.accountId());
            pstmt.setString(2, post.accountNickname());
            pstmt.setString(3, post.imageUrl());
            pstmt.setString(4, post.content());
            pstmt.setInt(5, post.likesCount());
            pstmt.setTimestamp(6, Timestamp.valueOf(post.createdAt()));
            pstmt.setTimestamp(7, Timestamp.valueOf(post.updatedAt()));
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    logger.info("Added post with ID: {}", id);
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to add post: {}", post, e);
        }
    }

    public static Post findPostById(long id) {
        String sql = "SELECT * FROM posts WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Post(
                            rs.getLong("id"),
                            rs.getString("account_id"),
                            rs.getString("account_nickname"),
                            rs.getString("image_url"),
                            rs.getString("content"),
                            rs.getInt("likes_count"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("updated_at").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to find post, id: {}", id, e);
        }
        return null;
    }

    public static List<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM posts ORDER BY created_at DESC";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                posts.add(new Post(
                        rs.getLong("id"),
                        rs.getString("account_id"),
                        rs.getString("account_nickname"),
                        rs.getString("image_url"),
                        rs.getString("content"),
                        rs.getInt("likes_count"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            logger.error("Failed to find posts", e);
        }
        return posts;
    }

    public static void updatePost(Post post) {
        String sql = "UPDATE posts SET account_nickname = ?, image_url = ?, content = ?, likes_count = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, post.accountNickname());
            pstmt.setString(2, post.imageUrl());
            pstmt.setString(3, post.content());
            pstmt.setInt(4, post.likesCount());
            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setLong(6, post.id());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Updated post with ID: {}", post.id());
            } else {
                logger.warn("No post found with ID: {}", post.id());
            }
        } catch (SQLException e) {
            logger.error("Failed to update post: {}", post, e);
        }
    }
}
