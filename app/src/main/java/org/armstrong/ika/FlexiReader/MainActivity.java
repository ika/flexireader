package org.armstrong.ika.FlexiReader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.ActionBar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import org.armstrong.ika.FlexiReader.cachedb.CacheDatabase;
import org.armstrong.ika.FlexiReader.cachedb.CacheRepository;
import org.armstrong.ika.FlexiReader.feedsdb.FeedsDatabase;
import org.armstrong.ika.FlexiReader.feedsdb.FeedsEntities;
import org.armstrong.ika.FlexiReader.feedsdb.FeedsRepository;
import org.armstrong.ika.FlexiReader.list.ListActivity;
import org.armstrong.ika.FlexiReader.main.MainFragment;
import org.armstrong.ika.FlexiReader.more.MoreActivity;
import org.armstrong.ika.FlexiReader.app.Utils;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static MainActivity instance;

    private AHBottomNavigation bottomNavigation;

    protected SharedPreferences sharedPreferences;

    protected FeedsRepository feedsRepository;

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

    protected CacheRepository cacheRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        instance = this;

        // install raw database
        //new CopierFeeds(this).initializeCopier();
        // install feeds in empty database
        new InstallFeeds(this);

        // read saved preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        color = sharedPreferences.getString("color", Integer.toString(R.color.colorPrimaryDark));
        prefCache = sharedPreferences.getString("prefCacheSetting", "48");
        textSize = sharedPreferences.getString("textSize", "16");

        // init Cache database
        cacheRepository = new CacheRepository(this);

        // init Feeds database
        feedsRepository = new FeedsRepository(this);

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
        textOne.setTextSize(Integer.parseInt(textSize) + 4);

        // get saved dbid
        savedID = sharedPreferences.getInt("dbid", 1);

        if (feedsRepository.checkIDexists(savedID) > 0) { // does ID exist?

            List<FeedsEntities> feedsEntities = feedsRepository.getRecordById(savedID);
            savedTitle = feedsEntities.get(0).getTitle();
            urlAddress = feedsEntities.get(0).getLink();
            feedID = feedsEntities.get(0).getFeedId();

        } else { // ID does not exist - take the last one

            List<FeedsEntities> feedsEntities = feedsRepository.getOneRow();
            savedTitle = feedsEntities.get(0).getTitle();
            urlAddress = feedsEntities.get(0).getLink();
            feedID = feedsEntities.get(0).getFeedId();

        }

        // set the actionbar title
        textOne.setText(savedTitle);

        // make Text One clickable
        textOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent listActivity = new Intent(MainActivity.this, ListActivity.class);
                startActivity(listActivity);
            }
        });

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
        bottomNavigation.setCurrentItem(1);

        // Quick return animation
        bottomNavigation.setBehaviorTranslationEnabled(true);

        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {

            public boolean onTabSelected(int position, boolean wasSelected) {

                switch (position) {
                    case 0:
                        Intent listActivity = new Intent(getApplicationContext(), ListActivity.class);
                        startActivity(listActivity);
                        break;
                    case 1:
                        if (!wasSelected) {
                            Utils.makeToast(getApplicationContext(), "text");
                        }
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
                .replace(R.id.main_frame_layout, MainFragment.newInstance(feedID, urlAddress, textSize))
                .commitNow();

    }

    public static MainActivity getInstance() {
        return instance;
    }


    public void showFeedCount() {

        String cnt = Integer.toString(cacheRepository.countCacheByFeed(feedID));
        cnt = cnt + " " + getString(R.string.items);

        Utils.makeToast(this, cnt);

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }


}
