package com.pansala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.pansala.Database.DBHelper;

public class UserTempleHandle extends AppCompatActivity {


    private RecyclerView recordsRv;

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
        setContentView(R.layout.activity_user_temple_handle);

        actionBar = getSupportActionBar();
        actionBar.setTitle("විහාරස්ථාන සොයන්න");

        //init views

        recordsRv = findViewById(R.id.userTempleRecords);

        //init db helper class
        ndbHelper = new DBHelper(this);

        //load Records (by default newest first)
        loadRecords(orderByNewest);

    }

    private void loadRecords(String orderBy) {
        currentOrderByStatus = orderBy;
        userTempleAdapter nadapterRecord = new userTempleAdapter(UserTempleHandle.this,
                ndbHelper.getAllTempleRecords(orderBy));

        recordsRv.setAdapter(nadapterRecord);

    }

    private void searchRecords(String query){
        userTempleAdapter nadapterRecord = new userTempleAdapter(UserTempleHandle.this,
                ndbHelper.searchTempleRecords(query));

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
        return super.onOptionsItemSelected(item);
    }

}
