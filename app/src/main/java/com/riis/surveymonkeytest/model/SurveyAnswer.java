package com.riis.surveymonkeytest.model;

public class SurveyAnswer {
    private boolean isVisible;
    private int id;
    private int position;
    private String text;

    public SurveyAnswer(boolean isVisible, int id, int position, String text) {
        this.isVisible = isVisible;
        this.id = id;
        this.position = position;
        this.text = text;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public int getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public String getText() {
        return text;
    }
}
