package com.emefilefrancis.journalapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.emefilefrancis.journalapp.database.AppDatabase;
import com.emefilefrancis.journalapp.database.JournalEntry;
import com.emefilefrancis.journalapp.database.MainViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends AppCompatActivity implements JournalAdapter.ItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecycleView;
    private JournalAdapter mJournalAdapter;

    private AppDatabase mDB;

    private DatabaseReference mJournalDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!isSignedIn()){
            Intent signInIntent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(signInIntent);
        }


        mRecycleView = findViewById(R.id.rv_journals);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mJournalAdapter = new JournalAdapter(this, this);
        mRecycleView.setAdapter(mJournalAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecycleView.addItemDecoration(decoration);

        mJournalDatabase = FirebaseDatabase.getInstance().getReference(getString(R.string.db_node_journal));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a User swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                final int position = viewHolder.getAdapterPosition();
                List<JournalEntry> journalEntries = mJournalAdapter.getJournalEntries();
                final JournalEntry journalEntry = journalEntries.get(position);
                AppExecutors.getsOnlyExecInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDB.journalDao().deleteJournal(journalEntry);
                    }
                });

                mJournalDatabase.child(String.valueOf(journalEntry.getId())).removeValue();
            }
        }).attachToRecyclerView(mRecycleView);

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent addJournalIntent = new Intent(MainActivity.this, AddJournalActivity.class);
                startActivity(addJournalIntent);
            }
        });
        mDB = AppDatabase.getsOnlyDBInstance(getApplicationContext());
        setupViewModel();
    }

    private boolean isSignedIn(){

        if(SavedSharedPreference.getPrefUsernameKey(MainActivity.this)
                .equals(SavedSharedPreference.DEFAULT_PREF_USERNAME_KEY)){
            return false;
        }else{
            return true;
        }
    }

    private void setupViewModel() {
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getmJournals().observe(this, new Observer<List<JournalEntry>>() {
            @Override
            public void onChanged(@Nullable List<JournalEntry> journalEntries) {
                Log.d(TAG, "Updating list from LiveData/ViewModel");
                mJournalAdapter.setJournalEntries(journalEntries);
            }
        });
    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(MainActivity.this, AddJournalActivity.class);
        intent.putExtra(AddJournalActivity.EXTRA_JOURNAL_ID, itemId);
        startActivity(intent);
    }


}
