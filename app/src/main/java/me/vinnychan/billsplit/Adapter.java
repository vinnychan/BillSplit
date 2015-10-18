package me.vinnychan.billsplit;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import java.util.ArrayList;

import me.vinnychan.billsplit.model.Item;


public class Adapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Item> objects;

    Adapter(Context context, ArrayList<Item> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.activity_list_items, parent, false);
        }

        Item p = objects.get(position);

        ((TextView) view.findViewById(R.id.tvDescr)).setText(p.getDescription());
        ((TextView) view.findViewById(R.id.tvPrice)).setText(p.getPrice() + "");
//        ((ImageView) view.findViewById(R.id.edit_button)).setImageResource(p.image);

        return view;
    }

//    Item getProduct(int position) {
//        return ((Item) getItem(position));
//    }
}
