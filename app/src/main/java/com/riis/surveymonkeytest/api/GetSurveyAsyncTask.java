package com.riis.surveymonkeytest.api;

import android.os.AsyncTask;

import com.riis.surveymonkeytest.listener.SurveyApiCallback;
import com.riis.surveymonkeytest.model.Survey;

import org.json.JSONException;

import java.io.IOException;

public class GetSurveyAsyncTask extends AsyncTask<Void, Void, Survey> {
    private SurveyApiCallback surveyApiCallback;

    public GetSurveyAsyncTask(SurveyApiCallback surveyApiCallback) {
        this.surveyApiCallback = surveyApiCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        surveyApiCallback.onCallStart();
    }

    @Override
    protected Survey doInBackground(Void... params) {
        HttpSurveyManager httpSurveyManager = new HttpSurveyManager();

        try {
            int surveyId = httpSurveyManager.getSurveyId();
            return httpSurveyManager.getSurveyDetails(surveyId);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Survey survey) {
        super.onPostExecute(survey);
        surveyApiCallback.onCallComplete(survey);
    }
}
