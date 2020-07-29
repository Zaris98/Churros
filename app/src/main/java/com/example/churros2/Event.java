package com.example.churros2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuLayout;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Event extends AppCompatActivity {
    public static final String TAG = "Event";

    SwipeMenuListView lv;
    TextView Event, Date, Time, Location, Description;
    FirebaseAuth firebaseAuth;
    ImageView  Profile;
    FloatingActionButton ADDEVENTS;
    FirebaseListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        firebaseAuth = FirebaseAuth.getInstance();
        Event = findViewById(R.id.event);
        Date = findViewById(R.id.date);
        Time = findViewById(R.id.time);
        Location = findViewById(R.id.location);
        Description = findViewById(R.id.description);
        Profile = findViewById(R.id.ivProfile);
        ADDEVENTS = findViewById(R.id.addEvents);
        lv = findViewById(R.id.listview);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        String userid = user.getUid();
        Query query = FirebaseDatabase.getInstance().getReference().child(userid).child("Events");



        ADDEVENTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Event.this, Add_Event.class));
            }
        });

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Event.this, Profile.class));
            }
        });


        FirebaseListOptions<EVENTLIST> options = new FirebaseListOptions.Builder<EVENTLIST>()
            .setLayout(R.layout.event)
            .setLifecycleOwner(Event.this)
            .setQuery(query, EVENTLIST.class)
            .build();
        adapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Object model, int position) {
                 Event = v.findViewById(R.id.event);
                 Date = v.findViewById(R.id.date);
                 Time = v.findViewById(R.id.time);
                 Location = v.findViewById(R.id.location);
                 Description = v.findViewById(R.id.description);

                EVENTLIST std = (EVENTLIST) model;
                Event.setText(std.getEvent());
                Date.setText(std.getDate());
                Time.setText(std.getTime());
                Location.setText(std.getLocation());
                Description.setText(std.getDescription());
            }
        };
        lv.setAdapter(adapter);



        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                    getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0x00, 0x66,
                    0xff)));
                // set item width
                openItem.setWidth(170);
                // set item title
                openItem.setTitle("Share");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

            }
        };
        lv.setMenuCreator(creator);


        lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Log.d(TAG, "onMenuItemClick: clicked item " + index);
                        Intent myintent = new Intent(Intent.ACTION_SEND);
                        myintent.setType("text/plain");
                        String sharebody = (Event.getText().toString() + "\n" + Date.getText().toString() + "\n" + Time.getText().toString() + "\n" + Location.getText().toString() + "\n" + Description.getText().toString());
                        String sharesub = "Your Subject here";
                        myintent.putExtra(Intent.EXTRA_SUBJECT,sharebody);
                        myintent.putExtra(Intent.EXTRA_TEXT,sharebody);
                        startActivity(Intent.createChooser(myintent, "Share Event"));
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.logoutmenu: {
                Logout();
            }
        }

        switch(item.getItemId()) {
            case R.id.Refreshmenu: {
                Refresh();
            }
        }
        switch(item.getItemId()) {
            case R.id.UserGuidemenu: {
                Userguide();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void Refresh(){
        startActivity(new Intent(Event.this, Event.class));
    }
    public void Userguide(){
        startActivity(new Intent(Event.this, UserGuide.class));
    }
    public void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(Event.this, MainActivity.class));
    }

}

