package com.pansala;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pansala.Database.DBHelper;

import java.util.Calendar;

public class Register extends AppCompatActivity {

    private Button loginButton, registerButton;
    private EditText editBday, eName, eNic, eEmail, eTel, ePass, eConfirmPass;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    DBHelper userDb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDb = new DBHelper(this);
        SQLiteDatabase db = userDb.getReadableDatabase();

        editBday = (EditText) findViewById(R.id.birthday);
        eName = (EditText) findViewById(R.id.fullname);
        eNic = (EditText) findViewById(R.id.idNumber);
        eEmail = (EditText) findViewById(R.id.email);
        eTel = (EditText) findViewById(R.id.phone);
        ePass = (EditText) findViewById(R.id.password);
        eConfirmPass = (EditText) findViewById(R.id.confirmpassword);

        //Select birthday when clicked
        editBday.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Register.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        //Set edittext to selected birthday
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int newMonth = month + 1;
              String date = year + "/" + newMonth + "/" + dayOfMonth;
              editBday.setText(date);
            }
        };

        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String fullName = eName.getText().toString();
                String birthday = editBday.getText().toString();
                String nic = eNic.getText().toString();
                String email = eEmail.getText().toString();
                String contactNo = eTel.getText().toString();
                String password = ePass.getText().toString();
                String confirmPass = eConfirmPass.getText().toString();

                //Custom Error Toast Settings
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.error_toast, (ViewGroup) findViewById(R.id.id_error_toast));

                TextView toastText = layout.findViewById(R.id.error_text);
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER,0,0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);

                //Validations
                if(fullName.trim().length() == 0){
                    toastText.setText("සම්පුර්ණ නම ඇතුලත් කරන්න.!");
                    toast.show();
                }
                else if(birthday.trim().length() == 0){
                    toastText.setText("උපන්දිනය ඇතුලත් කරන්න.!");
                    toast.show();
                }
                else if(nic.trim().length() == 0){
                    toastText.setText("හැදුනුම්පත් අංකය ඇතුලත් කරන්න.!");
                    toast.show();
                }
                else if(nic.trim().length() != 9 && nic.trim().length() != 11 ){
                    toastText.setText("අවලංගු හැදුනුම්පත් අංකයකි.!");
                    toast.show();
                }
                else if(email.trim().length() == 0){
                    toastText.setText("විද්යුත් තැපැල් ලිපිනය ඇතුලත් කරන්න.");
                    toast.show();
                }
                else if(!email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")){
                    toastText.setText("අවලංගු විද්යුත් තැපැල් ලිපිනයකි.!");
                    toast.show();
                }
                else if(contactNo.trim().length() == 0){
                    toastText.setText("දුරකථන අංකය ඇතුලත් කරන්න.!");
                    toast.show();
                }
                else if(contactNo.trim().length() != 10 ) {
                    toastText.setText("අවලංගු දුරකථන අංකයකි.!");
                    toast.show();
                }
                else if(password.trim().length() == 0){
                    toastText.setText("මුරපදය ඇතුලත් කරන්න.!");
                    toast.show();
                }
                else if(!password.matches(confirmPass)){
                    toastText.setText("මුරපදය  නොගැලපේ.!");
                    toast.show();
                }

                //Register New User
                else{
                    long status = userDb.addUser(fullName, birthday, nic, email, contactNo, password);

                    if (status > 0) {
                        regSuccessfulDialog successDialog = new regSuccessfulDialog();
                        successDialog.show(getSupportFragmentManager(),"success");

                    } else {
                        Toast.makeText(Register.this, "User can not be updated", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    //Finish activity when click login button
    public void openLoginActivity(){
        finish();
    }



}
