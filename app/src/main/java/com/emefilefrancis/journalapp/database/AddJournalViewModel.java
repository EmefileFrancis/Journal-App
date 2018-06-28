package com.emefilefrancis.journalapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

/**
 * Created by SP on 6/27/2018.
 */

public class AddJournalViewModel extends ViewModel{
    private LiveData<JournalEntry> journalEntry;

    public AddJournalViewModel(AppDatabase database, int journalEntryId){
        journalEntry = database.journalDao().loadJournalEntryById(journalEntryId);
    }

    public LiveData<JournalEntry> getJournalEntry() { return journalEntry; }
}
