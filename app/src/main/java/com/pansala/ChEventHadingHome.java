package com.pansala;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pansala.Database.DBHelper;

public class ChEventHadingHome extends AppCompatActivity {

    //views
    private FloatingActionButton addRecordBtn;
    private RecyclerView recordsRv;

    //DB helper
    private DBHelper CHDBEventHelper;

    //Action Bar
    ActionBar actionBar;

    //sort options
    String orderByNewest = CHEventConstants.EVENT_ADDED_TIMESTAMP + " DESC";
    String orderByOldest = CHEventConstants.EVENT_ADDED_TIMESTAMP + " ASC";
    String orderByTitleAsc = CHEventConstants.EVENT_NAME + " ASC";
    String orderByTitleDesc = CHEventConstants.EVENT_NAME + " DESC";

    //for refreshing records, refresh with last chosen sort option
    String currentOrderByStatus = orderByNewest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ch_event_list);


        actionBar = getSupportActionBar();
        actionBar.setTitle("පිංකම් කළමනාකරණය");

        //init views
        addRecordBtn = findViewById(R.id.addRecordBtn);
        recordsRv = findViewById(R.id.recordsRv);

        //init db helper class
        CHDBEventHelper = new DBHelper(this);

        //load Records (by default newest first)
        loadRecords(orderByNewest);

        //click to start add the record activity
        addRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChEventHadingHome.this, CHAddUpdateEventActivity.class);
                intent.putExtra("isEditMode", false);//want to add new data, set false
                startActivity(intent);

               // Toast.makeText(getApplicationContext(),"you just click the button",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRecords(String orderBy) {
        currentOrderByStatus = orderBy;
        CHAdapterEventRecord chAdapterEventRecord = new CHAdapterEventRecord(ChEventHadingHome.this,
                CHDBEventHelper.getAllRecords(orderBy));

        recordsRv.setAdapter(chAdapterEventRecord);

        //set no of records
        actionBar.setSubtitle("එකතුව: "+ CHDBEventHelper.getRecordsCount());
    }

    private void searchRecords(String query){
        CHAdapterEventRecord chAdapterEventRecord = new CHAdapterEventRecord(ChEventHadingHome.this,
                CHDBEventHelper.searchRecords(query));

                recordsRv.setAdapter(chAdapterEventRecord);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecords(currentOrderByStatus); //refresh records list
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.nmenu_main, menu);
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
        //handle menu items
        int id = item.getItemId();
        if (id == R.id.action_sort){
            //show sort options (show in dialog)
            sortOptionDialog();
        }else if (id == R.id.action_delete_all){
            //delete all records
            CHDBEventHelper.deleteAllData();
            onResume();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortOptionDialog() {
        //options to display in dialog
        String[] options = {"Title Ascending", "Title Descending", "Newest", "Oldest"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle option click
                        if (which == 0){
                            //title ascending
                            loadRecords(orderByTitleAsc);
                        }else if (which == 1){
                            //title descending
                            loadRecords(orderByTitleDesc);
                        }else if (which == 2){
                            //newest
                            loadRecords(orderByNewest);
                        }else if (which == 3){
                            //oldest
                            loadRecords(orderByOldest);
                        }
                    }
                })
                .create().show(); //show dialog
    }
}
