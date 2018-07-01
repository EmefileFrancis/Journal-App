package com.emefilefrancis.journalapp.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

/**
 * Created by SP on 6/27/2018.
 */

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();
    private LiveData<List<JournalEntry>> mJournals;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase mDB = AppDatabase.getsOnlyDBInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving from the database");
        mJournals = mDB.journalDao().loadAllJournals();
    }

    public LiveData<List<JournalEntry>> getmJournals() { return mJournals; }
}
