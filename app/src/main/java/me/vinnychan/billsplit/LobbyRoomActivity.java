package me.vinnychan.billsplit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.math.BigDecimal;

import me.vinnychan.billsplit.model.Item;

public class LobbyRoomActivity extends AppCompatActivity {

    String username;

    private Firebase firebaseRef;

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

        final TextView mTextCondition = (TextView) findViewById(R.id.mTextCondition);

        firebaseRef = new Firebase("https://billsplitdubhacks.firebaseio.com");

        Firebase itemRef = firebaseRef.child("items");
        Item sampleItem = new Item("sample item2", new BigDecimal(100));
        itemRef.push().setValue(sampleItem);

        firebaseRef.child("Item 1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue());
                mTextCondition.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
