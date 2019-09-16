package org.armstrong.ika.FlexiReader.modify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.TextView;

import org.armstrong.ika.FlexiReader.R;
import org.armstrong.ika.FlexiReader.list.ListActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ModifyRecordActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    private String color;

    private ActionBar ab;

    String textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.feed_modify);

        // read saved preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        color = sharedPreferences.getString("color", Integer.toString(R.color.colorPrimaryDark));
        textSize = sharedPreferences.getString("textSize", "16");

        Toolbar toolbar = findViewById(R.id.modify_toolbar);
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
        textOne.setText(getString(R.string.modify_feed));
        textOne.setTextColor(Color.parseColor("#FFFFFF"));
        textOne.setTextSize(Integer.parseInt(textSize) + 4);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.modify_frame_layout, ModifyRecordFragment.newInstance())
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
        Intent intent = new Intent(ModifyRecordActivity.this, ListActivity.class);
        startActivity(intent);
    }


}
