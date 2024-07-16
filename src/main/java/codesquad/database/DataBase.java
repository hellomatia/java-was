package codesquad.database;

import codesquad.domain.article.model.Comment;
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
        initializeComments();
    }

    private static void initializeUsers() {
        List<User> initialUsers = Arrays.asList(
                new User("Admin", "admin", "admin", "admin@example.com", null),
                new User("John Doe", "password123", "john", "john@example.com", null),
                new User("Jane Smith", "pass456", "jane", "jane@example.com", null),
                new User("Bob Johnson", "bobpass", "bob", "bob@example.com", null)
        );

        String sql = "MERGE INTO users KEY(user_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (User user : initialUsers) {
                pstmt.setString(1, user.userId());
                pstmt.setString(2, user.name());
                pstmt.setString(3, user.password());
                pstmt.setString(4, user.email());
                pstmt.setString(5, user.userImageUrl());
                pstmt.executeUpdate();
                System.out.println(user);
            }
        } catch (SQLException e) {
            logger.error("Failed to initialize users", e);
        }
    }

    private static void initializePosts() {
        List<Post> initialPosts = Arrays.asList(
                new Post(null, "리액티브 시스템1", "admin", "Admin", "",
                        "우리는 시스템 아키텍처에 대한 일관성 있는 접근이 필요하며, 필요한 모든 측면은 이미 개별적으로 인식되고 있다고 생각합니다. " +
                                "즉, 응답이 잘 되고, 탄력적이며 유연하고 메시지 기반으로 동작하는 시스템 입니다. " +
                                "우리는 이것을 리액티브 시스템(Reactive Systems)라고 부릅니다. " +
                                "리액티브 시스템으로 구축된 시스템은 보다 유연하고, 느슨한 결합을 갖고, 확장성이 있습니다. " +
                                "이로 인해 개발이 더 쉬워지고 변경 사항을 적용하기 쉬워집니다. " +
                                "이 시스템은 장애에 대해 더 강한 내성을 지니며, 비록 장애가 발생하더라도, 재난이 일어나기 보다는 간결한 방식으로 해결합니다. " +
                                "리액티브 시스템은 높은 응답성을 가지며 사용자에게 효과적인 상호적 피드백을 제공합니다.",
                        null, null, null),
                new Post(null, "리액티브 시스템2", "admin", "Admin", "",
                        "우리는 시스템 아키텍처에 대한 일관성 있는 접근이 필요하며, 필요한 모든 측면은 이미 개별적으로 인식되고 있다고 생각합니다. " +
                                "즉, 응답이 잘 되고, 탄력적이며 유연하고 메시지 기반으로 동작하는 시스템 입니다. " +
                                "우리는 이것을 리액티브 시스템(Reactive Systems)라고 부릅니다. " +
                                "리액티브 시스템으로 구축된 시스템은 보다 유연하고, 느슨한 결합을 갖고, 확장성이 있습니다. " +
                                "이로 인해 개발이 더 쉬워지고 변경 사항을 적용하기 쉬워집니다. " +
                                "이 시스템은 장애에 대해 더 강한 내성을 지니며, 비록 장애가 발생하더라도, 재난이 일어나기 보다는 간결한 방식으로 해결합니다. " +
                                "리액티브 시스템은 높은 응답성을 가지며 사용자에게 효과적인 상호적 피드백을 제공합니다.",
                        null, null, null)
        );

        String sql = "INSERT INTO posts (title, user_id, user_name, image_url, content) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Post post : initialPosts) {
                pstmt.setString(1, post.title());
                pstmt.setString(2, post.userId());
                pstmt.setString(3, post.userName());
                pstmt.setString(4, post.imageUrl());
                pstmt.setString(5, post.content());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("Failed to initialize posts", e);
        }
    }

    private static void initializeComments() {
        List<Comment> initialComments = Arrays.asList(
                new Comment(null, 1L, "john", "John Doe", "Great post about reactive systems!", null, null),
                new Comment(null, 1L, "jane", "Jane Smith", "I learned a lot from this. Thanks!", null, null),
                new Comment(null, 2L, "bob", "Bob Johnson", "Looking forward to more posts on this topic.", null, null)
        );

        String sql = "INSERT INTO comments (post_id, user_id, user_name, comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Comment comment : initialComments) {
                pstmt.setLong(1, comment.postId());
                pstmt.setString(2, comment.userId());
                pstmt.setString(3, comment.userName());
                pstmt.setString(4, comment.comment());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("Failed to initialize comments", e);
        }
    }

    public static void addUser(User user) {
        String sql = "MERGE INTO users KEY(user_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.userId());
            pstmt.setString(2, user.name());
            pstmt.setString(3, user.password());
            pstmt.setString(4, user.email());
            pstmt.setString(5, user.userImageUrl());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.info("Failed to add user: {}", user);
        }
    }

    public static User findUserByUserId(String userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getString("user_name"),
                            rs.getString("password"),
                            rs.getString("user_id"),
                            rs.getString("email"),
                            rs.getString("user_image_url"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
                users.add(new User(rs.getString("user_name"),
                        rs.getString("password"),
                        rs.getString("user_id"),
                        rs.getString("email"),
                        rs.getString("user_image_url")));
            }
        } catch (SQLException e) {
            logger.error("Failed to find users", e);
        }
        return users;
    }

    public static void addPost(Post post) {
        String sql = "INSERT INTO posts (title, user_id, user_name, image_url, content) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, post.title());
            pstmt.setString(2, post.userId());
            pstmt.setString(3, post.userName());
            pstmt.setString(4, post.imageUrl());
            pstmt.setString(5, post.content());
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
                            rs.getString("title"),
                            rs.getString("user_id"),
                            rs.getString("user_name"),
                            rs.getString("image_url"),
                            rs.getString("content"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("updated_at").toLocalDateTime(),
                            null
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
                        rs.getString("title"),
                        rs.getString("user_id"),
                        rs.getString("user_name"),
                        rs.getString("image_url"),
                        rs.getString("content"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime(),
                        null
                ));
            }
        } catch (SQLException e) {
            logger.error("Failed to find posts", e);
        }
        return posts;
    }

    public static void updatePost(Post post) {
        String sql = "UPDATE posts SET user_name = ?, image_url = ?, content = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, post.userName());
            pstmt.setString(2, post.imageUrl());
            pstmt.setString(3, post.content());
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

    public static void addComment(Comment comment) {
        String sql = "INSERT INTO comments (post_id, user_id, user_name, comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, comment.postId());
            pstmt.setString(2, comment.userId());
            pstmt.setString(3, comment.userName());
            pstmt.setString(4, comment.comment());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to add comment: {}", comment, e);
        }
    }

    public static List<Comment> findCommentsByPostId(Long postId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM comments WHERE post_id = ? ORDER BY created_at";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    comments.add(new Comment(
                            rs.getLong("id"),
                            rs.getLong("post_id"),
                            rs.getString("user_id"),
                            rs.getString("user_name"),
                            rs.getString("comment"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("updated_at").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to get comments for post: {}", postId, e);
        }
        return comments;
    }
}
