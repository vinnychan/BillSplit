package me.vinnychan.billsplit;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import me.vinnychan.billsplit.model.Item;

public class LobbyRoomActivity extends AppCompatActivity {

    String username;
    String room;

    private Firebase firebaseRef;
//    SeekBar seekBar;
//    TextView amountPaying;
//    TextView percentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_room);

        username = getIntent().getStringExtra("USERNAME");
        room = getIntent().getStringExtra("ROOM");

//        seekBar = (SeekBar) findViewById(R.id.SeekBarId);
//        amountPaying = (TextView) findViewById(R.id.amount_paying);
//        percentage = (TextView) findViewById(R.id.percentage);

        Snackbar.make(findViewById(R.id.lobby_content), "Welcome " + username + "!", Snackbar.LENGTH_LONG).show();
        Firebase.setAndroidContext(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseRef = new Firebase("https://billsplitdubhacks.firebaseio.com");

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        final List<Item> items = new ArrayList<>();

        final RVAdapter adapter = new RVAdapter(items, username, room);
        rv.setAdapter(adapter);


        firebaseRef.child("Items2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                items.clear();
                for (DataSnapshot itemObject : dataSnapshot.getChildren()) {
                    BigDecimal price;
//                    if (itemObject.child("price").getValue() instanceof Double) {
//                        price = new BigDecimal((double) itemObject.child("price").getValue());
//
//                    } else {
//                        price = new BigDecimal(0);
//                    }
//
//                    if (itemObject.child("price").getValue() instanceof Long) {
//                        price = new BigDecimal((long) itemObject.child("price").getValue());
//                    }

                    price = new BigDecimal(itemObject.child("price").getValue().toString());
                    Item i = new Item(itemObject.getKey(), price);
                    items.add(i);

                    System.out.println(itemObject.getValue().getClass().getName());


                }
                adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//            public void onStopTrackingTouch(SeekBar bar) {
//                int value = bar.getProgress(); // the value of the seekBar progress
//            }
//
//            public void onStartTrackingTouch(SeekBar bar) {
//
//            }
//
//            public void onProgressChanged(SeekBar bar,
//                                          int paramInt, boolean paramBoolean) {
//                percentage.setText("" + paramInt + "%"); // here in textView the percent will be shown
//            }
//        });




    }

}

