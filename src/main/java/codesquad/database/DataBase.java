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
        initializeUsers();
        initializePosts();
    }

    private static void initializeUsers() {
        List<User> initialUsers = Arrays.asList(
                new User("Admin", "admin", "admin", "admin@example.com"),
                new User("John Doe", "password123", "john", "john@example.com"),
                new User("Jane Smith", "pass456", "jane", "jane@example.com"),
                new User("Bob Johnson", "bobpass", "bob", "bob@example.com")
        );

        String sql = "MERGE INTO users KEY(userId) VALUES (?, ?, ?, ?)";
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
            logger.error("Failed to initialize users", e);
        }
    }

    private static void initializePosts() {
        List<Post> initialPosts = Arrays.asList(
                new Post(null, "admin", "Admin", "",
                        "우리는 시스템 아키텍처에 대한 일관성 있는 접근이 필요하며, 필요한 모든 측면은 이미 개별적으로 인식되고 있다고 생각합니다. " +
                                "즉, 응답이 잘 되고, 탄력적이며 유연하고 메시지 기반으로 동작하는 시스템 입니다. " +
                                "우리는 이것을 리액티브 시스템(Reactive Systems)라고 부릅니다. " +
                                "리액티브 시스템으로 구축된 시스템은 보다 유연하고, 느슨한 결합을 갖고, 확장성이 있습니다. " +
                                "이로 인해 개발이 더 쉬워지고 변경 사항을 적용하기 쉬워집니다. " +
                                "이 시스템은 장애에 대해 더 강한 내성을 지니며, 비록 장애가 발생하더라도, 재난이 일어나기 보다는 간결한 방식으로 해결합니다. " +
                                "리액티브 시스템은 높은 응답성을 가지며 사용자에게 효과적인 상호적 피드백을 제공합니다.",
                        0, null, null),
                new Post(null, "admin", "Admin", "",
                        "우리는 시스템 아키텍처에 대한 일관성 있는 접근이 필요하며, 필요한 모든 측면은 이미 개별적으로 인식되고 있다고 생각합니다. " +
                                "즉, 응답이 잘 되고, 탄력적이며 유연하고 메시지 기반으로 동작하는 시스템 입니다. " +
                                "우리는 이것을 리액티브 시스템(Reactive Systems)라고 부릅니다. " +
                                "리액티브 시스템으로 구축된 시스템은 보다 유연하고, 느슨한 결합을 갖고, 확장성이 있습니다. " +
                                "이로 인해 개발이 더 쉬워지고 변경 사항을 적용하기 쉬워집니다. " +
                                "이 시스템은 장애에 대해 더 강한 내성을 지니며, 비록 장애가 발생하더라도, 재난이 일어나기 보다는 간결한 방식으로 해결합니다. " +
                                "리액티브 시스템은 높은 응답성을 가지며 사용자에게 효과적인 상호적 피드백을 제공합니다.",
                        0, null, null)
        );

        String sql = "INSERT INTO posts (account_id, account_nickname, image_url, content) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Post post : initialPosts) {
                pstmt.setString(1, post.accountId());
                pstmt.setString(2, post.accountNickname());
                pstmt.setString(3, post.imageUrl());
                pstmt.setString(4, post.content());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("Failed to initialize posts", e);
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
        String sql = "INSERT INTO posts (account_id, account_nickname, image_url, content) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, post.accountId());
            pstmt.setString(2, post.accountNickname());
            pstmt.setString(3, post.imageUrl());
            pstmt.setString(4, post.content());
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
