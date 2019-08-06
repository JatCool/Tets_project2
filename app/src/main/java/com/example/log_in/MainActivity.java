package com.example.log_in;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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
    TextView reg,fog;


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



        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor cursor = mDb.rawQuery("Select u.name from Users u ,Log_in l Where l.login='"+log.getText().toString()+"' and l.pass='"+pass.getText().toString()+"' and u.id_u=l.id_u",null);

                        if(cursor.moveToFirst()){

                            ContentValues values = new ContentValues();
                            values.put("name",cursor.getString(0));
                            mDb.update("name_lg",values,null,null);
                            Intent intent = new Intent("com.example.log_in.Log");
                            log.setText("");pass.setText("");

                            startActivity(intent);
                        }
                        else{
                            Toast toast= Toast.makeText(getApplicationContext(),"Неверный логин или пароль",Toast.LENGTH_LONG); toast.show();
                            pass.setText("");
                        }
                    }
                }
        );
        fog = (TextView)findViewById(R.id.reg_link2);
        fog.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                     startActivity( new Intent("com.example.log_in.ForgetPass"));
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
}
