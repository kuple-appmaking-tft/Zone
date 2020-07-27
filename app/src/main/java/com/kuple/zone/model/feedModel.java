package com.kuple.zone.model;
import java.util.List;
public class feedModel {
    private List<BusInfo> shuttleBusInfos;
    private List<Timetable> timetable;


    public void setShuttleBusInfos(List<BusInfo> shuttleBusInfos) {
        this.shuttleBusInfos = shuttleBusInfos;
    }

    public void setTimetable(List<Timetable> timetable) {
        this.timetable = timetable;
    }

    public List<BusInfo> getShuttleBusInfos() {
        return shuttleBusInfos;
    }

    public List<Timetable> getTimetable() {
        return timetable;
    }

    @Override
    public String toString() {
        return "feedModel{" +
                "shuttleBus=" + shuttleBusInfos +
                ", timetable=" + timetable +
                '}';
    }

}