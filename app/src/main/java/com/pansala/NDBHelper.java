package com.pansala;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NDBHelper extends SQLiteOpenHelper {


    public NDBHelper(@Nullable Context context) {
        super(context, NConstants.DB_NAME, null, NConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table on that db
        db.execSQL(NConstants.CREATE_TABLE);


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //upgrade database(if there any structure change the change db version)

        //drop all the table if exist
        db.execSQL("DROP TABLE IF EXISTS "+ NConstants.TABLE_NAME);
        //create table again
        onCreate(db);

    }

    //insert record to db
    public long insertRecord(String name, String image, String history, String start,
                             String monk, String location, String phone, String addedTime, String updatedTime){

        //get writable database because we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //id will set automatically as set auto increment in query

        //insert data
        values.put(NConstants.C_NAME, name);
        values.put(NConstants.C_IMAGE, image);
        values.put(NConstants.C_HISTORY, history);
        values.put(NConstants.C_START, start);
        values.put(NConstants.C_MONK, monk);
        values.put(NConstants.C_LOCATION, location);
        values.put(NConstants.C_PHONE, phone);
        values.put(NConstants.C_ADDED_TIMESTAMP, addedTime);
        values.put(NConstants.C_UPDATED_TIMESTAMP, updatedTime);

        //insert row, it will return record id of saved record
        long id = db.insert(NConstants.TABLE_NAME, null,values);

        //close db connection
        db.close();

        //return id of inserted records
        return id;
    }

    //update existing record to db
    public void updateRecord(String id, String name, String image, String history, String start,
                             String monk, String location, String phone, String addedTime, String updatedTime){

        //get writable database because we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //id will set automatically as set auto increment in query

        //insert data
        values.put(NConstants.C_NAME, name);
        values.put(NConstants.C_IMAGE, image);
        values.put(NConstants.C_HISTORY, history);
        values.put(NConstants.C_START, start);
        values.put(NConstants.C_MONK, monk);
        values.put(NConstants.C_LOCATION, location);
        values.put(NConstants.C_PHONE, phone);
        values.put(NConstants.C_ADDED_TIMESTAMP, addedTime);
        values.put(NConstants.C_UPDATED_TIMESTAMP, updatedTime);

        //insert row, it will return record id of saved record
        db.update(NConstants.TABLE_NAME, values,NConstants.C_ID + " = ?", new String[]{id});

        //close db connection
        db.close();

    }

    //get all data
    public ArrayList<NModelRecord> getAllRecords(String orderBy){
        //order by query allow to sort data e.g. newest/oldest first, name asc/desc
        //it will return list or records since we have used return type ArrayList<NModelRecord>

        ArrayList<NModelRecord> recordsList = new ArrayList<>();
        //query to select records
        String selectQuery = "SELECT * FROM " + NConstants.TABLE_NAME + " ORDER BY " + orderBy;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all the records and add to list
        if (cursor.moveToFirst()){
            do{
                NModelRecord nmodelRecord = new NModelRecord(
                        ""+cursor.getInt(cursor.getColumnIndex(NConstants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(NConstants.C_NAME)),
                        ""+cursor.getString(cursor.getColumnIndex(NConstants.C_IMAGE)),
                        ""+cursor.getString(cursor.getColumnIndex(NConstants.C_HISTORY)),
                        ""+cursor.getString(cursor.getColumnIndex(NConstants.C_START)),
                        ""+cursor.getString(cursor.getColumnIndex(NConstants.C_MONK)),
                        ""+cursor.getString(cursor.getColumnIndex(NConstants.C_LOCATION)),
                        ""+cursor.getString(cursor.getColumnIndex(NConstants.C_PHONE)),
                        ""+cursor.getString(cursor.getColumnIndex(NConstants.C_ADDED_TIMESTAMP)),
                        ""+cursor.getString(cursor.getColumnIndex(NConstants.C_UPDATED_TIMESTAMP)));
                //add record to list
                recordsList.add(nmodelRecord);
            }while (cursor.moveToNext());
        }

        //close db connection
        db.close();

        //return the list
        return recordsList;
    }

    //search data
    public ArrayList<NModelRecord> searchRecords(String query){
        //order by query allow to sort data e.g. newest/oldest first, name asc/desc
        //it will return list or records since we have used return type ArrayList<NModelRecord>

        ArrayList<NModelRecord> recordsList = new ArrayList<>();
        //query to select records
        String selectQuery = "SELECT * FROM " + NConstants.TABLE_NAME + " WHERE " + NConstants.C_NAME + " LIKE '%" + query +"%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all the records and add to list
        if (cursor.moveToFirst()){
            do{
                NModelRecord nmodelRecord = new NModelRecord(
                        ""+cursor.getInt(cursor.getColumnIndex(NConstants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(NConstants.C_NAME)),
                        ""+cursor.getString(cursor.getColumnIndex(NConstants.C_IMAGE)),
                        ""+cursor.getString(cursor.getColumnIndex(NConstants.C_HISTORY)),
                        ""+cursor.getString(cursor.getColumnIndex(NConstants.C_START)),
                        ""+cursor.getString(cursor.getColumnIndex(NConstants.C_MONK)),
                        ""+cursor.getString(cursor.getColumnIndex(NConstants.C_LOCATION)),
                        ""+cursor.getString(cursor.getColumnIndex(NConstants.C_PHONE)),
                        ""+cursor.getString(cursor.getColumnIndex(NConstants.C_ADDED_TIMESTAMP)),
                        ""+cursor.getString(cursor.getColumnIndex(NConstants.C_UPDATED_TIMESTAMP)));
                //add record to list
                recordsList.add(nmodelRecord);
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
        db.delete(NConstants.TABLE_NAME,NConstants.C_ID+" = ?",new String[]{id});
        db.close();

    }
    //delete all data from table
    public void deleteAllData(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + NConstants.TABLE_NAME);
        db.close();

    }


    //get no of records
    public int getRecordsCount(){
        String countQuery = "SELECT * FROM " + NConstants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }

}

