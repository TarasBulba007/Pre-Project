package service;

import dao.UserDao;
import dao.UserDaoInterface;
import models.User;

import java.util.List;

public class UserService implements UserDaoInterface {

    public UserService() {
    }

    @Override
    public void createUser(User user) {
        getUserDao().createUser(user);
    }

    @Override
    public User getUserById(int id) {
       return getUserDao().getUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return  getUserDao().getAllUsers();
    }

    @Override
    public boolean updateUser(User user) {
        return getUserDao().updateUser(user);
    }

    @Override
    public boolean deleteUser(int id) {
        return  getUserDao().deleteUser(id);
    }

    @Override
    public void deleteAllUsers() {
        getUserDao().deleteAllUsers();
    }

    private static UserDao getUserDao() {
        return new UserDao();
    }
}
