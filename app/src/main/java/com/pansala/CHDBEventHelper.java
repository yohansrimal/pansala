package com.pansala;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CHDBEventHelper extends SQLiteOpenHelper {


    public CHDBEventHelper(@Nullable Context context) {
        super(context, CHEventConstants.DB_NAME, null, CHEventConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table on that db
        db.execSQL(CHEventConstants.CREATE_TABLE);


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //upgrade database(if there any structure change the change db version)

        //drop all the table if exist
        db.execSQL("DROP TABLE IF EXISTS "+ CHEventConstants.TABLE_NAME);
        //create table again
        onCreate(db);

    }

    //insert record to db
    public long insertRecord(String eventName, String eventDes, String eventDate,
                             String eventTime, String addedTime, String updatedTime){

        //get writable database because we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //id will set automatically as set auto increment in query

        //insert data
        values.put(CHEventConstants.EVENT_NAME, eventName);
        values.put(CHEventConstants.EVENT_DES, eventDes);
        values.put(CHEventConstants.EVENT_DATE, eventDate);
        values.put(CHEventConstants.EVENT_TIME, eventTime);
        values.put(CHEventConstants.EVENT_ADDED_TIMESTAMP, addedTime);
        values.put(CHEventConstants.EVENT_UPDATED_TIMESTAMP, updatedTime);

        //insert row, it will return record id of saved record
        long id = db.insert(CHEventConstants.TABLE_NAME, null,values);

        //close db connection
        db.close();

        //return id of inserted records
        return id;
    }

    //update existing record to db
    public void updateRecord(String id, String eventName, String eventDes, String eventDate,
                             String eventTime, String addedTime, String updatedTime){

        //get writable database because we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //id will set automatically as set auto increment in query

        //insert data
        values.put(CHEventConstants.EVENT_NAME, eventName);
        values.put(CHEventConstants.EVENT_DES, eventDes);
        values.put(CHEventConstants.EVENT_DATE, eventDate);
        values.put(CHEventConstants.EVENT_TIME, eventTime);
        values.put(CHEventConstants.EVENT_ADDED_TIMESTAMP, addedTime);
        values.put(CHEventConstants.EVENT_UPDATED_TIMESTAMP, updatedTime);

        //insert row, it will return record id of saved record
        db.update(CHEventConstants.TABLE_NAME, values, CHEventConstants.EVENT_ID + " = ?", new String[]{id});

        //close db connection
        db.close();

    }

    //get all data
    public ArrayList<CHEventModel> getAllRecords(String orderBy){
        //order by query allow to sort data e.g. newest/oldest first, name asc/desc
        //it will return list or records since we have used return type ArrayList<NModelRecord>

        ArrayList<CHEventModel> recordsList = new ArrayList<>();
        //query to select records
        String selectQuery = "SELECT * FROM " + CHEventConstants.TABLE_NAME + " ORDER BY " + orderBy;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all the records and add to list
        if (cursor.moveToFirst()){
            do{
                CHEventModel chEventModel = new CHEventModel(
                        ""+cursor.getInt(cursor.getColumnIndex(CHEventConstants.EVENT_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(CHEventConstants.EVENT_NAME)),
                        ""+cursor.getString(cursor.getColumnIndex(CHEventConstants.EVENT_DATE)),
                        ""+cursor.getString(cursor.getColumnIndex(CHEventConstants.EVENT_TIME)),
                        ""+cursor.getString(cursor.getColumnIndex(CHEventConstants.EVENT_DES)),
                        ""+cursor.getString(cursor.getColumnIndex(CHEventConstants.EVENT_ADDED_TIMESTAMP)),
                        ""+cursor.getString(cursor.getColumnIndex(CHEventConstants.EVENT_UPDATED_TIMESTAMP)));
                //add record to list
                recordsList.add(chEventModel);
            }while (cursor.moveToNext());
        }

        //close db connection
        db.close();

        //return the list
        return recordsList;
    }

    //search data
    public ArrayList<CHEventModel> searchRecords(String query){
        //order by query allow to sort data e.g. newest/oldest first, name asc/desc
        //it will return list or records since we have used return type ArrayList<NModelRecord>

        ArrayList<CHEventModel> recordsList = new ArrayList<>();
        //query to select records
        String selectQuery = "SELECT * FROM " + CHEventConstants.TABLE_NAME + " WHERE " + CHEventConstants.EVENT_NAME + " LIKE '%" + query +"%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all the records and add to list
        if (cursor.moveToFirst()){
            do{
                CHEventModel chEventModel = new CHEventModel(
                        ""+cursor.getInt(cursor.getColumnIndex(CHEventConstants.EVENT_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(CHEventConstants.EVENT_NAME)),
                        ""+cursor.getString(cursor.getColumnIndex(CHEventConstants.EVENT_DATE)),
                        ""+cursor.getString(cursor.getColumnIndex(CHEventConstants.EVENT_TIME)),
                        ""+cursor.getString(cursor.getColumnIndex(CHEventConstants.EVENT_DES)),
                        ""+cursor.getString(cursor.getColumnIndex(CHEventConstants.EVENT_ADDED_TIMESTAMP)),
                        ""+cursor.getString(cursor.getColumnIndex(CHEventConstants.EVENT_UPDATED_TIMESTAMP)));
                //add record to list
                recordsList.add(chEventModel);
            }while (cursor.moveToNext());
        }

        //close db connection
        db.close();

        //return the list
        return recordsList;
    }

    //delete data using id
    public  void deleteData(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(CHEventConstants.TABLE_NAME, CHEventConstants.EVENT_ID+" = ?",new String[]{id});
        db.close();

    }
    //delete all data from table
    public void deleteAllData(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + CHEventConstants.TABLE_NAME);
        db.close();

    }


    //get no of records
    public int getRecordsCount(){
        String countQuery = "SELECT * FROM " + CHEventConstants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }

}

