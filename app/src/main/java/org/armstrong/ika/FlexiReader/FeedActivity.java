package org.armstrong.ika.FlexiReader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class FeedActivity extends AboutActivity {

    private static String saveColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_records);

        // restore preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        saveColor = sharedPrefs.getString("prefColorSetting", "-41659");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true); // back arrow
        ab.setTitle(R.string.feeds_name);
        // change toolbar color
        ab.setBackgroundDrawable(new ColorDrawable(Integer.parseInt(saveColor)));

        // Floating Action Button
        FloatingActionButton fab1 = findViewById(R.id.fab1);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FeedActivity.this, AddRecordActivity.class);
                startActivity(intent);
            }
        });

        // static fragment FeedFragment

    }

    public void refreshFeeds() {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FeedActivity.this, MainActivity.class);
        startActivity(intent);
    }


}