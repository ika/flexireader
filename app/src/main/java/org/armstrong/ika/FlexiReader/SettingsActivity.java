package org.armstrong.ika.FlexiReader;

// https://examples.javacodegeeks.com/android/core/ui/settings/android-settings-example/

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

public class SettingsActivity extends AboutActivity {

    private static String saveColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // restore preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        saveColor = sharedPrefs.getString("prefColorSetting", "-41659");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true); // back arrow
        ab.setTitle(R.string.settings_name);

        // change toolbar color
        ab.setBackgroundDrawable(new ColorDrawable(Integer.parseInt(saveColor)));

        getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }



}
