package com.example.log_in;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.ContentObservable;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class Settings extends AppCompatActivity {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private EditText name, pass;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Bundle arguments = getIntent().getExtras();
        final String id_u = arguments.getString("id_u");
        name = (EditText) findViewById(R.id.name);
        pass = (EditText)findViewById(R.id.pass);
        btn = (Button) findViewById(R.id.change);
        Cursor data = mDb.rawQuery("Select u.name, l.pass FROM Users u, Log_in l WHERE l.id_u=u.id_u and u.id_u="+id_u,null);
        data.moveToFirst();
        name.setText(data.getString(0));
        pass.setText(data.getString(1));
        data.close();
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!name.getText().toString().isEmpty()){
                            if(pass.getText().length()==0)Toast.makeText(getApplicationContext(),"Поле пустое",Toast.LENGTH_LONG).show();
                            else if(pass.getText().length()<8)Toast.makeText(getApplicationContext(),"Пароль не может быть меньше 8 символов",Toast.LENGTH_LONG).show();
                            else if(pass.getText().length()>36)Toast.makeText(getApplicationContext(),"Пароль не может быть больше 36 символов",Toast.LENGTH_LONG).show();
                            else {
                                ContentValues val = new ContentValues();
                                val.put("name",name.getText().toString());
                                mDb.update("Users",val,"id_u="+id_u,null);
                                val.clear();
                                val.put("pass",pass.getText().toString());
                                mDb.update("Log_in",val,"id_u="+id_u,null);
                                val.clear();
                                val.put("name",name.getText().toString());
                                mDb.update("name_lg",val,null,null);
                                Toast.makeText(getApplicationContext(),"Данные успешно изменены",Toast.LENGTH_LONG).show();

                            }
                        }
                        else Toast.makeText(getApplicationContext(),"Поле с именем пустое!!!",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}
