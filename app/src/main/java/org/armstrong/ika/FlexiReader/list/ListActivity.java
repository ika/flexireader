package org.armstrong.ika.FlexiReader.list;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import org.armstrong.ika.FlexiReader.MainActivity;
import org.armstrong.ika.FlexiReader.R;
import org.armstrong.ika.FlexiReader.add.AddRecordActivity;
import org.armstrong.ika.FlexiReader.app.Utils;
import org.armstrong.ika.FlexiReader.more.MoreActivity;

public class ListActivity extends AppCompatActivity {

    public static ListActivity instance;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    private AHBottomNavigation bottomNavigation;

    public SearchView searchView;
    public ImageView closeButton;
    public EditText searchText;

    private ActionBar ab;

    private String color;
    String textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        instance = this;

        // read saved preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        color = sharedPreferences.getString("color", Integer.toString(R.color.colorPrimaryDark));
        textSize = sharedPreferences.getString("textSize", "16");

        searchView = findViewById(R.id.searchesInput);

        // input text field
        searchText = searchView.findViewById(R.id.search_src_text);

        // listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.list_frame_layout, ListFragment.newInstance(query))
                        .commitNow();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.list_frame_layout, ListFragment.newInstance(query))
                        .commitNow();

                return false;
            }

        });

        // Catch event on [x] button inside search view
        closeButton = searchView.findViewById(R.id.search_close_btn);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // clear search field
                searchText.setText("");

                hideSoftKeyboard(searchText);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.list_frame_layout, ListFragment.newInstance(""))
                        .commitNow();

            }
        });

        // Toolbar layout
        Toolbar toolbar = findViewById(R.id.list_toolbar);
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
        textOne.setText(getString(R.string.list));
        textOne.setTextColor(Color.parseColor("#FFFFFF"));
        textOne.setTextSize(Integer.parseInt(textSize) + 4);

        // Bottom navigation
        bottomNavigation = findViewById(R.id.bottom_navigation);

        int leftTitle = R.string.blank;
        int leftIcon = R.drawable.ic_bookmark_black_24dp;
        int leftColor = R.color.colorBottomNavigationActiveColored;

        // Bottom Menu
        AHBottomNavigationItem bookmark_item;
        bookmark_item = new AHBottomNavigationItem(leftTitle, leftIcon, leftColor);

        int midTitle = R.string.blank;
        int midIcon = R.drawable.ic_local_library_black_24dp;
        int midColor = R.color.colorBottomNavigationDisable;

        AHBottomNavigationItem bible_item;
        bible_item = new AHBottomNavigationItem(midTitle, midIcon, midColor);

        int rightTitle = R.string.blank;
        int rightIcon = R.drawable.ic_more_black_24dp;
        int rightColor = R.color.colorBottomNavigationNotification;

        AHBottomNavigationItem settings_item;
        settings_item = new AHBottomNavigationItem(rightTitle, rightIcon, rightColor);

        // Add items
        bottomNavigation.addItem(bookmark_item);
        bottomNavigation.addItem(bible_item);
        bottomNavigation.addItem(settings_item);

        // Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

        // Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);

        // Change colors
        bottomNavigation.setAccentColor(Integer.parseInt(color));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

        // Manage titles
//        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
//        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

        // Set current item programmatically
        bottomNavigation.setCurrentItem(0);

        // Quick return animation
        bottomNavigation.setBehaviorTranslationEnabled(true);

        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {

            public boolean onTabSelected(int position, boolean wasSelected) {

                switch (position) {
                    case 0:
                        if (!wasSelected) {
                            Utils.makeToast(getApplicationContext(), "list");
                        }
                        break;
                    case 1:
                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivity);
                        break;
                    case 2:
                        Intent moreActivity = new Intent(getApplicationContext(), MoreActivity.class);
                        startActivity(moreActivity);
                        break;
                }

                return true;
            }
        });

        // Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.list_frame_layout, ListFragment.newInstance(""))
                .commitNow();


    }

    public static ListActivity getInstance() {
        return instance;
    }

    public void setBackgroundColors(int selectedColor) {
        // change toolbar colour
        ab.setBackgroundDrawable(new ColorDrawable(selectedColor));

        // update settings colour
        sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString("color", Integer.toString(selectedColor));
        sharedPreferencesEditor.apply();
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                this.onBackPressed();
                break;

            case R.id.add_icon:
                Intent addRecord = new Intent(ListActivity.this, AddRecordActivity.class);
                startActivity(addRecord);
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ListActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }
}
