package com.playbox.sportex.Model;

public class User {
    private String Name;
    private String Password;
    private String Email;

    public User(){

    }
    public User(String name, String password, String email) {
        Name = name;
        Password = password;
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Name = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
