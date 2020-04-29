package com.pansala;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pansala.Database.DBHelper;
import com.pansala.Session.Session;

import java.util.Calendar;

public class EditProfile extends AppCompatActivity {

    private EditText editName, editBday, editContactNo, editEmail, editNic, editPassword, editConfirmPassword;
    private Session session;
    private Button saveButton;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    DBHelper userDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        session = new Session(this);

        userDb = new DBHelper(this);

        saveButton = findViewById(R.id.saveButton);

        editName = (EditText) findViewById(R.id.editfullname);
        editBday = (EditText) findViewById(R.id.editbirthday);
        editContactNo = (EditText) findViewById(R.id.editphone);
        editEmail = (EditText) findViewById(R.id.editemail);
        editNic = (EditText) findViewById(R.id.editIdNumber);
        editPassword = (EditText) findViewById(R.id.editpassword);
        editConfirmPassword = (EditText) findViewById( R.id.editconfirmpassword);


        //Select birthday when clicked
        editBday.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditProfile.this,
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

        setUserData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.menuLogout){
            session.logout();
            finish();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        else if(id==R.id.menueditProfile){
            Intent intent = new Intent(this, EditProfile.class);
            startActivity(intent);
        }

        else if(id==R.id.menuProfile){
            Intent intent = new Intent(this, viewProfile.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String fullName = editName.getText().toString();
                String birthday = editBday.getText().toString();
                String nic = editNic.getText().toString();
                String email = editEmail.getText().toString();
                String contactNo = editContactNo.getText().toString();
                String password = editPassword.getText().toString();
                String confirmPass = editConfirmPassword.getText().toString();

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
                    toastText.setText("මුරපදය තහවුරු කරන්න.!");
                    toast.show();
                }

                //Register New User
                else{
                    int id = Integer.parseInt(session.getSessionId());

                    boolean status = userDb.updateInfo(id, fullName, birthday, nic, email, contactNo, password);

                    if (status == true) {
                       updateSuccessfulDialog successDialog = new updateSuccessfulDialog();
                       successDialog.show(getSupportFragmentManager(),"success");
                       session.logout();

                    } else {
                        Toast.makeText(EditProfile.this, "User can not be update", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    public void setUserData(){
        int id = Integer.parseInt(session.getSessionId());
        Cursor details = userDb.getUserData(id);
        editName.setText(details.getString(details.getColumnIndex("fullname")));
        editBday.setText(details.getString(details.getColumnIndex("birthday")));
        editContactNo.setText("0" + details.getString(details.getColumnIndex("contactno")));
        editNic.setText(details.getString(details.getColumnIndex("nic")));
        editEmail.setText(details.getString(details.getColumnIndex("email")));
        editPassword.setText(details.getString(details.getColumnIndex("password")));
    }
}
