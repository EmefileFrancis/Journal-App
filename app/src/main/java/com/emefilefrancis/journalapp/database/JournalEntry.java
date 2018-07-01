package com.emefilefrancis.journalapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by SP on 6/25/2018.
 */
@Entity(tableName = "journal")
public class JournalEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "journal_title")
    private String journalTitle;

    @ColumnInfo(name = "journal_body")
    private String journalBody;

    @ColumnInfo(name = "journal_label")
    private String journalLabel;

    @ColumnInfo(name = "journal_color")
    private String journalColor;

//    @ColumnInfo(name = "journal_cloud_id")
//    private String journalCloudId;

    @ColumnInfo(name = "updated_date")
    private Date updatedDate;

    @Ignore
    public JournalEntry(String journalTitle, String journalBody, String journalLabel, String journalColor, Date updatedDate) {
        this.journalTitle = journalTitle;
        this.journalBody = journalBody;
        this.journalLabel = journalLabel;
        this.journalColor = journalColor;
        //this.journalCloudId = journalCloudId;
        this.updatedDate = updatedDate;
    }

    public JournalEntry(int id, String journalTitle, String journalBody, String journalLabel, String journalColor, Date updatedDate) {
        this.id = id;
        this.journalTitle = journalTitle;
        this.journalBody = journalBody;
        this.journalLabel = journalLabel;
        this.journalColor = journalColor;
        //this.journalCloudId = journalCloudId;
        this.updatedDate = updatedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJournalTitle() {
        return journalTitle;
    }

    public void setJournalTitle(String journalTitle) {
        this.journalTitle = journalTitle;
    }

    public String getJournalBody() {
        return journalBody;
    }

    public void setJournalBody(String journalBody) {
        this.journalBody = journalBody;
    }

    public String getJournalLabel() {
        return journalLabel;
    }

    public void setJournalLabel(String journalLabel) {
        this.journalLabel = journalLabel;
    }

    public String getJournalColor() {
        return journalColor;
    }

    public void setJournalColor(String journalColor) {
        this.journalColor = journalColor;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

//    public String getJournalCloudId() {
//        return journalCloudId;
//    }
//
//    public void setJournalCloudId(String journalCloudId) {
//        this.journalCloudId = journalCloudId;
//    }
}
