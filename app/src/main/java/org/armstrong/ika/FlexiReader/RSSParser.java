package org.armstrong.ika.FlexiReader;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static org.armstrong.ika.FlexiReader.MainActivity.swipeRefreshLayout;

public class RSSParser extends AsyncTask<Void, Void, Boolean> {

    String TAG = "LOGGING";

    private final Context context;
    private final InputStream inputStream;
    private final String feedID;
    private ArrayList<Article> articles = new ArrayList<>();
    private static boolean prefImages = false;
    private final DBManager dbManager;

    public RSSParser(Context context, InputStream inputStream, String feedID ) {
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

        prefImages = sharedPrefs.getBoolean("prefImages", false);

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

            if(dbManager.deleteCacheByFeed(feedID)) {
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
                                article.setTitle(ttl);

                            } else if (tagName.equalsIgnoreCase("link")) {
                                article.setLink(tagValue);

                            } else if (tagName.equalsIgnoreCase("description")) {

                                //EXTRACT IMAGE FROM DESCRIPTION
                                if (prefImages) {
                                    article.setImageUrl(HTMLUtils.extractDescriptionImage(tagValue));
                                } else {
                                    article.setImageUrl(HTMLUtils.extractDescriptionImage(""));
                                }

                                // extract text
                                article.setDescription(HTMLUtils.StripHtml(tagValue));

                                // feedID
                                article.setFeedID(feedID);

                                // ""("LOGGING", "DESC" + HTMLUtils.StripHtml(desc));

                            } else if (tagName.equalsIgnoreCase("pubDate")) {

                                try {
                                    SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss Z", Locale.ENGLISH);
                                    Date pDate = df.parse(tagValue);
                                    article.setpubDate(DateUtils.getDateDifference(pDate));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        if (tagName.equalsIgnoreCase("item")) {
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