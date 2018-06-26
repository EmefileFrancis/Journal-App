package com.emefilefrancis.journalapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by SP on 6/25/2018.
 */
@Dao
public interface JournalDao {

    @Query("SELECT * FROM journal ORDER BY journal_label")
    List<JournalEntry> loadAllJournals();

    @Insert
    void insertJournal(JournalEntry journalEntry);

    @Delete
    void deleteJournal(JournalEntry journalEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateJournal(JournalEntry journalEntry);

    @Query("SELECT * FROM journal WHERE id = :id")
    JournalEntry loadJournalEntryById(int id);
}
