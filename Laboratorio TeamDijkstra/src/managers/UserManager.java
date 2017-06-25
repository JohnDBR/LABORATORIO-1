/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import classes.User;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author john
 */
public class UserManager {

    private ArrayList<User> users = new ArrayList<>();

    public UserManager() {
        read();
    }

    //ESSENTIAL OPERATIONS
    public String logIn(String username, String password) {
        if (getUser(username) != null) {
            if (getUser(username).getPassword().equals(password)) {
                return getUser(username).getLevel();
            }
        }
        return "NONE";
    }

    //CREATE
    public boolean createUser(String username, String password, String level) {
        boolean repeatedUser = false;
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                repeatedUser = true;
                break;
            }
        }
        if (!repeatedUser) {
            users.add(new User(username, password, level));
            save();
            return true;
        } else {
            read();
        }
        return false;
    }

    //READ
    public User getUser(String username) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equalsIgnoreCase(username)) {
                return users.get(i);
            }
        }
        return null;
    }

    //UPDATE
    public boolean updateUser(String username, String password, String repeatPassword, String level) {
        if (getUser(username) != null) {
            if (password.equals(repeatPassword)) {
                getUser(username).setPassword(password);
                getUser(username).setLevel(level);
                save();
                return true;
            }
        }
        return false;
    }

    //DELETE
    public boolean deleteUser(String username) {
        if (getUser(username) != null) {
            users.remove(getUser(username));
            save();
            return true;
        }
        return false;
    }

    //STORRER
    public void save() {

        try {
            // Write to disk with FileOutputStream
            FileOutputStream f_out = new FileOutputStream("./database/user,load.data");

            // Write object with ObjectOutputStream
            ObjectOutputStream obj_out = new ObjectOutputStream(f_out);

            // Write object out to disk
            obj_out.writeObject(users);
        } catch (Exception ex) {
            System.out.println("Datos  no guardados!");
        }

    }

    public void read() {
        try {
            // Read from disk using FileInputStream
            FileInputStream f_in = new FileInputStream("./database/user,load.data");

            // Read object using ObjectInputStream
            ObjectInputStream obj_in = new ObjectInputStream(f_in);

            // Read an object
            users =  (ArrayList<User>) obj_in.readObject();
        } catch (Exception ex) {
            System.out.println("No ha sido creado el archivo a cargar");
            users = new ArrayList<>();
        }
    }

    //GETTERS SETTERS
    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

}
