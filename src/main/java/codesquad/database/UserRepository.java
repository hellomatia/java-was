package codesquad.database;

import codesquad.domain.user.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
    public static final UserRepository INSTANCE = new UserRepository();
    private long id;
    private final Map<Long, User> users;

    private  UserRepository() {
        users = new ConcurrentHashMap<>();
    }

    public Long add(User user) {
        ++id;
        users.put(id, user);
        return id;
    }
}
