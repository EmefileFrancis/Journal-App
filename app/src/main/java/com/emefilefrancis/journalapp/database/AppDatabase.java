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
@Database(entities = {JournalEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "journalsDB";
    private static AppDatabase onlyDBInstance;

    public static AppDatabase getOnlyDBInstance(Context context){
        if(onlyDBInstance == null){
            synchronized (LOCK){
                onlyDBInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME).build();
            }
        }
        return onlyDBInstance;
    }

    public abstract JournalDao journalDao();

}
