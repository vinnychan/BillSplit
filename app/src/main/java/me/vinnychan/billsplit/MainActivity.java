package me.vinnychan.billsplit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button joinButton = (Button) findViewById(R.id.join_button);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameEditText = (EditText) findViewById(R.id.username_edittext);
                String username = usernameEditText.getText().toString();

                if (username.equals("")) {
                    return;
                }

                Toast.makeText(getBaseContext(), "Welcome " + username + "!", Toast.LENGTH_LONG).show();

                // To change to a new activity, we do it with "Intents"
                Intent goToLobbyRoom = new Intent(getBaseContext(), LobbyRoomActivity.class);
                goToLobbyRoom.putExtra("USERNAME", username);
                startActivity(goToLobbyRoom);

            }
        });
    }
}
