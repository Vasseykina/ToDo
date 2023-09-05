package Controller;
import Model.*;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class LogOutServlet extends HttpServlet{

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        HttpSession session = request.getSession(false);
        if(session!= null) {
            session.invalidate();
        }

        response.sendRedirect("ToDoList");
    }

}
