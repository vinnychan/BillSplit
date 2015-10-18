package me.vinnychan.billsplit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button joinButton = (Button) findViewById(R.id.join_button);
        Button createButton = (Button) findViewById(R.id.create_button);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Take a picture of your receipt!", Toast.LENGTH_LONG).show();

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameEditText = (EditText) findViewById(R.id.username_edittext);
                String username = usernameEditText.getText().toString();

                if (username.equals("")) {
                    return;
                }

                Toast.makeText(getBaseContext(), "Welcome " + username + "!", Toast.LENGTH_LONG).show();

                Intent goToChatRoom = new Intent(getBaseContext(), LobbyRoomActivity.class);
                goToChatRoom.putExtra("USERNAME", username);
                startActivity(goToChatRoom);
            }
        });
    }

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Intent goParseImage = new Intent(getBaseContext(), ParseImageActivity.class);
            goParseImage.putExtra("image", byteArray);
            startActivity(goParseImage);
        }
    }
}
