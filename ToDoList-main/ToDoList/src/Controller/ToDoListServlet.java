package Controller;

import Model.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.http.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.http.Cookie;


import java.util.ArrayList;

public class ToDoListServlet extends HttpServlet{

    private ToDoList list;
    private UsersManager users;

    public void init(ServletConfig config) {
        try {
            list = new ToDoList();
            users = new UsersManager();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        try {
            ///list.readFile();
            list.readAllFiles();
            //users.readIdsFile();
            String state = request.getParameter("state");
            if(state.equals("AddList")){
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(30*60);
                String name = (String)session.getAttribute("name");
                String title = request.getParameter("title");
                list.addList(name, title);
                list.writeFile();
            }

            if(state.equals("DeleteList")){
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(30*60);
                String name = (String)session.getAttribute("name");
                int id = Integer.parseInt(request.getParameter("id"));
                list.delete(name, id, -1);
                list.writeFile();
            }

            if(state.equals("addSubList")){
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(30*60);
                String name = (String)session.getAttribute("name");
                int id = Integer.parseInt(request.getParameter("id"));
                String title = request.getParameter("title");
                list.addSub(name, id, title);
                list.writeFile();
            }

            if(state.equals("deleteSubList")){
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(30*60);
                String name = (String)session.getAttribute("name");
                int titleId = Integer.parseInt(request.getParameter("titleId"));
                int subId = Integer.parseInt(request.getParameter("subId"));
                list.delete(name, titleId, subId);
                list.writeFile();
            }

            if(state.equals("ShareList")){
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(30*60);
                String name = (String)session.getAttribute("name");
                String destinationUser = request.getParameter("user");
                String ids = request.getParameter("ids");

                String[] tmp = ids.split(":");
                for (int i = 0; i < tmp.length; i++) {
                    int id = Integer.parseInt(tmp[i]);
                    list.shareList(name, id, destinationUser);
                }

            }

            if(state.equals("stopSharing")){
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(30*60);
                String name = (String)session.getAttribute("name");
                int id = Integer.parseInt(request.getParameter("id"));
                list.stopSharing(name, id);

            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {

        try{

            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(30*60);
            String name = (String)session.getAttribute("name");
            if(name == null)
                response.sendRedirect("LogIn");

            else {
                list.readAllFiles();
                users.readAll();

                LinkedHashMap<Integer, ArrayList<String>> l1 = list.getLists();
                LinkedHashMap<Integer, String> l2 = list.getListIds();
                LinkedHashMap<String, ArrayList<Integer>> ids = users.getUsersLists();
                LinkedHashMap<String, ArrayList<Integer>> sharedLists = users.getSharedLists();
                LinkedHashMap<String, ArrayList<Integer>> sharedListsOwners = users.getSharedListsOwners();


                ArrayList<Integer> myIds = ids.get(name);
                LinkedHashMap<String, ArrayList<String>> myLists = new LinkedHashMap<>();
                ArrayList<Integer> mySharedIds = sharedListsOwners.get(name);
                ArrayList<Boolean> mySharedLists = new ArrayList<>();



                for(int i = 0; i < myIds.size(); i++){
                    String title = l2.get(myIds.get(i));
                    ArrayList<String> sub = l1.get(myIds.get(i));
                    myLists.put(title, sub);
                }

                /*for(int i = 0; i < mySharedIds.size(); i++){
                    String title = l2.get(mySharedIds.get(i));
                    mySharedLists.add(title);
                }*/

                if(mySharedIds != null ) {
                    for (int i = 0; i < myIds.size(); i++) {
                        if (mySharedIds.contains(myIds.get(i)))
                            mySharedLists.add(true);
                        else
                            mySharedLists.add(false);

                    }
                }
                else
                    mySharedLists = new ArrayList<>();

                request.setAttribute("session", session);
                request.setAttribute("lists", myLists);
                request.setAttribute("sharedLists", mySharedLists);


                RequestDispatcher rd = request.getRequestDispatcher("/src/View/ToDoList.jsp");
                rd.forward(request, response);
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


}
