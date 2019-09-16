package org.armstrong.ika.FlexiReader;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.armstrong.ika.FlexiReader.feedsdb.FeedsEntities;
import org.armstrong.ika.FlexiReader.feedsdb.FeedsRepository;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

public class InstallFeeds {

    protected FeedsRepository feedsRepository;

    protected Context context;

    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor sharedPreferencesEditor;

    private int DB_VERSION = 1;
    private String DB_NAME = "InstalledFeedsVersion";

    private boolean entryExists;

    String[] feedsArray = {
            "Fox News Latest!http://feeds.foxnews.com/foxnews/latest",
            "Fox News Popular!http://feeds.foxnews.com/foxnews/most-popular",
            "Fox News Tech!http://feeds.foxnews.com/foxnews/tech",
            "Fox News World!http://feeds.foxnews.com/foxnews/world",
            "Fox News Sports!http://feeds.foxnews.com/foxnews/sports",
            "Fox News Politics!http://feeds.foxnews.com/foxnews/politics",
            "Fox News U.S.!http://feeds.foxnews.com/foxnews/national",
            "Antiwar.com!http://original.antiwar.com/feed/",
            "Android Central!http://feeds.feedburner.com/androidcentral?format=xml",
            "DistroWatch!https://distrowatch.com/news/dw.xml",
            "TechRepublic!https://www.techrepublic.com/rssfeeds/articles/",
            "Linux Journal!https://www.linuxjournal.com/node/feed",
            "Linux Magazine News!http://www.linux-magazine.com/rss/feed/lmi_news",
            "DR Bornholm!https://www.dr.dk/Nyheder/Service/feeds/regionale/bornholm/",
            "DR Nyheder Politik!https://www.dr.dk/nyheder/service/feeds/politik",
            "DR Nyheder Kultur!https://www.dr.dk/nyheder/service/feeds/kultur",
            "DR Nyheder Vejret!https://www.dr.dk/nyheder/service/feeds/vejret",
            "DR Nyheder Indland!https://www.dr.dk/nyheder/service/feeds/indland",
            "TV2 Danmark!http://feeds.tv2.dk/nyheder/rss",
            "IOL Crime & Courts!http://rss.iol.io/iol/news/crime-courts",
            "IOL Business Report!http://rss.iol.io/iol/business-report",
            "IOL Sport!http://rss.iol.io/iol/sport",
            "IOL Motoring!http://rss.iol.io/iol/motoring",
            "IOL South Africa!http://rss.iol.io/iol/news/south-africa",
            "NASA!https://www.nasa.gov/rss/dyn/breaking_news.rss",
            "Russia Today!https://www.rt.com/rss/",
            "ABC News!https://abcnews.go.com/abcnews/topstories",
            "BBC!http://feeds.bbci.co.uk/news/rss.xml",
            "Al Jazeera!https://www.aljazeera.com/xml/rss/all.xml",
            "News24!http://feeds.news24.com/articles/News24/TopStories/rss",
            "Estcourt News!https://estcourtnews.co.za/rss-output/",
            "Sky News Home!http://feeds.skynews.com/feeds/rss/home.xml",
            "Sky News UK!http://feeds.skynews.com/feeds/rss/uk.xml",
            "Sky News World!http://feeds.skynews.com/feeds/rss/world.xml",
            "Sky News US!http://feeds.skynews.com/feeds/rss/us.xml",
            "Sky News business!http://feeds.skynews.com/feeds/rss/business.xml",
            "Sky News Politics!http://feeds.skynews.com/feeds/rss/politics.xml",
            "Sky News Technology!http://feeds.skynews.com/feeds/rss/technology.xml",
            "Sky News Entertainment!http://feeds.skynews.com/feeds/rss/entertainment.xml",
            "CBN News U.S.!http://www1.cbn.com/app_feeds/rss/news/rss.php?section=us"
    };

    public InstallFeeds(Context context) {
        this.context = context;

        // read saved preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (getInstalledVersion()) { // version needs updating

            feedsRepository = new FeedsRepository(context);

            List<FeedsEntities> feedsEntities = feedsRepository.getFeedsRecords();

            if (feedsEntities.size() > 0) { // if entries already exist

                URL urlOne, urlTwo;

                for (int x = 0; x < feedsArray.length; x++) {

                    entryExists = false;

                    String parts[] = feedsArray[x].split("!", 2);

                    try {
                        urlOne = new URL(parts[1]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    for (int i = 0; i < feedsEntities.size(); i++) { // for each feed that exists

                        try {
                            urlTwo = new URL(feedsEntities.get(i).getLink());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        if (urlOne.equals(urlTwo)) { // link already exists
                            entryExists = true;
                            break;
                        }

                    }

                    if (entryExists == false) {

                        String feedID = UUID.randomUUID().toString();
                        long seconds = System.currentTimeMillis() / 1000l;

                        FeedsEntities feeds = new FeedsEntities();
                        feeds.setTitle(parts[0]); // title
                        feeds.setLink(urlOne.toString()); // link
                        feeds.setFeedId(feedID);
                        feeds.setTime(seconds);

                        feedsRepository.insertFeed(feeds);
                    }

                }


            } else { // no previous entries exist - add all

                for (int i = 0; i < feedsArray.length; i++) {

                    String parts[] = feedsArray[i].split("!", 2);

                    String feedID = UUID.randomUUID().toString();
                    long seconds = System.currentTimeMillis() / 1000l;

                    FeedsEntities feeds = new FeedsEntities();
                    feeds.setTitle(parts[0]); // title
                    feeds.setLink(parts[1]); // link
                    feeds.setFeedId(feedID);
                    feeds.setTime(seconds);

                    feedsRepository.insertFeed(feeds);

                }

            }

            writeInstalledVersion();

        }

    }

    private boolean getInstalledVersion() {

        int version = sharedPreferences.getInt(DB_NAME, 0);
        return (version < DB_VERSION) ? true : false;

    }

    private void writeInstalledVersion() {

        sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putInt(DB_NAME, DB_VERSION);
        sharedPreferencesEditor.apply();
    }

}
