package org.armstrong.ika.FlexiReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class HTMLUtils {

    private HTMLUtils() {
    }

    public static String StripHtml(String html) {
        Whitelist wlist = new Whitelist();
        //wlist.addTags("&nbsp;");
        html = Jsoup.clean(html, wlist);
        html = html.replace("&nbsp;", " ");
        html = html.trim();
        return html;
    }

    public static String extractDescriptionImage(String input) {

        String extracedData = null;

        Document document;
        document = Jsoup.parse(input);
        Elements tag = document.getElementsByTag("img");

        for (Element el : tag) {
            if (el.hasAttr("src")) {
                extracedData = el.attr("src");
            }
        }

        return extracedData;

    }

}
