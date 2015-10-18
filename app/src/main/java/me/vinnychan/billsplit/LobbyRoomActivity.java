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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.vinnychan.billsplit.model.Item;
import me.vinnychan.billsplit.model.User;

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


        firebaseRef.child("rooms").child(room).child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                items.clear();
                for (DataSnapshot itemObject : dataSnapshot.getChildren()) {

                    String itemID = itemObject.getKey();
                    String itemDescription = itemObject.child("description").getValue().toString();
                    BigDecimal itemPrice = BigDecimal.valueOf(Double.parseDouble(itemObject.child("price").getValue().toString().substring(1)));

                    Iterable<DataSnapshot> userProportns = itemObject.child("userProportions").getChildren();
                    HashMap<User, BigDecimal> userProportions = new HashMap<User, BigDecimal>();
                    for (DataSnapshot up: userProportns) {
                        BigDecimal amt = new BigDecimal(Double.parseDouble(up.getValue().toString().substring(1)));
                        userProportions.put(new User(up.getKey()), amt); // TODO !!! NEED TO NOT CREATE USER, incorrect
                    }

                    Iterable<DataSnapshot> usersNotSpecified = itemObject.child("usersWhoDidNotSpecify").getChildren();
                    HashSet<User> userProportionNotSpecified = new HashSet<User>();
                    for (DataSnapshot user: usersNotSpecified) {
                        userProportionNotSpecified.add(new User(user.child("name").getValue().toString())); // todo look into overriding user equal function
                    }

                    Iterable<DataSnapshot> usersAmtSpecified = itemObject.child("usersWhoSpecifiedAmts").getChildren();
                    HashSet<User> userSpecifiedAmtProportion = new HashSet<User>();
                    for (DataSnapshot user: usersNotSpecified) {
                        userSpecifiedAmtProportion.add(new User(user.child("name").getValue().toString()));
                    }

                    Iterable<DataSnapshot> usersPerctSpecified = itemObject.child("usersWhoSpecifiedPercentages").getChildren();
                    HashMap<User, Integer> specifiedPercentageProportions = new HashMap<User, Integer>();
                    for (DataSnapshot userPerctg: usersPerctSpecified) {
                        User user = new User(userPerctg.getKey());
                        int percentage = Integer.parseInt(userPerctg.getValue().toString());
                        specifiedPercentageProportions.put(user, percentage);
                    }

                    Item i = new Item(itemDescription, itemPrice);
                    i.setID(itemID);
                    i.setUserProportions(userProportions);
                    i.setUserProportionNotSpecified(userProportionNotSpecified);
                    i.setUserSpecifiedAmtProportion(userSpecifiedAmtProportion);
                    i.setSpecifiedPercentageProportions(specifiedPercentageProportions);

                    items.add(i);


//                    BigDecimal price;
////                    if (itemObject.child("price").getValue() instanceof Double) {
////                        price = new BigDecimal((double) itemObject.child("price").getValue());
////
////                    } else {
////                        price = new BigDecimal(0);
////                    }
////
////                    if (itemObject.child("price").getValue() instanceof Long) {
////                        price = new BigDecimal((long) itemObject.child("price").getValue());
////                    }
//
//                    // NOTE: NOT ALL ITEM FIELDS ARE PARSED BELOW!
//                    price = new BigDecimal(itemObject.child("price").getValue().toString().substring(1));
//                    String descrip = itemObject.child("description").getValue().toString();
//                    Item i = new Item(itemObject.getKey(), price);
//                    items.add(i);

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

