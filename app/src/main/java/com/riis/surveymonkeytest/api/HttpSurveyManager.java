package com.riis.surveymonkeytest.api;

import com.riis.surveymonkeytest.model.Survey;
import com.riis.surveymonkeytest.model.SurveyAnswer;
import com.riis.surveymonkeytest.model.SurveyPage;
import com.riis.surveymonkeytest.model.SurveyQuestion;
import com.riis.surveymonkeytest.model.SurveySubmitRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpSurveyManager {
    private static final String AUTH_TOKEN =
            "AZP0g9q4xAM3unUhy6.w-bB9YvKUJ1DbTS-XhIhl9e9fI-hzT0lvLebg00N28hbfZzl51d3sFSD6gjpTRUZZGq1YCMBvbznVJkcwl7mR6SjfBcucKxMexLChjBmWpVRc";
    private static final String GET_SURVEYS_URL = "https://api.surveymonkey.com/v3/surveys";

    public int getSurveyId() throws IOException, JSONException {
        OkHttpClient httpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(GET_SURVEYS_URL)
                .addHeader("Authorization", "Bearer " + AUTH_TOKEN)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = httpClient.newCall(request).execute();

        return new JSONObject(response.body().string()).getJSONArray("data").getJSONObject(0).getInt("id");
    }

    public Survey getSurveyDetails(int id) throws IOException, JSONException {
        OkHttpClient httpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(getSurveyPageUrl(id))
                .addHeader("Authorization", "Bearer " + AUTH_TOKEN)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = httpClient.newCall(request).execute();

        JSONObject jsonResponse = new JSONObject(response.body().string());
        String surveyTitle = jsonResponse.getString("title");
        JSONArray jsonArray = jsonResponse.getJSONArray("pages");

        List<SurveyPage> surveyPages = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject pageObject = jsonArray.getJSONObject(i);
            int pagePosition = pageObject.getInt("position");
            int pageId = pageObject.getInt("id");

            List<SurveyQuestion> surveyQuestions = new ArrayList<>();
            JSONArray questionArray = pageObject.getJSONArray("questions");
            for (int j = 0; j < questionArray.length(); j++) {
                JSONObject questionObject = questionArray.getJSONObject(j);
                boolean isQuestionVisible = questionObject.getBoolean("visible");

                if (!isQuestionVisible) {
                    continue;
                }

                int questionPosition = questionObject.getInt("position");
                int questionId = questionObject.getInt("id");

                List<SurveyAnswer> surveyAnswers = new ArrayList<>();
                JSONArray answerArray = questionObject.getJSONObject("answers").getJSONArray("choices");
                for (int k = 0; k < answerArray.length(); k++) {
                    JSONObject answerObject = answerArray.getJSONObject(k);

                    boolean isAnswerVisible = answerObject.getBoolean("visible");
                    if (!isAnswerVisible) {
                        continue;
                    }

                    int answerPosition = answerObject.getInt("position");
                    int answerId = answerObject.getInt("id");
                    String answerText = answerObject.getString("text");

                    SurveyAnswer surveyAnswer = new SurveyAnswer(isAnswerVisible, answerId, answerPosition, answerText);
                    surveyAnswers.add(surveyAnswer);
                }

                String questionText = questionObject.getJSONArray("headings").getJSONObject(0).getString("heading");

                SurveyQuestion surveyQuestion = new SurveyQuestion(isQuestionVisible, questionId,
                        questionPosition, surveyAnswers, questionText);
                surveyQuestions.add(surveyQuestion);
            }

            SurveyPage surveyPage = new SurveyPage(pageId, pagePosition, surveyQuestions);
            surveyPages.add(surveyPage);
        }

        return new Survey(id, surveyPages, surveyTitle);
    }

    public boolean submitSurveyAnswers(SurveySubmitRequest surveySubmitRequest) throws IOException, JSONException {
        OkHttpClient httpClient = new OkHttpClient();

        JSONObject requestObject = new JSONObject();

        JSONObject customObject = new JSONObject();
        customObject.put("user_id", "thisIsATest");
        requestObject.put("custom_variables", customObject);

        HashMap<Integer, HashMap<Integer, Integer>> pageQuestionMap = surveySubmitRequest.getPageQuestionMap();
        JSONArray pagesArray = new JSONArray();
        for (Integer pageKey : pageQuestionMap.keySet()) {
            HashMap<Integer, Integer> questionAnswerMap = pageQuestionMap.get(pageKey);

            JSONObject pageObject = new JSONObject();
            pageObject.put("id", pageKey.toString());

            JSONArray questionsArray = new JSONArray();
            for (Integer questionKey : questionAnswerMap.keySet()) {
                Integer answerId = questionAnswerMap.get(questionKey);

                JSONObject questionObject = new JSONObject();
                questionObject.put("id", questionKey.toString());

                JSONArray answerArray = new JSONArray();
                JSONObject answerObject = new JSONObject();
                answerObject.put("choice_id", answerId.toString());
                answerArray.put(answerObject);

                questionObject.put("answers", answerArray);
                questionsArray.put(questionObject);
            }

            pageObject.put("questions", questionsArray);
            pagesArray.put(pageObject);
        }

        requestObject.put("pages", pagesArray);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(requestObject));

        Request request = new Request.Builder()
                .url(getSurveyAnswerUrl())
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + AUTH_TOKEN)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = httpClient.newCall(request).execute();

        return response.isSuccessful();
    }

    private String getSurveyAnswerUrl() {
        return "https://api.surveymonkey.com/v3/collectors/164344137/responses";
    }

    private String getSurveyPageUrl(int id) {
        return "https://api.surveymonkey.com/v3/surveys/" + id + "/details";
    }
}
