package com.emefilefrancis.journalapp.database;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * Created by SP on 6/28/2018.
 */

public class AddJournalViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDB;
    private final int journalId;

    public AddJournalViewModelFactory(AppDatabase database, int id){
        mDB = database;
        journalId = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddJournalViewModel(mDB, journalId);
    }
}
