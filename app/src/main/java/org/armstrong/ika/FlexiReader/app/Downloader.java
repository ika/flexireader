package org.armstrong.ika.FlexiReader.app;

import android.content.Context;
import android.os.AsyncTask;

import com.rometools.modules.mediarss.MediaEntryModuleImpl;
import com.rometools.modules.mediarss.MediaModule;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEnclosureImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import org.armstrong.ika.FlexiReader.MainActivity;
import org.armstrong.ika.FlexiReader.cachedb.CacheEntities;
import org.armstrong.ika.FlexiReader.cachedb.CacheRepository;
import org.armstrong.ika.FlexiReader.main.MainFragment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Downloader extends AsyncTask<Void, Void, SyndFeed> {

    protected CacheRepository cacheRepository;
    protected CacheEntities cacheEntities;

    private Context context;
    private URL url;
    private String feedID;
    XmlReader reader = null;

    //private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public Downloader(Context context, String urlAddress, String feedID) throws MalformedURLException {

        this.context = context;
        //this.url = urlAddress;
        this.feedID = feedID;

        url = new URL(urlAddress);

        cacheRepository = new CacheRepository(context);

    }

    protected SyndFeed doInBackground(Void... params) {


        //InputStream inputStream;

        SyndFeed syndFeed = null;

//        try {
//            URLConnection urlConnection = new URL(url).openConnection();
//            inputStream = urlConnection.getInputStream();
//
//            InputSource inputSource = new InputSource(inputStream);
//
//            SyndFeedInput input = new SyndFeedInput();
//            feed = input.build(inputSource);
//
//            inputStream.close();
//
//        } catch (Exception e) {
//            Log.e("logg", "doInBackground: Exception occured when building url " + url, e);
//        } finally {
//
//        }

        try {

            try {
                reader = new XmlReader(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                syndFeed = new SyndFeedInput().build(reader);
            } catch (FeedException e) {
                e.printStackTrace();
            }

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return syndFeed;
    }

    protected void onPostExecute(SyndFeed syndFeed) {

        String image = "";

        if (syndFeed != null) {

            if (syndFeed.getEntries().size() > 0) {

                // delete all feed entries
                cacheRepository.deleteCacheByFeedId(feedID);

                for (SyndEntry entry : syndFeed.getEntries()) {

                    // title
                    String title = entry.getTitle().trim();

                    // link
                    String link = entry.getLink();

                    // check media first
                    image = getMediaContentUrl(entry);

                    // then try enclosure url
                    if (image.length() < 1) {
                        List<SyndEnclosure> result = getEnclosures(entry);

                        if (result.size() > 0) {
                            for (int x = 0; x < result.size(); x++) {
                                image = result.get(x).getUrl();
                            }
                        }

                    }

                    // finally, try description
                    if (image.length() < 1) {
                        image = Utils.extractDescriptionImage(entry.getDescription().toString());
                    }

                    // description - strip html
                    String description = entry.getDescription().getValue().replaceAll("\\<[^>]*>", "").trim();

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
                    cacheRepository.insertCacheRecord(cacheEntities);

                    // reset image var
                    image = "";

                }

            } else {

                Utils.makeToast(context.getApplicationContext(), "Zero results!");

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

    public List getEnclosures(SyndEntry entry) {

        List<SyndEnclosure> enc = entry.getEnclosures();
        List<SyndEnclosure> result = new ArrayList<SyndEnclosure>(enc.size());

        if(enc != null) {
            for (SyndEnclosure e : enc) {
                SyndEnclosureImpl ee = new SyndEnclosureImpl();
                if (e != null) {
                    ee.setUrl(e.getUrl());
                    ee.setLength(e.getLength());
                    ee.setType(e.getType());
                }
                result.add(ee);
            }
        }
        return result;
    }

    public String getMediaContentUrl(SyndEntry entry) {

        String image = "";
        MediaEntryModuleImpl mediaEntryModule = (MediaEntryModuleImpl) entry.getModule(MediaModule.URI);

        if(mediaEntryModule != null ) {

            if (mediaEntryModule.getMediaContents().length > 0) {
                String type = mediaEntryModule.getMediaContents()[mediaEntryModule.getMediaContents().length - 1].getType();
                if (type.equals("image/jpeg")) {
                    image = mediaEntryModule.getMediaContents()[mediaEntryModule.getMediaContents().length - 1].getReference().toString();
                }

            } else if (mediaEntryModule.getMetadata().getThumbnail().length > 0) {
                image = mediaEntryModule.getMetadata().getThumbnail()[mediaEntryModule.getMetadata().getThumbnail().length - 1].getUrl().toString();

            }

        }

//        System.out.println("*************************************");
//        System.out.println(mediaEntryModule);
//        System.out.println("*************************************");

        return image;


    }
}
