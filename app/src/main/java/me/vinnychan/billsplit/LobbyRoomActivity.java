package me.vinnychan.billsplit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.math.BigDecimal;

import me.vinnychan.billsplit.model.Item;

public class LobbyRoomActivity extends AppCompatActivity {

    String username;


    private Firebase fireBaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_room);

        username = getIntent().getStringExtra("USERNAME");

        Firebase.setAndroidContext(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        fireBaseRef = new Firebase("https://billsplitdubhacks.firebaseio.com");

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        Item item = new Item("test", new BigDecimal(1.00));

        RVAdapter adapter = new RVAdapter(item.getItems());
        rv.setAdapter(adapter);

        System.out.println(fireBaseRef.child("Items2"));


        for (Item oitem : item.getItems()) {
            fireBaseRef.child(oitem.getDescription()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println(dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }

    }

}
