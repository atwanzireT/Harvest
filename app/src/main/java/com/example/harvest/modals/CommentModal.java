package com.example.harvest.modals;

public class CommentModal {
    String issueID, comment, author;

    public CommentModal(){}
    public CommentModal(String issueID, String comment, String author) {
        this.issueID = issueID;
        this.comment = comment;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIssueID() {
        return issueID;
    }

    public void setIssueID(String issueID) {
        this.issueID = issueID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
