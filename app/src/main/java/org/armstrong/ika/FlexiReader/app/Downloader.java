package org.armstrong.ika.FlexiReader.app;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.armstrong.ika.FlexiReader.MainActivity;
import org.armstrong.ika.FlexiReader.cachedb.CacheDatabase;
import org.armstrong.ika.FlexiReader.cachedb.CacheEntities;
import org.armstrong.ika.FlexiReader.main.MainFragment;
import org.xml.sax.InputSource;

import java.io.InputStream;

import java.net.URLConnection;

import java.net.URL;
import java.util.zip.GZIPInputStream;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;

public class Downloader extends AsyncTask<Void, Void, SyndFeed> {

    protected CacheDatabase cacheDatabase;
    protected CacheEntities cacheEntities;

    private Context context;
    private String url;
    private String feedID;

    //private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public Downloader(Context context, String urlAddress, String feedID) {

        this.context = context;
        this.url = urlAddress;
        this.feedID = feedID;

        cacheDatabase = CacheDatabase.getInstance(context);

    }

    @Override
    protected SyndFeed doInBackground(Void... params) {

        SyndFeed feed = null;
        InputStream is;

        try {
            URLConnection openConnection = new URL(url).openConnection();
            is = new URL(url).openConnection().getInputStream();
            if ("gzip".equals(openConnection.getContentEncoding())) {
                is = new GZIPInputStream(is);
            }
            InputSource source = new InputSource(is);
            SyndFeedInput input = new SyndFeedInput();
            feed = input.build(source);

            is.close();

        } catch (Exception e) {
            Log.e("logg", "doInBackground: Exception occured when building the feed object out of the url ", e);
        } finally {

        }

        return feed;

    }

    @Override
    protected void onPostExecute(SyndFeed feed) {

        SyndFeed syndFeed = feed;

        if (syndFeed != null) {

            if (syndFeed.getEntries().size() > 0) {

                // delete all feed entries
                cacheDatabase.cacheDoa().deleteCacheByFeedId(feedID);

                for (SyndEntry entry : syndFeed.getEntries()) {

                    String description = "";
//                if (entry.getDescription() != null && !entry.getDescription().equals("")) {
//                    // strip html from description
//                    description = entry.getDescription().getValue().replaceAll("\\<[^>]*>", "");
//                }

                    // strip html from description
                    description = entry.getDescription().getValue().replaceAll("\\<[^>]*>", "");


                    // make hash from title
//                String ttl = entry.getTitle().replaceAll("[^a-zA-Z]", "");
//                String hash = new String(Hex.encodeHex(DigestUtils.md5(ttl)));
                    //String hash = entry.getUri();

                    // title
                    String title = entry.getTitle();

                    // link
                    String link = entry.getLink();

                    // image url
                    String image = Utils.extractDescriptionImage(entry.getDescription().toString());

                    // date
                    //String date = "Published " + FORMATTER.format(item.getPubDate());
                    String date = "Published: " + entry.getPublishedDate();

                    cacheEntities = new CacheEntities();

                    cacheEntities.setTitle(title);
                    cacheEntities.setLink(link);
                    cacheEntities.setDescription(description);
                    cacheEntities.setPubDate(date);
                    cacheEntities.setImageUrl(image);
                    cacheEntities.setFeedId(feedID);
                    //cacheEntities.setHash(hash);

                    // save
                    cacheDatabase.cacheDoa().insertCacheRecord(cacheEntities);

                }

            }

        } else {

            Utils.makeToast(context.getApplicationContext(), "Connection Error!");

        }

        // turn refresh off
        MainFragment.getInstance().swipeRefreshLayout.setRefreshing(false);

        // display
        MainFragment.getInstance().displayList();

        // show feed count
        MainActivity.getInstance().showFeedCount();
    }


}
