package com.example.harvest.modals;

public class CommentModal {
    String issueID, comment;

    public CommentModal(String issueID, String comment) {
        this.issueID = issueID;
        this.comment = comment;
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
