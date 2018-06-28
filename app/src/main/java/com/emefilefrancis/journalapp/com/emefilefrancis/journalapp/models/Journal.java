package com.emefilefrancis.journalapp.com.emefilefrancis.journalapp.models;

/**
 * Created by SP on 6/28/2018.
 */


public class Journal {
    public String author;
    public String journalTitle;
    public String journalBody;
    public String journalLabel;
    public String journalColor;
    public String journalDate;

    public Journal(){

    }

    public Journal(String author, String journalTitle, String journalBody, String journalLabel, String journalColor, String journalDate) {

        this.author = author;
        this.journalTitle = journalTitle;
        this.journalBody = journalBody;
        this.journalLabel = journalLabel;
        this.journalColor = journalColor;
        this.journalDate = journalDate;
    }
}
