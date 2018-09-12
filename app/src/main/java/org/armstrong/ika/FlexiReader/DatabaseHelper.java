package org.armstrong.ika.FlexiReader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // database name
    private static final String DB_NAME = "FlexiReader.db";

    // database version
    private static final int DB_VERSION = 2;

    // Table Namees
    public static final String TABLE_NAME = "feeds";
    public static final String TABLE_NAME_CACHE = "cache";

    // Table columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PDATE = "pubDate";
    public static final String COLUMN_IMAGEURL = "imageUrl";
    public static final String COLUMN_FEEDID = "feedID";
    public static final String COLUMN_HASH = "hash";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    //public static final String TIME_FUNCTION =  '(strftime('%s', 'now'));

    // Create feeds
    private static final String CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME
            + "( "
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_LINK + " TEXT, "
            + COLUMN_FEEDID + " TEXT "
            +" )";

    // create cache
    private static final String CREATE_CACHE = "CREATE TABLE "
            + TABLE_NAME_CACHE
            + "( "
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_LINK + " TEXT, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_PDATE +" TEXT, "
            + COLUMN_IMAGEURL + " TEXT, "
            + COLUMN_FEEDID + " TEXT, "
            + COLUMN_HASH + " TEXT, "
            + COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT (strftime('%s', 'now'))" // automatic insert
            +" )";

    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_CACHE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CACHE);
        onCreate(db);
    }
}