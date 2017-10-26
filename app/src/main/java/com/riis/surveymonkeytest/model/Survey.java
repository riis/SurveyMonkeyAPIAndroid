package com.riis.surveymonkeytest.model;

import java.util.List;

public class Survey {
    private int id;
    private List<SurveyPage> pageList;
    private String name;

    public Survey(int id, List<SurveyPage> pageList, String name) {
        this.id = id;
        this.pageList = pageList;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public List<SurveyPage> getPageList() {
        return pageList;
    }

    public String getName() {
        return name;
    }
}
