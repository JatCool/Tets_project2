package com.example.log_in;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class Log extends AppCompatActivity {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private static final int STORAGE_PERMISSION_CODE = 911;
    private final int PICK_IMAGE_REQUEST = 1;
    private String imagePath;
    private TextView tx;
    private ImageView imgview;
    private String id_u;
    private VideoView rofl;

    @Override
    protected void onResume(){
        super.onResume();
        Cursor cursor = mDb.rawQuery("Select l.name FROM name_lg l , Users u WHERE u.id_u=l.id_u", null);
        cursor.moveToFirst();
        tx = (TextView) findViewById(R.id.textView);
        tx.setText("Добро пожаловать, " + cursor.getString(0));
        cursor.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestStoragePermission();
        setContentView(R.layout.activity_log);
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
        Cursor cursor = mDb.rawQuery("Select l.name, u.pic_path,l.id_u FROM name_lg l , Users u WHERE u.id_u=l.id_u", null);
        TextView tx = (TextView) findViewById(R.id.textView);
        imgview = (ImageView) findViewById(R.id.imageView3);
         rofl = (VideoView)findViewById(R.id.videoView) ;
         rofl.setOnClickListener(
                 new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                        VideoChuser();
                     }
                 }
         );
        imgview.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showFileChooser();
                    }
                }
        );

        cursor.moveToFirst();
        try {


            if (!cursor.getString(1).equals("0")) {
                File image = new File(cursor.getString(1));
                if(image.exists()) {
                    Picasso.with(this)
                            .load(image)
                            .resize(400, 400)
                            .centerCrop()
                            .into(imgview);
                }
            }
        }
        catch(Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG);
        }
        id_u = cursor.getString(2);
        tx.setText("Добро пожаловать, " + cursor.getString(0));
        cursor.close();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.menu.main_menu, menu);
            return true;
        }
  void VideoChuser(){
      Intent intent = new Intent();
      intent.setType("video/*");
      intent.setAction(Intent.ACTION_PICK);
      startActivityForResult(Intent.createChooser(intent, "Выберите видеокартинку"), PICK_IMAGE_REQUEST);
  }
        @Override
        public boolean onOptionsItemSelected(MenuItem menuItem){
            int id = menuItem.getItemId();
            switch (id) {
                case R.id.settings:{
                    Intent set = new Intent(this, Settings.class);
                    set.putExtra("id_u",id_u);
                    startActivity(set);
                    return true;
            }
                case R.id.site:{
                    startActivity(new Intent(this,Serials.class));
                    return true;
                }


            }
            return super.onOptionsItemSelected(menuItem);
        }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            imagePath = getRealPathFromURI(selectedImageUri);
            Picasso.with(this)
                    .load(new File(imagePath))
                    .resize(400, 400)
                    .centerCrop()
                    .into(imgview);
            ContentValues values = new ContentValues();
            values.put("pic_path",imagePath);
            mDb.update("Users",values,"id_u="+id_u,null);

               Uri selectedVideoUri = data.getData();
               imagePath = getRealPathFromURIVideo(selectedVideoUri);
               rofl.setVideoPath(imagePath);
               rofl.setMediaController(new MediaController(this));
               rofl.requestFocus();
               rofl.start();

        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private String getRealPathFromURIVideo(Uri contentUri) {
        String[] proj = {MediaStore.Video.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Предоставлены права на чтение.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Права на чтение не были предоставлены.", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
}
