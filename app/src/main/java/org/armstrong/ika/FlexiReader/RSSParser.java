package org.armstrong.ika.FlexiReader;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import static org.armstrong.ika.FlexiReader.MainActivity.swipeRefreshLayout;

public class RSSParser extends AsyncTask<Void, Void, Boolean> {

    String TAG = "LOGGING";

    private final Context context;
    private final InputStream inputStream;
    private final String feedID;
    private ArrayList<Article> articles = new ArrayList<>();
    private static boolean prefImages;
    private static String prefCache;
    private final DBManager dbManager;

    public RSSParser(Context context, InputStream inputStream, String feedID) {
        this.context = context;
        this.inputStream = inputStream;
        this.feedID = feedID;

        dbManager = new DBManager(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // get user preferences
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        prefImages = sharedPrefs.getBoolean("prefImagesSettings", false);
        prefCache = sharedPrefs.getString("prefCacheSetting", "6");

    }

    @Override
    protected Boolean doInBackground(Void... params) {

        return this.parseRSS();
    }

    @Override
    protected void onPostExecute(Boolean isParsed) {
        super.onPostExecute(isParsed);

        if (isParsed) {

            swipeRefreshLayout.setRefreshing(false);

            if (dbManager.deleteCacheByDate(prefCache)) {

                // reverse
                Collections.reverse(articles);

                // save
                if (dbManager.insertCacheRecord(articles)) {
                    // display
                    MainActivity.getInstance().displayList();
                }
            }

        } else {
            Log.e(TAG, "onPostExecute: UNABLE TO PARSE");
        }
    }

    private Boolean parseRSS() {

        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(inputStream, null);
            int event = parser.getEventType();

            String tagValue = null;
            Boolean isSiteMeta = true;

            articles.clear();
            Article article = new Article();

            do {

                String tagName = parser.getName();

                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase("item")) {
                            article = new Article();
                            isSiteMeta = false;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        tagValue = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:

                        if (!isSiteMeta) {
                            if (tagName.equalsIgnoreCase("title")) {
                                String ttl = tagValue.trim();

                                if (TextUtils.isEmpty(article.getTitle())) {
                                    article.setTitle(ttl);
                                }

                                // Hash on title
                                if (TextUtils.isEmpty(article.getHash())) {
                                    ttl = ttl.replaceAll("[^a-zA-Z]", "");
                                    String hash = new String(Hex.encodeHex(DigestUtils.md5(ttl)));
                                    article.setHash(hash);
                                }

                            } else if (tagName.equalsIgnoreCase("link")) {
                                if (TextUtils.isEmpty(article.getLink())) {
                                    article.setLink(tagValue);
                                }


                            } else if (tagName.equalsIgnoreCase("description")) {

                                if (TextUtils.isEmpty(article.getImageUrl())) {
                                    if (prefImages) {
                                        article.setImageUrl(HTMLUtils.extractDescriptionImage(tagValue));
                                    } else {
                                        article.setImageUrl("");
                                    }
                                }

                                if (TextUtils.isEmpty(article.getDescription())) {
                                    article.setDescription(HTMLUtils.StripHtml(tagValue));
                                }


                            } else if (tagName.equalsIgnoreCase("pubDate")) {
                                if (TextUtils.isEmpty(article.getpubDate())) {
                                    article.setpubDate(tagValue);
                                }

                            } else if (tagName.equalsIgnoreCase("dc:date")) {
                                if (TextUtils.isEmpty(article.getpubDate())) {
                                    article.setpubDate(tagValue);
                                }
                            }
                        }

                        if (tagName.equalsIgnoreCase("item")) {
                            // feedID
                            article.setFeedID(feedID);

                            articles.add(article);
                            isSiteMeta = true;
                        }

                        break;
                }

                event = parser.next();

            } while (event != XmlPullParser.END_DOCUMENT);

            return true;

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


}
