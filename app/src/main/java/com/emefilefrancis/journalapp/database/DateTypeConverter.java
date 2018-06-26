package com.emefilefrancis.journalapp.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by SP on 6/25/2018.
 */

public class DateTypeConverter {
    @TypeConverter
    public static Date toDate(Long timestamp){
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestampForSQLite(Date date){
        return date == null ? null : date.getTime();
    }
}
