package com.emefilefrancis.journalapp.com.emefilefrancis.journalapp.models;

/**
 * Created by SP on 6/28/2018.
 */


public class Journal {
    private String author;
    private String journalTitle;
    private String journalBody;
    private String journalLabel;
    private String journalColor;
    private long journalCloudId;
    private String journalDate;

    public Journal(){

    }

    public Journal(String author, String journalTitle, String journalBody, String journalLabel, String journalColor, long journalCloudId, String journalDate) {

        this.author = author;
        this.journalTitle = journalTitle;
        this.journalBody = journalBody;
        this.journalLabel = journalLabel;
        this.journalColor = journalColor;
        this.journalCloudId = journalCloudId;
        this.journalDate = journalDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public long getJournalCloudId() {
        return journalCloudId;
    }

    public void setJournalCloudId(long journalCloudId) {
        this.journalCloudId = journalCloudId;
    }

    public String getJournalDate() {
        return journalDate;
    }

    public void setJournalDate(String journalDate) {
        this.journalDate = journalDate;
    }
}
