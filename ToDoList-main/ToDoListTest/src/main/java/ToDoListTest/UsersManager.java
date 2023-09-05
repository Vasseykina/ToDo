package ToDoListTest;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;


public class UsersManager {

    private final String idsPath = "/Users/gratchuvalsky/Desktop/apache-tomcat-8.5.82/webapps/ToDoList/dataBase/UsersLists.txt";
    private final String AuthPath = "/Users/gratchuvalsky/Desktop/apache-tomcat-8.5.82/webapps/ToDoList/dataBase/UsersAuth.txt";
    private final String sharedPath = "/Users/gratchuvalsky/Desktop/apache-tomcat-8.5.82/webapps/ToDoList/dataBase/SharedLists.txt";


    private LinkedHashMap<String, String> usersAuth;
    private LinkedHashMap<String, ArrayList<Integer>> usersLists;
    private LinkedHashMap<String, ArrayList<Integer>> sharedLists;
    private LinkedHashMap<String, ArrayList<Integer>> sharedListsOwners;


    public UsersManager() throws Exception {
        usersLists = new LinkedHashMap<>();
        usersAuth = new LinkedHashMap<>();
        sharedLists = new LinkedHashMap<>();
        sharedListsOwners = new LinkedHashMap<>();

    }


    public LinkedHashMap<String, String> getUsersAuth(){
        return usersAuth;
    }

    public LinkedHashMap<String, ArrayList<Integer>> getUsersLists(){
        return usersLists;
    }

    public LinkedHashMap<String, ArrayList<Integer>> getSharedLists(){
        return sharedLists;
    }

    public LinkedHashMap<String, ArrayList<Integer>> getSharedListsOwners(){
        return sharedListsOwners;
    }


    public boolean checkAuth(String name, String password){
        if(usersAuth.containsKey(name)){
            if(usersAuth.get(name).equals(password)){
                return true;
            }
        }
        return false;
    }


    public synchronized void addUsersList(String user, int id) throws Exception {
        if(!usersLists.keySet().equals(usersAuth.keySet())) {
            for (String key : usersAuth.keySet()) {
                ArrayList<Integer> a = new ArrayList<>();
                if (!usersLists.containsKey(key))
                    usersLists.put(key, a);
            }
        }
        ArrayList<Integer> tmp = usersLists.get(user);
        if(tmp == null)
            tmp = new ArrayList<>();

        tmp.add(id);
        usersLists.put(user, tmp);

    }

    public synchronized void deleteUsersList(String user, int id){
        if(!usersLists.keySet().equals(usersAuth.keySet())) {
            for (String key : usersAuth.keySet()) {
                ArrayList<Integer> a = new ArrayList<>();
                if (!usersLists.containsKey(key))
                    usersLists.put(key, a);
            }
        }
        ArrayList<Integer> tmp = usersLists.get(user);
        tmp.remove(Integer.valueOf(id));
        usersLists.put(user, tmp);
    }

    public synchronized void readIdsFile() throws Exception{
        File list = new File(idsPath);

        if(list.length() != 0) {

            usersLists = new LinkedHashMap<>();

            Scanner sc = new Scanner(list);

            String user;
            ArrayList<Integer> ids ;

            while (sc.hasNextLine()) {
                ids = new ArrayList<>();
                String[] arr = sc.nextLine().split(":");
                user = arr[0];

                if(arr.length > 1) {
                    if (!arr[1].trim().isEmpty()) {
                        for (int i = 1; i < arr.length; i++)
                            ids.add(Integer.parseInt(arr[i]));

                    }
                }
                usersLists.put(user, ids);

            }
            sc.close();

        }

        else{
            usersLists = new LinkedHashMap<>();
        }
    }





    public synchronized void writeIdsFile() throws Exception{
        StringBuilder sb = new StringBuilder();
        FileWriter fstream = new FileWriter(idsPath);
        PrintWriter out = new PrintWriter(fstream);

        if(usersLists.size() == 0 && usersAuth.size() == 0){
            out.write("".trim());
        }

        else {
            if(!usersLists.keySet().equals(usersAuth.keySet())){
                for(String key : usersAuth.keySet()){
                    ArrayList<Integer> a = new ArrayList<>();
                    if(!usersLists.containsKey(key))
                        usersLists.put(key, a);
                }


            }

            for(String key : usersLists.keySet()){
                sb.append(key + ":");
                //out.write(key + ":");
                ArrayList<Integer> tmp = usersLists.get(key);
                if(tmp.size() != 0) {
                    for (int i = 0; i < tmp.size(); i++) {

                        if (i == tmp.size() - 1)
                            sb.append(tmp.get(i));
                            // out.write(tmp.get(i));

                        else
                            sb.append(tmp.get(i) + ":");
                        //out.write(tmp.get(i) + ":");
                    }
                }
                out.write(sb.toString());
                out.write("\n");
                sb = new StringBuilder();

            }
        }
        fstream.close();
    }

