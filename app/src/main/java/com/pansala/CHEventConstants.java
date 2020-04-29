package com.pansala;

public class CHEventConstants {
    //dbname
    public static final String DB_NAME = "pansala.db";
    //db version
    public static final int DB_VERSION = 1;
    //table name
    public static final String TABLE_NAME = "EVENT_TABLE";
    //columns/fields of table
    public static final String EVENT_ID  = "EVENT_ID";
    public static final String EVENT_NAME  = "EVENT_NAME";
    public static final String EVENT_DATE  = "EVENT_DATE";
    public static final String EVENT_TIME  = "EVENT_TIME";
    public static final String EVENT_DES  = "EVENT_DES";
    public static final String EVENT_ADDED_TIMESTAMP  = "ADDED_TIME_STAMP";
    public static final String EVENT_UPDATED_TIMESTAMP  = "UPDATED_TIME_STAMP";

    //Create table query
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + EVENT_NAME + " TEXT,"
            + EVENT_DES + " TEXT,"
            + EVENT_DATE + " TEXT,"
            + EVENT_TIME + " TEXT,"
            + EVENT_ADDED_TIMESTAMP + " TEXT,"
            + EVENT_UPDATED_TIMESTAMP + " TEXT"
            + ")";

}
