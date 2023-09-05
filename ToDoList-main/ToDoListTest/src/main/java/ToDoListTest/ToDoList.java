package ToDoListTest;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class ToDoList {

    private final String path = "/Users/gratchuvalsky/Desktop/apache-tomcat-8.5.82/webapps/ToDoList/dataBase/Lists.txt";

    private LinkedHashMap <Integer, ArrayList<String>> lists;
    private LinkedHashMap <Integer, String> listIds;
    private ArrayList<Integer> id_numbers;
    private UsersManager users;


    public ToDoList() throws Exception {
        lists = new LinkedHashMap<>();
        listIds = new LinkedHashMap<>();
        id_numbers = new ArrayList<>();
        users = new UsersManager();
    }


    public synchronized void shareList(String user, int id, String destinationUser) throws Exception {


        if(!destinationUser.equals(user)) {
            ArrayList<Integer> ids = users.getUsersLists().get(user);
            int commonId = ids.get(id);

            if (!users.getSharedLists().keySet().equals(users.getUsersAuth().keySet())) {
                for (String key : users.getUsersAuth().keySet()) {
                    ArrayList<Integer> a = new ArrayList<>();
                    if (!users.getSharedLists().containsKey(key))
                        users.setSharedLists(key, a);
                }
            }

            if (!users.getSharedListsOwners().keySet().equals(users.getUsersAuth().keySet())) {
                for (String key : users.getUsersAuth().keySet()) {
                    ArrayList<Integer> a = new ArrayList<>();
                    if (!users.getSharedListsOwners().containsKey(key))
                        users.setSharedListsOwners(key, a);
                }

            }

            ArrayList<Integer> tmp2 = users.getSharedLists().get(destinationUser);
            ArrayList<Integer> tmp3 = users.getSharedListsOwners().get(user);


            if (tmp2 == null)
                tmp2 = new ArrayList<>();

            if (tmp3 == null)
                tmp3 = new ArrayList<>();

            tmp2.add(commonId);
            users.setSharedLists(destinationUser, tmp2);
            tmp3.add(commonId);
            users.setSharedListsOwners(user, tmp3);

            users.writeAll();
        }
    }


    public synchronized void stopSharing(String user, int id) throws Exception {

        ArrayList<Integer> ids = users.getUsersLists().get(user);
        int commonId = ids.get(id);

        if(!users.getSharedLists().keySet().equals(users.getUsersAuth().keySet())) {
            for (String key : users.getUsersAuth().keySet()) {
                ArrayList<Integer> a = new ArrayList<>();
                if (!users.getSharedLists().containsKey(key))
                    users.setSharedLists(key, a);
            }
        }

        if(!users.getSharedListsOwners().keySet().equals(users.getUsersAuth().keySet())){
            for(String key : users.getUsersAuth().keySet()){
                ArrayList<Integer> a = new ArrayList<>();
                if(!users.getSharedListsOwners().containsKey(key))
                    users.setSharedListsOwners(key, a);
            }

        }

        ArrayList<Integer> tmp2;
        for(String key : users.getSharedLists().keySet()){
            tmp2 = users.getSharedLists().get(key);
            if(tmp2 != null && !tmp2.isEmpty()) {
                if (tmp2.contains(commonId)) {
                    tmp2.remove(Integer.valueOf(commonId));
                    users.setSharedLists(key, tmp2);
                }
            }

        }
        ArrayList<Integer> tmp3 = users.getSharedListsOwners().get(user);

        if(tmp3 == null)
            tmp3 = new ArrayList<>();

        tmp3.remove(Integer.valueOf(commonId));
        users.setSharedListsOwners(user, tmp3);

        users.writeAll();

    }



    public LinkedHashMap<Integer, ArrayList<String>> getLists(){
        return lists;
    }

    public LinkedHashMap<Integer, String> getListIds(){
        return listIds;
    }


    public synchronized boolean addSub(String user, int id, String sublist) throws Exception {
        LinkedHashMap<String, ArrayList<Integer>> usersLists = users.getUsersLists();

        ArrayList<Integer> UserListIds = usersLists.get(user);
        int commonId = UserListIds.get(id);

        if(UserListIds.contains(commonId)){
            if(lists.containsKey(commonId)){
                ArrayList<String> subs = lists.get(commonId);
                if(!subs.contains(sublist)) {
                    subs.add(sublist);
                    lists.put(commonId, subs);
                }
                else
                    return false;
            }
        }

        else
            return false;

        return true;
    }

    public synchronized boolean addList(String user, String title) throws Exception {

        ArrayList<String> tmp = new ArrayList<>();
        LinkedHashMap<String, ArrayList<Integer>> usersLists = users.getUsersLists();

        if(lists.size() == 0){
            lists.put(0, tmp);
            id_numbers.add(0);
            listIds.put(0, title);
            users.addUsersList(user, 0);
            users.writeIdsFile();
        }

        else {
            ArrayList<Integer> UserListIds = usersLists.get(user);
            for(int i = 0; i < UserListIds.size(); i++){
                String tmpTitle = listIds.get(UserListIds.get(i));
                if(tmpTitle.equals(title))
                    return false;
            }

            int id = id_numbers.size();
            lists.put(id, tmp);
            id_numbers.add(id);
            listIds.put(id, title);
            users.addUsersList(user, id);
            users.writeIdsFile();
        }
        return true;
    }

    public synchronized void delete(String user, int titleId, int sublistId) throws Exception {
        LinkedHashMap<String, ArrayList<Integer>> usersLists = users.getUsersLists();
        ArrayList<Integer> UserListIds = usersLists.get(user);
        int id = UserListIds.get(titleId);


        if(sublistId == -1){
            lists.remove(id);
            listIds.remove(id);
            id_numbers.remove(Integer.valueOf(id));
            stopSharing(user, titleId);
            users.deleteUsersList(user, id);
            users.writeSharedFile();
            users.writeIdsFile();

        }

        else{
            ArrayList<String> tmp = lists.get(id);
            tmp.remove(sublistId);
            lists.put(id, tmp);
        }

    }



    public synchronized void readFile() throws Exception{
        File list = new File(path);

        if(list.length() != 0) {
            lists  = new LinkedHashMap<>();
            listIds = new LinkedHashMap<>();
            id_numbers = new ArrayList<>();

            Scanner sc = new Scanner(list);

            int id = 0;
            String title;
            ArrayList<String> tmp ;

            while (sc.hasNextLine()) {
                tmp = new ArrayList<>();
                String[] arr = sc.nextLine().split(":");
                arr[0].trim();
                id = Integer.parseInt(arr[0]);
                title = arr[1];

                for(int i = 2; i < arr.length; i++ )
                    tmp.add(arr[i]);

                id_numbers.add(id);
                listIds.put(id, title);
                lists.put(id, tmp);
            }
            sc.close();

        }

        else{
            lists  = new LinkedHashMap<>();
            listIds = new LinkedHashMap<>();
            id_numbers = new ArrayList<>();
        }
    }

    public synchronized void writeFile() throws Exception{
        FileWriter fstream = new FileWriter(path);
        PrintWriter out = new PrintWriter(fstream);

        if(lists.size() == 0){
            out.write("");
        }
        else {

            for(int key : listIds.keySet()){
                out.write(key + ":" + listIds.get(key) + ":");
                ArrayList<String> tmp = lists.get(key);
                for(int i = 0; i < tmp.size(); i++){

                    if(i == tmp.size()-1)
                        out.write(tmp.get(i));

                    else
                        out.write(tmp.get(i) + ":");
                }
                out.write("\n");

            }
        }
        fstream.close();
    }

    public synchronized void readAllFiles() throws Exception {
        users.readAll();
        readFile();
    }


}
