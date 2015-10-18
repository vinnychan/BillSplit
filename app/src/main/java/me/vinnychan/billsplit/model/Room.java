package me.vinnychan.billsplit.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by TING on 17-Oct-2015.
 * Represents a "room"/group of people who are splitting the bill amongst them
 */
public class Room {
    private User admin;
    private ArrayList<User> users;
    private Receipt receipt;

    public Room(User admin) {
        this.admin = admin;
        users = new ArrayList<User>();
    }

    public User getAdmin() { return admin; }

    public String getAdminID() { return admin.getID(); }

    public ArrayList<User> getUsers() { return users; }

    public Receipt getReceipt() { return receipt; }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public void addMember(User member) {
        users.add(member);
    }

    public void removeMember(User member) {
        users.remove(member);
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }
}
