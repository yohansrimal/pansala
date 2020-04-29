package com.pansala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pansala.Database.DBHelper;
import com.pansala.Session.Session;


public class Login extends AppCompatActivity {

    private Button loginButton, registerButton; //Initialize Buttons
    private EditText eEmail, ePassword; //Initialize Edittexts
    private Session session; //Initiliaze Session
    DBHelper userDb; //Initiliaze userDb Object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDb = new DBHelper(this); //Create new object from DBHelper class
        session = new Session(this); //Create new session object from Session class

        //Get Buttons from layouts
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);

        //Get EditTexts from layouts
        eEmail = (EditText) findViewById(R.id.lusername);
        ePassword = (EditText) findViewById(R.id.lpassword);

        checkLogin();
        checkAdmin(); //Check Admin method for check is there any administrator is registered


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        }); //Change activity to register activity
    }

    @Override
    protected void onResume() {
        super.onResume();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Custom Error Toast Settings
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.error_toast, (ViewGroup) findViewById(R.id.id_error_toast));

                TextView toastText = layout.findViewById(R.id.error_text);
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER,0,0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);

                //Get user entered values from layout
                String em = eEmail.getText().toString();
                String pw = ePassword.getText().toString();

                //Login Validations
                if(em.trim().length() == 0){
                    toastText.setText("විද්යුත් තැපැල් ලිපිනය ඇතුලත් කරන්න.!");
                    toast.show();
                }
                else if(!em.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")){
                    toastText.setText("අවලංගු විද්යුත් තැපැල් ලිපිනයකි.!");
                    toast.show();
                }
                else if(pw.trim().length() == 0){
                    toastText.setText("මුරපදය ඇතුලත් කරන්න.!");
                    toast.show();
                }
                else{
                    String data[] = userDb.loginUser(em,pw); // Check user is exists and return array of ID, fullname, and type of user

                    if(data != null){ //If user exists
                        String userPersonalDataTable = "UserID" + data[0].toString(); //Create new tables for store selected events and temples
                        session.setSessionId(data[0].toString()); //Set session data
                        session.setSessionFn(data[1].toString()); //Set session data
                        session.setSessionType(data[2].toString()); //Set session data

                        userDb.CreateUserTempleTable(userPersonalDataTable); //Call method that create personal tables

                        if(data[2].toString().equals("user")){
                            openUserInterface();
                        }
                        else{
                            openAdminInterface();
                        }
                    }
                    else{
                        toastText.setText("විද්යුත් තැපෑල හෝ මුරපදය වැරදි.!");
                        toast.show();
                    }
                }
            }
        });

    }

    public void checkLogin(){
        String sessionId = session.getSessionId().toString();
        String type = session.getSessionType().toString();

        if(sessionId.isEmpty()){

        }
        else{
            if(type.equals("user")){
                openUserInterface();
            }
            else if (type.equals("administrator")){
                openAdminInterface();
            }
        }
    }
    public void checkAdmin(){
        if(userDb.getAdmin()){}
        else{
            long status = userDb.addAdmin();
            if(status > 0) {}
            else{
                Toast.makeText(Login.this, "Admin Creation Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void openRegisterActivity(){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void openUserInterface(){
        finish();
        Intent intent = new Intent(this, userDashboard.class);
        startActivity(intent);
    }

    public void openAdminInterface(){
        finish();
        Intent intent = new Intent(this, adminDashboard.class);
        startActivity(intent);
    }
}
