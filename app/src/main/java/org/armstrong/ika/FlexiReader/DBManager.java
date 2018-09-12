package org.armstrong.ika.FlexiReader;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static org.armstrong.ika.FlexiReader.DatabaseHelper.COLUMN_HASH;
import static org.armstrong.ika.FlexiReader.DatabaseHelper.COLUMN_TIMESTAMP;
import static org.armstrong.ika.FlexiReader.DatabaseHelper.TABLE_NAME;
import static org.armstrong.ika.FlexiReader.DatabaseHelper.TABLE_NAME_CACHE;

import static org.armstrong.ika.FlexiReader.DatabaseHelper.COLUMN_PDATE;
import static org.armstrong.ika.FlexiReader.DatabaseHelper.COLUMN_DESCRIPTION;
import static org.armstrong.ika.FlexiReader.DatabaseHelper.COLUMN_FEEDID;
import static org.armstrong.ika.FlexiReader.DatabaseHelper.COLUMN_IMAGEURL;
import static org.armstrong.ika.FlexiReader.DatabaseHelper.COLUMN_LINK;
import static org.armstrong.ika.FlexiReader.DatabaseHelper.COLUMN_TITLE;
import static org.armstrong.ika.FlexiReader.DatabaseHelper.COLUMN_ID;

public class DBManager {

    private DatabaseHelper dbHelper;
    private final Context context;
    private SQLiteDatabase database;

    public DBManager(Context c) {

        context = c;
    }

