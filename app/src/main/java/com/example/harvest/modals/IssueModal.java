package com.example.harvest.modals;

import android.net.Uri;

public class IssueModal {

    String title, detail, author;
    String imageUri;
    public IssueModal() {
        // Default constructor required for calls to DataSnapshot.getValue(IssueModal.class)
    }

    public IssueModal(String title, String detail, String author, Uri imageUri){}
    //    constructor
    public IssueModal(String title, String detail, String author,String imageUri) {
        this.title = title;
        this.detail = detail;
        this.author = author;
        this.imageUri=imageUri;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getAuthor() {
        return author;
    }

    public String getImageUri() {return imageUri;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public void setImageUri(String imageUri) {this.imageUri=imageUri;  }


}
