package me.vinnychan.billsplit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

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

        final TextView mTextCondition = (TextView) findViewById(R.id.mTextCondition);

        fireBaseRef = new Firebase("https://billsplitdubhacks.firebaseio.com");

        fireBaseRef.child("Item 1").addValueEventListener(new ValueEventListener() {
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
