package me.vinnychan.billsplit.model;

/**
 * Created by TING on 17-Oct-2015.
 */
public class User {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void setName(String nm) { name = nm; }

    @Override
    public String toString() {
        return name;
    }
}
