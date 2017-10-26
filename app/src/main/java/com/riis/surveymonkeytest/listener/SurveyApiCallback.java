package com.riis.surveymonkeytest.listener;

import com.riis.surveymonkeytest.model.Survey;

public interface SurveyApiCallback {
    void onCallStart();
    void onCallComplete(Survey survey);
    void onCallComplete();
}
