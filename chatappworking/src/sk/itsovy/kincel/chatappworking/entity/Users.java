package sk.itsovy.kincel.chatappworking.entity;

public class Users {
    private int id;
    private String login;
    private String password;

    public Users(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}