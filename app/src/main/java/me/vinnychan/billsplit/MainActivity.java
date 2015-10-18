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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

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
                // Write to file
                File storage = Environment.getExternalStorageDirectory();
                File image = new File(storage, "image.jpg");
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));

                Log.w("Algorithmia", "Starting activity for result ");
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
