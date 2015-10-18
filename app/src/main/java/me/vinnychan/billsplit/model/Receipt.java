package me.vinnychan.billsplit.model;

import android.media.Image;

import java.util.ArrayList;

/**
 * Created by TING on 17-Oct-2015.
 */
public class Receipt {
    private Image photo;
    private ArrayList<Item> items;
    private String roomID;

    public Receipt(Image photo, Room room) {
        this.photo = photo;
        items = new ArrayList<>();
        roomID = room.getId();
    }

    public int getNumItems() { return items.size(); }

    public Image getPhoto() { return photo; }

    public ArrayList<Item> getItems() { return items; }

    public void setPhoto(Image photo) { this.photo = photo; }

    public void addItem(Item item) { items.add(item); }

    public void removeItem(Item item) { items.remove(item); }

    private String createNewId() {
        return "";
        //todo firebase stuff
    }
}
