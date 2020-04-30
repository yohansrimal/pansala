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

import com.pansala.Session.Session;

public class userDashboard extends AppCompatActivity {

    private TextView name;
    private Session session;
    private CardView templeSearch, myTemples;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        session = new Session(this);

        templeSearch = (CardView) findViewById(R.id.SearchTemples);
        myTemples = (CardView) findViewById(R.id.myTemples);

        name = (TextView) findViewById(R.id.dashboardName);

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

        templeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userDashboard.this, UserTempleHandle.class);
                startActivity(intent);
            }
        });

        myTemples.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userDashboard.this, userTempleList.class);
                startActivity(intent);
            }
        });
    }
}
