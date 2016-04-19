package com.fitzgerald.cs682.assignment1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class TrainingTips extends AppCompatActivity {
    private static final String TAG = "appLog TrainingTips";
    private long routineId;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_tips);

        //store this value for later
        Intent intent = getIntent();
        this.routineId = intent.getExtras().getLong("routineId");

        //get the network info
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        final String TRAINING_TIPS_URL = "https://www.cesarsway.com/dog-training/"+
                "obedience/5-essential-commands-you-can-teach-your-dog";
        //check if there is a network conntection
        if (networkInfo != null && networkInfo.isConnected()) {
            //display the website in the webview
            WebView webView = (WebView) findViewById(R.id.webView);
            if(webView != null){
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(TRAINING_TIPS_URL);
            }
        } else {
            Toast.makeText(getBaseContext(), "No network connection available.",
                    Toast.LENGTH_SHORT).show();
        }

        //starts the process over by taking the user to the dog selection page
        Button returnButton = (Button) findViewById(R.id.returnButton);
        if(returnButton!=null)

        {
            final Intent trickResults = new Intent(this, TrickResultsActivity.class);
            returnButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.i(TAG, "trainingTips onClick");
                    trickResults.putExtra("routineId", routineId);
                    startActivity(trickResults);
                }
            });
        }
    }
}