    private void open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
    }

    public void close() {
        dbHelper.close();
    }

    public boolean deleteCacheByFeed(String feedID) {

        String sql = "DELETE FROM " + TABLE_NAME_CACHE
                + " WHERE " + COLUMN_FEEDID + " = '" + feedID + "'";
        open();
        database = dbHelper.getWritableDatabase();
        database.execSQL(sql);
        database.close();

        return true;
    }

    public boolean deleteCacheByDate(String  hours) {

        int h = Integer.parseInt(hours);

        long unixTime = System.currentTimeMillis() / 1000L;
        int limit = 60 * 60 * h; // 0 hours - clear cache
        int offset = (int) (unixTime - limit);

        //Log.e("LOGGING", "deleteCacheByDate UNIXTIME: " + unixTime);
        //Log.e("LOGGING", "deleteCacheByDate OFFSET: " + offset);

        String sql = "DELETE FROM " + TABLE_NAME_CACHE
                + " WHERE " + COLUMN_TIMESTAMP + " < '" + offset + "'";
        open();
        database = dbHelper.getWritableDatabase();
        database.execSQL(sql);
        database.close();

        return true;
    }

    public int countCacheByHash(String hash, String feedID) {

        String sql = "SELECT * FROM " + TABLE_NAME_CACHE
                + " WHERE " + COLUMN_HASH + " = '" + hash + "'"
                + " AND " + COLUMN_FEEDID + " = '" + feedID + "'";
        //open();
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, null);

        int cnt = cursor.getCount();

        cursor.close();
        //database.close();

        return cnt;
    }

    public int countCacheByFeed(String feedID) {

        String sql = "SELECT * FROM " + TABLE_NAME_CACHE
                + " WHERE " + COLUMN_FEEDID + " = '" + feedID + "'";
        open();
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, null);

        int cnt = cursor.getCount();

        cursor.close();
        database.close();

        return cnt;
    }

    public int countCache() {

        String sql = "SELECT * FROM " + TABLE_NAME_CACHE;
        open();
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, null);

        int cnt = cursor.getCount();

        cursor.close();
        database.close();

        return cnt;
    }

    public ArrayList<Article> getAllCacheRecords(String feedID) {

        ArrayList<Article> values = new ArrayList<Article>();

        String sql = "SELECT "
                + COLUMN_TITLE + ", "
                + COLUMN_LINK + ", "
                + COLUMN_DESCRIPTION + ", "
                + COLUMN_PDATE + ", "
                + COLUMN_IMAGEURL + ", "
                + COLUMN_FEEDID
                + " FROM " + TABLE_NAME_CACHE
                + " WHERE " + COLUMN_FEEDID + " = '" + feedID + "'"
                + " ORDER BY " + COLUMN_ID + " DESC";

        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, null);

        Article value;
        if (cursor.moveToFirst()) {
            do {
                value = new Article();

                value.setTitle(cursor.getString(0));
                value.setLink(cursor.getString(1));
                value.setDescription(cursor.getString(2));
                value.setpubDate(cursor.getString(3));
                value.setImageUrl(cursor.getString(4));
                value.setFeedID(cursor.getString(5));

                values.add(value);

            } while (cursor.moveToNext());
        }
        //cursor.close();
        database.close();

        return values;
    }

    public boolean insertCacheRecord(ArrayList<Article> articles ) {

        open();
        database = dbHelper.getWritableDatabase();
        
        ContentValues contentValue = new ContentValues();

        List<Article> allValues = articles;

        for (Article value : allValues) {

            contentValue.put(COLUMN_TITLE, value.getTitle());
            contentValue.put(COLUMN_LINK, value.getLink());
            contentValue.put(COLUMN_DESCRIPTION, value.getDescription());
            contentValue.put(COLUMN_PDATE, value.getpubDate());
            contentValue.put(COLUMN_IMAGEURL, value.getImageUrl());
            contentValue.put(COLUMN_FEEDID, value.getFeedID());
            contentValue.put(COLUMN_HASH, value.getHash());

            int cnt = this.countCacheByHash(value.getHash(), value.getFeedID());

            if(cnt < 1) {
                long id = database.insert(TABLE_NAME_CACHE, null, contentValue);
            }
        }

        database.close();

        return true;
    }

    public long insertRecord(ValuesModel value) {

        ContentValues contentValue = new ContentValues();

        contentValue.put(COLUMN_TITLE, value.getTitle());
        contentValue.put(COLUMN_LINK, value.getLink());
        contentValue.put(COLUMN_FEEDID, value.getFeedID());

        open();
        database = dbHelper.getWritableDatabase();
        long id = database.insert(TABLE_NAME, null, contentValue);
        database.close();

        return id;
    }

    public Cursor getCursorRecords() {

        String[] columns = new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_LINK, COLUMN_FEEDID};

        open();
        database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        database.close();

        return cursor;
    }

    public void updateRecord(ValuesModel value) {

        String sql = "UPDATE "
                + TABLE_NAME
                + " SET "
                + COLUMN_TITLE + " = '" + value.getTitle() + "', "
                + COLUMN_LINK + " = '" + value.getLink()
                + "' WHERE " + COLUMN_ID + " = '" + value.getId() + "'";
        open();
        database = dbHelper.getWritableDatabase();
        database.execSQL(sql);
        database.close();
    }

    public void deleteRecord(ValuesModel value) {

        String sql = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = '" + value.getId() + "'";
        open();
        database = dbHelper.getWritableDatabase();
        database.execSQL(sql);
        database.close();
    }

    public int countRecords() {

        String sql = "SELECT * FROM " + TABLE_NAME;
        open();
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, null);

        int cnt = cursor.getCount();

        cursor.close();
        database.close();

        return cnt;
    }

    public ArrayList<ValuesModel> getAllRecords() {

        ArrayList<ValuesModel> values = new ArrayList<ValuesModel>();

        String sql = "SELECT "
                + COLUMN_ID + ", "
                + COLUMN_TITLE + ", "
                + COLUMN_LINK + ", "
                + COLUMN_FEEDID
                + " FROM " + TABLE_NAME;

        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, null);

        ValuesModel valuesModel;
        if (cursor.moveToFirst()) {
            do {
                valuesModel = new ValuesModel();

                valuesModel.setId(cursor.getString(0));
                valuesModel.setTitle(cursor.getString(1));
                valuesModel.setLink(cursor.getString(2));
                valuesModel.setFeedID(cursor.getString(3));

                values.add(valuesModel);

            } while (cursor.moveToNext());
        }
        //cursor.close();
        database.close();

        return values;
    }

    public boolean checkIDexists(ValuesModel value) {

        boolean state = true;

        String sql = "SELECT * FROM " + TABLE_NAME
                + " WHERE " + COLUMN_ID + " = '" + value.getId() + "'";

        open();
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            state = false;
        }
        cursor.close();
        database.close();

        return state;

    }

    public Cursor getRecordById(ValuesModel value) {

        String sql = "SELECT "
                + COLUMN_ID + ", "
                + COLUMN_TITLE + ", "
                + COLUMN_LINK + ", "
                + COLUMN_FEEDID
                + " FROM " + TABLE_NAME
                + " WHERE " + COLUMN_ID + " = '" + value.getId() + "'";

        open();
        database = dbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery(sql, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        close();

        return cursor;

    }

    public Cursor getOneRow() { // select first row

        String sql = "SELECT "
                + COLUMN_ID + ", "
                + COLUMN_TITLE + ", "
                + COLUMN_LINK + ", "
                + COLUMN_FEEDID
                + " FROM " + TABLE_NAME
                + " ORDER BY " + COLUMN_ID
                + " ASC LIMIT 1";

        open();
        database = dbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery(sql, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        database.close();

        return cursor;

    }


}
