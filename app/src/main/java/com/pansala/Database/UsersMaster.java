package com.pansala.Database;

import android.provider.BaseColumns;

public final class UsersMaster {
    public UsersMaster() {
    }

    public static class Users implements BaseColumns{
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_FULLNAME = "fullname";
        public static final String COLUMN_BIRTHDAY = "birthday";
        public static final String COLUMN_NIC = "nic";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_CONTACTNO = "contactno";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_TYPE = "type";
    }
}
