package codesquad.database;

import codesquad.domain.user.model.User;

public class DataBase {
    private static final UserRepository userRepository = UserRepository.INSTANCE;

    private DataBase() {
    }

    public static long addUser(User user) {
        return userRepository.add(user);
    }
}
