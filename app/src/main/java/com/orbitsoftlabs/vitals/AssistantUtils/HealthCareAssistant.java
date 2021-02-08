package com.orbitsoftlabs.vitals.AssistantUtils;

import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.EditText;
import android.widget.TextView;

import com.orbitsoftlabs.vitals.R;
import com.orbitsoftlabs.vitals.HelperUtil;
import com.orbitsoftlabs.vitals.HomeUtils.HomeConstituents;
import com.orbitsoftlabs.vitals.HomeUtils.HomeHealthWorker;

import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.ChatView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.Objects;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.android.GsonFactory;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;


public class HealthCareAssistant extends Activity implements AIListener {

    private ChatView mChatView;

    private HealthCareAssistant context = this;

    public static final String TAG = "AI";

    private Gson gson = GsonFactory.getGson();

    private TextView resultTextView;
    private EditText contextEditText;
    private EditText queryEditText;
    private AIRequest aiRequest;
    private AIDataService aiDataService;
    private AIService aiService;

    private Animation fadeIn,fadeOut;
    private AnimationSet animation;

    private Keyboard keyCode;

    private String last_activity;
    private Class aClass;

    private TextInputLayout tl_message;
    private MaterialButton btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_care_assistant);

        tl_message = findViewById(R.id.tl_message);
        btn_send = findViewById(R.id.btn_send);

        final AIConfiguration config = new AIConfiguration("2dd8a6690e96460b9e188bb3cf4d141e",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiDataService = new AIDataService(config);

        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateDecelerateInterpolator()); //add this
        fadeIn.setDuration(500);

        fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateDecelerateInterpolator()); //add this
        fadeOut.setDuration(300);
        //User id
        int myId = 0;
        //User icon
        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_foreground);
        //User name
        String myName = "You";

        int yourId = 1;
        Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_foreground);
        String yourName = "Assistant";

        final User me = new User(myId, myName, myIcon);
        final User you = new User(yourId, yourName, yourIcon);

        mChatView = (ChatView) findViewById(R.id.chat_view);
        //Set UI parameters if you need
        mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.my_app_color_on_primary));
        mChatView.setLeftBubbleColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        mChatView.setMessageFontSize(24);
        mChatView.setSendIcon(R.drawable.ic_outline_send_24px);

        mChatView.setSendButtonColor(R.color.colorPrimary);
        //mChatView.setSendButtonColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mChatView.setDateSeparatorFontSize(0);
        mChatView.setTimeLabelFontSize(0);
        mChatView.setRightMessageTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        mChatView.setLeftMessageTextColor(Color.WHITE);
        mChatView.setUsernameTextColor(Color.WHITE);
        mChatView.setSendTimeTextColor(Color.WHITE);
        mChatView.setDateSeparatorColor(R.color.colorNavigationBar);
        mChatView.setDateSeparatorFontSize(0);
        mChatView.setInputTextHint("Ask something.");
        mChatView.setMessageMarginTop(5);
        mChatView.setMessageMarginBottom(5);
        mChatView.setEnableSwipeRefresh(true);
        mChatView.setSoundEffectsEnabled(true);

        final Message receivedMessage = new Message.Builder()
                .setUser(you)
                .setRight(false)
                .setText("Hi, How can I help?")
                .hideIcon(false)
                .setUsernameVisibility(false)
                .build();
        mChatView.receive(receivedMessage);
        //Click Send Button

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new message

                if (!Objects.requireNonNull(tl_message.getEditText()).getText().toString().equals("")){
                    sendRequest();
                    Message message = new Message.Builder()
                            .setUser(me)
                            .setUsernameVisibility(false)
                            .setRight(true)
                            .setText(Objects.requireNonNull(tl_message.getEditText()).getText().toString())
                            .hideIcon(true)
                            .build();
                    //Set to chat view
                    mChatView.send(message);
                    //Reset edit text
                    tl_message.getEditText().setText("");
                    if (message.getText().toString().matches("Open Google Maps")){
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=Hospitals with Panretinal Laser Surgery");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                } else {
                    tl_message.getEditText().setHint("Ask something...");
                }

            }
        });

        final MaterialButton what_can_you_do = findViewById(R.id.what_can_you_do);
        what_can_you_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
                Message message = new Message.Builder()
                        .setUser(me)
                        .setUsernameVisibility(false)
                        .setRight(true)
                        .setText(what_can_you_do.getText().toString())
                        .hideIcon(true)
                        .build();
                //Set to chat view
                //mChatView.send(message);
                //Reset edit text
                Objects.requireNonNull(tl_message.getEditText()).setText(what_can_you_do.getText().toString());
            }
        });

        final MaterialButton solution = findViewById(R.id.solutions);
        solution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
                Message message = new Message.Builder()
                        .setUser(me)
                        .setUsernameVisibility(false)
                        .setRight(true)
                        .setText(solution.getText().toString())
                        .hideIcon(true)
                        .build();
                //Set to chat view
                //mChatView.send(message);
                //Reset edit text
                Objects.requireNonNull(tl_message.getEditText()).setText(solution.getText().toString());
            }
        });

        final MaterialButton near = findViewById(R.id.nearest_opthal);
        near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
                Message message = new Message.Builder()
                        .setUser(me)
                        .setUsernameVisibility(false)
                        .setRight(true)
                        .setText(near.getText().toString())
                        .hideIcon(true)
                        .build();
                //Set to chat view
                //mChatView.send(message);
                //Reset edit text
                Objects.requireNonNull(tl_message.getEditText()).setText(near.getText().toString());

            }
        });
        final MaterialButton about_dr = findViewById(R.id.about_dr);
        about_dr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
                Message message = new Message.Builder()
                        .setUser(me)
                        .setUsernameVisibility(false)
                        .setRight(true)
                        .setText(about_dr.getText().toString())
                        .hideIcon(true)
                        .build();
                //Set to chat view
                //mChatView.send(message);
                //Reset edit text
                Objects.requireNonNull(tl_message.getEditText()).setText(about_dr.getText().toString());
            }
        });
        final MaterialButton help = findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
                Message message = new Message.Builder()
                        .setUser(me)
                        .setUsernameVisibility(false)
                        .setRight(true)
                        .setText(help.getText().toString())
                        .hideIcon(true)
                        .build();
                //Set to chat view
                //mChatView.send(message);
                //Reset edit text
                Objects.requireNonNull(tl_message.getEditText()).setText(help.getText().toString());
            }
        });

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

    private void sendRequest() {
        final String queryString = String.valueOf(Objects.requireNonNull(tl_message.getEditText()).getText().toString());
        @SuppressLint("StaticFieldLeak")
        final AsyncTask<String, Void, AIResponse> task = new AsyncTask<String, Void, AIResponse>() {
            private AIError aiError;
            @Override
            protected AIResponse doInBackground(final String... params) {

                final AIRequest request = new AIRequest();

                String query = params[0];

                if (!TextUtils.isEmpty(query))
                    request.setQuery(query);
                try {
                    return aiDataService.request(request);
                } catch (final AIServiceException e) {
                    aiError = new AIError(e);
                    return null;
                }
            }
            @Override
            protected void onPostExecute(final AIResponse response) {
                if (response != null) {
                    onResult(response);
                } else {
                    onError(aiError);
                }
            }
        };
        task.execute(queryString);
    }

    @Override
    public void onResult(final AIResponse response) {
        Result result = response.getResult();

        final String speech = result.getFulfillment().getSpeech();
        // Show results in TextView.
        //results.setText(speech);
        MaterialButton suggestion1 = findViewById(R.id.what_can_you_do);
        MaterialButton suggestion2 = findViewById(R.id.solutions);
        MaterialButton suggestion3 = findViewById(R.id.nearest_opthal);
        MaterialButton suggestion4 = findViewById(R.id.about_dr);
        MaterialButton suggestion5 = findViewById(R.id.help);

        int yourId = 1;
        Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_foreground);
        String yourName = "Assistant";

        int myId = 0;
        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        String myName = "You";

        final User me = new User(myId, myName, myIcon);

        final User you = new User(yourId, yourName, yourIcon);
        //Receive message
        final Message receivedMessage = new Message.Builder()
                .setUser(you)
                .setRight(false)
                .setText(speech)
                .hideIcon(false)
                .setUsernameVisibility(false)
                .build();
        mChatView.receive(receivedMessage);

        TextView no_parameters = findViewById(R.id.no_parameters);

        String entries_1 = result.getStringParameter("s_1");
        String entries_2 = result.getStringParameter("s_2");
        String entries_3 = result.getStringParameter("s_3");
        String entries_4 = result.getStringParameter("s_4");
        String entries_5 = result.getStringParameter("s_5");

        suggestion1.setText(entries_1);
        suggestion1.startAnimation(fadeIn);

        suggestion2.setText(entries_2);
        suggestion2.startAnimation(fadeIn);

        suggestion3.setText(entries_3);
        suggestion3.startAnimation(fadeIn);

        suggestion4.setText(entries_4);
        suggestion4.startAnimation(fadeIn);

        suggestion5.setText(entries_5);
        suggestion5.startAnimation(fadeIn);

        // Get parameters



        if (entries_1.equals("")){
            suggestion1.setVisibility(View.GONE);
            suggestion1.startAnimation(fadeOut);
        }
        if (!entries_1.equals("")){
            suggestion1.setVisibility(View.VISIBLE);
            suggestion1.startAnimation(fadeIn);
            if (suggestion1.getVisibility() == View.VISIBLE){
                no_parameters.setVisibility(View.INVISIBLE);
                no_parameters.setAnimation(fadeOut);
            }
        }
        if (entries_2.equals("")){
            suggestion2.setVisibility(View.GONE);
            suggestion2.startAnimation(fadeOut);
        }
        if (!entries_2.equals("")){
            suggestion2.setVisibility(View.VISIBLE);
            suggestion2.startAnimation(fadeIn);
            if (suggestion2.getVisibility() == View.VISIBLE){
                no_parameters.setVisibility(View.INVISIBLE);
                no_parameters.setAnimation(fadeOut);
            }
        }
        if (entries_3.equals("")){
            suggestion3.setVisibility(View.GONE);
            suggestion3.startAnimation(fadeOut);
        }
        if (!entries_3.equals("")){
            suggestion3.setVisibility(View.VISIBLE);
            suggestion3.startAnimation(fadeIn);
            if (suggestion3.getVisibility() == View.VISIBLE){
                no_parameters.setVisibility(View.INVISIBLE);
                no_parameters.setAnimation(fadeOut);
            }
        }
        if (entries_4.equals("")){
            suggestion4.setVisibility(View.GONE);
            suggestion4.startAnimation(fadeOut);
        }
        if (!entries_4.equals("")){
            suggestion4.setVisibility(View.VISIBLE);
            suggestion4.startAnimation(fadeIn);
            if (suggestion4.getVisibility() == View.VISIBLE){
                no_parameters.setVisibility(View.INVISIBLE);
                no_parameters.setAnimation(fadeOut);
            }
        }
        if (entries_5.equals("")){
            suggestion5.setVisibility(View.GONE);
            suggestion5.startAnimation(fadeOut);
        }
        if (!entries_5.equals("")){
            suggestion5.setVisibility(View.VISIBLE);
            suggestion5.startAnimation(fadeIn);
            if (suggestion5.getVisibility() == View.VISIBLE){
                no_parameters.setVisibility(View.INVISIBLE);
                no_parameters.setAnimation(fadeOut);
            }
        }
        if (entries_1.equals("") && entries_2.equals("") && entries_3.equals("") && entries_4.equals("") && entries_5.equals("")){
            suggestion1.setVisibility(View.GONE);
            suggestion1.startAnimation(fadeOut);
            suggestion2.setVisibility(View.GONE);
            suggestion2.startAnimation(fadeOut);
            suggestion3.setVisibility(View.GONE);
            suggestion3.startAnimation(fadeOut);
            suggestion4.setVisibility(View.GONE);
            suggestion4.startAnimation(fadeOut);
            suggestion5.setVisibility(View.GONE);
            suggestion5.startAnimation(fadeOut);

            if (suggestion1.getVisibility() == View.GONE){
                no_parameters.setVisibility(View.VISIBLE);
                no_parameters.setAnimation(fadeIn);
            }

        }
        if (!entries_1.equals("") && !entries_2.equals("") && !entries_3.equals("") && !entries_4.equals("") && !entries_5.equals("")){
            suggestion1.setVisibility(View.VISIBLE);
            suggestion1.startAnimation(fadeIn);
            suggestion2.setVisibility(View.VISIBLE);
            suggestion2.startAnimation(fadeIn);
            suggestion3.setVisibility(View.VISIBLE);
            suggestion3.startAnimation(fadeIn);
            suggestion4.setVisibility(View.VISIBLE);
            suggestion4.startAnimation(fadeIn);
            suggestion5.setVisibility(View.VISIBLE);
            suggestion5.startAnimation(fadeIn);
            if (suggestion1.getVisibility() == View.VISIBLE){
                no_parameters.setVisibility(View.INVISIBLE);
                no_parameters.setAnimation(fadeOut);
            }
        }
    }

    @Override
    public void onError(AIError error) {
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
    public void onBackPressed(){
        super.onBackPressed();
        HelperUtil.backNavigationFinish(this, aClass);
    }
}