    public synchronized void readAuthFile() throws Exception{
        File list = new File(AuthPath);

        if(list.length() != 0) {
            usersAuth = new LinkedHashMap<>();

            Scanner sc = new Scanner(list);

            String user;
            String password;

            while (sc.hasNextLine()) {
                String[] arr = sc.nextLine().split(":");
                user = arr[0];
                password = arr[1];

                usersAuth.put(user, password);

            }
            sc.close();

        }

        else{
            usersAuth = new LinkedHashMap<>();
        }
    }

    public synchronized void readSharedFile() throws Exception{
        File list = new File(sharedPath);

        if(list.length() != 0) {

            sharedLists = new LinkedHashMap<>();
            sharedListsOwners = new LinkedHashMap<>();
            Scanner sc = new Scanner(list);

            String user;
            String owner;

            while (sc.hasNextLine()) {
                ArrayList<Integer> sharedIds = new ArrayList<>() ;
                ArrayList<Integer> ownersIds = new ArrayList<>();
                ArrayList<String> owners = new ArrayList<>();
                ArrayList<Integer> ids = new ArrayList<>();

                String[] arr = sc.nextLine().split(":");
                user = arr[0];


                if(arr.length > 1) {
                    if (!arr[1].trim().isEmpty()) {
                        for (int i = 1; i < arr.length; i++) {
                            String[] pair = arr[i].split("=");
                            ids.add(Integer.parseInt(pair[1]));
                            if(!sharedListsOwners.containsKey(pair[0])){
                                ArrayList<Integer> tmp = new ArrayList<>();
                                tmp.add(Integer.parseInt(pair[1]));
                                sharedListsOwners.put(pair[0], tmp);
                            }
                            else{
                                ArrayList<Integer> tmp = sharedListsOwners.get(pair[0]);
                                if(!tmp.contains(Integer.parseInt(pair[1]))) {
                                    tmp.add(Integer.parseInt(pair[1]));
                                    sharedListsOwners.put(pair[0], tmp);

                                }
                            }
                        }
                    }
                }
                sharedLists.put(user,ids);
            }
            sc.close();

        }

        else{
            sharedLists = new LinkedHashMap<>();
            sharedListsOwners = new LinkedHashMap<>();
        }
    }

    public synchronized void writeSharedFile() throws Exception{
        StringBuilder sb = new StringBuilder();
        FileWriter fstream = new FileWriter(sharedPath);
        PrintWriter out = new PrintWriter(fstream);

        if(sharedLists.size() == 0 && usersAuth.size() == 0){
            out.write("".trim());
        }

        else {
            if(!sharedLists.keySet().equals(usersAuth.keySet())){
                for(String key : usersAuth.keySet()){
                    ArrayList<Integer> a = new ArrayList<>();
                    if(!sharedLists.containsKey(key))
                        sharedLists.put(key, a);
                }


            }

            if(!sharedListsOwners.keySet().equals(usersAuth.keySet())){
                for(String key : usersAuth.keySet()){
                    ArrayList<Integer> a = new ArrayList<>();
                    if(!sharedListsOwners.containsKey(key))
                        sharedListsOwners.put(key, a);
                }


            }

            for(String key : sharedLists.keySet()){
                sb.append(key + ":");
                //out.write(key + ":");
                ArrayList<Integer> tmp = sharedLists.get(key);
                if(tmp.size() != 0) {
                    for (int i = 0; i < tmp.size(); i++) {
                        for(String name : sharedListsOwners.keySet()){
                            ArrayList<Integer> ids = sharedListsOwners.get(name);
                            if(ids.contains(tmp.get(i))){
                                if (i == tmp.size() - 1)
                                    sb.append(name + "=" + tmp.get(i));
                                else
                                    sb.append(name + "=" + tmp.get(i) + ":");

                            }
                        }
                    }
                }
                out.write(sb.toString());
                out.write("\n");
                sb = new StringBuilder();

            }
        }
        fstream.close();
    }

    public synchronized void setSharedLists(String user, ArrayList<Integer> ids){
        sharedLists.put(user, ids);
    }

    public synchronized void setSharedListsOwners(String user, ArrayList<Integer> ids){
        sharedListsOwners.put(user, ids);
    }


    public synchronized void readAll() throws Exception {
        readAuthFile();
        readSharedFile();
        readIdsFile();
    }
    public synchronized void writeAll() throws Exception {
        writeSharedFile();
        writeIdsFile();
    }

}
