package org.armstrong.ika.FlexiReader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;

public class ColorActivity extends AboutActivity {

    String TAG = "LOG";
    private static String saveColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        saveColor = preferences.getString("prefColorSetting", "-41659");

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab;
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true); // back arrow
        ab.setTitle(R.string.activity_color);

        // change initial toolbar color
        ab.setBackgroundDrawable(new ColorDrawable(Integer.parseInt(saveColor)));

        // Color Picker
        ColorPickerView colorPickerView = findViewById(R.id.color_picker_view);

        colorPickerView.setInitialColor(Integer.parseInt(saveColor), true);

        colorPickerView.addOnColorChangedListener(new OnColorChangedListener() {

            public void onColorChanged(int selectedColor) {
                // Handle on color change
                saveColor = Integer.toString(selectedColor);

                ab.setBackgroundDrawable(new ColorDrawable(selectedColor));
                //""(TAG, "onColorChanged: 0x" + Integer.toHexString(selectedColor));
                //""(TAG, "onColorChanged: 0x" + selectedColor);
            }
        });


    }

    public void onPause() {
        super.onPause();
        saveSelection();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.about, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        switch (id) {
//
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void saveSelection() {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("prefColorSetting", saveColor);
        editor.apply();

    }

    @Override
    public void onBackPressed() { //back button
        Intent intent = new Intent(ColorActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
