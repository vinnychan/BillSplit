package me.vinnychan.billsplit.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by TING on 17-Oct-2015.
 * Represents a "room"/group of people who are splitting the bill amongst them
 */
public class Room {
    private String name;
    private User admin;
    private ArrayList<User> users;
    private Receipt receipt;
    private Set<Item> items;

    public Room(String name, User admin) {
        this.admin = admin;
        this.name = name;
        users = new ArrayList<User>();
        users.add(admin);
        items = new HashSet<Item>();
    }

    public String getName() { return name; }

    public User getAdmin() { return admin; }

    public ArrayList<User> getUsers() { return users; }

    public Receipt getReceipt() { return receipt; }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public void addMember(User member) {
        users.add(member);
        for (Item i: items) {
            i.addUser(member);
        }
    }

    public void removeMember(User member) {
        if (!member.equals(admin))
            users.remove(member);
        // todo
        for (Item i: items) {
            i.removeUser(member);
        }
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public void setItems(Set<Item> items) { this.items = items; }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void addItem(Item i) {
        items.add(i);
    }

    public void removeItem(Item i) {
        items.remove(i);
    }
}
