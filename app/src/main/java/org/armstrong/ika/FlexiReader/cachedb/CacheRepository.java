package org.armstrong.ika.FlexiReader.cachedb;

import android.content.Context;

import java.util.List;

public class CacheRepository {

    protected CacheDoa cacheDoa;
    protected List<CacheEntities> cacheEntities;

    public CacheRepository(Context context) {
        cacheDoa = CacheDatabase.getInstance(context).cacheDoa();
    }

    public void deleteCacheByFeedId(String feedId){
        cacheDoa.deleteCacheByFeedId(feedId);
    }

    public int countCacheByFeed(String feedId) {
        return cacheDoa.countCacheByFeed(feedId);
    }

    public List<CacheEntities> getAllCacheRecords(String feedId) {
        return cacheEntities = cacheDoa.getAllCacheRecords(feedId);
    }

    public void insertCacheRecord(CacheEntities cacheEntities){
        cacheDoa.insertCacheRecord(cacheEntities);
    }


}
