package com.fitzgerald.cs682.assignment1;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TrickResultsActivity extends AppCompatActivity {
    private static final String TAG = "appLog TrickResultActiv";
    private int successfulTricks = 0;
    private int totalTricks = 0;
    private String dogName = "";
    private TelephonyManager telMgr;

    private static final int PICK_CONTACT_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trick_results);

        //check if the user has necessary permissions when launching the page
        checkTelephonyPermissions();
        telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        //get the list of tricks that the user selected from the previous page
        Intent intent = getIntent();
        long routineId = intent.getExtras().getLong("routineId");
        Log.i(TAG, "dogId = " + routineId);

        if(routineId >= 0) {
            TrickDBHelper dbHelper = new TrickDBHelper(getApplicationContext());

            //query DB and parse trick results
            getTrickResults(routineId, dbHelper);

            TextView heading = (TextView) findViewById(R.id.trickResultsHeading);
            if(heading != null){
                String text = this.dogName + " completed " + this.successfulTricks + " out of " + this.totalTricks + " total tricks!";
                heading.setText(text);
            }
        }

        //allows the user to share the results by picking a contact and sending an SMS
        Button shareResults = (Button) findViewById(R.id.shareResultsButton);
        if(shareResults != null) {
            shareResults.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.i(TAG, "shareResults onClick");
                    selectContactFromList();
                }
            });
        }

        //starts the process over by taking the user to the dog selection page
        Button startOver = (Button) findViewById(R.id.startOverButton);
        if(startOver != null) {
            startOver.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.i(TAG, "startOver onClick");
                    selectDogIntent();
                }
            });
        }
    }

    /**
     * The user will need READ_PHONE_STATE and SEND_SMS
     * permissions to complete some activities on this page
     * If the access isn't already granted, it will be requested
     * If access is not granted, the function will be hidden from the UI
     */
    private void checkTelephonyPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            //request the permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.SEND_SMS
                    }, PERMISSION_REQUEST_CODE);
        }
    }
    /**
     * Checks the result of the permission request
     * This drives the display of the "Share Results" button on the page
     * If permission is not granted, the button will be hidden, and vice versa
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //the user has access to telephone status. show share button.
                    Button shareResults = (Button) findViewById(R.id.shareResultsButton);
                    if (shareResults != null) {
                        shareResults.setVisibility(View.VISIBLE);
                    }
                } else {
                    //telephone permissions not granted. hide the share button
                    Button shareResults = (Button) findViewById(R.id.shareResultsButton);
                    if (shareResults != null) {
                        shareResults.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            }
        }
    }

    /**
     * Select a contact to send results to from the contacts list
     */
    private void selectContactFromList(){
        Intent selectContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        selectContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(selectContactIntent, PICK_CONTACT_REQUEST);
    }

    /**
     * check that the phone has network signal,
     * then call the sendSMS method
     */
    private void sendSmsMessage(String phoneNumber) {
        //check for network signal and call state
        if (isTelephoneNetworkAvailable(telMgr) && "IDLE".equals(getCallState(telMgr))) {
            sendSMS(phoneNumber, "I just trained my dog using the Training " +
                    "Routine Generator app! " + this.dogName + " completed " + this.successfulTricks
                    + " out of " + this.totalTricks + " total tricks!");
        } else {
            Toast.makeText(getBaseContext(), "No network signal or call in progress. SMS not sent", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Utilize the SMSManager to send an SMS message directly from this activity
     * @param phoneNumber - number to send SMS message to
     * @param message - string text to send as the SMS message body
     */
    void sendSMS(String phoneNumber, String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        //setup intents
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //when the SMS has been sent
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                //toast the results of the message
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //when the SMS has been delivered
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        //send the SMS message
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    /**
     * Return string representation of the current call status
     * @param telMgr - telephonyManager object
     * @return - string representatino of call status
     */
    private String getCallState(final TelephonyManager telMgr){
        int callState = telMgr.getCallState();
        String callStateString = "NA";
        switch (callState) {
            case TelephonyManager.CALL_STATE_IDLE:
                callStateString = "IDLE";
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                callStateString = "OFFHOOK";
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                callStateString = "RINGING";
                break;
        }
        return callStateString;
    }

    /**
     * Return true if a network is available
     * @param telMgr - telephonyManager object
     * @return - network availability
     */
    private boolean isTelephoneNetworkAvailable(final TelephonyManager telMgr) {
        return ((!(telMgr.getNetworkOperator() != null && telMgr.getNetworkOperator().equals(""))));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.i(TAG, "Activity result code = " + requestCode);
        if(requestCode == PICK_CONTACT_REQUEST){
            if(resultCode == RESULT_OK){
                Uri contactUri = data.getData();
                //we are only interested in the phone number
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor c = getContentResolver().query(contactUri, projection, null, null, null);

                if(c != null){
                    c.moveToFirst();
                    String number = c.getString(c.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Log.i(TAG, "Phone Number = " + number);

                    //Send text message to the phone number selected
                    sendSmsMessage(number);
                    c.close();
                }
            }
        }
    }

    /**
     * create and starts the selectdog activity
     */
    private void selectDogIntent(){
        Intent selectDogIntent = new Intent(this, SelectDogActivity.class);
        startActivity(selectDogIntent);
    }


    /**
     * Run query to return tricks results for the dog's routine
     * @param routineId
     * @param dbHelper
     * @return
     */
    private Cursor getTrickResults(long routineId, TrickDBHelper dbHelper){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT trickRoutine.id, trickRoutine.dogName, trick.trickName, trick.trickResult FROM " +
                dbHelper.ROUTINE_TABLE_NAME+" INNER JOIN "+dbHelper.TRICK_TABLE_NAME+
                " ON trickRoutine.id=trick.dogId WHERE trick.dogId=?";
        Cursor c = db.rawQuery(query, new String[]{String.valueOf(routineId)});

        parseTrickResults(c, dbHelper);

        db.close();

        return c;
    }

    /**
     * Step through the cursor to parse data
     * creates a text view and adds it to the layout for each row
     * gets the dog name and updates the total and successful number of tricks.
     * @param c
     * @param dbHelper
     */
    private void parseTrickResults(Cursor c, TrickDBHelper dbHelper){
        c.moveToFirst();
        this.dogName = c.getString(c.getColumnIndex(dbHelper.ROUTINE_FIELD2));
        View resultsLayout = findViewById(R.id.resultLayout);

        while (!c.isAfterLast()){
            //get the trick name and status
            String trickName = c.getString(c.getColumnIndex(dbHelper.TRICK_FIELD2));
            String trickResult = c.getString(c.getColumnIndex(dbHelper.TRICK_FIELD3));
            Log.i(TAG, "Trick data = " + trickName + " : " + trickResult);

            //create a text view and set custom style and text
            TextView result = new TextView(
                    new ContextThemeWrapper(this, R.style.TrickResultTextView), null, 0);
            result.setBackground(getResources().getDrawable(R.drawable.trick_skipped_background));
            String text = "The trick " + trickName + " was " + trickResult + "!";
            result.setText(text);

            //set background based on trick result
            if(trickResult.equals("Skipped")){
                result.setTextColor(getResources().getColor(R.color.pink2));
            }else{
                result.setTextColor(getResources().getColor(R.color.green1));
                this.successfulTricks ++;
            }

            //add the trick results to the layout with the separator
            ((LinearLayout)resultsLayout).addView(result);
            this.totalTricks ++;

            c.moveToNext();
        }
    }

    @Override
    public void onBackPressed()
    {
        selectDogIntent();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }

}
