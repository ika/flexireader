package org.armstrong.ika.FlexiReader;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.jsoup.safety.Whitelist;

public class HTMLUtils {

    private HTMLUtils() {
    }

    public static String StripHtml(String html) {
        Whitelist wlist = new Whitelist();
        //wlist.addTags("&nbsp;");
        html = Jsoup.clean(html, wlist);
        html = html.replace("&nbsp;", " ");
        html = html.replace("&amp;", "&");
        html = html.trim();
        return html;
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

    public static String extracMediaContentImage(String input) {

        String url = "";

        Log.e("LOGGING", "extracMediaContentImage: " + input);
        return url;

    }

}
