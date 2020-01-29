package userStorage;

import models.User;

import java.util.Date;
import java.util.List;

public interface UserStorage {

    void createUser(User user);

    User getUserById(int id);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUser(int id);
    void deleteAllUsers();
}

