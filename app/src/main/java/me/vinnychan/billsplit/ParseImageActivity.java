package me.vinnychan.billsplit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.JsonElement;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.vinnychan.billsplit.model.Algorithmia;
import me.vinnychan.billsplit.model.Item;
import me.vinnychan.billsplit.model.Receipt;
import me.vinnychan.billsplit.model.Room;
import me.vinnychan.billsplit.model.User;


public class ParseImageActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;

    Room room;
    Receipt receipt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("");

        // imageView = (ImageView) findViewById(R.id.imageView);
        Intent i = getIntent();
        final byte[] byteArray = i.getByteArrayExtra("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        // imageView.setImageBitmap(bmp);

        Room room = (Room) i.getSerializableExtra("room");
        receipt = new Receipt(room);



        new AsyncTask<Void,Void,String>() {
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                progressDialog = ProgressDialog.show(ParseImageActivity.this, "Loading...",
                        "Tearing apart your receipt!", false, false);
            }
            @Override
            protected String doInBackground(Void... params) {
                return parseImage(byteArray);
            }
            @Override
            protected void onPostExecute(String text) {
                Log.w("Algorithmia", "Got response: " + text);
                setContentView(R.layout.activity_parse_image);


                String mockOCRText = "TAXES PER $100 EARNED\n\nSERVED EV:\nCANADA'S GOVERNMENTS\n\nGUEST\nAVERAGE rmlu\n\n4qu w. 1015\n\nINCOME TAXES $14.34\nPAYROLL TAXES $1\"\nSALES TAXES $7\nPROPERTY TAXES $4\nPROFIT TAXES $4\n\"SIN\" TAXES $1\nVEHICLE/FUEL TAXES $1\nOTHER TAXES $2\nTOTAL $44\n\nGDVERNMENTS TAKE $44\nFROM EVERY $100\nCANADIAN FAMILIES EARN\n\nwwwfraserinshlutearg\n\n";

                parseItems(receipt, text);
                for (Item i : receipt.getItems())
                    Log.d("Parser", i.toString());
                progressDialog.dismiss();

                Intent goListItems = new Intent(getBaseContext(), ListItemsActivity.class);
                goListItems.putExtra("Receipt", receipt);
                startActivity(goListItems);

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

    public static void parseItems(Receipt receipt, String ocrOutput) {
        Matcher m = Pattern.compile(".*\\d+\n").matcher(ocrOutput);
        while (m.find()) {
            String item = m.group(0);
            String itemName = item.substring(0, item.lastIndexOf(" ")).trim();
            String price = item.substring(item.lastIndexOf(" ") + 1).trim().replace("$", "");
            receipt.addItem(new Item(itemName, new BigDecimal(price)));
        }
    }

}
