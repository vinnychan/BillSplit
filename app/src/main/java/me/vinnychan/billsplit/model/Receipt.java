package me.vinnychan.billsplit.model;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by TING on 17-Oct-2015.
 */
public class Receipt implements Parcelable {
    private ArrayList<Item> items;
    private String roomID;

    public Receipt(Room room) {
        items = new ArrayList<>();
    }

    public int getNumItems() { return items.size(); }

    public ArrayList<Item> getItems() { return items; }

    public void addItem(Item item) { items.add(item); }

    public void removeItem(Item item) { items.remove(item); }

    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
        Item[] arr = items.toArray( new Item[items.size()]);
        out.writeArray(arr);
        out.writeString(roomID);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Receipt> CREATOR = new Parcelable.Creator<Receipt>() {
        public Receipt createFromParcel(Parcel in) {
            return new Receipt(in);
        }

        public Receipt[] newArray(int size) {
            return new Receipt[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Receipt(Parcel in) {
        Item[] arr = new Item[100];
        in.readArray(Item.class.getClassLoader());
        items = new ArrayList<Item>(Arrays.asList(arr));
        roomID = in.readString();
    }
}
