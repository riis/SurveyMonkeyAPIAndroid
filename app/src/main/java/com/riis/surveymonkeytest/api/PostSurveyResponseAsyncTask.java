package com.riis.surveymonkeytest.api;

import android.os.AsyncTask;

import com.riis.surveymonkeytest.listener.SurveyApiCallback;
import com.riis.surveymonkeytest.model.SurveySubmitRequest;

import org.json.JSONException;

import java.io.IOException;

public class PostSurveyResponseAsyncTask extends AsyncTask<SurveySubmitRequest, Void, Void> {
    private SurveyApiCallback surveyApiCallback;

    public PostSurveyResponseAsyncTask(SurveyApiCallback surveyApiCallback) {
        this.surveyApiCallback = surveyApiCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        surveyApiCallback.onCallStart();
    }

    @Override
    protected Void doInBackground(SurveySubmitRequest... params) {
        try {
            HttpSurveyManager httpSurveyManager = new HttpSurveyManager();
            httpSurveyManager.submitSurveyAnswers(params[0]);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        surveyApiCallback.onCallComplete();
    }
}
