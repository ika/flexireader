package org.armstrong.ika.FlexiReader.feedsdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import static org.armstrong.ika.FlexiReader.app.Constants.DB_NAME_FEEDS;

@Database(entities = {FeedsEntities.class}, version = 1)
public abstract class FeedsDatabase extends RoomDatabase {

    public abstract FeedsDoa feedsDoa();

    private static FeedsDatabase INSTANCE;

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };

    public static FeedsDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (FeedsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FeedsDatabase.class, DB_NAME_FEEDS)
                            .allowMainThreadQueries()
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }

        return INSTANCE;
    }


}
