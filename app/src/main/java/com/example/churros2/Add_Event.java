package com.example.churros2;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Add_Event extends AppCompatActivity {

    //Creating Variables
    TextView DisplayDate, DisplayTime, Description1 ;
    EditText Location, Eventtitle;
    Button Save;
    DatePickerDialog.OnDateSetListener DateSetListener;
    TimePickerDialog.OnTimeSetListener TimeSetListener;
    //Variable created for the Authentication
    FirebaseAuth firebaseAuth;
    //Variable created for the Database
    FirebaseDatabase firebaseDatabase;
    String date, time, description, location, eventtitle;
    DatabaseReference databaseReference;
    //Variables needed for notification
    private static final String TAG = "Add_Event";
    private static final String CHANNEL_ID = "Zaris1";
    private static final String CHANNEL_NAME = "Zaris";
    private static final String CHANNEL_DESC = "Zaris Notification";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__event);

        //Used to add a toolbar
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Creating instances for the variables
        DisplayDate = findViewById(R.id.DateInput);
        DisplayTime = findViewById(R.id.TimeInput);
        Location = findViewById(R.id.LocationPicker);
        Description1 = findViewById(R.id.Description);
        Save = findViewById(R.id.SaveEvent);
        //Gets instance from the FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        //Gets instance from the FirebaseDatabase
        firebaseDatabase = FirebaseDatabase.getInstance();
        //Gets Reference from the FirebaseDatabase
        databaseReference = firebaseDatabase.getReference();
        Eventtitle = findViewById(R.id.EventTitle);


        //Used to create a notification
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
        //Button which saves data onto the database
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date = DisplayDate.getText().toString();
                time = DisplayTime.getText().toString();
                location = Location.getText().toString();
                description = Description1.getText().toString();
                eventtitle = Eventtitle.getText().toString();
                if(date.isEmpty() || time.isEmpty() || location.isEmpty() || description.isEmpty() || eventtitle.isEmpty() ){
                    Toast.makeText(Add_Event.this, "Please Enter All The Details", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(Add_Event.this, Event.class));
                }
                addNotification();
                addevent();
            }
        });

        //Displays the Date picker when pressing the textview
        DisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                //Creating variables for the dates
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Add_Event.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, DateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        //Displays the Time picker when pressing the textview
        DisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar Time = Calendar.getInstance();
                int hour = Time.get(Calendar.HOUR_OF_DAY);
                int minute = Time.get(Calendar.MINUTE);
                TimePickerDialog dialog = new TimePickerDialog(Add_Event.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, TimeSetListener, hour, minute,android.text.format.DateFormat.is24HourFormat(Add_Event.this));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
            });

        //When the date textview is pressed this will show the layout the date would be in
        DateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyyy" + dayOfMonth + "/" + month + "/" + year );

                String date = dayOfMonth + "/" + month + "/" + year;
                DisplayDate.setText(date);
            }
        };

        TimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.d(TAG, "onTimeSet: hh:mm" + hourOfDay + ":" + minute );

                String time = hourOfDay + ":" + minute ;
                DisplayTime.setText(time);
            }
        };

    }

    private void addevent() {
        date = DisplayDate.getText().toString();
        time = DisplayTime.getText().toString();
        location = Location.getText().toString();
        description = Description1.getText().toString();
        eventtitle = Eventtitle.getText().toString();

        if (date.isEmpty() || time.isEmpty() || location.isEmpty() || description.isEmpty() || eventtitle.isEmpty()) {
            Toast.makeText(this, "Please Enter All The Details", Toast.LENGTH_LONG).show();
        } else {
            //Gets the current users ID from the database
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                String userID = user.getUid();
                //Targets the firebase database and creates children classess that store values
                databaseReference.child(userID).child("Events").child(eventtitle).child("Event").setValue("Event: " + eventtitle);
                databaseReference.child(userID).child("Events").child(eventtitle).child("Date").setValue("Date: " + date);
                databaseReference.child(userID).child("Events").child(eventtitle).child("Time").setValue("Time: " + time);
                databaseReference.child(userID).child("Events").child(eventtitle).child("Location").setValue("Location: " + location);
                databaseReference.child(userID).child("Events").child(eventtitle).child("Description").setValue("Description: " + description);
                Toast.makeText(this, "Event Created", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void addNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Churros")
            .setContentText("Event Created")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, builder.build());


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.logoutmenu:{
                Logout();
            }
        }
        switch(item.getItemId()) {
            case R.id.UserGuidemenu: {
                Userguide();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void Userguide(){
        startActivity(new Intent(Add_Event.this, UserGuide.class));
    }
    public void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(Add_Event.this, MainActivity.class));
    }
}
