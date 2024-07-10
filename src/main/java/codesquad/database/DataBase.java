package codesquad.database;

import codesquad.domain.user.model.User;

import java.util.List;

public class DataBase {
    private static final UserRepository userRepository = UserRepository.INSTANCE;

    private DataBase() {
    }

    public static void addUser(User user) {
        userRepository.add(user);
    }

    public static User findUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public static List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }
}
