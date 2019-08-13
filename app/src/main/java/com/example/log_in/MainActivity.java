package com.example.log_in;

import androidx.annotation.IntRange;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    EditText log,pass;
    Button btn,btn2;
    TextView reg;
    ProgressBar progressBar;
    Excute e;

@Override
protected void onRestart(){
super.onRestart();
progressBar.setVisibility(View.INVISIBLE);
log.setText(""); pass.setText("");
}
@Override
public void onBackPressed(){
    openQuitDialog();
}
private void openQuitDialog(){
    AlertDialog.Builder quit = new AlertDialog.Builder(MainActivity.this);
    quit.setTitle("Вы уверены, что хотите закрыть приложение?");
    quit.setPositiveButton("Да", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            finish();
        }
    });
    quit.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

        }
    });
    quit.show();
    //AlertDialog show = quit;

}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        log=(EditText)findViewById(R.id.log);
        pass=(EditText)findViewById(R.id.pass);
        btn=(Button)findViewById(R.id.button);
progressBar = (ProgressBar)findViewById(R.id.progressBar);

        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        e = new Excute();
                        if(log.getText().toString().equals("")&&pass.getText().toString().equals(""))Toast.makeText(getApplicationContext(),"Поля пустые",Toast.LENGTH_LONG).show();
                        else if(log.getText().toString().equals(""))Toast.makeText(getApplicationContext(),"Поле логина пустое",Toast.LENGTH_LONG).show();
                        else if(pass.getText().toString().equals(""))Toast.makeText(getApplicationContext(),"Поле пароля пустое",Toast.LENGTH_LONG).show();
                        else {
                            progressBar.setVisibility(View.VISIBLE);
                           e.execute(log.getText().toString(),pass.getText().toString());


                        }
                    }
                }
        );


        reg=(TextView)findViewById(R.id.reg_link);
        reg.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent("com.example.log_in.Registr");
                        log.setText("");pass.setText("");
                        startActivity(intent);
                    }
                }
        );

    }

    class Excute extends AsyncTask<String, Integer,Integer>
    {
        int result=-1;
     @Override
        protected Integer  doInBackground(String... unused) {
         Cursor cursor = mDb.rawQuery("Select u.name , u.id_u from Users u ,Log_in l Where l.login='"+unused[0]+"' and l.pass='"+unused[1]+"' and u.id_u=l.id_u",null);

         if(cursor.moveToFirst()){

             ContentValues values = new ContentValues();
             values.put("name",cursor.getString(0));
             values.put("id_u",cursor.getString(1));
             mDb.update("name_lg",values,null,null);
              cursor.close();



         }
         else{
             result = 0;
             return 0;
         }
         result = 1;
         return 1;
     }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer == 0 ){
                progressBar.setVisibility(View.INVISIBLE);
                Toast toast = Toast.makeText(getApplicationContext(), "Неверный логин или пароль", Toast.LENGTH_LONG);
                toast.show();
                pass.setText("");
            }
            else{
      Intent intent = new Intent("com.example.log_in.Log");
              startActivity(intent);
            }
        }
    }
}
