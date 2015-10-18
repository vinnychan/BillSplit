package me.vinnychan.billsplit.model;

import java.io.Serializable;

/**
 * Created by TING on 17-Oct-2015.
 */
public class User implements Serializable {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void setName(String nm) { name = nm; }
}
