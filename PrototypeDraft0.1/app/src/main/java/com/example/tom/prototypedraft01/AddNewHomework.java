package com.example.tom.prototypedraft01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.CalendarContract;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import java.util.GregorianCalendar;


public class AddNewHomework extends ActionBarActivity {

    TextView mainTextView;
    Button mainButton;
    EditText inputbox;
    EditText subjectEditText;
    EditText descriptionEditText;

    Button doneButton;
    static String DateInput;
    public String setdateday;
    public String setdatemonth;
    public String setdateyear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_AddNewHomework);
        mainTextView = (TextView) findViewById(R.id.main_textview);
        mainButton = (Button) findViewById(R.id.TimeInpButton);
        mainButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                DialogFragment DatePickerFrag = new CalendarDatePickerFragment();
                DatePickerFrag.show(getSupportFragmentManager(), "datePicker");
            }
        });
        doneButton = (Button) findViewById(R.id.buttonDone);
        doneButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String Subject = subjectEditText.getText().toString();
                String Description = descriptionEditText.getText().toString();
                AddCalendarEvent(Integer.parseInt(setdateday), Integer.parseInt(setdatemonth), Integer.parseInt(setdateyear), Subject, Description);
                Toast calendarSavedToast = Toast.makeText(getApplicationContext(), "Event (should be) saved to calendar!", Toast.LENGTH_SHORT);
                calendarSavedToast.show();
            }
        });
        inputbox = (EditText) findViewById(R.id.inputBox);
        subjectEditText = (EditText) findViewById(R.id.subjectEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        // Register to receive messages. We are registering an observer (mMessageReceiver) to receive Intents with actions named "custom-event-name".
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("set-date"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            setdateday = intent.getStringExtra("input-day");
            setdatemonth = intent.getStringExtra("input-month");
            setdateyear = intent.getStringExtra("input-year");
            String setdate = setdateday +"/"+ setdatemonth +"/"+ setdateyear;
            Log.d("receiver", "Got message: " + setdate);
            inputbox.setText(setdate);

        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    public void AddCalendarEvent(int Day, int Month,int Year, String Subject, String Description){
        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setType("vnd.android.cursor.item/event");
        calIntent.putExtra(CalendarContract.Events.TITLE, Subject + " Homework");
        calIntent.putExtra(CalendarContract.Events.DESCRIPTION, Description);
        calIntent.putExtra(CalendarContract.Events.HAS_ALARM, true);

        GregorianCalendar calDate = new GregorianCalendar(Year, Month, Day);
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                calDate.getTimeInMillis());
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                calDate.getTimeInMillis());

        startActivity(calIntent);
    }
}


