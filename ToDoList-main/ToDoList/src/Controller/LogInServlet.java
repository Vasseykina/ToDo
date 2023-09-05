package Controller;
import Model.*;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.http.Cookie;


public class LogInServlet extends HttpServlet{

    private UsersManager users ;

    public void init(ServletConfig config) {
        try {
            users = new UsersManager();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        try {
            response.setContentType("text/html");
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            users.readAuthFile();
            if (users.checkAuth(name, password)) {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                session = request.getSession(true);
                session.setAttribute("name", name);
                session.setMaxInactiveInterval(30 * 60);
                response.sendRedirect("ToDoList");

            } else {
                request.getRequestDispatcher("/src/View/LoginLink.html").include(request, response);
                out.print("Sorry, username or password is wrong !");

            }
            out.println("</html></body>");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) {

        try{

            RequestDispatcher rd = request.getRequestDispatcher("/src/View/LoginLink.html");
            rd.forward(request, response);

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
