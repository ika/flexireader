package org.armstrong.ika.FlexiReader.app;

import android.content.Context;
import android.widget.Toast;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

// https://github.com/eugenp/tutorials/tree/master/core-java-security

public final class Utils {

    public static void makeToast(Context context, String text) {

        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static String loadAsset(Context context, String inputFile) {

        String fContents = null;

        try {
            InputStream stream = context.getAssets().open(inputFile);

            int size = stream.available();

            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();

            fContents = new String(buffer);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fContents;

    }

    public static int calculateOffset(int h) {

        long unixTime = System.currentTimeMillis() / 1000L;
        int limit = 60 * 60 * h; // 0 hours - clear cache
        int offset = (int) (unixTime - limit);

        return offset;
    }

    public static String extractDescriptionImage(String input) {

        String url = "";

        Document document = Jsoup.parse(input);
        Elements img = document.select("img");
        for (Element el : img) {
            url = el.absUrl("src");
        }

        return url;

    }

}
