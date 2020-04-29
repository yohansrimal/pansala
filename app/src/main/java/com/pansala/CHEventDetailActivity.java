package com.pansala;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.pansala.Database.DBHelper;

import java.util.Calendar;
import java.util.Locale;

public class CHEventDetailActivity extends AppCompatActivity {

    //views
    //private CircularImageView profileIv;
    private TextView eventBio, eventNameTv, eventDesTv, eventDateTv, eventTimeTv, addedTimeTv, updatedTimeTv;

    //action Bar
    private ActionBar actionBar;

    //db helper
    private DBHelper CHDBEventHelper;

    private String recordID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ch_event_detail);

        //setting up action bar with title and back button
        actionBar = getSupportActionBar();
        actionBar.setTitle("පන්සල් ලැයිස්තුව");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get record id from adapter through intent
        Intent intent = getIntent();
        recordID = intent.getStringExtra("RECORD_ID");

        //init db helper class
        CHDBEventHelper = new DBHelper(this);

        //init views
        //profileIv = findViewById(R.id.profileIv);
        eventBio = findViewById(R.id.eventBio);
        eventNameTv = findViewById(R.id.eventNameTv);
        eventDesTv = findViewById(R.id.eventDesTv);
        eventDateTv = findViewById(R.id.eventDateTv);
        eventTimeTv = findViewById(R.id.eventTimeTv);
        addedTimeTv = findViewById(R.id.addedTimeTv);
        updatedTimeTv = findViewById(R.id.updatedTimeTv);

        showRecordDetails();
    }

    private void showRecordDetails() {

        //get Record details
        //query to select record base on record id
        String selectQuery = "SELECT * FROM " + CHEventConstants.TABLE_NAME + " WHERE " + CHEventConstants.EVENT_ID +" =\"" + recordID+"\"";
        SQLiteDatabase db = CHDBEventHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //keep checking in whole db for that record
        if (cursor.moveToFirst()){
            do{
                //get data
                String eventId = ""+ cursor.getInt(cursor.getColumnIndex(CHEventConstants.EVENT_ID));
                String eventName = ""+ cursor.getString(cursor.getColumnIndex(CHEventConstants.EVENT_NAME));
                String eventDes = ""+ cursor.getString(cursor.getColumnIndex(CHEventConstants.EVENT_DES));
                String eventDate = ""+ cursor.getString(cursor.getColumnIndex(CHEventConstants.EVENT_DATE));
                String eventTime = ""+ cursor.getString(cursor.getColumnIndex(CHEventConstants.EVENT_TIME));
                String addedTime = ""+ cursor.getString(cursor.getColumnIndex(CHEventConstants.EVENT_ADDED_TIMESTAMP));
                String updatedTime = ""+ cursor.getString(cursor.getColumnIndex(CHEventConstants.EVENT_UPDATED_TIMESTAMP));

//                //convert timestamp to  dd/mm/yyyy hh:mm aa e.g. 22/06/2020 05.20 AM
                Calendar calendar1 = Calendar.getInstance(Locale.getDefault());
                calendar1.setTimeInMillis(Long.parseLong(addedTime));
                String timeAdded = ""+DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar1);

                Calendar calendar2 = Calendar.getInstance(Locale.getDefault());
                calendar1.setTimeInMillis(Long.parseLong(updatedTime));
                String timeUpdated = ""+DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar2);

                //set data
                eventNameTv.setText(eventName);
                eventDesTv.setText(eventDes);
                eventDateTv.setText(eventDate);
                eventTimeTv.setText(eventTime);
                addedTimeTv.setText(timeAdded);
                updatedTimeTv.setText(timeUpdated);

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
}
