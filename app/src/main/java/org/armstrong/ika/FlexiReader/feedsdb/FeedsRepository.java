package org.armstrong.ika.FlexiReader.feedsdb;

import android.content.Context;

import java.util.List;

public class FeedsRepository {

    protected FeedsDoa feedsDoa;
    private List<FeedsEntities> feedsEntities;

    public FeedsRepository(Context context) {
        feedsDoa = FeedsDatabase.getInstance(context).feedsDoa();
    }

    public List<FeedsEntities> getFeedsRecords() {
        return feedsEntities = feedsDoa.getFeedsRecords();
    }

    public List<FeedsEntities> getFeedSearchRecords(String query) {
        return feedsEntities = feedsDoa.getFeedSearchRecords(query);
    }

    public void updateRecord(String title, String link, int id, long time) {
        feedsDoa.updateRecord(title, link, id, time);
    }

    public void updateTime(long time, int id) {
        feedsDoa.updateTime(time, id);
    }

    public void deleteRecord(int id){
        feedsDoa.deleteRecord(id);
    }

    public int countRecords(){
        return feedsDoa.countRecords();
    }

    public int checkIDexists(int id){
        return feedsDoa.checkIDexists(id);
    }

    public List<FeedsEntities> getRecordById(int id){
        return feedsEntities = feedsDoa.getRecordById(id);
    }

    public List<FeedsEntities> getOneRow(){
        return feedsEntities = feedsDoa.getOneRow();
    }

    public void insertFeed(FeedsEntities feedsEntities) {
        feedsDoa.insertFeed(feedsEntities);
    }

}
