package com.riis.surveymonkeytest.model;

import android.util.SparseIntArray;

import java.util.HashMap;

public class SurveySubmitRequest {
    private int surveyId;
    private HashMap<Integer, HashMap<Integer, Integer>> pageQuestionMap;

    public SurveySubmitRequest(int surveyId) {
        this.surveyId = surveyId;
        pageQuestionMap = new HashMap<>();
    }

    public int getSurveyId() {
        return surveyId;
    }

    public HashMap<Integer, HashMap<Integer, Integer>> getPageQuestionMap() {
        return pageQuestionMap;
    }

    public void addAnswer(int pageId, int questionId, int answerId) {
        if (pageQuestionMap.containsKey(pageId)) {
            HashMap<Integer, Integer> questionAnswerMap = pageQuestionMap.get(pageId);
            questionAnswerMap.put(questionId, answerId);
        } else {
            HashMap<Integer, Integer> questionAnswerMap = new HashMap<>();
            questionAnswerMap.put(questionId, answerId);
            pageQuestionMap.put(pageId, questionAnswerMap);
        }
    }
}
