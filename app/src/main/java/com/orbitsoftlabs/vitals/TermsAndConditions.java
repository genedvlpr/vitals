package com.orbitsoftlabs.vitals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.orbitsoftlabs.vitals.HomeUtils.HomeConstituents;
import com.orbitsoftlabs.vitals.HomeUtils.HomeHealthWorker;

public class TermsAndConditions extends AppCompatActivity {

    private Class aClass;
    private String last_activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        WebView simpleWebView = findViewById(R.id.terms_and_condition_view);
        // specify the url of the web page in loadUrl function
        simpleWebView.loadUrl("https://firebasestorage.googleapis.com/v0/b/vitals-b53aa.appspot.com/o/resources%2Fterms_and_condition.html?alt=media&token=54545f76-a0cb-4b50-8b66-d5c77de8850c");

        try {
            Intent i = getIntent();
            last_activity = i.getStringExtra("activity");
            Log.d("Last activity", last_activity+"");

            if (last_activity.contains("Worker")){
                aClass = HomeHealthWorker.class;
            }   else {
                aClass = HomeConstituents.class;
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        HelperUtil.backNavigationFinish(this,aClass);
    }
}
