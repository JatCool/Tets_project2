package com.example.log_in;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Serials extends AppCompatActivity {

    ListView films;
    private List<Film> states = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        APIconnector.film_id = 5;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serials);
        new Connect().execute();


    }
    class Connect extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String...unsude){
//
            try {
                for(int i=0;i<10;i++) {
                    states.add(new Film(new APIconnector().FilmConnect()));
             }

            }
            catch (IOException e){
                //Toast.makeText(getApplicationContext(),"Произошла ошибка",Toast.LENGTH_LONG)
                        //.show();
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG)
                        .show();
                System.exit(0);
            }
           return "!";



        }
        @Override
        protected void onPostExecute(String result){
            films = (ListView) findViewById(R.id.countriesList);
            StayAdapter stateAdapter = new StayAdapter(getApplicationContext(), R.layout.list_item, states);
            films.setAdapter(stateAdapter);
            Toast.makeText(getApplicationContext(),"Загрузка завершена",Toast.LENGTH_LONG).show();
//            String [] info = result.split(" ");
//            name.setText(info[1]);
//            try {
//                Picasso.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w500"+info[0]).into(img);
//
//            }
//            catch (Exception e){
//                name.setText(info.length);
//           }


       }
    }
}
