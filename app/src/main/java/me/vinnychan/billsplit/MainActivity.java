package me.vinnychan.billsplit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import com.firebase.client.Firebase;
import com.firebase.client.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import me.vinnychan.billsplit.model.Item;
import me.vinnychan.billsplit.model.Room;
import me.vinnychan.billsplit.model.User;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private Firebase firebaseRef;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        firebaseRef = new Firebase("https://billsplitdubhacks.firebaseio.com");

        setContentView(R.layout.activity_main);

        Button joinButton = (Button) findViewById(R.id.join_button);
        Button createButton = (Button) findViewById(R.id.create_button);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameEditText = (EditText) findViewById(R.id.username_edittext);
                String username = usernameEditText.getText().toString();
                Firebase userRef = firebaseRef.child("users");
                final User user = new User(username);

                firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        int newRoomName = Integer.parseInt(snapshot.child("numberOfRooms").getValue().toString()) + 1;
                        firebaseRef.child("numberOfRooms").setValue(Integer.toString(newRoomName));
                        Firebase roomRef = firebaseRef.child("rooms");
                        Room room = new Room(Integer.toString(newRoomName), user);

                        Map<String, Object> roomMap = new HashMap<String, Object>(4);
                        roomMap.put("name", Integer.toString(newRoomName));
                        roomMap.put("users", room.getUsers());
                        roomMap.put("admin", user);
                        Map<String, Map<String, Object>> roomFB = new HashMap<String, Map<String, Object>>();
                        roomFB.put(Integer.toString(newRoomName), roomMap);
                        roomRef.child(Integer.toString(newRoomName)).setValue(roomMap);

                        Toast.makeText(getBaseContext(), "Take a picture of your receipt!", Toast.LENGTH_LONG).show();

                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        // Write to file
                        File storage = Environment.getExternalStorageDirectory();
                        File image = new File(storage, "image.jpg");
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
                        cameraIntent.putExtra("roomID", Integer.toString(newRoomName));

                        Log.w("Algorithmia", "Starting activity for result ");
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameEditText = (EditText) findViewById(R.id.username_edittext);
                final String username = usernameEditText.getText().toString();
                if (username.equals("")) {
                    return;
                }

                EditText roomnameEditText = (EditText) findViewById(R.id.roomname_edittext);
                final String roomname = roomnameEditText.getText().toString();
                if (roomname.equals("")) {
                    return;
                }

                firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.child("rooms").hasChild(roomname)) return;

                        DataSnapshot roomRef = snapshot.child("rooms").child(roomname);
                        User roomAdmin = new User(roomRef.child("admin").getValue().toString());
                        String roomName = roomRef.child("name").getValue().toString();

                        Room room = new Room(roomName, roomAdmin);

                        ArrayList<User> users = new ArrayList<User>();
                        for (DataSnapshot u: roomRef.child("users").getChildren()) {
                            users.add(new User(u.child("name").getValue().toString()));
                        }
                        room.setUsers(users);

                        Set<Item> items = new HashSet<Item>();
                        for (DataSnapshot item: roomRef.child("items").getChildren()) {
                            String itemID = item.getKey();
                            String itemDescription = item.child("description").getValue().toString();
                            BigDecimal itemPrice = BigDecimal.valueOf(Double.parseDouble(item.child("price").getValue().toString().substring(1)));

                            Iterable<DataSnapshot> userProportns = item.child("userProportions").getChildren();
                            HashMap<User, BigDecimal> userProportions = new HashMap<User, BigDecimal>();
                            for (DataSnapshot up: userProportns) {
                                BigDecimal amt = new BigDecimal(Double.parseDouble(up.getValue().toString().substring(1)));
                                userProportions.put(new User(up.getKey()), amt); // TODO !!! NEED TO NOT CREATE USER, incorrect
                            }

                            Iterable<DataSnapshot> usersNotSpecified = item.child("usersWhoDidNotSpecify").getChildren();
                            HashSet<User> userProportionNotSpecified = new HashSet<User>();
                            for (DataSnapshot user: usersNotSpecified) {
                                userProportionNotSpecified.add(new User(user.child("name").getValue().toString())); // todo look into overriding user equal function
                            }

                            Iterable<DataSnapshot> usersAmtSpecified = item.child("usersWhoSpecifiedAmts").getChildren();
                            HashSet<User> userSpecifiedAmtProportion = new HashSet<User>();
                            for (DataSnapshot user: usersNotSpecified) {
                                userSpecifiedAmtProportion.add(new User(user.child("name").getValue().toString()));
                            }

                            Iterable<DataSnapshot> usersPerctSpecified = item.child("usersWhoSpecifiedPercentages").getChildren();
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
                        }
                        room.setItems(items);

                        room.addMember(new User(username));

                        firebaseRef.child("rooms").child(roomname).child("users").setValue(room.getUsers());
                        // todo update items in firebase since proportions changed after adding new member

                        Toast.makeText(getBaseContext(), "Welcome " + username + "!", Toast.LENGTH_LONG).show();

                        Intent goToChatRoom = new Intent(getBaseContext(), LobbyRoomActivity.class);
                        goToChatRoom.putExtra("USERNAME", username);
                        goToChatRoom.putExtra("ROOM", room.getName());
                        startActivity(goToChatRoom);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.w("Algorithmia", "Got activity result " + requestCode + " " + resultCode);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            File storage = Environment.getExternalStorageDirectory();
            File image = new File(storage, "image.jpg");

            byte[] byteArray = new byte[0];
            try {
                byteArray = FileUtils.readFileToByteArray(image);
                Intent goParseImage = new Intent(getBaseContext(), ParseImageActivity.class);
                goParseImage.putExtra("image", byteArray);
                startActivity(goParseImage);
            } catch (IOException e) {
                Log.e("Algorithmia", "Got exception", e);
                e.printStackTrace();
            }
        }
    }
}
