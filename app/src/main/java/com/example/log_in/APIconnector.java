package com.example.log_in;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class APIconnector {
    static int film_id = 5;
    public String FilmConnect()throws IOException {
        String value = "";
       try {


           URL githubEndpoint = new URL("https://api.themoviedb.org/3/movie/" + film_id + "?api_key=953f4c14b6405a3b2ae1163d86a60644");
           HttpsURLConnection myConnection =
                   (HttpsURLConnection) githubEndpoint.openConnection();
           myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");

           InputStream responseBody = myConnection.getInputStream();
           InputStreamReader responseBodyReader =
                   new InputStreamReader(responseBody, "UTF-8");
           if (myConnection.getResponseCode() == 200) {
               JsonReader jsonReader = new JsonReader(responseBodyReader);
               jsonReader.beginObject();
               while (jsonReader.hasNext()) {
                   String key = jsonReader.nextName();
                   if (key.equals("title") || key.equals("poster_path")) {
                       value += jsonReader.nextString() + "!";

                   } else jsonReader.skipValue();
               }
           }

           film_id++;

           if (!value.equals("")) return value;
           else value = FilmConnect();
       }
       catch (Exception e) {
           film_id++;
          value =  FilmConnect();
       }
         return value;
    }


}
