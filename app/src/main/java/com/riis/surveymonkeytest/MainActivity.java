package com.riis.surveymonkeytest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.riis.surveymonkeytest.api.GetSurveyAsyncTask;
import com.riis.surveymonkeytest.api.PostSurveyResponseAsyncTask;
import com.riis.surveymonkeytest.listener.SurveyApiCallback;
import com.riis.surveymonkeytest.model.Survey;
import com.riis.surveymonkeytest.model.SurveyAnswer;
import com.riis.surveymonkeytest.model.SurveyPage;
import com.riis.surveymonkeytest.model.SurveyQuestion;
import com.riis.surveymonkeytest.model.SurveySubmitRequest;

public class MainActivity extends AppCompatActivity implements SurveyApiCallback {
    private ProgressDialog progressDialog;
    private RadioGroup answerRadioGroup;
    private SurveySubmitRequest submitRequest;
    private TextView questionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView = (TextView) findViewById(R.id.question_text_view);
        answerRadioGroup = (RadioGroup) findViewById(R.id.answer_radio_group);

        Button submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSurvey();
            }
        });

        new GetSurveyAsyncTask(this).execute();
    }

    @Override
    public void onCallStart() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onCallComplete() {
        progressDialog.dismiss();
    }

    @Override
    public void onCallComplete(Survey survey) {
        setUpSurvey(survey);
        onCallComplete();
    }

    private void setUpSurvey(Survey survey) {
        submitRequest = new SurveySubmitRequest(survey.getId());

        for (final SurveyPage surveyPage : survey.getPageList()) {
            for (final SurveyQuestion surveyQuestion : surveyPage.getQuestionList()) {
                questionTextView.setText(surveyQuestion.getQuestion());

                for (final SurveyAnswer surveyAnswer : surveyQuestion.getAnswerList()) {
                    RadioButton radioButton = new RadioButton(this);
                    radioButton.setText(surveyAnswer.getText());
                    radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                submitRequest.addAnswer(surveyPage.getId(), surveyQuestion.getId(), surveyAnswer.getId());
                                setAnswer();
                            }
                        }
                    });

                    answerRadioGroup.addView(radioButton);
                }
            }
        }
    }

    private void submitSurvey() {
        if (submitRequest.getPageQuestionMap().size() == 0) {
            Toast.makeText(this, "Please answer the question", Toast.LENGTH_LONG).show();
            return;
        }

        new PostSurveyResponseAsyncTask(this).execute(submitRequest);
    }

    private void setAnswer() {
        Toast.makeText(this, "Answered", Toast.LENGTH_SHORT).show();
    }
}
