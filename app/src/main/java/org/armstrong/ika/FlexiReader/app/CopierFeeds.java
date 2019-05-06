package org.armstrong.ika.FlexiReader.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.armstrong.ika.FlexiReader.feedsdb.FeedsDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.armstrong.ika.FlexiReader.app.Constants.DB_NAME_FEEDS;

public class CopierFeeds {

    private Context context;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    // database version
    private static final int DB_VERSION = 2;

    // database name
    private String DB_NAME = null;

    public CopierFeeds(Context context) {
        this.context = context;

        // read saved preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public void initializeCopier() {

        this.DB_NAME = DB_NAME_FEEDS;

        installOrUpdateIfNecessary();

    }

    private void installOrUpdateIfNecessary() {

        if (installedDatabaseIsOutdated()) {

            context.deleteDatabase(DB_NAME);

            installDatabaseFromAssets();

            writeDatabaseVersionInPreferences();

            FeedsDatabase.getInstance(context);

        }

    }

    private boolean installedDatabaseIsOutdated() {

        int version = sharedPreferences.getInt(DB_NAME, 0);

        return (version < DB_VERSION) ? true : false;

    }

    private void writeDatabaseVersionInPreferences() {

        sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putInt(DB_NAME, DB_VERSION);
        sharedPreferencesEditor.apply();
    }

    private void installDatabaseFromAssets() {

        final File dbPath = context.getDatabasePath(DB_NAME);
        dbPath.getParentFile().mkdirs();

        try {
            final InputStream inputStream = context.getAssets().open(DB_NAME);
            final OutputStream outputStream = new FileOutputStream(dbPath);

            byte[] buffer = new byte[8192];
            int length;

            while ((length = inputStream.read(buffer, 0, 8192)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();

            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}



