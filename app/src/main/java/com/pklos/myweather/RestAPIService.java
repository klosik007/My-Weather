package com.pklos.myweather;

import android.util.JsonReader;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class RestAPIService {
    static JsonReader getStream(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection)obj.openConnection();
        InputStream resBody;
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
           resBody = connection.getInputStream();
        }else
            return null;

        connection.disconnect();

        InputStreamReader resBodyReader = new InputStreamReader(resBody, "UTF-8");
        JsonReader jsonReader = new JsonReader(resBodyReader);

        return jsonReader;
    }

    static String readCity(JsonReader jsonReader) throws IOException{
        String city = "";
        try{
            jsonReader.beginObject();
            while(jsonReader.hasNext()){
                String key = jsonReader.nextName();
                if(key.equals("name")){
                    city = jsonReader.nextName();
                    break;
                }else{
                    jsonReader.skipValue();
                }
            }
        }finally{
            jsonReader.endObject();
            jsonReader.close();
        }

        return city;
    }

    static Map<String, Double> readMainParameters(JsonReader jsonReader) throws IOException {
        double temperature;
        double feels_like_temp;
        double temp_min;
        double temp_max;
        double pressure;
        double humidity;

        Map<String, Double> mainParams = new HashMap<>();

        try{
            jsonReader.beginObject();
            while(jsonReader.hasNext()){
                String key = jsonReader.nextName();
                if(key.equals("main")){
                    temperature = fetchObjectDoubleDataFromJSON(jsonReader, "temp");
                    feels_like_temp = fetchObjectDoubleDataFromJSON(jsonReader, "feels_like");
                    temp_min = fetchObjectDoubleDataFromJSON(jsonReader, "temp_min");
                    temp_max = fetchObjectDoubleDataFromJSON(jsonReader, "temp_max");
                    pressure = fetchObjectDoubleDataFromJSON(jsonReader, "pressure");
                    humidity = fetchObjectDoubleDataFromJSON(jsonReader, "humidity");

                    mainParams.put("temp", temperature);
                    mainParams.put("feels_like", feels_like_temp);
                    mainParams.put("temp_min", temp_min);
                    mainParams.put("temp_max", temp_max);
                    mainParams.put("pressure", pressure);
                    mainParams.put("humidity", humidity);
                }else{
                    jsonReader.skipValue();
                }
            }
        }finally{
            jsonReader.endObject();
            jsonReader.close();
        }

        return mainParams;
    }

    private static String fetchObjectStringDataFromJSON(JsonReader jsonReader, String jsonKey) throws IOException{
        String value = "";

        try{
            jsonReader.beginObject();
            while (jsonReader.hasNext()){
                String key = jsonReader.nextName();

                if(key.equals(jsonKey)){
                    value = jsonReader.nextString();
                    break;
                }
                else {
                    jsonReader.skipValue();
                }
            }
        }finally{
            jsonReader.endObject();
            jsonReader.close();
        }

        return value;
    }

    private static double fetchObjectDoubleDataFromJSON(JsonReader jsonReader, String jsonKey) throws IOException{
        double value = -1.00;

        try{
            jsonReader.beginObject();
            while (jsonReader.hasNext()){
                String key = jsonReader.nextName();

                if(key.equals(jsonKey)){
                    value = jsonReader.nextDouble();
                    break;
                }
                else {
                    jsonReader.skipValue();
                }
            }
        }finally{
            jsonReader.endObject();
            jsonReader.close();
        }

        return value;
    }
    static String fetchArrayDataFromJSON(JsonReader jsonReader, String jsonKey) throws Exception{
        String value = "";

        jsonReader.beginArray();
        while (jsonReader.hasNext()){
            String key = jsonReader.nextName();

            if(key.equals(jsonKey)){
                value = jsonReader.nextString();
                break;
            }
            else {
                jsonReader.skipValue();
            }
        }

        jsonReader.endArray();
        jsonReader.close();

        return value;
    }
//    static JSONObject fetchJSON(String url) throws Exception{
//        URL obj = new URL(url);
//        HttpURLConnection connection = (HttpURLConnection)obj.openConnection();
//
//        //connection.setRequestProperty("User-Agent", "res-api-app-my-weather");
//        connection.setRequestMethod("GET");
//        int responseCode = connection.getResponseCode();
//        if (responseCode == HttpURLConnection.HTTP_OK){
//            BufferedReader br = new BufferedReader(
//                    new InputStreamReader(connection.getInputStream())
//            );
//
//            String inputLine;
//            StringBuffer res = new StringBuffer();
//
//            while((inputLine = br.readLine()) != null){
//                res.append(inputLine);
//            }
//            br.close();
//            connection.disconnect();
//
//            JSONObject json = new JSONObject(res.toString());
//
//            return json;
//        }
//        else return null;
//    }
//
//    static String getDataFromJSON(JSONObject jsonObject, String name) throws Exception{
//        return jsonObject.getString(name);
//    }
}
