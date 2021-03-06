package com.fitzgerald.cs682.assignment1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

public class TrickActivity extends AppCompatActivity implements  TrickActivityFragment.TrickListener{

    private static final String TAG = "appLog TrickActivity";
    private String dogName;
    private int tricksCompleted;
    private int tricksTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trick);

        //get the list of tricks that the user selected from the previous page
        Intent intent = getIntent();
        ArrayList<String> tricks = intent.getStringArrayListExtra("toDoTricks");
        this.dogName = intent.getExtras().getString("dogName");

        Log.i(TAG, "The dog knows the tricks: " + android.text.TextUtils.join(", ", tricks));
        if (tricks != null && tricks.size() > 0) {

            Log.i(TAG, "The training list of tricks: " + TextUtils.join(", ", tricks));
            //setup the fragment
            TrickActivityFragment fragment = new TrickActivityFragment();

            //past the trick list to the fragment in a bundle
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("trickList", tricks);
            fragment.setArguments(bundle);
            Log.i(TAG, " bundle values: " + fragment.getArguments().toString());

            getFragmentManager().beginTransaction().add(R.id.activityTrick, fragment).commit();
        }
    }

    /**
     * Insert the trick results to the sqlite db
     * @param trickCompleteList
     */
    public void tricksCompleted(ArrayList<ArrayList <String>> trickCompleteList){
        long routineInsertId = -1;
        if(!trickCompleteList.isEmpty()){
            TrickDBHelper dbHelper = new TrickDBHelper(getApplicationContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            //set the routine values
            ContentValues routineValues = new ContentValues();
            routineValues.put(dbHelper.ROUTINE_FIELD2, this.dogName);

            routineInsertId = db.insert(dbHelper.ROUTINE_TABLE_NAME, null, routineValues);
            Log.i(TAG, " Routine insertion id : " + routineInsertId);

            //set the trick results
            for(ArrayList<String> trick : trickCompleteList){
                ContentValues trickValues = new ContentValues();
                trickValues.put(dbHelper.TRICK_FIELD1, routineInsertId);
                trickValues.put(dbHelper.TRICK_FIELD2, trick.get(0));
                trickValues.put(dbHelper.TRICK_FIELD3, trick.get(1));

                db.insert(dbHelper.TRICK_TABLE_NAME, null, trickValues);
                Log.i(TAG, " trick was inserted");
            }
            db.close();
        }

        //create intent to instantiate new activity
        final Intent trickResultIntent = new Intent(this, TrickResultsActivity.class);
        //pass the list of tricks to do to the next activity
        trickResultIntent.putExtra("routineId", routineInsertId);
        trickResultIntent.putExtra("dogName", this.dogName);
        Log.i(TAG, " go to trickResultActivity");
        startActivity(trickResultIntent);
    }

    public void getTrickData(int tricksCompleted, int tricksTotal){
        this.tricksCompleted = tricksCompleted;
        this.tricksTotal = tricksTotal;
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,SelectDogActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");

        if(this.tricksCompleted < this.tricksTotal){
            // theNotification has title, text, and icon as implemented
            Notification theNotification = new Notification.Builder(this)
                    .setContentTitle("Trick Routine Generator")
                    .setContentText("Hey, come back! " + this.dogName + " has " +
                            (this.tricksTotal - this.tricksCompleted) + " tricks left.")
                    .setSmallIcon(R.drawable.launcher_image)
                    .setContentIntent(PendingIntent.getActivity(this,
                            0, new Intent(this, TrickActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                            Intent.FLAG_ACTIVITY_SINGLE_TOP),
                            PendingIntent.FLAG_CANCEL_CURRENT))
                    .setAutoCancel(true)
                    .build();

            theNotification.flags |= Notification.FLAG_ONGOING_EVENT;

            // theNotification is on the Notification bar and has ID MY_NOTIFICATION_ID
            final NotificationManager notificationManager = (NotificationManager)
                    this.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, theNotification);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState");
    }
}
