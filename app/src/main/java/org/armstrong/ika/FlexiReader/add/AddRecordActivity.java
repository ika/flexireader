package org.armstrong.ika.FlexiReader.add;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.TextView;

import org.armstrong.ika.FlexiReader.R;
import org.armstrong.ika.FlexiReader.feeds.FeedActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddRecordActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    private String color;

    private ActionBar ab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.feed_add);

        // read saved preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        color = sharedPreferences.getString("color", Integer.toString(R.color.colorPrimaryDark));

        Toolbar toolbar = findViewById(R.id.add_toolbar);
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
        textOne.setText("Add Feed");
        textOne.setTextColor(Color.parseColor("#FFFFFF"));
        textOne.setTextSize(18);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.add_frame_layout, AddRecordFragment.newInstance())
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddRecordActivity.this, FeedActivity.class);
        startActivity(intent);
    }

}
