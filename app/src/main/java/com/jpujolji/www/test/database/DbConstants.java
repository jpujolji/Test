/*
 * Copyright (c) 2016. Jorge Pujol - Todos los derechos reservados.
 * Escrito por Jorge Pujol <jpujolji@gmail.com>, marzo 2016.
 */

package com.jpujolji.www.test.database;

public class DbConstants {

    public static abstract class TableEntry {
        public static final String TABLE_NAME = "tbl_entry";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_SUMMARY = "summary";
        public static final String COLUMN_IMAGE= "image";
        public static final String COLUMN_ARTIST= "artist";
        public static final String COLUMN_PRICE= "price";
        public static final String COLUMN_LINK= "link";
        public static final String COLUMN_RIGHTS= "rights";
        public static final String COLUMN_ID_CATEGORY = "id_category";
    }

    public static abstract class TableCategory {
        public static final String TABLE_NAME = "tbl_category";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_DESCRIPTION = "description";
    }

    static final String CREATE_TABLE_ENTRY = "CREATE TABLE IF NOT EXISTS " +
            TableEntry.TABLE_NAME + "(" +
            TableEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TableEntry.COLUMN_TITLE + " TEXT," +
            TableEntry.COLUMN_SUMMARY + " TEXT," +
            TableEntry.COLUMN_IMAGE + " TEXT," +
            TableEntry.COLUMN_ARTIST + " TEXT," +
            TableEntry.COLUMN_PRICE + " TEXT," +
            TableEntry.COLUMN_LINK + " TEXT," +
            TableEntry.COLUMN_RIGHTS + " TEXT," +
            TableEntry.COLUMN_ID_CATEGORY + " INTEGER);";

    static final String CREATE_TABLE_CATEGORY = "CREATE TABLE IF NOT EXISTS " +
            TableCategory.TABLE_NAME + "(" +
            TableCategory.COLUMN_ID + " INTEGER," +
            TableCategory.COLUMN_DESCRIPTION + " TEXT);";


}