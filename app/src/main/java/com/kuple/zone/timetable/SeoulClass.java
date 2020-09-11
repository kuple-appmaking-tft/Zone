package com.kuple.zone.timetable;

public class SeoulClass {
    public String classNum; // 분반
    public String code; // 학수번호
    public String name; // 수업이름
    public String professor; // 교수이름
    public String sel; // 전공 or 교양
    public String time; // 날짜 , 시간 , 위치 eX) 수(7-9) 농심국제관 308호

    public String getClassNum(){return classNum;}
    public String getCode(){return code;}
    public String getName(){return name;}
    public String getProfessor(){return professor;}
    public String getSel(){return sel;}
    public String getTime(){return time;}

    public void setClassNum(String classNum) {this.classNum = classNum;}
    public void setCode(String code){this.code = code;}
    public void setName(String name){this.name = name;}
    public void setProfessor(String professor){this.professor = professor;}
    public void setSel(String sel){this.sel = sel;}
    public void setTime(String time){this.time = time;}
}


