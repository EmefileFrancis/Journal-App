package com.emefilefrancis.journalapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

/**
 * Created by SP on 6/25/2018.
 */
@Database(entities = {JournalEntry.class}, version = 3, exportSchema = false)
@TypeConverters(DateTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "journalsDB";
    private static AppDatabase sOnlyDBInstance;

    public static AppDatabase getsOnlyDBInstance(Context context){
        if(sOnlyDBInstance == null){
            synchronized (LOCK){
                sOnlyDBInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME).fallbackToDestructiveMigration().build();
            }
        }
        return sOnlyDBInstance;
    }

    public abstract JournalDao journalDao();

}
