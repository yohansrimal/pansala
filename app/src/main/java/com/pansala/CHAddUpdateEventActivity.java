package com.pansala;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pansala.Database.DBHelper;

public class CHAddUpdateEventActivity extends AppCompatActivity {

    //views
    private EditText nameGiving, givingDes, givingDate, givingTime;
    private FloatingActionButton saveEventBtn;


    private String eId,eventName, eventDes, eventDate, eventTime, addedTime, updatedTime;
    private boolean isEditMode = false;

    //defining AwesomeValidation object
    private AwesomeValidation awesomeValidation;

    //db helper
    private DBHelper dbHelper;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ch_add_event_record);

        //init
        //actionBar
        actionBar = getSupportActionBar();
        //title
        actionBar.setTitle("පන්සල ගිණුම සාදන්න");
        //back button
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        /*
         * The library provides 3 types of validation
         * BASIC
         * COLORATION
         * UNDERLABEL
         * */
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        //init views
        nameGiving = findViewById(R.id.nameGiving);
        givingDes = findViewById(R.id.givingDes);
        givingDate = findViewById(R.id.givingDate);
        givingTime = findViewById(R.id.givingTime);
        saveEventBtn = findViewById(R.id.saveEventBtn);

        //adding validation to edittexts
        awesomeValidation.addValidation(this, R.id.nameGiving, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerrorEvent);
        awesomeValidation.addValidation(this, R.id.givingDes, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.namedes);
        awesomeValidation.addValidation(this, R.id.givingDate, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", R.string.dateerrorEvent);
        awesomeValidation.addValidation(this, R.id.givingTime, "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$", R.string.timeerror);

        //get data from intent
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode", false);
        if (isEditMode){
            //update data
            actionBar.setTitle("Update Date");

            eId = intent.getStringExtra("EVENT_ID");
            eventName = intent.getStringExtra("EVENT_NAME");
            eventDes = intent.getStringExtra("EVENT_DES");
            eventDate = intent.getStringExtra("EVENT_DATE");
            eventTime = intent.getStringExtra("EVENT_TIME");
            addedTime = intent.getStringExtra("ADDED_TIME");
            updatedTime = intent.getStringExtra("UPDATED_TIME");

            //set data to views
            nameGiving.setText(eventName);
            givingDes.setText(eventDes);
            givingDate.setText(eventDate);
            givingTime.setText(eventTime);



        }else{
            //add data
            actionBar.setTitle("Add Event");

        }


        //init db helper
        dbHelper = new DBHelper(this);

//
        //click save button to save record
        saveEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (awesomeValidation.validate()) {
                    //process the data further
                    inputData();
                }

            }
        });

    }

    private void inputData() {
        //get Data
        eventName = ""+nameGiving.getText().toString().trim();
        eventDes = ""+givingDes.getText().toString().trim();
        eventDate = ""+givingDate.getText().toString().trim();
        eventTime = ""+givingTime.getText().toString().trim();

        if (isEditMode){
            //update data
            String timestamp = ""+System.currentTimeMillis();
            dbHelper.updateRecord(
                    ""+eId,
                    ""+eventName,
                    ""+eventDes,
                    ""+eventDate,
                    ""+eventTime,
                    ""+addedTime,
                    ""+updatedTime
            );

            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
        }else {
            //new data
            String timestamp = ""+System.currentTimeMillis();
            long id = dbHelper.insertRecord(
                    ""+eventName,
                    ""+eventDes,
                    ""+eventDate,
                    ""+eventTime,
                    ""+timestamp,
                    ""+timestamp
            );

            Toast.makeText(getApplicationContext(), "Record Added against ID: "+id, Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}

