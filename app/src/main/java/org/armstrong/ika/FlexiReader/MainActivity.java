package org.armstrong.ika.FlexiReader;

// http://camposha.info/source/android-rss-listview-downlaodparseshow-headlines-with-images-and-text-source
// https://www.androidhive.info/2015/05/android-swipe-down-to-refresh-listview-tutorial/

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.armstrong.ika.FlexiReader.app.CopierFeeds;
import org.armstrong.ika.FlexiReader.cachedb.CacheDatabase;
import org.armstrong.ika.FlexiReader.feedsdb.FeedsDatabase;
import org.armstrong.ika.FlexiReader.feedsdb.FeedsDoa;
import org.armstrong.ika.FlexiReader.feedsdb.FeedsEntities;
import org.armstrong.ika.FlexiReader.main.MainFragment;
import org.armstrong.ika.FlexiReader.more.MoreActivity;
import org.armstrong.ika.FlexiReader.app.Utils;

import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static MainActivity instance;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    protected FeedsDatabase feedsDatabase;

    private ActionBar ab;

    private TextView textOne;
    private TextView textTwo;
    private String color;

    private static int savedID = 0; // _id
    private static String urlAddress = null; // link
    private static String feedID = ""; // feedID
    private static String savedTitle; // ab title
    private String prefCache;
    private String textSize;

    protected CacheDatabase cacheDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        instance = this;

        new CopierFeeds(this).initializeCopier();

        // read saved preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        color = sharedPreferences.getString("color", Integer.toString(R.color.colorPrimaryDark));
        prefCache = sharedPreferences.getString("prefCacheSetting", "48");
        textSize = sharedPreferences.getString("textSize", "14");

        // init Cache database
        cacheDatabase = CacheDatabase.getInstance(this);

        // init Feeds database
        feedsDatabase = FeedsDatabase.getInstance(this);

        // delete old entrys
        //cacheDatabase.cacheDoa().deleteCacheByDate(Utils.calculateOffset(Integer.parseInt(prefCache)));

        // Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // custom title bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_activity);

        ab = getSupportActionBar();
        // set back arrow
        //ab.setDisplayHomeAsUpEnabled(true);
        // change toolbar color
        ab.setBackgroundDrawable(new ColorDrawable(Integer.parseInt(color)));

        // Action Bar Text One
        textOne = toolbar.findViewById(R.id.action_bar_title);
        textOne.setTextColor(Color.parseColor("#FFFFFF"));
        textOne.setTextSize(18);

        // Navigation Drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        // change header background color
        View header = navigationView.getHeaderView(0);
        LinearLayout sideNavLayout = header.findViewById(R.id.side_nav_layout);
        sideNavLayout.setBackgroundColor(Integer.parseInt(color));

        navigationView.setNavigationItemSelectedListener(this);

        // get saved dbid
        savedID = sharedPreferences.getInt("dbid", 0);

        if (feedsDatabase.feedsDoa().checkIDexists(savedID) > 0) { // does ID exist?

            List<FeedsEntities> feedsEntities = feedsDatabase.feedsDoa().getRecordById(savedID);
            savedTitle = feedsEntities.get(0).getTitle();
            urlAddress = feedsEntities.get(0).getLink();
            feedID = feedsEntities.get(0).getFeedId();

        } else { // ID does not exist - take the last one

            List<FeedsEntities> feedsEntities = feedsDatabase.feedsDoa().getOneRow();
            savedTitle = feedsEntities.get(0).getTitle();
            urlAddress = feedsEntities.get(0).getLink();
            feedID = feedsEntities.get(0).getFeedId();

        }

        // set the actionbar title
        textOne.setText(savedTitle);

        // populate drawer menu
        Menu drawerMenu = navigationView.getMenu();

        List<FeedsEntities> allValues = feedsDatabase.feedsDoa().getFeedsRecords();

        for (FeedsEntities value : allValues) {
            drawerMenu.add(R.id.second_group, value.getId(), 0, value.getTitle())
                    .setIcon(R.drawable.ic_chevron_right_black_24dp);
        }

        drawerMenu.setGroupCheckable(R.id.second_group, true, true);
        drawerMenu.setGroupVisible(R.id.second_group, true);

        // Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame_layout, MainFragment.newInstance(feedID, urlAddress, textSize))
                .commitNow();

    }

    public static MainActivity getInstance() {
        return instance;
    }


    public void showFeedCount() {

        String cnt = Integer.toString(cacheDatabase.cacheDoa().countCacheByFeed(feedID));
        cnt = cnt + " items";

        Utils.makeToast(this, cnt);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int menuID = item.getItemId();

        switch (menuID) {

            case R.id.nav_settings:
                Intent moreActivity = new Intent(MainActivity.this, MoreActivity.class);
                startActivity(moreActivity);
                break;

            default:

                saveSelection(menuID);

                if (!String.valueOf(savedID).equals(String.valueOf(menuID))) {
                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(i);
                }

                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void saveSelection(int menuID) {

        sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putInt("dbid", menuID);
        sharedPreferencesEditor.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }


}
