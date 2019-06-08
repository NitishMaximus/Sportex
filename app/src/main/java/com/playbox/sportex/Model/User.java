package com.playbox.sportex.Model;

import java.util.HashMap;

public class User {
    private String Name;
    private String Password;
    private String Email;
    private HashMap<String, Boolean> JoinedGame;

    public User(){

    }
    public User(String Name, String Password, String Email) {
        this.Name = Name;
        this.Password = Password;
        this.Email = Email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public HashMap<String, Boolean> getJoinedGame() {
        return JoinedGame;
    }

    public void setJoinedGame(HashMap<String, Boolean> JoinedGame) {
        this.JoinedGame = JoinedGame;
    }

}
