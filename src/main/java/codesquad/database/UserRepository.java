package codesquad.database;

import codesquad.domain.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
    public static final UserRepository INSTANCE = new UserRepository();
    private final Map<String, User> users;

    private  UserRepository() {
        users = new ConcurrentHashMap<>();
        users.put("admin", new User("admin", "admin", "admin", "code.@s.com"));
    }

    public void add(User user) {
        users.put(user.userId(), user);
    }

    public User findByUserId(String userId) {
        return users.get(userId);
    }

    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }
}
