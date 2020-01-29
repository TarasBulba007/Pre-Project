package servlets;

import dao.UserDao;
import models.User;
import service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@WebServlet({"/"})

public class LoginServlet extends HttpServlet  {
    private static final long serialVersionUID = 1L;
    private UserService service;


    public void init() {
        service = new UserService();
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                case "/deleteAll":
                    deleteAllUsers(req, resp);
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
        } catch (SQLException | ParseException ex) {
            throw new ServletException(ex);
        }
    }

    private void listUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<User> users = service.getAllUsers();
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

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User existingUser = service.getUserById(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("userForm.jsp");
        request.setAttribute("user", existingUser);
        dispatcher.forward(request, response);

    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ParseException {
        String login = request.getParameter("login");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        LocalDate birthDate = LocalDate.parse(request.getParameter("birthDate"));
        User newUser = new User(login, name, email, phoneNumber, birthDate);
        System.out.println("new USer: " + newUser.getId() +" " + newUser.getLogin() + " " + newUser.getName() + " " +  newUser.getEmail() + " " + newUser.getBirthDate().toString());
        service.createUser(newUser);
        response.sendRedirect("list");


    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ParseException {
        int id = Integer.parseInt(request.getParameter("id"));
        String login = request.getParameter("login");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        LocalDate birthDate = LocalDate.parse(request.getParameter("birthDate"));
        User var = new User(id, login, name, email, phoneNumber, birthDate);
        System.out.println(var.getBirthDate());
        service.updateUser(var);
        response.sendRedirect("list");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        service.deleteUser(id);
        response.sendRedirect("list");
    }

    private void deleteAllUsers(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        service.deleteAllUsers();
        response.sendRedirect("list");
    }
}
