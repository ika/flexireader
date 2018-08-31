package org.armstrong.ika.FlexiReader;

// https://stackoverflow.com/questions/13057942/android-parsing-xml-and-insert-to-sqlite-database

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.UUID;

public class Preloads {

    private DBManager dbManager = null;

    public void getPreloads(Context context) {

        dbManager = new DBManager(context.getApplicationContext());

        String TAG = "LOGGING";

        XmlResourceParser parser = context.getResources().getXml(R.xml.preloads);

        try {

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("item")) {
                    String title = null, link = null;
                    while (parser.next() != XmlPullParser.END_TAG) {
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                        name = parser.getName();
                        if (name.equals("title")) {
                            title = readText(parser);
                        } else if (name.equals("link")) {
                            link = readText(parser);
                        }

                    }

                    String feedID = UUID.randomUUID().toString();
                    //feedID = feedID.replace("-", "");

                    ValuesModel value = new ValuesModel();
                    value.setTitle(title);
                    value.setLink(link);
                    value.setFeedID(feedID);

                    dbManager.insertRecord(value);

                }
            } // end of while


        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (dbManager != null) {
                dbManager.close();
            }

        }

    }

    private String readText(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

}
