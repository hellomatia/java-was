package codesquad.domain.user;

import codesquad.domain.user.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
    private long id;
    private final Map<Long, User> users;

    public UserRepository() {
        users = new ConcurrentHashMap<>();
    }

    public Long join(User user) {
        ++id;
        users.put(id, user);
        return id;
    }
}
