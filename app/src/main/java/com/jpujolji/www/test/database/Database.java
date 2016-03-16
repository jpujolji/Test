/*
 * Copyright (c) 2016. Jorge Pujol - Todos los derechos reservados.
 * Escrito por Jorge Pujol <jpujolji@gmail.com>, marzo 2016.
 */

package com.jpujolji.www.test.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jpujolji.www.test.models.Category;
import com.jpujolji.www.test.models.Entry;

import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final String DB_NAME = "database.db";
    private static final int DB_VERSION = 1;

    private DbHelper dbHelper;
    Context context;

    SQLiteDatabase db;

    public Database(Context mContext) {
        context = mContext;
        dbHelper = new DbHelper(context, DB_NAME, null, DB_VERSION);
    }

    // Abre una nueva conexión a la base de datos.
    public Database open() throws Exception {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void insertEntries(List<Entry> entries) {
        db.beginTransaction();
        db.delete(DbConstants.TableCategory.TABLE_NAME, null, null);
        db.delete(DbConstants.TableEntry.TABLE_NAME, null, null);
        for (Entry entry : entries) {
            ContentValues values = new ContentValues();
            values.putNull(DbConstants.TableEntry.COLUMN_ID);
            values.put(DbConstants.TableEntry.COLUMN_TITLE, entry.title);
            values.put(DbConstants.TableEntry.COLUMN_ID_CATEGORY, entry.category.id);
            values.put(DbConstants.TableEntry.COLUMN_SUMMARY, entry.summary);
            values.put(DbConstants.TableEntry.COLUMN_IMAGE, entry.image);
            values.put(DbConstants.TableEntry.COLUMN_ARTIST, entry.artist);
            values.put(DbConstants.TableEntry.COLUMN_PRICE, entry.price);
            values.put(DbConstants.TableEntry.COLUMN_LINK, entry.link);
            values.put(DbConstants.TableEntry.COLUMN_RIGHTS, entry.rights);
            db.insert(DbConstants.TableEntry.TABLE_NAME, null, values);

            Cursor cursor = db.rawQuery("SELECT * FROM " + DbConstants.TableCategory.TABLE_NAME +
                    " WHERE " + DbConstants.TableCategory.COLUMN_ID + " = " + entry.category.id, null);

            if (!cursor.moveToFirst()) {
                values = new ContentValues();
                values.put(DbConstants.TableCategory.COLUMN_ID, entry.category.id);
                values.put(DbConstants.TableCategory.COLUMN_DESCRIPTION, entry.category.description);
                db.insert(DbConstants.TableCategory.TABLE_NAME, null, values);
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        Cursor cursor = db.query(DbConstants.TableCategory.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category(cursor.getInt(0), cursor.getString(1));
                categories.add(category);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return categories;
    }

    public List<Entry> getEntries(int idCategory) {
        List<Entry> entries = new ArrayList<>();
        Cursor cursor;
        if (idCategory != 0) {
            cursor = db.query(DbConstants.TableEntry.TABLE_NAME, null,
                    DbConstants.TableEntry.COLUMN_ID_CATEGORY + "=?", new String[]{"" + idCategory}, null, null, null);
        } else {
            cursor = db.query(DbConstants.TableEntry.TABLE_NAME, null, null, null, null, null, null);
        }

        if (cursor.moveToFirst()) {
            do {
                Entry entry = new Entry(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
                entries.add(entry);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return entries;
    }

    public Entry getEntry(int idEntry) {
        Entry entry = null;
        Cursor cursor = db.query(DbConstants.TableEntry.TABLE_NAME, null,
                DbConstants.TableEntry.COLUMN_ID + "=?", new String[]{"" + idEntry}, null, null, null);
        if (cursor.moveToFirst()) {
            entry = new Entry(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));

        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return entry;
    }
}
