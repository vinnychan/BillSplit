package me.vinnychan.billsplit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.vinnychan.billsplit.model.Item;
import me.vinnychan.billsplit.model.Receipt;
import me.vinnychan.billsplit.model.Room;
import me.vinnychan.billsplit.model.User;


public class ParseImageActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_image);
        imageView = (ImageView) findViewById(R.id.imageView);
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imageView.setImageBitmap(bmp);


        // assume we have our OCR output, here we parse the output string
        Room mockRoom = new Room(new User("nerd"));
        Receipt receipt = new Receipt(bmp, mockRoom);
        String mockOcrOutput = "TAXES PER $100 EARNED\n\nSERVED EV:\nCANADA'S GOVERNMENTS\n\nGUEST\nAVERAGE rmlu\n\n4qu w. 1015\n\nINCOME TAXES $14.34\nPAYROLL TAXES $1\"\nSALES TAXES $7\nPROPERTY TAXES $4\nPROFIT TAXES $4\n\"SIN\" TAXES $1\nVEHICLE/FUEL TAXES $1\nOTHER TAXES $2\nTOTAL $44\n\nGDVERNMENTS TAKE $44\nFROM EVERY $100\nCANADIAN FAMILIES EARN\n\nwwwfraserinshlutearg\n\n";
        parseItems(receipt, mockOcrOutput);
        for (Item i : receipt.getItems())
            Log.d("Parser", i.toString());
    }


    public static void parseItems(Receipt receipt, String ocrOutput) {
        Matcher m = Pattern.compile(".*\\d+\n").matcher(ocrOutput);
        while (m.find()) {
            String item = m.group(0);
            String itemName = item.substring(0, item.lastIndexOf(" ")).trim();
            String price = item.substring(item.lastIndexOf(" ")+1).trim().replace("$", "");
            receipt.addItem(new Item(itemName, new BigDecimal(price)));
        }
    }

}
