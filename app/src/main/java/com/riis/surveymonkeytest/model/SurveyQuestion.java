package com.riis.surveymonkeytest.model;

import java.util.List;

public class SurveyQuestion {
    private boolean isVisible;
    private int id;
    private int position;
    private List<SurveyAnswer> answerList;
    private String question;

    public SurveyQuestion(boolean isVisible, int id, int position, List<SurveyAnswer> answerList, String question) {
        this.isVisible = isVisible;
        this.id = id;
        this.position = position;
        this.answerList = answerList;
        this.question = question;
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

    public List<SurveyAnswer> getAnswerList() {
        return answerList;
    }

    public String getQuestion() {
        return question;
    }
}
