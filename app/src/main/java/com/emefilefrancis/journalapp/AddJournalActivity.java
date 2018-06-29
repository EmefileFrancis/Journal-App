package com.emefilefrancis.journalapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;

import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
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

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private DatabaseReference journalReference;


    private EditText etJournalTitle;
    private EditText etJournalBody;
    private CheckBox cbInspiration;
    private CheckBox cbWork;
    private CheckBox cbPersonal;
    private RadioGroup rgColor;
    private Button btnAdd;

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

        mDb = AppDatabase.getOnlyDBInstance(getApplicationContext());


        databaseReference = FirebaseDatabase.getInstance().getReference();
        journalReference = FirebaseDatabase.getInstance().getReference("journals");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "firebaseUser: " + firebaseUser.getEmail());


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
        etJournalTitle.setFocusable(false);
        etJournalTitle.setClickable(true);
        etJournalTitle.setFocusableInTouchMode(false);
        etJournalBody.setFocusable(false);
        etJournalBody.setFocusableInTouchMode(false);
        etJournalBody.setClickable(true);
    }

    /*public void enableEdit(View view) {
        Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show();
        etJournalTitle.setFocusable(true);
        etJournalTitle.setFocusableInTouchMode(true);
        etJournalTitle.setClickable(true);
        etJournalBody.setFocusable(true);
        etJournalBody.setFocusableInTouchMode(true);
        etJournalBody.setClickable(true);
    }*/

    private void enableEditByPen(){
        etJournalTitle.setFocusable(true);
        etJournalTitle.setFocusableInTouchMode(true);
        etJournalTitle.setClickable(true);
        etJournalBody.setFocusable(true);
        etJournalBody.setFocusableInTouchMode(true);
        etJournalBody.setClickable(true);
    }

    private void populateUI(JournalEntry journalEntry) {
        if(journalEntry == null){
            return;
        }
        etJournalTitle.setText(journalEntry.getJournalTitle());
        etJournalBody.setText(journalEntry.getJournalBody());
        String labelString = getLabelsStringFromDB(journalEntry);
        Log.i(TAG, "This is labelString: " + labelString);
        setLabelView(labelString);
        setColorView(journalEntry.getJournalColor());
    }

    private void setColorView(String journalColor) {

        switch (journalColor){
            case ORANGE:
                rgColor.check(R.id.radioButton1);
                break;
            case BLUE:
                rgColor.check(R.id.radioButton2);
                break;
            case LEMON:
                rgColor.check(R.id.radioButton3);
                break;
            case YELLOW:
                rgColor.check(R.id.radioButton4);
                break;
            case PINK:
                rgColor.check(R.id.radioButton5);
                break;
            case PURPLE:
                rgColor.check(R.id.radioButton6);
                break;
            case DARK_BLUE:
                rgColor.check(R.id.radioButton7);
                break;
            case LIGHT_BLUE:
                rgColor.check(R.id.radioButton8);
                break;
            case WHITE:
                break;

        }
    }

    private String getLabelsStringFromDB(JournalEntry journalEntry) {
        String labelString = journalEntry.getJournalLabel();
        return labelString;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
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
            case R.id.reminder_menu:
                Toast.makeText(this, "Reminder Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_journal_menu:
                Toast.makeText(this, "Edit Clicked", Toast.LENGTH_SHORT).show();
                enableEditByPen();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkIfJournalBodyIsEmpty() {
        if(etJournalBody.getText().toString().equals("")){
            return true;
        }
        return false;
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
        etJournalTitle = findViewById(R.id.et_journal_title);
        etJournalBody = findViewById(R.id.et_journal_body);
        cbInspiration = findViewById(R.id.cb_inspiration);
        cbWork = findViewById(R.id.cb_work);
        cbPersonal = findViewById(R.id.cb_personal);
        rgColor = findViewById(R.id.radio_group);
    }

    public void saveToDB() {
        String title = etJournalTitle.getText().toString();
        String body = etJournalBody.getText().toString();
        List<String> labels = getSelectedLabelsFromViews();
        Log.i(TAG, "labels: " + labels);
        String color = getColorFromViews();
        Date updatedDate = new Date();

        final JournalEntry journalEntry = new JournalEntry(title, body, getLabelsString(labels), color, updatedDate);
        AppExecutors.getOnlyExecInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                //add new Journal to the DB
                if(mJournalId == DEFAULT_JOURNAL_ID){
                    mDb.journalDao().insertJournal(journalEntry);
                    Log.d(TAG, "Saved new journal to DB");
                }else{
                    journalEntry.setId(mJournalId);
                    mDb.journalDao().updateJournal(journalEntry);
                    Log.d(TAG, "Updated a journal");
                }
                finish();
            }
        });

        //Save to cloud
        processCloudPersistence(journalEntry);
    }

    private void processCloudPersistence(JournalEntry journalEntry){
        if(mJournalId == DEFAULT_JOURNAL_ID){
            String id = journalReference.push().getKey();
            journalReference.child(id).setValue(
                    journalForCloud(
                            firebaseUser.getEmail(),
                            journalEntry.getJournalTitle(),
                            journalEntry.getJournalBody(),
                            journalEntry.getJournalLabel(),
                            journalEntry.getJournalColor(),
                            journalEntry.getUpdatedDate().toString()));
        }else{
            //journalReference.child()
        }

//        databaseReference.child("users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//
//                if(user == null){
//                    Log.e(TAG, "onDataChange: User data is null");
//                    Toast.makeText(AddJournalActivity.this, "onDataChanged: User data is null!", Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                saveNewJournalToCloud(user.email, journalEntry.getJournalTitle(),
//                        journalEntry.getJournalBody(),journalEntry.getJournalLabel(),
//                        journalEntry.getJournalColor(), journalEntry.getUpdatedDate().toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e(TAG, "onCancelled: Failed to read user!");
//            }
//        });
    }

    private Journal journalForCloud(String author,
                                    String journalTitle,
                                    String journalBody,
                                    String journalLabel,
                                    String journalColor,
                                    String journalDate){
        Journal journal = new Journal(author, journalTitle, journalBody, journalLabel, journalColor, journalDate);
        return journal;
    }

    public String getLabelsString(List<String> labels){
        String labelsString = "";
        if(labels.size() == 1){
            labelsString = labels.get(0);
            return labelsString;
        }else if(labels.size() == 0){
            return labelsString;
        }else if(labels.size() > 1){
            for(int i=0; i<labels.size(); i++) {
                labelsString = labelsString + "|" + labels.get(i);
            }
        }
        String newString = labelsString.substring(1);
        return newString;
    }


    public List<String> getSelectedLabelsFromViews() {
        List<String> labels = new ArrayList<>();

        if(cbInspiration.isChecked()){
            labels.add(INSPIRATION);
        }
        if(cbPersonal.isChecked()){
            labels.add(PERSONAL);
        }
        if(cbWork.isChecked()){
            labels.add(WORK);
        }
        return labels;
    }

    public String getColorFromViews(){
        String color = "";
        int checkedId = rgColor.getCheckedRadioButtonId();

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
            for(int i=0; i<labels.length; i++){
                switch(labels[i]){
                    case INSPIRATION:
                        cbInspiration.setChecked(true);
                        break;
                    case WORK:
                        cbWork.setChecked(true);
                        break;
                    case PERSONAL:
                        cbPersonal.setChecked(true);
                        break;
                }
            }
            return;
        }
        switch (labelString){
            case INSPIRATION:
                cbInspiration.setChecked(true);
                break;
            case WORK:
                cbWork.setChecked(true);
                break;
            case PERSONAL:
                cbPersonal.setChecked(true);
                break;
        }
    }


}
