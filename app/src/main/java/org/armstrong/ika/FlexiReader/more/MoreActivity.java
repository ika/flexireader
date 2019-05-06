package org.armstrong.ika.FlexiReader.more;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.TextView;

import org.armstrong.ika.FlexiReader.MainActivity;
import org.armstrong.ika.FlexiReader.R;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MoreActivity extends AppCompatActivity {

    public static MoreActivity instance;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    private ActionBar ab;

    private static TextView textOne;
    private static TextView textTwo;

    private String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_activity);

        instance = this;

        // read saved preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        color = sharedPreferences.getString("color", Integer.toString(R.color.colorPrimaryDark));

        // Toolbar layout
        Toolbar toolbar = findViewById(R.id.more_toolbar);
        setSupportActionBar(toolbar);

        // custom title bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_activity);

        ab = getSupportActionBar();
        // set back arrow
        ab.setDisplayHomeAsUpEnabled(true);
        // change toolbar color
        ab.setBackgroundDrawable(new ColorDrawable(Integer.parseInt(color)));

        // Action Bar Text One
        TextView textOne = toolbar.findViewById(R.id.action_bar_title);
        textOne.setText("More");
        textOne.setTextColor(Color.parseColor("#FFFFFF"));
        textOne.setTextSize(18);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.more_frame_layout, MoreFragment.newInstance())
                .commitNow();


    }

    public static MoreActivity getInstance() {
        return instance;
    }

    public void setBackgroundColors(int selectedColor) {
        // change toolbar colour
        ab.setBackgroundDrawable(new ColorDrawable(selectedColor));

        // update settings colour
        sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString("color", Integer.toString(selectedColor));
        sharedPreferencesEditor.commit();
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
        Intent intent = new Intent(MoreActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
