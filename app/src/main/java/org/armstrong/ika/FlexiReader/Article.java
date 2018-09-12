package org.armstrong.ika.FlexiReader;

public class Article {

    String id;
    String title;
    String link;
    String description;
    String pubDate;
    String imageUrl;
    String feedID;
    String hash;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() { return link; }

    public void setLink(String link) { this.link = link; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getpubDate() {
        return pubDate;
    }

    public void setpubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFeedID() {
        return feedID;
    }

    public void setFeedID(String feedID) {
        this.feedID = feedID;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

}
