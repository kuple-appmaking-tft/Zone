package com.kuple.zone.model;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class UserModel {

    private String uid;

    private String userEmail;
    private String phoneNumber;
    public String nickname;

    private String userName;
    private String token;
    private String userphoto;
    private String usermsg;

    private StudentModel studentModel;

    private ArrayList<String> favoritList;
    private ArrayList<BoardInfo> boardInfoList;
    private ArrayList<ReplyActModel> replyList;

    public ArrayList<ReplyActModel> getReplyList() {
        return replyList;
    }

    public void setReplyList(ArrayList<ReplyActModel> replyList) {
        this.replyList = replyList;
    }

    public ArrayList<BoardInfo> getBoardInfoList() {
        return boardInfoList;
    }

    public void setBoardInfoList(ArrayList<BoardInfo> boardInfoList) {
        this.boardInfoList = boardInfoList;
    }

    public StudentModel getStudentModel() {
        return studentModel;
    }

    public void setStudentModel(StudentModel studentModel) {
        this.studentModel = studentModel;
    }

    public ArrayList<String> getFavoritList() {
        return favoritList;
    }

    public void setFavoritList(ArrayList<String> favoritList) {
        this.favoritList = favoritList;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public String getUsermsg() {
        return usermsg;
    }

    public void setUsermsg(String usermsg) {
        this.usermsg = usermsg;
    }

}

