package com.emefilefrancis.journalapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.emefilefrancis.journalapp.database.AppDatabase;
import com.emefilefrancis.journalapp.database.JournalEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddJournalActivity extends AppCompatActivity {

    public static final String RED = "red";
    public static final String GREEN = "green";
    public static final String YELLOW = "yellow";
    public static final String ORANGE = "orange";

    public static final String INSPIRATION = "inspiration";
    public static final String WORK = "work";
    public static final String PERSONAL = "personal";

    private static final int DEFAULT_TASK_ID = -1;
    public static final String EXTRA_JOURNAL_ID = "extraJournalId";

    private AppDatabase mDb;

    private EditText etJournalTitle;
    private EditText etJournalBody;
    private CheckBox cbInspiration;
    private CheckBox cbWork;
    private CheckBox cbPersonal;
    private RadioGroup rgColor;
    private Button btnAdd;

    private int mTaskId = DEFAULT_TASK_ID;

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

    private void initViews() {
        etJournalTitle = findViewById(R.id.et_journal_title);
        etJournalBody = findViewById(R.id.et_journal_body);
        cbInspiration = findViewById(R.id.cb_inspiration);
        cbWork = findViewById(R.id.cb_work);
        cbPersonal = findViewById(R.id.cb_personal);
        rgColor = findViewById(R.id.radio_group);
        btnAdd = findViewById(R.id.addButton);
    }

    public void saveToDB(View view) {
        String title = etJournalTitle.getText().toString();
        String body = etJournalBody.getText().toString();
        List<String> labels = getSelectedLabelsFromViews();
        String color = getColorFromViews();
        Date updatedDate = new Date();

        final JournalEntry journalEntry = new JournalEntry(title, body, getLabelsString(labels), color, updatedDate);
        AppExecutors.getOnlyExecInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                //add new Journal to the DB
                if(mTaskId == DEFAULT_TASK_ID){
                    mDb.journalDao().insertJournal(journalEntry);
                }else{
                    journalEntry.setId(mTaskId);
                    mDb.journalDao().updateJournal(journalEntry);
                }
                finish();
            }
        });
    }

    public String getLabelsString(List<String> labels){
        String labelsString = "";
        for(int i=0; i<labels.size(); i++){
            labelsString =  labelsString + "|" + labels.get(i);
        }
        return labelsString;
    }


    public List<String> getSelectedLabelsFromViews() {
        List<String> labels = new ArrayList<>();

        if(cbInspiration.isChecked()){
            labels.add(INSPIRATION);
        }else if(cbPersonal.isChecked()){
            labels.add(PERSONAL);
        }else if(cbWork.isChecked()){
            labels.add(WORK);
        }
        return labels;
    }

    public String getColorFromViews(){
        String color = null;
        int checkedId = rgColor.getCheckedRadioButtonId();
        switch(checkedId){
            case R.id.radioButton1:
                color = RED;
                break;
            case R.id.radioButton2:
                color = GREEN;
                break;
            case R.id.radioButton3:
                color = YELLOW;
                break;
            case R.id.radioButton4:
                color = ORANGE;
        }
        return color;
    }
}
