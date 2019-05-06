package org.armstrong.ika.FlexiReader.feeds;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.armstrong.ika.FlexiReader.add.AddRecordActivity;
import org.armstrong.ika.FlexiReader.R;
import org.armstrong.ika.FlexiReader.more.MoreActivity;

public class FeedActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    private String color;
    private String textSize;

    private ActionBar ab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_activity);

        // read saved preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        color = sharedPreferences.getString("color", Integer.toString(R.color.colorPrimaryDark));
        textSize = sharedPreferences.getString("textSize", "14");

        Toolbar toolbar = findViewById(R.id.feed_toolbar);
        setSupportActionBar(toolbar);

        // custom title bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_activity);

        ab = getSupportActionBar();
        // back arrow
        ab.setDisplayHomeAsUpEnabled(true);
        // change toolbar color
        ab.setBackgroundDrawable(new ColorDrawable(Integer.parseInt(color)));

        // Action Bar Text One
        TextView textOne = toolbar.findViewById(R.id.action_bar_title);
        textOne.setText("News Feeds");
        textOne.setTextColor(Color.parseColor("#FFFFFF"));
        textOne.setTextSize(18);

        // Floating Action Button
        FloatingActionButton fab1 = findViewById(R.id.fab1);
        fab1.setBackgroundTintList(ColorStateList.valueOf(Integer.parseInt(color)));

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FeedActivity.this, AddRecordActivity.class);
                startActivity(intent);
            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.feed_frame_layout, FeedFragment.newInstance(textSize))
                .commitNow();

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                this.onBackPressed();
                break;

            default:
                break;
        }
        return true;
    }

//    public void refreshFeeds() {
//        finish();
//        startActivity(getIntent());
//    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FeedActivity.this, MoreActivity.class);
        startActivity(intent);
    }


}