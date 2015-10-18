package me.vinnychan.billsplit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;

import me.vinnychan.billsplit.model.Algorithmia;


public class ParseImageActivity extends ActionBarActivity {

    private static final int CAMERA_REQUEST = 1888;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_image);
        // imageView = (ImageView) findViewById(R.id.imageView);
        final byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        // imageView.setImageBitmap(bmp);

        new AsyncTask<Void,Void,String>() {
            @Override
            protected void onPreExecute() {
                // Start loading spinner...
            }
            @Override
            protected String doInBackground(Void... params) {
                return parseImage(byteArray);
            }
            @Override
            protected void onPostExecute(String text) {
                
                Log.w("Algorithmia", "Got response: " + text);
                Toast.makeText(ParseImageActivity.this, "Got text " + text, Toast.LENGTH_LONG);
            }
        }.execute();
    }

    private String parseImage(final byte[] byteArray) {
        String algo = "ocr/RecognizeCharacters/0.2.0";
        String apiKey = "simQoo3Uq+EqGGfPAIXIYjUh3Fq1";

        // for the input file: use byte[] array object
        try {

            // Write file to data api
            Algorithmia.uploadFile("my/dubhacks/test.png", byteArray, apiKey);

            Algorithmia.AlgoResponse response = Algorithmia.call(algo, "\"data://my/dubhacks/test.png\"", apiKey);
            if(response.error == null) {
                JsonElement json = response.result;
                String text = json.getAsString();
                return text;
            } else {
                Log.e("Algorithmia", "Got error: " + response.error);
                return null;
            }
        } catch(Exception e) {
            Log.e("Algorithmia", "Algorithmia error", e);
            return null;
        }
    }
}
