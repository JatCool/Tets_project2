package com.example.log_in;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;

import javax.xml.validation.Validator;

public class Registr extends AppCompatActivity {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    EditText log, name, pass, pass1;
    Button check, reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registr);
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
        log = (EditText) findViewById(R.id.reg_log);
        name = (EditText) findViewById(R.id.name);
        pass = (EditText) findViewById(R.id.reg_pass);
        pass1 = (EditText) findViewById(R.id.reg_pass1);
        check = (Button) findViewById(R.id.check);
        reg = (Button) findViewById(R.id.confirm);
        reg.setClickable(false);

        check.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!log.getText().toString().isEmpty()) {
                            if (Check())
                                reg.setClickable(true);
                        }
                        else {
                            Toast t = Toast.makeText(getApplicationContext(), "Поле пустое", Toast.LENGTH_LONG);
                            t.show();
                        }
                    }
                }
        );
        reg.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!log.getText().toString().isEmpty()) {
                            if(Check()){
                                if(!pass.getText().toString().isEmpty()&&!pass1.getText().toString().isEmpty()&&pass.getText().toString().equals(pass1.getText().toString())) {
                                    Cursor _id = mDb.rawQuery("Select id_u from Users ORDER by id_u DESC LIMIT 1", null);
                                    _id.moveToFirst();
                                    int cur_id = Integer.parseInt(_id.getString(0));
                                    ContentValues values = new ContentValues();
                                    values.put("name", name.getText().toString());
                                    mDb.insert("Users", null, values);
                                    values.clear();
                                    values.put("login", log.getText().toString());
                                    values.put("pass", pass1.getText().toString());
                                    values.put("id_u", cur_id + 1);
                                    mDb.insert("Log_in", null, values);
                                    Toast t = Toast.makeText(getApplicationContext(), "Вы успешно зарегистрировались", Toast.LENGTH_LONG);
                                    t.show();
                                    finish();
                                }
                                else{Toast t = Toast.makeText(getApplicationContext(),"Пароли не совподают или одно из полей пустое",Toast.LENGTH_LONG);t.show();}
                            }
                        }
                        else{ Toast t = Toast.makeText(getApplicationContext(),"Поле пустое",Toast.LENGTH_LONG);t.show(); check.setBackgroundColor(Color.GRAY);}
                    }
                }
        );


    }

    public boolean Check() {
        Cursor cursor = mDb.rawQuery("Select u.name from Users u ,Log_in l Where l.login='" + log.getText().toString()+"' and u.id_u=l.id_u", null);
        if (cursor.moveToFirst()) {
            check.setBackgroundColor(Color.RED);
            Toast e = Toast.makeText(getApplicationContext(),"Такой логин уже существует",Toast.LENGTH_LONG);e.show();
            return false;

        } else {
            check.setBackgroundColor(Color.GREEN);
            Toast.makeText(getApplicationContext(),"Этот логин свободный",Toast.LENGTH_LONG).show();
            return true;
        }
    }
}
