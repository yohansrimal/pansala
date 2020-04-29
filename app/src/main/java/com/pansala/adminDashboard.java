package com.pansala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pansala.Session.Session;

public class adminDashboard extends AppCompatActivity {

    private TextView name;
    private Session session;
    CardView templeSettings, eventSettings, dhammaSchoolSettings, userSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        session = new Session(this);

        name = (TextView) findViewById(R.id.dashboardName);

        templeSettings = (CardView) findViewById(R.id.templeSettings);
        eventSettings = (CardView) findViewById(R.id.eventSettings);
        dhammaSchoolSettings = (CardView) findViewById(R.id.dhammaSchoolSettings);
        userSettings = (CardView) findViewById(R.id.userSettings);

        name.setText(session.getSessionFn().toString());
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

        templeSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminDashboard.this, NTempleHome.class);
                startActivity(intent);
            }
        });

        eventSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminDashboard.this, ChEventHadingHome.class);
                startActivity(intent);
            }
        });

        dhammaSchoolSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminDashboard.this, td_dahampasala_enter.class);
                startActivity(intent);
            }
        });

        userSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(adminDashboard.this, "Executed user settings", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
