package org.armstrong.ika.FlexiReader.cachedb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cache")
public class CacheEntities {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "link")
    private String link;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "pubDate")
    private String pubDate;
    @ColumnInfo(name = "imageUrl")
    private String imageUrl;
    @ColumnInfo(name = "feedId")
    private String feedId;

    public int getId(){return id;}
    public void setId(int id) {this.id = id;}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLink() { return  link;}
    public void setLink(String link) {this.link = link;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getPubDate() {return pubDate;}
    public void setPubDate(String pubDate) {this.pubDate = pubDate;}

    public String getImageUrl() {return imageUrl;}
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    public String getFeedId() {return feedId;}
    public void setFeedId(String feedId) {this.feedId = feedId;}


}
