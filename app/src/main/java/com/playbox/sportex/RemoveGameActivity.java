package com.playbox.sportex;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.playbox.sportex.Model.User;
import com.playbox.sportex.utils.PreferenceUtils;

import java.util.HashMap;

public class RemoveGameActivity extends AppCompatActivity {
    Button delete;
    TextView sport, location, date, time, distance, goingNo;
    int playerCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_game);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_games = database.getReference("Games");
        final DatabaseReference table_user = database.getReference("User");

        delete = findViewById(R.id.delete);
        sport = findViewById(R.id.sport);
        location = findViewById(R.id.location);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        distance = findViewById(R.id.distance);
        goingNo = findViewById(R.id.going);

        Intent i = getIntent();
        final String key = i.getStringExtra("Key");
        final String sportStr = i.getStringExtra("Sport");
        final String locationStr = i.getStringExtra("Address");
        final String dateStr = i.getStringExtra("Date");
        final String lat = i.getStringExtra("Latitude");
        final String lon = i.getStringExtra("Longitude");
        final String timeStr = i.getStringExtra("Time");
        final String distanceStr = i.getStringExtra("Distance");
        final String organizerStr = i.getStringExtra("Organizer");

        sport.setText(sportStr);
        location.setText(locationStr);
        date.setText(dateStr);
        time.setText(timeStr);
        distance.setText("~" + distanceStr +" kms");

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:"+lat+","+lon+"?q="+locationStr);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        //Count Going Players
        HashMap<String, Boolean> map = (HashMap<String, Boolean>) i.getSerializableExtra("Going");
        for (HashMap.Entry<String, Boolean> entry : map.entrySet()) {
            playerCount += 1;
            //Log.d("gong", entry.getKey() + "/" + entry.getValue());
        }
        goingNo.setText("Going("+Integer.toString(playerCount)+"):");

        GoingFragment gf = new GoingFragment(map, organizerStr);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.goingFragment, gf);
        ft.commit();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table_games.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.child(key).getRef().removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.child(PreferenceUtils.getPhone(getApplicationContext())).child("JoinedGame").child(key).getRef().removeValue();
                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            String phone = dataSnapshot1.getKey();
                            dataSnapshot.child(phone).child("JoinedGame").child(key).getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                startActivity(new Intent(RemoveGameActivity.this, DashboardActivity.class));
            }
        });

    }
}