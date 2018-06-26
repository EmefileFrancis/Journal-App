package com.emefilefrancis.journalapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.emefilefrancis.journalapp.database.AppDatabase;
import com.emefilefrancis.journalapp.database.JournalEntry;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends AppCompatActivity implements JournalAdapter.ItemClickListener {

    private RecyclerView mRecycleView;
    private JournalAdapter mJournalAdapter;

    private AppDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecycleView = findViewById(R.id.rv_journals);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mJournalAdapter = new JournalAdapter(this, this);
        mRecycleView.setAdapter(mJournalAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecycleView.addItemDecoration(decoration);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                AppExecutors.getOnlyExecInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<JournalEntry> journalEntries = mJournalAdapter.getJournalEntries();
                        mDB.journalDao().deleteJournal(journalEntries.get(position));
                        retrieveJournalEntries();
                    }
                });
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
        mDB = AppDatabase.getOnlyDBInstance(getApplicationContext());
    }

    private void retrieveJournalEntries() {
        AppExecutors.getOnlyExecInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<JournalEntry> journalEntries = mDB.journalDao().loadAllJournals();
                runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        mJournalAdapter.setJournalEntries(journalEntries);
                    }
                });
            }
        });
    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(MainActivity.this, AddJournalActivity.class);
        intent.putExtra(AddJournalActivity.EXTRA_JOURNAL_ID, itemId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveJournalEntries();
    }
}
