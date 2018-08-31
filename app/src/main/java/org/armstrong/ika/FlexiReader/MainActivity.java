package org.armstrong.ika.FlexiReader;

// http://camposha.info/source/android-rss-listview-downlaodparseshow-headlines-with-images-and-text-source
// https://www.androidhive.info/2015/05/android-swipe-down-to-refresh-listview-tutorial/

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import java.util.List;


public class MainActivity extends AboutActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static MainActivity instance;

    private final String TAG = "LOG";

    private static String savedID = "0"; // _id
    private static String urlAddress = null; // link
    private static String savedTitle = ""; // title
    private static String feedID = ""; // feedID

    public static SwipeRefreshLayout swipeRefreshLayout;

    private ListView listView;

    //private final Context context;
    private static SharedPreferences sharedPrefs;
    private static String saveColor;

    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        // init database
        dbManager = new DBManager(this);

        listView = findViewById(R.id.listview);

        // get default values - called only once (false) true resets
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // restore preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        saveColor = sharedPrefs.getString("prefColorSetting", "-41659");

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        // change toolbar color
        ab.setBackgroundDrawable(new ColorDrawable(Integer.parseInt(saveColor)));

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
        sideNavLayout.setBackgroundColor(Integer.parseInt(saveColor));

        navigationView.setNavigationItemSelectedListener(this);

        // get saved dbid
        savedID = sharedPrefs.getString("dbid", "0");

        // count feed entries
        int cnt = dbManager.countRecords();

        if (cnt < 1) {
            new Preloads().getPreloads(this);
        }

        ValuesModel values = new ValuesModel();
        values.setId(savedID);

        //""(TAG, "onCreate: savedID " + savedID);

        if (dbManager.checkIDexists(values)) { // does ID exist?

            Cursor cursor = dbManager.getRecordById(values);
            savedTitle = cursor.getString(1);
            urlAddress = cursor.getString(2);
            feedID = cursor.getString(3);

        } else { // ID does not exist - take the first one.

            Cursor cursor = dbManager.getOneRow();
            savedTitle = cursor.getString(1);
            urlAddress = cursor.getString(2);
            feedID = cursor.getString(3);
        }

        ab.setTitle(savedTitle);

        //""(TAG, "onCreate: feedID " + feedID);

        // populate drawer menu
        Menu drawerMenu = navigationView.getMenu();

        List<ValuesModel> allValues = dbManager.getAllRecords();

        for (ValuesModel value : allValues) {
            drawerMenu.add(R.id.second_group, Integer.parseInt(value.getId()), 100, value.getTitle() );
        }

        drawerMenu.setGroupCheckable(R.id.second_group, true, true);
        drawerMenu.setGroupVisible(R.id.second_group, true);

        // setup refresh
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {  // on pull down
                downLoadFeed();
            }
        });
        swipeRefreshLayout.setDistanceToTriggerSync(30);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // run refresh
        swipeRefreshLayout.post(new Runnable() { // on start up
                                    @Override
                                    public void run() {
                                        if (checkFeedCount() < 1 ) {
                                            downLoadFeed();
                                        } else {
                                            displayList();
                                        }
                                    }
                                }
        );

    }

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int menuID = item.getItemId();

        switch (menuID) {
            case R.id.nav_feeds:
                Intent a = new Intent(MainActivity.this, FeedActivity.class);
                startActivity(a);
                break;
            case R.id.nav_settings:
                Intent b = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(b);
                break;
            case R.id.nav_color:
                Intent c = new Intent(MainActivity.this, ColorActivity.class);
                startActivity(c);
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

    private int checkFeedCount() {

        int cnt = dbManager.countCacheByFeed(feedID);
        //""(TAG, "countCacheByFeed: " + cnt);

        return cnt;

    }

    public void displayList() {

        listView.setAdapter(new CustomAdapter(this, dbManager.getAllCacheRecords(feedID)));

    }

    private void downLoadFeed() {

        if (feedID != null && feedID.length() > 0) {

            if (urlAddress != null && urlAddress.length() > 0) {
                swipeRefreshLayout.setRefreshing(true);
                new Downloader(MainActivity.this, urlAddress, feedID ).execute();
            }
        }
    }

    private void saveSelection(int menuID) {

        String dbid = Integer.toString(menuID);

        //""(TAG, "saveSelection menuID: " + dbid);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("dbid", dbid);
        editor.apply();

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