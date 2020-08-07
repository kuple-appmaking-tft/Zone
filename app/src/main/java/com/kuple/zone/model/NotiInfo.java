package com.kuple.zone.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class NotiInfo {
    private String title;
    private String content;
    private String documentId;
    private String BoardName;
    @ServerTimestamp
    private Date date;

    public String getBoardName() {
        return BoardName;
    }

    public void setBoardName(String boardName) {
        BoardName = boardName;
    }

    public NotiInfo() {
    }

    public NotiInfo(String title, String content, String documentId, Date date,String BoardName) {
        this.title = title;
        this.content = content;
        this.documentId = documentId;
        this.date = date;
        this.BoardName=BoardName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "NotiInfo{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", documentId='" + documentId + '\'' +
                ", BoardName='" + BoardName + '\'' +
                ", date=" + date +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

}
