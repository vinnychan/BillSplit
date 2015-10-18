package me.vinnychan.billsplit.model;

import android.graphics.Bitmap;
import android.media.Image;

import java.util.ArrayList;

/**
 * Created by TING on 17-Oct-2015.
 */
public class Receipt {
    private ArrayList<Item> items;
    private String roomID;

    public Receipt( Room room) {
        items = new ArrayList<>();
    }

    public int getNumItems() { return items.size(); }

    public ArrayList<Item> getItems() { return items; }

    public void addItem(Item item) { items.add(item); }

    public void removeItem(Item item) { items.remove(item); }
}
