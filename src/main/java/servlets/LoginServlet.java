package servlets;

import dao.UserDao;
import models.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet({"/"})

public class LoginServlet extends HttpServlet  {
    private static final long serialVersionUID = 1L;
    private UserDao userStorageService;

    public void init() {
        userStorageService = new UserDao();
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();

        try {
            switch(action) {
                case "/new":
                    showNewForm(req, resp);
                    break;
                case "/insert":
                    insertUser(req, resp);
                    break;
                case "/delete":
                    deleteUser(req, resp);
                    break;
                case "/edit":
                    showEditForm(req, resp);
                    break;
                case "/update":
                    updateUser(req, resp);
                    break;
                default:
                    listUser(req, resp);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<User> users = userStorageService.getAllUsers();
        for (User el : users){
            System.out.println(el);
        }
        request.setAttribute("users", users);
        RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");

        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("userForm.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User existingUser = userStorageService.getUserById(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("userForm.jsp");
        request.setAttribute("user", existingUser);
        dispatcher.forward(request, response);

    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String login = request.getParameter("login");
        String name = request.getParameter("name");
        String secondName = request.getParameter("secondName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        User newUser = new User(login, name, secondName, email, phoneNumber);
        System.out.println("new USer: " + newUser.getId() +" " + newUser.getLogin() + " " + newUser.getName() + " " +  newUser.getEmail() + " " + newUser.getPhoneNumber());
        userStorageService.createUser(newUser);
        response.sendRedirect("list");


    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String login = request.getParameter("login");
        String name = request.getParameter("name");
        String secondName = request.getParameter("secondName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");

        User var = new User(id, login, name, secondName, email, phoneNumber);
        userStorageService.updateUser(var);
        response.sendRedirect("list");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        userStorageService.deleteUser(id);
        response.sendRedirect("list");

    }
}
