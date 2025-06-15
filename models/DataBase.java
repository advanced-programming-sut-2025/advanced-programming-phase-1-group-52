package models;

import java.io.*;
import java.util.ArrayList;

public class DataBase {
    private static final String FILE_PATH = "users.dat";

    public static ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            users = (ArrayList<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
        }
        return users;
    }

    public static void saveUsers(ArrayList<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isUsernameTaken(String username) {
        ArrayList<User> users = loadUsers();
        return users.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    public static void addUser(User user) {
        ArrayList<User> users = loadUsers();
        users.add(user);
        saveUsers(users);
    }

    public static User getUserByUsername(String username) {
        ArrayList<User> users = loadUsers();
        return users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
    }
}