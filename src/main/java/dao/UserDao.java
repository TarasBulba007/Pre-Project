package dao;

import models.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDao implements UserDaoInterface {

    public UserDao() {
    }

    private static com.mysql.jdbc.Connection getServerConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());
            StringBuilder url = new StringBuilder();
            url.append("jdbc:mysql://");
            url.append("localhost:");
            url.append("3306/");
            url.append("preproject?");
            url.append("user=root&");
            url.append("password=HfpdjlLtdeitrjn25lj33");
            System.out.println("URL: " + url + "\n");
            return (com.mysql.jdbc.Connection) DriverManager.getConnection(url.toString());
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    @Override
    public void createUser(User user)  {
        try (Connection connection = getServerConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(login, name, email, phoneNumber, birthDate) values(?,?,?,?,?) ")) {
            System.out.println("Create user: " +  user.getLogin() + " " + user.getName() + " " +" " + user.getEmail() + " " + user.getPhoneNumber());
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPhoneNumber());
            preparedStatement.setDate(5, java.sql.Date.valueOf(user.getBirthDate()));
        //    java.sql.Date date = new java.sql.Date(user.getBirthDate().getDayOfYear());
        //    preparedStatement.setDate(5, date);
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUserById(int id) {
        User user = null;
        try (PreparedStatement preparedStatement = getServerConnection().prepareStatement("SELECT id, login, name, email, phoneNumber, birthDate FROM users WHERE id =?");) {
            preparedStatement.setInt(1, id);
            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                String login = res.getNString("login");
                String name = res.getString("name");
                String email = res.getString("email");
                String phoneNumber = res.getString("phoneNumber");
                LocalDate birthDate = res.getDate("birthDate").toLocalDate();
                user = new User(id, login, name, email, phoneNumber, birthDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = getServerConnection().prepareStatement("SELECT *FROM users");) {
                System.out.println(preparedStatement);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String login = rs.getString("login");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String phoneNumber = rs.getString("phoneNumber");
                    LocalDate birthDate = rs.getDate("birthDate").toLocalDate();
                    users.add(new User(id, login, name, email,phoneNumber, birthDate));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return users;
        }

    @Override
    public boolean updateUser(User user)  {
        boolean updated = false;
        try (PreparedStatement statement = getServerConnection().prepareStatement("UPDATE users SET login=?, name=?, email=?, phoneNumber=?, birthDate=? WHERE id=?");) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhoneNumber());
            statement.setDate(5, java.sql.Date.valueOf(user.getBirthDate()));
            statement.setInt(6, user.getId());
            updated = statement.executeUpdate() > 0;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return updated;
    }

    /**
     * @Override public void updateUserBirthDate(User user, String password, Date date) {
     * if (validateUser(user.getLogin(), password)) {
     * try {
     * PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE login=?");
     * statement.setString(1, user.getLogin());
     * ResultSet res = statement.executeQuery();
     * res.next();
     * statement = connection.prepareStatement("UPDATE users SET birthDate=? WHERE login=?");
     * statement.setDate(1, (java.sql.Date) date);
     * statement.setNString(2, user.getLogin());
     * statement.executeUpdate();
     * res.close();
     * statement.close();
     * } catch (SQLException e) {
     * e.printStackTrace();
     * }
     * }
     * }
     * @param id
     */

    @Override
    public boolean deleteUser(int id)  {
        boolean del = false;
        try (PreparedStatement statement = getServerConnection().prepareStatement("DELETE FROM users WHERE id=?");) {
            statement.setInt(1, id);
            del = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return del;
    }

    @Override
    public void deleteAllUsers() {
        try (PreparedStatement statement = getServerConnection().prepareStatement("DELETE FROM users")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


   public void createTableUsers() throws SQLException {
       Statement statement = getServerConnection().createStatement();
       statement.execute("CREATE TABLE users (id  int(3) NOT NULL AUTO_INCREMENT, login varchar(255) NOT NULL, name varchar(255) NOT NULL, email varchar(220) NOT NULL, phoneNumber varchar(120), birthDate DATE NOT NULL , PRIMARY KEY (id));");
       statement.close();
   }

    public void dropTableUsers() throws SQLException {
        Statement statement = getServerConnection().createStatement();
        statement.executeUpdate("DROP TABLE IF EXISTS users");
        statement.close();
    }
}
