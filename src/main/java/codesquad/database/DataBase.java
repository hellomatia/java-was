package codesquad.database;

import codesquad.domain.article.model.Comment;
import codesquad.domain.article.model.Post;
import codesquad.domain.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.*;

public class DataBase {
    private static final Logger logger = LoggerFactory.getLogger(DataBase.class);
    private static String[] postId;

    static {
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

        String sql = "INSERT INTO users VALUES ( ? , ? , ? , ? , ? )";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (User user : initialUsers) {
                pstmt.setString(1, user.userId());
                pstmt.setString(2, user.name());
                pstmt.setString(3, user.password());
                pstmt.setString(4, user.email());
                pstmt.setString(5, user.userImageUrl());
                pstmt.executeQuery();
            }
        } catch (SQLException e) {
            logger.error("Failed to initialize users", e);
        }
    }

    private static void initializePosts() {
        List<Post> initialPosts = Arrays.asList(
                new Post(null, "리액티브 시스템1", "admin", "Admin", "/image",
                        "우리는 시스템 아키텍처에 대한 일관성 있는 접근이 필요하며, 필요한 모든 측면은 이미 개별적으로 인식되고 있다고 생각합니다. " +
                                "즉, 응답이 잘 되고, 탄력적이며 유연하고 메시지 기반으로 동작하는 시스템 입니다. " +
                                "우리는 이것을 리액티브 시스템(Reactive Systems)라고 부릅니다. " +
                                "리액티브 시스템으로 구축된 시스템은 보다 유연하고, 느슨한 결합을 갖고, 확장성이 있습니다. " +
                                "이로 인해 개발이 더 쉬워지고 변경 사항을 적용하기 쉬워집니다. " +
                                "이 시스템은 장애에 대해 더 강한 내성을 지니며, 비록 장애가 발생하더라도, 재난이 일어나기 보다는 간결한 방식으로 해결합니다. " +
                                "리액티브 시스템은 높은 응답성을 가지며 사용자에게 효과적인 상호적 피드백을 제공합니다.",
                        null, null, null),
                new Post(null, "리액티브 시스템2", "admin", "Admin", "/image",
                        "우리는 시스템 아키텍처에 대한 일관성 있는 접근이 필요하며, 필요한 모든 측면은 이미 개별적으로 인식되고 있다고 생각합니다. " +
                                "즉, 응답이 잘 되고, 탄력적이며 유연하고 메시지 기반으로 동작하는 시스템 입니다. " +
                                "우리는 이것을 리액티브 시스템(Reactive Systems)라고 부릅니다. " +
                                "리액티브 시스템으로 구축된 시스템은 보다 유연하고, 느슨한 결합을 갖고, 확장성이 있습니다. " +
                                "이로 인해 개발이 더 쉬워지고 변경 사항을 적용하기 쉬워집니다. " +
                                "이 시스템은 장애에 대해 더 강한 내성을 지니며, 비록 장애가 발생하더라도, 재난이 일어나기 보다는 간결한 방식으로 해결합니다. " +
                                "리액티브 시스템은 높은 응답성을 가지며 사용자에게 효과적인 상호적 피드백을 제공합니다.",
                        null, null, null)
        );

        String sql = "INSERT INTO posts VALUES ( ? , ? , ? , ? , ? , ? )";
        postId = new String[]{generatedId(), generatedId()};
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int postIdx = 0;
            for (Post post : initialPosts) {
                int idx = 1;
                pstmt.setString(idx++, postId[postIdx++]);
                pstmt.setString(idx++, post.title());
                pstmt.setString(idx++, post.userId());
                pstmt.setString(idx++, post.userName());
                pstmt.setString(idx++, post.imageUrl());
                pstmt.setString(idx++, encodeContent(post.content()));
                pstmt.executeQuery();
            }
        } catch (SQLException e) {
            logger.error("Failed to initialize posts", e);
        }
    }

    private static void initializeComments() {
        List<Comment> initialComments = Arrays.asList(
                new Comment(null, postId[0], "john", "John Doe", "Great post about reactive systems!", null, null),
                new Comment(null, postId[0], "jane", "Jane Smith", "I learned a lot from this. Thanks!", null, null),
                new Comment(null, postId[1], "bob", "Bob Johnson", "Looking forward to more posts on this topic.", null, null)
        );

        String sql = "INSERT INTO comments VALUES ( ? , ? , ? , ? , ? )";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Comment comment : initialComments) {
                int idx = 1;
                pstmt.setString(idx++, generatedId());
                pstmt.setString(idx++, comment.postId());
                pstmt.setString(idx++, comment.userId());
                pstmt.setString(idx++, comment.userName());
                pstmt.setString(idx++, encodeContent(comment.comment()));
                pstmt.executeQuery();
            }
        } catch (SQLException e) {
            logger.error("Failed to initialize comments", e);
        }
    }

    public static void addUser(User user) {
        String sql = "INSERT INTO users VALUES ( ? , ? , ? , ? , ? )";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.userId());
            pstmt.setString(2, user.name());
            pstmt.setString(3, user.password());
            pstmt.setString(4, user.email());
            pstmt.setString(5, user.userImageUrl());
            pstmt.executeQuery();
        } catch (SQLException e) {
            logger.info("Failed to add user: {}", user);
        }
    }

    public static User findUserByUserId(String userId) {
        String sql = "SELECT user_name , password , user_id , email , user_image_url FROM users WHERE user_id = ?";
        try (Connection conn = DataBaseManager.getConnection();
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
            logger.info("Failed to find user, userId: {}", userId, e);
        }
        return null;
    }

    public static List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_name , password , user_id , email , user_image_url FROM users";
        try (Connection conn = DataBaseManager.getConnection();
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
        String sql = "INSERT INTO posts VALUES ( ? , ? , ? , ? , ? , ? )";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int idx = 1;
            pstmt.setString(idx++, generatedId());
            pstmt.setString(idx++, post.title());
            pstmt.setString(idx++, post.userId());
            pstmt.setString(idx++, post.userName());
            pstmt.setString(idx++, post.imageUrl());
            pstmt.setString(idx++, encodeContent(post.content()));
            System.out.println(post.content());
            System.out.println(encodeContent(post.content()));
            pstmt.executeQuery();
        } catch (SQLException e) {
            logger.error("Failed to add post: {}", post, e);
        }
    }

    public static List<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT id , title , user_id , user_name , image_url , content FROM posts";
        try (Connection conn = DataBaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                posts.add(new Post(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("user_id"),
                        rs.getString("user_name"),
                        rs.getString("image_url"),
                        decodeContent(rs.getString("content")),
                        null,
                        null,
                        null
                ));
            }
        } catch (SQLException e) {
            logger.error("Failed to find posts", e);
        }
        List<Post> sortPosts = new ArrayList<>(posts);
        for (int i = posts.size() - 1; i >= 0 ; i--) {
            sortPosts.set(posts.size() - i - 1, posts.get(i));
        }
        return sortPosts;
    }

    public static void addComment(Comment comment) {
        String sql = "INSERT INTO comments VALUES ( ?, ? , ? , ? , ? )";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int idx = 1;
            pstmt.setString(idx++, generatedId());
            pstmt.setString(idx++, comment.postId());
            pstmt.setString(idx++, comment.userId());
            pstmt.setString(idx++, comment.userName());
            pstmt.setString(idx++, encodeContent(comment.comment()));
            pstmt.executeQuery();
        } catch (SQLException e) {
            logger.error("Failed to add comment: {}", comment, e);
        }
    }

    public static List<Comment> findCommentsByPostId(String postId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT id , post_id , user_id , user_name , comment FROM comments WHERE post_id = ? ORDER BY created_at";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    comments.add(new Comment(
                            rs.getString("id"),
                            rs.getString("post_id"),
                            rs.getString("user_id"),
                            rs.getString("user_name"),
                            decodeContent(rs.getString("comment")),
                            null,
                            null
                    ));
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to get comments for post: {}", postId, e);
        }
        return comments;
    }

    private static String generatedId() {
        return UUID.randomUUID().toString();
    }

    private static String encodeContent(String content) {
        try {
            return Base64.getEncoder().encodeToString(content.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("Failed to encode content", e);
        }
        return null;
    }

    private static String decodeContent(String encodedContent) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedContent);
            return new String(decodedBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Failed to decode content", e);
        }
        return null;
    }
}
