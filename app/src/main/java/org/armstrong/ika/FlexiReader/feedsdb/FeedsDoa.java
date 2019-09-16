package org.armstrong.ika.FlexiReader.feedsdb;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface FeedsDoa {

    @Query("SELECT * FROM feeds ORDER BY time DESC")
    List<FeedsEntities> getFeedsRecords();

    @Query("SELECT * FROM feeds WHERE title LIKE  '%' || :query || '%' ORDER BY time DESC")
    List<FeedsEntities> getFeedSearchRecords(String query);

    @Query("UPDATE feeds SET title=:title, link=:link, time=:time WHERE id=:id")
    void updateRecord(String title, String link, int id, long time);

    @Query("UPDATE feeds SET time=:time WHERE id=:id")
    void updateTime(long time, int id);

    @Query("DELETE FROM feeds WHERE id=:id")
    void deleteRecord(int id);

    @Query("SELECT count(*) FROM feeds")
    int countRecords();

    @Query("SELECT count(*) FROM feeds WHERE id=:id")
    int checkIDexists(int id);

    @Query("SELECT * FROM feeds WHERE id=:id")
    List<FeedsEntities> getRecordById(int id);

    @Query("SELECT * FROM feeds ORDER BY id DESC LIMIT 1")
    List<FeedsEntities> getOneRow();

    @Insert
    void insertFeed(FeedsEntities feedsEntities);


}
