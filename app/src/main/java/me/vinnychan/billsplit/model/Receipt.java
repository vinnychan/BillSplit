package me.vinnychan.billsplit.model;

import android.graphics.Bitmap;
import android.media.Image;

import java.util.ArrayList;

/**
 * Created by TING on 17-Oct-2015.
 */
public class Receipt {
    private Bitmap photo;
    private ArrayList<Item> items;
    private String roomID;

    public Receipt(Bitmap photo, Room room) {
        this.photo = photo;
        items = new ArrayList<>();
    }

    public int getNumItems() { return items.size(); }

    public Bitmap getPhoto() { return photo; }

    public ArrayList<Item> getItems() { return items; }

    public void setPhoto(Bitmap photo) { this.photo = photo; }

    public void addItem(Item item) { items.add(item); }

    public void removeItem(Item item) { items.remove(item); }
}
