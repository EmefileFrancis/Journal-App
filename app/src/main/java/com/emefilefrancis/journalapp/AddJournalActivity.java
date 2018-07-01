package com.emefilefrancis.journalapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;

import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


import com.emefilefrancis.journalapp.com.emefilefrancis.journalapp.models.Journal;
import com.emefilefrancis.journalapp.com.emefilefrancis.journalapp.models.User;

import com.emefilefrancis.journalapp.database.AddJournalViewModel;
import com.emefilefrancis.journalapp.database.AddJournalViewModelFactory;
import com.emefilefrancis.journalapp.database.AppDatabase;
import com.emefilefrancis.journalapp.database.JournalEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddJournalActivity extends AppCompatActivity {

    public static final String ORANGE = "orange";
    public static final String BLUE = "blue";
    public static final String LEMON = "lemon";
    public static final String YELLOW = "yellow";
    public static final String PINK = "pink";
    public static final String PURPLE = "purple";
    public static final String DARK_BLUE = "dark_blue";
    public static final String LIGHT_BLUE = "light_blue";
    private static final String WHITE = "white";


    public static final String INSPIRATION = "inspiration";
    public static final String WORK = "work";
    public static final String PERSONAL = "personal";

    private static final int DEFAULT_JOURNAL_ID = -1;
    public static final String EXTRA_JOURNAL_ID = "extraJournalId";
    private static final String TAG = AddJournalActivity.class.getSimpleName();

    private AppDatabase mDb;

    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mJournalReference;


    private EditText mJournalTitle;
    private EditText mJournalBody;
    private CheckBox mInspiration;
    private CheckBox mWork;
    private CheckBox mPersonal;
    private RadioGroup mColor;
    private Button mBtnAdd;

    private int mJournalId = DEFAULT_JOURNAL_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        initViews();

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDb = AppDatabase.getsOnlyDBInstance(getApplicationContext());


        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mJournalReference = FirebaseDatabase.getInstance().getReference(getString(R.string.db_node_journal));

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(EXTRA_JOURNAL_ID)){
            if(mJournalId == DEFAULT_JOURNAL_ID){
                disableEdit();
                mJournalId = intent.getIntExtra(EXTRA_JOURNAL_ID, DEFAULT_JOURNAL_ID);

                AddJournalViewModelFactory addJournalViewModelFactory = new AddJournalViewModelFactory(mDb, mJournalId);
                final AddJournalViewModel addJournalViewModel = ViewModelProviders
                        .of(this, addJournalViewModelFactory)
                        .get(AddJournalViewModel.class);
                addJournalViewModel.getJournalEntry().observe(this, new Observer<JournalEntry>() {
                    @Override
                    public void onChanged(@Nullable JournalEntry journalEntry) {
                        addJournalViewModel.getJournalEntry().removeObserver(this);
                        populateUI(journalEntry);
                    }
                });
            }
        }
    }

    public void disableEdit(){
        mJournalTitle.setFocusable(false);
        mJournalTitle.setClickable(true);
        mJournalTitle.setFocusableInTouchMode(false);
        mJournalBody.setFocusable(false);
        mJournalBody.setFocusableInTouchMode(false);
        mJournalBody.setClickable(true);
    }

    private void enableEditByPen(){
        mJournalTitle.setFocusable(true);
        mJournalTitle.setFocusableInTouchMode(true);
        mJournalTitle.setClickable(true);
        mJournalBody.setFocusable(true);
        mJournalBody.setFocusableInTouchMode(true);
        mJournalBody.setClickable(true);
    }

    private void populateUI(JournalEntry journalEntry) {
        if(journalEntry == null){
            return;
        }
        mJournalTitle.setText(journalEntry.getJournalTitle());
        mJournalBody.setText(journalEntry.getJournalBody());
        String labelString = getLabelsStringFromDB(journalEntry);
        Log.i(TAG, "This is labelString: " + labelString);
        setLabelView(labelString);
        setColorView(journalEntry.getJournalColor());
    }

    private void setColorView(String journalColor) {

        switch (journalColor){
            case ORANGE:
                mColor.check(R.id.radioButton1);
                break;
            case BLUE:
                mColor.check(R.id.radioButton2);
                break;
            case LEMON:
                mColor.check(R.id.radioButton3);
                break;
            case YELLOW:
                mColor.check(R.id.radioButton4);
                break;
            case PINK:
                mColor.check(R.id.radioButton5);
                break;
            case PURPLE:
                mColor.check(R.id.radioButton6);
                break;
            case DARK_BLUE:
                mColor.check(R.id.radioButton7);
                break;
            case LIGHT_BLUE:
                mColor.check(R.id.radioButton8);
                break;
            case WHITE:
                break;

        }
    }

    private String getLabelsStringFromDB(JournalEntry journalEntry) {
        return journalEntry.getJournalLabel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_journal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(checkIfJournalBodyIsEmpty()){
            return super.onOptionsItemSelected(item);
        }
        switch(item.getItemId()){
            case android.R.id.home:
                Toast.makeText(this, "Journal Saved", Toast.LENGTH_LONG).show();
                saveToDB();
                break;
            case R.id.edit_journal_menu:
                Toast.makeText(this, "You can now edit", Toast.LENGTH_LONG).show();
                enableEditByPen();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkIfJournalBodyIsEmpty() {
        return mJournalBody.getText().toString().equals("");
    }

    @Override
    public void onBackPressed() {

        if(checkIfJournalBodyIsEmpty()){
            super.onBackPressed();
            return;
        }
        saveToDB();
        Toast.makeText(this, "Journal Saved", Toast.LENGTH_LONG).show();
        super.onBackPressed();
    }

    private void initViews() {
        mJournalTitle = findViewById(R.id.et_journal_title);
        mJournalBody = findViewById(R.id.et_journal_body);
        mInspiration = findViewById(R.id.cb_inspiration);
        mWork = findViewById(R.id.cb_work);
        mPersonal = findViewById(R.id.cb_personal);
        mColor = findViewById(R.id.radio_group);
    }

    public void saveToDB() {
        final String title = mJournalTitle.getText().toString();
        final String body = mJournalBody.getText().toString();
        final List<String> labels = getSelectedLabelsFromViews();
        Log.i(TAG, "labels: " + labels);
        final String color = getColorFromViews();
        final Date updatedDate = new Date();
        //String cloudId = updatedDate.toString() + mFirebaseUser.getDisplayName();

        final JournalEntry journalEntry = new JournalEntry(title, body, getLabelsString(labels), color, updatedDate);
        AppExecutors.getsOnlyExecInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                //add new Journal to the DB
                if(mJournalId == DEFAULT_JOURNAL_ID){
                    long cloudId = mDb.journalDao().insertJournal(journalEntry);
                    Log.i(TAG, "THIS IS cloudId " + cloudId);
                    Journal journalForCloud = journalForCloud(mFirebaseUser.getEmail(),
                            title, body, getLabelsString(labels), color, cloudId,
                            updatedDate.toString());
                    insertToCloud(journalForCloud);
                    Log.d(TAG, "Saved new journal to DB");
                }else{
                    journalEntry.setId(mJournalId);
                    mDb.journalDao().updateJournal(journalEntry);
                    int cloudId = journalEntry.getId();
                    Journal cloudJournal = journalForCloud(mFirebaseUser.getEmail(),
                            title, body, getLabelsString(labels), color, cloudId,
                            updatedDate.toString());
                    updateCloud(cloudJournal);
                    Log.d(TAG, "Updated a journal");
                }
                finish();
            }
        });

        //Save to cloud

    }

    private void insertToCloud(Journal journal){
            mJournalReference.child(String.valueOf(journal.getJournalCloudId())).setValue(
                    journalForCloud(
                            mFirebaseUser.getEmail(),
                            journal.getJournalTitle(),
                            journal.getJournalBody(),
                            journal.getJournalLabel(),
                            journal.getJournalColor(),
                            journal.getJournalCloudId(),
                            journal.getJournalDate().toString()));

    }

    private void updateCloud(Journal journal){
        mJournalReference.child(String.valueOf(journal.getJournalCloudId())).setValue(
                journalForCloud(
                        mFirebaseUser.getEmail(),
                        journal.getJournalTitle(),
                        journal.getJournalBody(),
                        journal.getJournalLabel(),
                        journal.getJournalColor(),
                        journal.getJournalCloudId(),
                        journal.getJournalDate().toString()));
    }

    private Journal journalForCloud(String author,
                                    String journalTitle,
                                    String journalBody,
                                    String journalLabel,
                                    String journalColor,
                                    long journalCloudId,
                                    String journalDate){
        return new Journal(author, journalTitle, journalBody, journalLabel, journalColor, journalCloudId, journalDate);
    }

    public String getLabelsString(List<String> labels){
        StringBuilder labelsString = new StringBuilder();
        if(labels.size() == 1){
            labelsString = new StringBuilder(labels.get(0));
            return labelsString.toString();
        }else if(labels.size() == 0){
            return labelsString.toString();
        }else if(labels.size() > 1){
            for(int i=0; i<labels.size(); i++) {
                labelsString.append("|").append(labels.get(i));
            }
        }
        return labelsString.substring(1);
    }


    public List<String> getSelectedLabelsFromViews() {
        List<String> labels = new ArrayList<>();

        if(mInspiration.isChecked()){
            labels.add(INSPIRATION);
        }
        if(mPersonal.isChecked()){
            labels.add(PERSONAL);
        }
        if(mWork.isChecked()){
            labels.add(WORK);
        }
        return labels;
    }

    public String getColorFromViews(){
        String color;
        int checkedId = mColor.getCheckedRadioButtonId();

        switch(checkedId){
            case R.id.radioButton1:
                color = ORANGE;
                break;
            case R.id.radioButton2:
                color = BLUE;
                break;
            case R.id.radioButton3:
                color = LEMON;
                break;
            case R.id.radioButton4:
                color = YELLOW;
                break;
            case R.id.radioButton5:
                color = PINK;
                break;
            case R.id.radioButton6:
                color = PURPLE;
                break;
            case R.id.radioButton7:
                color = DARK_BLUE;
                break;
            case R.id.radioButton8:
                color = LIGHT_BLUE;
                break;
            default:
                color = WHITE;
        }
        return color;
    }

    public void setLabelView(String labelString) {

        if(labelString.equals("")){
            return;
        }
        if(labelString.contains("|")){
            Log.i(TAG, "labelString contains |");
            String[] labels = labelString.split("\\|");
            Log.i(TAG, "This is labels array " + labels);
            for (String label : labels) {
                switch (label) {
                    case INSPIRATION:
                        mInspiration.setChecked(true);
                        break;
                    case WORK:
                        mWork.setChecked(true);
                        break;
                    case PERSONAL:
                        mPersonal.setChecked(true);
                        break;
                }
            }
            return;
        }
        switch (labelString){
            case INSPIRATION:
                mInspiration.setChecked(true);
                break;
            case WORK:
                mWork.setChecked(true);
                break;
            case PERSONAL:
                mPersonal.setChecked(true);
                break;
        }
    }
}
