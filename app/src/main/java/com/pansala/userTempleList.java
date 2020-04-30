package com.pansala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.pansala.Database.DBHelper;
import com.pansala.Session.Session;

public class userTempleList extends AppCompatActivity {


    private RecyclerView recordsRv;
    private Session session;

    //DB helper
    private DBHelper ndbHelper;

    //Action Bar
    ActionBar actionBar;

    //sort options
    String orderByNewest = NConstants.C_ADDED_TIMESTAMP + " DESC";

    //for refreshing records, refresh with last chosen sort option
    String currentOrderByStatus = orderByNewest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_temple_list);

        actionBar = getSupportActionBar();
        actionBar.setTitle("මගේ විහාරස්ථාන");

        //init views

        recordsRv = findViewById(R.id.userTempleRecordsList);

        //init db helper class
        ndbHelper = new DBHelper(this);

        //new session object
        session = new Session(this);


        //load Records (by default newest first)
        loadRecords(orderByNewest);

    }

    private void loadRecords(String orderBy) {
        String userID = session.getSessionId();
        currentOrderByStatus = orderBy;
        userTempleViewAdapter nadapterRecord = new userTempleViewAdapter(userTempleList.this,
                ndbHelper.getUsersTempleRecords(userID));

        recordsRv.setAdapter(nadapterRecord);

    }

    private void searchRecords(String query){
        String userID = session.getSessionId();
        userTempleViewAdapter nadapterRecord = new userTempleViewAdapter(userTempleList.this,
                ndbHelper.searchUserTempleRecords(query, userID));

        recordsRv.setAdapter(nadapterRecord);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecords(currentOrderByStatus); //refresh records list
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.usertemplemenu, menu);
        //search view
        MenuItem item =menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //search when search button on keyboard clicked
                searchRecords(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //search as u type
                searchRecords(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handle menu item
        return super.onOptionsItemSelected(item);
    }


}
