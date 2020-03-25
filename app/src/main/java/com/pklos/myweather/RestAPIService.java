package com.pklos.myweather;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

class RestAPIService {
    static Reader getStream(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection)obj.openConnection();
        InputStream resBody;
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
           resBody = connection.getInputStream();
        } else
            return null;

        connection.disconnect();

        Reader resBodyReader = new InputStreamReader(resBody, "UTF-8");

        return resBodyReader;
    }

    static InputStreamReader getStream2(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection)obj.openConnection();
        InputStream resBody;
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            resBody = connection.getInputStream();
        } else
            return null;

        connection.disconnect();

//        JsonReader resBodyReader = new JsonReader(new InputStreamReader(resBody, "UTF-8"));
        InputStreamReader resBodyReader = new InputStreamReader(resBody, "UTF-8");
        return resBodyReader;
    }

    public static void readJsonStream(JsonReader in) throws IOException {
        int message = -1;
        try {
            in.beginObject();
            while (in.hasNext()){
                String name = in.nextName();
                if (name.equals("message")){
                    message = in.nextInt();
                    break;
                }else{
                    in.skipValue();
                }
            }
            Log.d("message", String.valueOf(message));
        } finally {
            in.endObject();
            in.close();
        }
    }
}
