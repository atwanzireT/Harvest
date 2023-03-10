package com.example.harvest.modals;

public class IssueModal {

    String title, detail, author;

    public IssueModal(){}
    //    constructor
    public IssueModal(String title, String detail, String author) {
        this.title = title;
        this.detail = detail;
        this.author = author;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
