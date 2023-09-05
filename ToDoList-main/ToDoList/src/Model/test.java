package Model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class test {

    public static void main(String[] args) throws Exception {

        ToDoList l = new ToDoList();
        l.addList("Andrew", "cars");
        l.addList("Ann", "houses");
        l.addList("Andrew", "houses");


       /* l.addSub("cars", "car 3");

        l.add("bikes", "bike 1");
        l.add("bikes", "bike 2");
        l.add("bikes", "bike 3");

        l.add("house", "house 1");
        l.add("house", "house 3");*/

        l.writeFile();
        /*l.readFile();
        print(l.getLists());

        l.add("cars", "car2");
        l.writeFile();
        l.readFile();
        print(l.getLists());

        l.add("house", "house2");
        l.writeFile();
        l.readFile();
        print(l.getLists());

        l.delete(1, 2);
        l.writeFile();
        l.readFile();
        print(l.getLists());*/

       /* print(l.getLists());

        l.add("cars", "car2");

        print(l.getLists());

        l.add("house", "house2");

        print(l.getLists());

        l.delete(1, 2);

        print(l.getLists());*/


       /* UsersManager m = new UsersManager();

        m.readIdsFile();
        m.deleteUsersList("Andrew", 5);
        m.deleteUsersList("Ann", 3);

        m.addUsersList("Andrew", 5);
        m.addUsersList("Ann", 3);
        m.addUsersList("Vasya", 1);
        m.writeIdsFile();*/




    }

    public static void print(LinkedHashMap<String, ArrayList<String>> tmp) {
        for (String key : tmp.keySet()) {
            ArrayList<String> s = tmp.get(key);
            System.out.print(key + " = ");
            for (int i = 0; i < s.size(); i++) {
                System.out.print(s.get(i) + ", ");
            }
            System.out.print("\n");
        }
        System.out.print("\n\n\n");

    }
}
