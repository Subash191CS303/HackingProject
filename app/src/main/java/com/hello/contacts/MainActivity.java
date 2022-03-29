package com.hello.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    TextView textView,path;
    Button dir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.text);


    }

    @Override
    protected void onStart() {
        super.onStart();
        File file=new File(getExternalFilesDir(null)+"/Hacker");
        if(!file.exists()){
            file.mkdir();
        }
        else{
            Toast.makeText(this, "File already exists"+file, Toast.LENGTH_SHORT).show();
        }
        gettingcontacts();

    }
    private void gettingcontacts(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},0);
        }
        try {
        File file=new File(getExternalFilesDir(null)+"/Hacker");
        file.delete();
        File Contacts=new File(file,"Contacts.txt");

            FileWriter writer=new FileWriter(Contacts);

        ContentResolver contentResolver=getContentResolver();
        Uri uri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor=contentResolver.query(uri,null,null,null,null);
        Log.w("Contact Provider Demo", "Total # No of Contacts ::: " +Integer.toString(cursor.getCount()));
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                @SuppressLint("Range") String contactname= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String phoneNumber= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                textView.setText("Contact Name : "+contactname+" Ph : "+phoneNumber);
                writer.append("Contact Name : "+contactname+" Ph : "+phoneNumber+"\r\n");
                writer.flush();
                Log.i("Content Provider","Contact Name ::: "+contactname+" Ph # "+phoneNumber);
            }
            writer.close();
        }
        }
         catch (IOException e) {
            e.printStackTrace();
        }
    }
}