package org.armstrong.ika.FlexiReader.feedsdb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "feeds")
public class FeedsEntities {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "link")
    private String link;
    @ColumnInfo(name = "feedId")
    private String feedId;
    @ColumnInfo(name = "time")
    private long time;

    public int getId(){return id;}
    public void setId(int id) {this.id = id;}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLink() { return  link;}
    public void setLink(String link) {this.link = link;}

    public String getFeedId() {return feedId;}
    public void setFeedId(String feedId) {this.feedId = feedId;}

    public long getTime() {return time;}
    public void setTime(long time) {this.time = time;}


}
