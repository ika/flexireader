package org.armstrong.ika.FlexiReader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

public class ModifyRecordActivity extends AboutActivity {

    private static String saveColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modify_record);

        // restore preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        saveColor = sharedPrefs.getString("prefColorSetting", "-41659");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true); // back arrow
        ab.setTitle(R.string.feeds_modify_record);

        // change toolbar color
        ab.setBackgroundDrawable(new ColorDrawable(Integer.parseInt(saveColor)));

        // static fragment ModifyRecordFragment


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ModifyRecordActivity.this, FeedActivity.class);
        startActivity(intent);
    }


}
