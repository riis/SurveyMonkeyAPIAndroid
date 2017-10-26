package com.riis.surveymonkeytest.model;

import java.util.List;

public class SurveyPage {
    private int id;
    private int pageNumber;
    private List<SurveyQuestion> questionList;

    public SurveyPage(int id, int pageNumber, List<SurveyQuestion> questionList) {
        this.id = id;
        this.pageNumber = pageNumber;
        this.questionList = questionList;
    }

    public int getId() {
        return id;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public List<SurveyQuestion> getQuestionList() {
        return questionList;
    }
}
