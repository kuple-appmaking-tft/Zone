package com.kuple.zone.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class ReplyActModel {

    private String deleted_at;
    private String content;
    @ServerTimestamp
    private Date date;
    private String replyId;
    private List<String> uidLikelist;
    private String documentId;
    private String boardName;

    public ReplyActModel() {
    }

    public ReplyActModel(String deleted_at, String content, Date date, String replyId, List<String> uidLikelist, String documentId, String boardName) {
        this.deleted_at = deleted_at;
        this.content = content;
        this.date = date;
        this.replyId = replyId;
        this.uidLikelist = uidLikelist;
        this.documentId = documentId;
        this.boardName = boardName;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public List<String> getUidLikelist() {
        return uidLikelist;
    }

    public void setUidLikelist(List<String> uidLikelist) {
        this.uidLikelist = uidLikelist;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }
}
