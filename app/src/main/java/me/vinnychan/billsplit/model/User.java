package me.vinnychan.billsplit.model;

/**
 * Created by TING on 17-Oct-2015.
 */
public class User {
    private String id;
    private String name;

    public User(String name) {
        id = ""; //todo
        this.name = name;
    }

    public String getID() { return id; }

    public String getName() { return name; }

    public void setName(String nm) { name = nm; }
}
