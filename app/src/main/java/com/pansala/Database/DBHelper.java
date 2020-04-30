package com.pansala.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pansala.CHEventConstants;
import com.pansala.CHEventModel;
import com.pansala.NConstants;
import com.pansala.NModelRecord;
import com.pansala.Session.Session;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "pansala.db";

    //temple database columns and table names
    public static final String TABLE_NAME ="dahampasala_table";
    public static final String  COL_1 ="ID";
    public static final String COL_2 ="pansalaName";
    public static final String COL_3 ="chiefThero";
    public static final String COL_4 ="dahampasalaName";
    public static final String COL_5 ="studentsCount";
    public static final String COL_6 ="classesCount";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String  SQL_CREATE_ENTRIES = "CREATE TABLE " + UsersMaster.Users.TABLE_NAME + " (" +
                UsersMaster.Users._ID + " INTEGER PRIMARY KEY," +
                UsersMaster.Users.COLUMN_FULLNAME + " Text," +
                UsersMaster.Users.COLUMN_BIRTHDAY + " Text," +
                UsersMaster.Users.COLUMN_NIC + " Number," +
                UsersMaster.Users.COLUMN_EMAIL + " Text," +
                UsersMaster.Users.COLUMN_CONTACTNO + " Number," +
                UsersMaster.Users.COLUMN_PASSWORD + " Text," +
                UsersMaster.Users.COLUMN_TYPE + " Text)" ;

        db.execSQL(SQL_CREATE_ENTRIES); //Create user table

        //Create dahampasal table
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, pansalaName TEXT, chiefThero TEXT, dahampasalaName TEXT, studentsCount INTEGER, classesCount INTEGER  )");

        //Create Event table
        db.execSQL(CHEventConstants.CREATE_TABLE);

        //Create temple table
        db.execSQL(NConstants.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UsersMaster.Users.TABLE_NAME);
    }

    //User control ******************************************************************************************************************************************

    public long addUser(String fullname, String birthday, String nic, String email, String contactNo, String password){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UsersMaster.Users.COLUMN_FULLNAME, fullname);
        values.put(UsersMaster.Users.COLUMN_BIRTHDAY, birthday);
        values.put(UsersMaster.Users.COLUMN_NIC, nic);
        values.put(UsersMaster.Users.COLUMN_EMAIL, email);
        values.put(UsersMaster.Users.COLUMN_CONTACTNO, contactNo);
        values.put(UsersMaster.Users.COLUMN_PASSWORD, password);
        values.put(UsersMaster.Users.COLUMN_TYPE, "user");

        long newUserId = db.insert(UsersMaster.Users.TABLE_NAME, null, values);
        return newUserId;
    }



    public String[] loginUser(String email, String password){

        SQLiteDatabase db = getReadableDatabase();

        String[] projection ={
                UsersMaster.Users._ID,
                UsersMaster.Users.COLUMN_FULLNAME,
                UsersMaster.Users.COLUMN_BIRTHDAY,
                UsersMaster.Users.COLUMN_NIC,
                UsersMaster.Users.COLUMN_EMAIL,
                UsersMaster.Users.COLUMN_CONTACTNO,
                UsersMaster.Users.COLUMN_PASSWORD,
                UsersMaster.Users.COLUMN_TYPE,
        };

        String sortOrder = UsersMaster.Users.COLUMN_FULLNAME + " DESC";

        Cursor cursor = db.query(
                UsersMaster.Users.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
              sortOrder
        );

        while(cursor.moveToNext()){
            String em = cursor.getString(cursor.getColumnIndexOrThrow(UsersMaster.Users.COLUMN_EMAIL));
            String fn = cursor.getString(cursor.getColumnIndexOrThrow(UsersMaster.Users.COLUMN_FULLNAME));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(UsersMaster.Users.COLUMN_TYPE));
            String id = cursor.getString(cursor.getColumnIndexOrThrow(UsersMaster.Users._ID));
            String pw = cursor.getString(cursor.getColumnIndexOrThrow(UsersMaster.Users.COLUMN_PASSWORD));


            if(em.equals(email) && pw.equals(password)){
                String ar[] = new String[3];
                ar[0]= id;
                ar[1] = fn;
                ar[2] = type;
                return ar;
            }

        }
        cursor.close();
        String empty[] = null;
        return empty;
    }


    public boolean getAdmin(){

        SQLiteDatabase db = getReadableDatabase();

        String[] projection ={
                UsersMaster.Users._ID,
                UsersMaster.Users.COLUMN_FULLNAME,
                UsersMaster.Users.COLUMN_BIRTHDAY,
                UsersMaster.Users.COLUMN_NIC,
                UsersMaster.Users.COLUMN_EMAIL,
                UsersMaster.Users.COLUMN_CONTACTNO,
                UsersMaster.Users.COLUMN_PASSWORD,
                UsersMaster.Users.COLUMN_TYPE,
        };



        String sortOrder = UsersMaster.Users.COLUMN_FULLNAME + " DESC";

        Cursor cursor = db.query(
                UsersMaster.Users.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        while(cursor.moveToNext()){
            String type = cursor.getString(cursor.getColumnIndexOrThrow(UsersMaster.Users.COLUMN_TYPE));

            if(type.equals("administrator")) {
                return true;
            }

        }
        cursor.close();
        return false;
    }

    public long addAdmin(){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UsersMaster.Users.COLUMN_FULLNAME, "Admin");
        values.put(UsersMaster.Users.COLUMN_BIRTHDAY, "1976/01/01");
        values.put(UsersMaster.Users.COLUMN_NIC, "123456789");
        values.put(UsersMaster.Users.COLUMN_EMAIL, "admin@pansala.com");
        values.put(UsersMaster.Users.COLUMN_CONTACTNO, "0713168965");
        values.put(UsersMaster.Users.COLUMN_PASSWORD, "Admin123");
        values.put(UsersMaster.Users.COLUMN_TYPE, "administrator");

        long newUserId = db.insert(UsersMaster.Users.TABLE_NAME, null, values);
        return newUserId;
    }

    public void CreateUserTempleTable(String Table_Name)
    {
        SQLiteDatabase db = getWritableDatabase();

        String  SQL_CREATE_USERTEMPLE = "CREATE TABLE IF NOT EXISTS " + Table_Name + " (" +
                TempleMaster.UserTemple._ID + " INTEGER PRIMARY KEY," +
                TempleMaster.UserTemple.COLUMN_TEMPLEID + " Text)" ;

        db.execSQL(SQL_CREATE_USERTEMPLE);
        db.close();
    }

    public Cursor getUserData(int id){

        SQLiteDatabase db = getReadableDatabase();

        String[] projection ={
                UsersMaster.Users._ID,
                UsersMaster.Users.COLUMN_FULLNAME,
                UsersMaster.Users.COLUMN_BIRTHDAY,
                UsersMaster.Users.COLUMN_NIC,
                UsersMaster.Users.COLUMN_EMAIL,
                UsersMaster.Users.COLUMN_CONTACTNO,
                UsersMaster.Users.COLUMN_PASSWORD,
                UsersMaster.Users.COLUMN_TYPE,
        };

        String selection = UsersMaster.Users._ID + "=" + id;

        Cursor cursor = db.query(
                UsersMaster.Users.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
                return cursor;
        }
        return null;
    }

    // Update method
    public boolean updateInfo(int id, String fullName, String birthday, String nic, String email, String contactNo, String password) {

        SQLiteDatabase db = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(UsersMaster.Users.COLUMN_FULLNAME, fullName);
        values.put(UsersMaster.Users.COLUMN_BIRTHDAY, birthday);
        values.put(UsersMaster.Users.COLUMN_NIC, nic);
        values.put(UsersMaster.Users.COLUMN_EMAIL, email);
        values.put(UsersMaster.Users.COLUMN_CONTACTNO, contactNo);
        values.put(UsersMaster.Users.COLUMN_PASSWORD, password);
        values.put(UsersMaster.Users.COLUMN_TYPE, "user");

        // Which row to update
        String selection = UsersMaster.Users._ID + " LIKE ?";
        // Specify arguments n place holder
        String[] selectionArgs = {String.valueOf(id)};


        int count = db.update(UsersMaster.Users.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        if (count == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteAccount(int id) {
        SQLiteDatabase db = getReadableDatabase();

        String selection = UsersMaster.Users._ID + " LIKE ?";

        String[] selectionArgs = {String.valueOf(id)};
        int count = db.delete(UsersMaster.Users.TABLE_NAME, selection, selectionArgs);

        if (count == 1) {
            return true;
        } else {
            return false;
        }
    }


    //Dhampasala DB Methods*********************************************************************************************************************


    public boolean insertData(String pansala_name, String thero_name, String dahampasala_name, String students_count, String classes_count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,pansala_name);
        contentValues.put(COL_3,thero_name);
        contentValues.put(COL_4,dahampasala_name);
        contentValues.put(COL_5,students_count);
        contentValues.put(COL_6,classes_count);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }
        else
            return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME,null);
        return res;
    }
    public boolean updateData(String id, String pansala_name, String thero_name, String dahampasala_name, String students_count, String classes_count){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,pansala_name);
        contentValues.put(COL_3,thero_name);
        contentValues.put(COL_4,dahampasala_name);
        contentValues.put(COL_5,students_count);
        contentValues.put(COL_6,classes_count);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] { id });
        return true;
    }

    public Integer deleteDahampasalaData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }

    //Event Handling *********************************************************************************************************************************************


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

    // Temple Handling *******************************************************************************************************************************

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
    public ArrayList<NModelRecord> getAllTempleRecords(String orderBy){
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
    public ArrayList<NModelRecord> searchTempleRecords(String query){
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
    public  void deleteTempleData(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NConstants.TABLE_NAME,NConstants.C_ID+" = ?",new String[]{id});
        db.close();
    }

    //delete all data from table
    public void deleteAllTempleData(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + NConstants.TABLE_NAME);
        db.close();
    }


    //get no of records
    public int getTempleRecordsCount(){
        String countQuery = "SELECT * FROM " + NConstants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    //Handling user private temples and events

    public long addTemple(String templeId, String UserId){
        String Table_Name = "UserID" + UserId;
        String C_TID  = "tempId";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_TID, templeId);

        long result = db.insert(Table_Name, null, values);
        return result;
    }


    //get all user temple list data
    public ArrayList<NModelRecord> getUsersTempleRecords(String userId){
        //order by query allow to sort data e.g. newest/oldest first, name asc/desc
        //it will return list or records since we have used return type ArrayList<NModelRecord>

        ArrayList<NModelRecord> recordsList = new ArrayList<>();
        //query to select records
        String selectQuery = "SELECT * FROM " + NConstants.TABLE_NAME + " t, " + "UserID" + userId + " u" +
                " WHERE " + "t.ID = u.tempId";
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

    //search user temple data
    public ArrayList<NModelRecord> searchUserTempleRecords(String query, String userId){
        //order by query allow to sort data e.g. newest/oldest first, name asc/desc
        //it will return list or records since we have used return type ArrayList<NModelRecord>

        ArrayList<NModelRecord> recordsList = new ArrayList<>();
        //query to select records
        String selectQuery = "SELECT * FROM " + NConstants.TABLE_NAME + " t, " + "UserID" + userId + " u" +
                " WHERE " + "t.ID = u.tempId" +
         " AND " + NConstants.C_NAME + " LIKE '%" + query +"%'";

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

    //delete user's temple data
    public boolean removeTempleData(String id, String userId){
        SQLiteDatabase db = getWritableDatabase();
        String Table_Name = "UserID"+ userId;
        String ID_COL = "tempId";
        String selection = ID_COL + " LIKE ?";

        String[] selectionArgs = {String.valueOf(id)};
        int count = db.delete(Table_Name, selection, selectionArgs);

        if (count == 1) {
            return true;
        } else {
            return false;
        }
    }


}
