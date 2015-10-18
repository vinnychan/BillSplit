package me.vinnychan.billsplit.model;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.apache.commons.io.IOUtils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

// depends on:
// compile 'commons-io:commons-io:2.4'
// compile 'com.google.code.gson:gson:2.3.1'


public class Algorithmia {

    // Expects algo like "nlp/summarizer"
    public static AlgoResponse call(String algo, String inputJson, String apiKey) throws IOException {
        // Construct HTTP request
        URL url = new URL("https://api.algorithmia.com/v1/algo/" + algo + "?timeout=3000");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Authorization", "Simple " + apiKey);

        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();

        try {
            // Write json to output stream
            os.write(inputJson.getBytes());
            os.close();
            conn.connect();

            int status = conn.getResponseCode();
            if(status == 200) {
                // Parse response
                InputStream is = conn.getInputStream();
                String result = IOUtils.toString(is);
                AlgoResponse response = new Gson().fromJson(result, AlgoResponse.class);
                return response;
            } else {
                throw new IOException("Status code not ok");
            }
        } finally {
            conn.disconnect();
        }
    }

    // Expects algo like "nlp/summarizer"
    public static AlgoResponse call(String algo, byte[] input, String apiKey) throws IOException {
        // Construct HTTP request
        URL url = new URL("https://api.algorithmia.com/v1/algo/" + algo + "?timeout=3000");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-type", "application/octet-stream");
        conn.setRequestProperty("Authorization", "Simple " + apiKey);

        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();

        try {
            // Write json to output stream
            os.write(input);
            os.close();
            conn.connect();

            int status = conn.getResponseCode();
            if(status == 200) {
                // Parse response
                InputStream is = conn.getInputStream();
                String result = IOUtils.toString(is);
                AlgoResponse response = new Gson().fromJson(result, AlgoResponse.class);
                return response;
            } else {
                throw new IOException("Status code not ok: " + status + " " + url);
            }
        } finally {
            conn.disconnect();
        }
    }

    public static void uploadFile(String dataUrl, byte[] file, String apiKey) throws IOException {
        // Construct HTTP request
        URL url = new URL("https://api.algorithmia.com/v1/data/" + dataUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-type", "application/octet-stream");
        conn.setRequestProperty("Authorization", "Simple " + apiKey);

        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();

        try {
            // Write file to output stream
//            FileUtils.copyFile(file, os);
            os.write(file);
            os.close();

            conn.connect();

            int status = conn.getResponseCode();
            if(status != 200) {
                throw new IOException("Status code not ok: " + status);
            }
        } finally {
            conn.disconnect();
        }
    }

    public static class AlgoResponse {
        public JsonElement result;
        public JsonElement error;
    }
}
