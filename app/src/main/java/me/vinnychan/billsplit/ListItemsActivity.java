package me.vinnychan.billsplit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
//import android.widget.ArrayAdapter;
//import android.widget.ListAdapter;

import android.widget.ListView;

import java.math.BigDecimal;
import java.util.ArrayList;

import me.vinnychan.billsplit.model.ConvertAdapter;
import me.vinnychan.billsplit.model.Item;
import me.vinnychan.billsplit.model.Receipt;
import me.vinnychan.billsplit.model.Room;
import me.vinnychan.billsplit.model.User;

public class ListItemsActivity extends AppCompatActivity {
    Receipt receipt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        setTitle("Receipt Items");
        Intent i = getIntent();
        receipt = (Receipt) i.getSerializableExtra("Receipt");
        populateList();
//        ListView lvMain = (ListView) findViewById(R.id.listView);
//        lvMain.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        final ListItemAdapter adapter = new ListItemAdapter(receipt.getItems());
        rv.setAdapter(adapter);
    }

    public void populateList() {
        receipt = new Receipt(new Room("roomname", new User("user")));
        Item i2 = new Item("descrip", new BigDecimal(0.43));
        Item i3 = new Item("descrip2", new BigDecimal(0.34));
        receipt.addItem(i2);
        receipt.addItem(i3);

        ArrayList<Item> items = receipt.getItems();
        ArrayList<String> descriptions = new ArrayList<String>();
        ArrayList<BigDecimal> prices = new ArrayList<BigDecimal>();
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                descriptions.add(item.getDescription());
                prices.add(item.getPrice());
            }
        }
        String[] descripArray = descriptions.toArray(new String[descriptions.size()]);

//        ArrayAdapter<String> adapter = new
//                ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, descripArray);

//        ListView view=
//                (ListView) findViewById(R.id.list);
//        view.setAdapter(adapter);
    }
}
