package ToDoListTest;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ToDoListTest
{

    @Test
    public void runAllTests() throws Exception {
        testShareList();
        testStopSharing();
        testAddListAndSubList();
        testDeleteListAndSubList();
        testReadAndWriteFile();
        testCheckAuth();
        testAddUsersList();
        testDeleteUsersList();
        testReadAuthFile();
        testReadAndWriteIdsFile();
        testReadAndWriteSharedFile();
        eraseAllFiles();

    }

    public void testShareList() throws Exception {
        ToDoList l = new ToDoList();
        UsersManager m = new UsersManager();
        eraseAllFiles();
        l.readAllFiles();
        l.addList("Andrew", "Houses");
        l.shareList("Andrew", 0, "Ann");
        l.writeFile();
        l.readAllFiles();
        m.readAll();
        LinkedHashMap<String, ArrayList<Integer>> sharedLists = m.getSharedLists();
        LinkedHashMap<String, ArrayList<Integer>> sharedListsOwners = m.getSharedListsOwners();
        ArrayList<Integer> res = sharedLists.get("Ann");
        assertEquals(true, sharedLists.containsKey("Ann") && res.contains(0) && res.size() == 1);
        res = sharedListsOwners.get("Andrew");
        assertEquals(true, sharedLists.containsKey("Andrew") && res.contains(0) && res.size() == 1);

    }


    public void testStopSharing() throws Exception {
        ToDoList l = new ToDoList();
        UsersManager m = new UsersManager();

        l.readAllFiles();
        l.stopSharing("Andrew", 0);
        m.readAll();

        LinkedHashMap<String, ArrayList<Integer>> sharedLists = m.getSharedLists();
        LinkedHashMap<String, ArrayList<Integer>> sharedListsOwners = m.getSharedListsOwners();
        ArrayList<Integer> res = sharedLists.get("Ann");
        assertEquals(false, sharedLists.containsKey("Ann") && res.contains(0) && res.size() == 1);
        res = sharedListsOwners.get("Andrew");
        if(res == null){
            assertEquals(false, sharedLists.containsKey("Andrew") && res != null);

        }
        else
            assertEquals(false, sharedLists.containsKey("Andrew") && res.contains(0) && res.size() == 1);

    }

    public void testAddListAndSubList() throws Exception {
        eraseAllFiles();
        ToDoList l = new ToDoList();
        UsersManager users = new UsersManager();
        users.readAll();
        l.readAllFiles();
        l.addList("Andrew", "Houses");
        l.addSub("Andrew", 0, "house1");
        l.writeFile();
        l.readAllFiles();
        users.readAll();
        LinkedHashMap<String, ArrayList<Integer>> usersLists = users.getUsersLists();
        LinkedHashMap <Integer, String> listIds = l.getListIds();
        LinkedHashMap <Integer, ArrayList<String>> lists = l.getLists();
        ArrayList<Integer> res = usersLists.get("Andrew");
        ArrayList<String> subs = lists.get(0);
        assertEquals(true, usersLists.containsKey("Andrew") && res.contains(0) && res.size() == 1);
        assertEquals(true, listIds.containsKey(0) && listIds.get(0).equals("Houses") && listIds.size() == 1);
        assertEquals(true, lists.containsKey(0) && subs.contains("house1") && lists.size() == 1 && subs.size() == 1);

    }

    public void testDeleteListAndSubList() throws Exception {
        ToDoList l = new ToDoList();
        UsersManager users = new UsersManager();
        users.readAll();
        l.readAllFiles();
        l.delete("Andrew", 0, 0);
        l.writeFile();
        l.readFile();
        LinkedHashMap <Integer, ArrayList<String>> lists = l.getLists();
        ArrayList<String> subs = lists.get(0);
        assertEquals(true, lists.containsKey(0) && !subs.contains("house1"));
        l.delete("Andrew", 0, -1);
        l.writeFile();
        l.readFile();
        users.readAll();
        LinkedHashMap<String, ArrayList<Integer>> usersLists = users.getUsersLists();
        LinkedHashMap <Integer, String> listIds = l.getListIds();
        ArrayList<Integer> res = usersLists.get("Andrew");
        lists = l.getLists();
        assertEquals(true, usersLists.containsKey("Andrew") && !res.contains(0) && res.size() == 0);
        assertEquals(true, !listIds.containsKey(0) && listIds.size() == 0);

    }

    public void testReadAndWriteFile() throws Exception {
        eraseAllFiles();
        ToDoList l = new ToDoList();
        l.addList("Andrew", "Houses");
        l.addSub("Andrew", 0, "house1");
        l.writeFile();
        l.readFile();
        LinkedHashMap <Integer, ArrayList<String>> lists = l.getLists();
        LinkedHashMap <Integer, String> listIds = l.getListIds();
        ArrayList<String> subs = lists.get(0);
        assertEquals(true, listIds.containsKey(0) && listIds.get(0).equals("Houses") && listIds.size() == 1);
        assertEquals(true, lists.containsKey(0) && subs.contains("house1") && lists.size() == 1 && subs.size() == 1);

    }

    public void testCheckAuth() throws Exception {
        UsersManager m = new UsersManager();
        m.readAuthFile();
        assertEquals(true, m.checkAuth("Andrew", "1234"));
        assertEquals(false, m.checkAuth("Andrew", "1228358"));
    }

    public void testAddUsersList() throws Exception {
        eraseAllFiles();
        UsersManager m = new UsersManager();
        m.readIdsFile();
        m.readAuthFile();
        m.addUsersList("Vasya", 0);
        m.writeIdsFile();
        m.readIdsFile();
        LinkedHashMap<String, ArrayList<Integer>> usersLists = m.getUsersLists();
        ArrayList<Integer> userIds = usersLists.get("Vasya");
        assertEquals(true, usersLists.containsKey("Vasya")&& userIds.contains(0));

    }

    public void testDeleteUsersList() throws Exception {
        eraseAllFiles();
        UsersManager m = new UsersManager();
        m.readIdsFile();
        m.readAuthFile();
        m.deleteUsersList("Vasya", 0);
        m.writeIdsFile();
        m.readIdsFile();
        m.readAuthFile();
        LinkedHashMap<String, ArrayList<Integer>> usersLists = m.getUsersLists();

        assertEquals(true, usersLists.containsKey("Vasya") && usersLists.get("Vasya").size() == 0);

    }

    public void testReadAuthFile() throws Exception {
        UsersManager m = new UsersManager();
        m.readAuthFile();
        LinkedHashMap<String, String> usersAuth = m.getUsersAuth();

        assertEquals(true, usersAuth.containsKey("Andrew") && usersAuth.containsValue("1234"));
        assertEquals(true, usersAuth.containsKey("Vasya") && usersAuth.containsValue("djknd"));
        assertEquals(true, usersAuth.containsKey("Ann") && usersAuth.containsValue("jdkfnd"));

    }

    public void testReadAndWriteIdsFile() throws Exception {
        eraseAllFiles();
        UsersManager m = new UsersManager();
        m.addUsersList("Andrew", 0);
        m.addUsersList("Vasya", 1);
        m.addUsersList("Ann", 2);
        m.writeIdsFile();
        m.readIdsFile();
        LinkedHashMap<String, ArrayList<Integer>> usersLists = m.getUsersLists();
        ArrayList<Integer> ids = usersLists.get("Andrew");
        assertEquals(true, usersLists.containsKey("Andrew") && ids.size() == 1 && ids.contains(0));
        ids = usersLists.get("Vasya");
        assertEquals(true, usersLists.containsKey("Vasya") && ids.size() == 1 && ids.contains(1));
        ids = usersLists.get("Ann");
        assertEquals(true, usersLists.containsKey("Ann") && ids.size() == 1 && ids.contains(2));

    }

    public void testReadAndWriteSharedFile() throws Exception {
        eraseAllFiles();
        UsersManager m = new UsersManager();
        m.readAuthFile();
        ArrayList<Integer> tmp = new ArrayList<>();
        tmp.add(0);
        m.setSharedLists("Andrew", tmp);
        tmp = new ArrayList<>();
        tmp.add(0);
        m.setSharedListsOwners("Ann", tmp);
        m.writeSharedFile();
        m.writeSharedFile();

        LinkedHashMap<String, ArrayList<Integer>> sharedLists = m.getSharedLists();
        LinkedHashMap<String, ArrayList<Integer>> sharedListsOwners = m.getSharedListsOwners();
        ArrayList<Integer> res = sharedLists.get("Andrew");
        assertEquals(true, sharedLists.containsKey("Andrew") && res.contains(0) && res.size() == 1);
        res = sharedListsOwners.get("Ann");
        assertEquals(true, sharedLists.containsKey("Ann") && res.contains(0) && res.size() == 1);
    }



    public void eraseAllFiles() throws IOException {
        String path1 = "/Users/gratchuvalsky/Desktop/apache-tomcat-8.5.82/webapps/ToDoList/dataBase/Lists.txt";
        FileWriter fstream1 = new FileWriter(path1);
        PrintWriter out1 = new PrintWriter(fstream1);
        out1.write("");
        out1.close();

        String path2 = "/Users/gratchuvalsky/Desktop/apache-tomcat-8.5.82/webapps/ToDoList/dataBase/UsersLists.txt";
        FileWriter fstream2 = new FileWriter(path2);
        PrintWriter out2 = new PrintWriter(fstream2);
        out2.write("");
        out2.close();

        String path3 = "/Users/gratchuvalsky/Desktop/apache-tomcat-8.5.82/webapps/ToDoList/dataBase/SharedLists.txt";
        FileWriter fstream3 = new FileWriter(path3);
        PrintWriter out3 = new PrintWriter(fstream3);
        out3.write("");
        out3.close();
    }



}
