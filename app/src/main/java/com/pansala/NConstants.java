package com.pansala;

public class NConstants {
    //dbname
    public static final String DB_NAME = "pansala.db";
    //db version
    public static final int DB_VERSION = 1;
    //table name
    public static final String TABLE_NAME = "TEMPLE_LIST_TABLE";
    //columns/fields of table
    public static final String C_ID  = "ID";
    public static final String C_NAME  = "NAME";
    public static final String C_IMAGE  = "IMAGE";
    public static final String C_HISTORY  = "HISTORY";
    public static final String C_START  = "START";
    public static final String C_MONK  = "MONK";
    public static final String C_LOCATION  = "LOCATION";
    public static final String C_PHONE  = "PHONE";
    public static final String C_ADDED_TIMESTAMP  = "ADDED_TIME_STAMP";
    public static final String C_UPDATED_TIMESTAMP  = "UPDATED_TIME_STAMP";

    //Create table query
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + C_NAME + " TEXT,"
            + C_IMAGE + " TEXT,"
            + C_HISTORY + " TEXT,"
            + C_START + " TEXT,"
            + C_MONK + " TEXT,"
            + C_LOCATION + " TEXT,"
            + C_PHONE + " TEXT,"
            + C_ADDED_TIMESTAMP + " TEXT,"
            + C_UPDATED_TIMESTAMP + " TEXT"
            + ")";

}
