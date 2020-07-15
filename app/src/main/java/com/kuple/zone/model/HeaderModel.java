package com.kuple.zone.model;

import java.util.List;

public class HeaderModel {
    public String text;
    public HeaderModel() {
    }

    public HeaderModel(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "HeaderModel{" +
                "text='" + text + '\'' +
                '}';
    }

}
