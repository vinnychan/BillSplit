package me.vinnychan.billsplit.model;

import android.media.Image;

import java.util.ArrayList;

/**
 * Created by TING on 17-Oct-2015.
 */
public class Receipt {
    private String id;
    private Image photo;
    private ArrayList<Item> items;

    public int getNumItems() {
        return items.size();
    }

    public String getID() {
        return id;
    }
}
