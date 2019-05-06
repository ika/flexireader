package org.armstrong.ika.FlexiReader.cachedb;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface CacheDoa {

//    @Query("DELETE FROM cache WHERE timestamp < :offset")
//    void deleteCacheByDate(int offset);

    @Query("DELETE FROM cache WHERE feedId=:feedId")
    void deleteCacheByFeedId(String feedId);

//    @Query("SELECT count(*) FROM cache WHERE hash=:hash AND feedId=:feedId")
//    int countCacheByHash(String hash, String feedId);

    @Query("SELECT count(*) FROM cache WHERE feedId=:feedId")
    int countCacheByFeed(String feedId);

    @Query("SELECT * FROM cache WHERE feedId=:feedId ORDER BY id ASC")
    List<CacheEntities> getAllCacheRecords(String feedId);

    @Insert
    void insertCacheRecord(CacheEntities cacheEntities);
}
