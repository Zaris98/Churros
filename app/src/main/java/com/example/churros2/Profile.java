package com.example.churros2;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.Change;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {

    ImageView ProfilePic, Event;
    TextView ProfileName, ProfileEmail, ProfileAge, ProfileGender;
    FirebaseAuth firebaseauth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Profile");
        ProfilePic = findViewById(R.id.idPP);
        ProfileName = findViewById(R.id.tvProfileName);
        ProfileEmail = findViewById(R.id.tvProfileEmail);
        ProfileAge = findViewById(R.id.tvAgeProfile);
        ProfileGender = findViewById(R.id.tvGenderProfile);
        Event = findViewById(R.id.ivEvents);
        firebaseauth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(firebaseauth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(ProfilePic);
            }
        });
        //Gets the database reference to get data.
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseauth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                //Gets the data from the database and uses it to set the text
                ProfileName.setText("Name: " + userProfile.getUserName());
                ProfileEmail.setText("E-Mail: " + userProfile.getUserEmail());
                ProfileAge.setText("Age: " + userProfile.getUserAge());
                ProfileGender.setText("Gender: " + userProfile.getUserGender());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Profile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });

        //This is used to send the user to the evenets page
        Event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, Event.class));
            }
        });




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    //This is the options that the user can select on the top right of the page
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.logoutmenu:{
                Logout();
            }

        }
        switch(item.getItemId()) {
            case R.id.Refreshmenu: {
                Refresh();
            }
        }

        switch(item.getItemId()) {
            case R.id.AddEventmenu: {
                addevents();
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
        startActivity(new Intent(Profile.this, UserGuide.class));
    }
    private void Refresh(){
        startActivity(new Intent(Profile.this, Profile.class));
    }
    private void addevents(){
        startActivity(new Intent(Profile.this, Add_Event.class));
    }

    private void Logout(){
        firebaseauth.signOut();
        finish();
        startActivity(new Intent(Profile.this, MainActivity.class));
    }

}
