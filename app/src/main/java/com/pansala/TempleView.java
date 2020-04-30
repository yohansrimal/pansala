package com.pansala;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.pansala.Database.DBHelper;
import com.pansala.Session.Session;

import java.util.Calendar;
import java.util.Locale;

public class TempleView extends AppCompatActivity {

    //views
    private CircularImageView profileIv;
    private TextView templeBio, nameTv, historyTempleTv, startTv, monkNameTv, locationTv, monkTeleTv,addedTimeTv,updatedTimeTv;
    private Button removeTemple;

    //action Bar
    private ActionBar actionBar;

    //db helper
    private DBHelper mydb;

    private Session session;

    private String recordID,templeID,templeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temple_view);

        //get record id from adapter through intent
        Intent intent = getIntent();
        recordID = intent.getStringExtra("RECORD_ID");
        templeName = intent.getStringExtra("TEMPLE_NAME");

        //setting up action bar with title and backbutton
        actionBar = getSupportActionBar();
        actionBar.setTitle(templeName);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        removeTemple = (Button) findViewById(R.id.removeTempleButton);

        //init db helper class
        mydb = new DBHelper(this);


        //init views
        profileIv = findViewById(R.id.profileIv);
        templeBio = findViewById(R.id.templeBio);
        nameTv = findViewById(R.id.nameTv);
        historyTempleTv = findViewById(R.id.historyTempleTv);
        startTv = findViewById(R.id.startTv);
        monkNameTv = findViewById(R.id.monkNameTv);
        locationTv = findViewById(R.id.locationTv);
        monkTeleTv = findViewById(R.id.monkTeleTv);
        addedTimeTv = findViewById(R.id.addedTimeTv);
        updatedTimeTv = findViewById(R.id.updatedTimeTv);

        //Create new session object
        session = new Session(this);

        showRecordDetails();
    }

    private void showRecordDetails() {

        //get Record details
        //query to select record base on record id
        String selectQuery = "SELECT * FROM " + NConstants.TABLE_NAME + " WHERE " + NConstants.C_ID +" =\"" + recordID+"\"";
        SQLiteDatabase db = mydb.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //keep checking in whole db for that record
        if (cursor.moveToFirst()){
            do{
                //get data
                String id = ""+ cursor.getInt(cursor.getColumnIndex(NConstants.C_ID));
                String name = ""+ cursor.getString(cursor.getColumnIndex(NConstants.C_NAME));
                String image = ""+ cursor.getString(cursor.getColumnIndex(NConstants.C_IMAGE));
                String history = ""+ cursor.getString(cursor.getColumnIndex(NConstants.C_HISTORY));
                String start = ""+ cursor.getString(cursor.getColumnIndex(NConstants.C_START));
                String monk = ""+ cursor.getString(cursor.getColumnIndex(NConstants.C_MONK));
                String templelocation = ""+ cursor.getString(cursor.getColumnIndex(NConstants.C_LOCATION));
                String phone = ""+ cursor.getString(cursor.getColumnIndex(NConstants.C_PHONE));
                String addedTime = ""+ cursor.getString(cursor.getColumnIndex(NConstants.C_ADDED_TIMESTAMP));
                String updatedTime = ""+ cursor.getString(cursor.getColumnIndex(NConstants.C_UPDATED_TIMESTAMP));

                //Assign id to temple id
                templeID = id;

                //convert timestamp to  dd/mm/yyyy hh:mm aa e.g. 22/06/2020 05.20 AM
                Calendar calendar1 = Calendar.getInstance(Locale.getDefault());
                calendar1.setTimeInMillis(Long.parseLong(addedTime));
                String timeAdded = ""+DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar1);

                Calendar calendar2 = Calendar.getInstance(Locale.getDefault());
                calendar1.setTimeInMillis(Long.parseLong(updatedTime));
                String timeUpdated = ""+DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar2);

                //set data
                nameTv.setText(name);
                historyTempleTv.setText(history);
                startTv.setText(start);
                monkNameTv.setText(monk);
                locationTv.setText(templelocation);
                monkTeleTv.setText(phone);
                addedTimeTv.setText(timeAdded);
                updatedTimeTv.setText(timeUpdated);
                //if user doesn't attach image then imageUri will be null, so set a default image in that case
                if (image.equals("null")){
                    //no image in record, set default
                    profileIv.setImageResource(R.drawable.templenew);
                }else {
                    //have image in the record
                    profileIv.setImageURI(Uri.parse(image));
                }

            }while (cursor.moveToNext());
        }
        //close db connection
        db.close();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();// go to previous activity
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();

        removeTemple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = session.getSessionId();
                boolean status = mydb.removeTempleData(recordID, userID);

                if(status == true){
                    finish();
                    Toast.makeText(TempleView.this, "Temple Deleted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(TempleView.this, "Temple Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
