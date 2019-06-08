package com.playbox.sportex;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.playbox.sportex.Model.Games;
import com.playbox.sportex.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ShowGamesActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_games);

        final ProgressBar progbar = findViewById(R.id.progbar);

        final ArrayList<Games> gameList = new ArrayList<>();
        final ArrayList<String> keyList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_games = database.getReference("Games");
        final DatabaseReference table_user = database.getReference("User");

        Intent i = getIntent();
        final String sport = i.getStringExtra("sport");

        recyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        progbar.setVisibility(View.VISIBLE);

        table_games.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Games game = snapshot.getValue(Games.class);
                    final String key = snapshot.getKey();
                    Calendar cal = Calendar.getInstance();
                    long current_time = cal.getTimeInMillis();
                    if((Long.parseLong(game.getGameTimeInMillis().trim())-current_time)<0){
                        table_games.child(key).getRef().removeValue();
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

                    }else{
                        double distance = getDistance(Double.parseDouble(game.getLatitude()),
                                Double.parseDouble(game.getLongitude()),
                                Double.parseDouble(PreferenceUtils.getsharedLatitude(ShowGamesActivity.this)),
                                Double.parseDouble(PreferenceUtils.getsharedLongitude(ShowGamesActivity.this)));
                        if(!PreferenceUtils.getName(getApplicationContext()).equals(game.getOrganizer()) && sport.equals(game.getSport()) && distance<10.0 && (Long.parseLong(game.getGameTimeInMillis().trim())-current_time)>0){
                            keyList.add(key);
                            gameList.add(game);
                        }
                    }
                }
                mAdapter = new MyAdapter(gameList, keyList, getApplicationContext());
                recyclerView.setAdapter(mAdapter);
                progbar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public double getDistance(double lat1, double lon1, double lat2, double lon2){
        double R = 6371.0;
        double dLat = deg2rad(lat2-lat1);
        double dLon = deg2rad(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c; // Distance in km
        return d;
    }

    public double deg2rad(double deg) {
        return deg*(Math.PI/180);
    }
}
