package me.vinnychan.billsplit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

                        Iterable<DataSnapshot> ds = snapshot.child("rooms").child(roomname).getChildren();
                        User admin = new User("");
                        String name = "";
                        ArrayList<User> users = new ArrayList<User>();
                        for (DataSnapshot s: ds) {
                            if (s.getKey() == "admin") {
                                admin = new User(s.getValue().toString());
                            } else if (s.getKey() == "name") {
                                name = s.getValue().toString();
                            } else {
                                for (DataSnapshot u: s.getChildren()) {
                                    users.add(new User(u.child("name").getValue().toString()));
                                }
                            }
                        }
                        Room room = new Room(name, admin);
                        room.setUsers(users);

                        room.addMember(new User(username));
//                        HashMap<String, Object> newUsersList = new HashMap<String, Object>(1);
//                        newUsersList.put("users", room.getUsers());
                        firebaseRef.child("rooms").child(roomname).child("users").setValue(room.getUsers());

                        Toast.makeText(getBaseContext(), "Welcome " + username + "!", Toast.LENGTH_LONG).show();

                        Intent goToChatRoom = new Intent(getBaseContext(), LobbyRoomActivity.class);
                        goToChatRoom.putExtra("USERNAME", username);
//                goToChatRoom.putExtra("ROOM", room);
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
