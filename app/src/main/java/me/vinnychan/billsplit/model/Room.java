package me.vinnychan.billsplit.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by TING on 17-Oct-2015.
 * Represents a "room"/group of people who are splitting the bill amongst them
 */
public class Room {
    private String id;
    private User admin;
    private Collection<User> users;
    private Receipt receipt;

    public Room(User admin) {
        this.admin = admin;
        users = new ArrayList<User>();
    }

    public void addReceipt(Receipt receipt) {
        this.receipt = receipt;
    }
}
