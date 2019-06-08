package com.playbox.sportex;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.playbox.sportex.Model.Games;
import com.playbox.sportex.Model.User;
import com.playbox.sportex.utils.PreferenceUtils;

import java.util.HashMap;
import java.util.Map;

public class JoinGameActivity extends AppCompatActivity {

    Button join;
    TextView sport, location, date, time, distance, goingNo;
    int playerCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_games = database.getReference("Games");
        final DatabaseReference table_user = database.getReference("User");

        join = findViewById(R.id.join);
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
        final String lat = i.getStringExtra("Latitude");
        final String lon = i.getStringExtra("Longitude");
        final String dateStr = i.getStringExtra("Date");
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

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table_games.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(key).child("Going").child(PreferenceUtils.getName(getApplicationContext())).exists()){
                            Toast.makeText(getApplicationContext(), "Game already joined", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Games joiningGame = dataSnapshot.child(key).getValue(Games.class);
                            HashMap<String, Boolean> going = joiningGame.getGoing();
                            going.put(PreferenceUtils.getName(getApplicationContext()), true);
                            table_games.child(key).child("Going").setValue(going);
                            Toast.makeText(getApplicationContext(), "Game joined", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.child(PreferenceUtils.getPhone(getApplicationContext())).getValue(User.class);
                        HashMap<String, Boolean> joinedgames = new HashMap<>();
                        try{
                            joinedgames = user.getJoinedGame();
                            joinedgames.put(key, true);
                        }catch (NullPointerException e){
                            joinedgames = new HashMap<String, Boolean>(){{put(key, true);}};
                        }
                        table_user.child(PreferenceUtils.getPhone(getApplicationContext())).child("JoinedGame").setValue(joinedgames);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
