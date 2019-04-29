package com.playbox.sportex;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.playbox.sportex.Model.Games;
import com.playbox.sportex.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public class ShowGamesActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_games);

        final ArrayList<Games> gg = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_games = database.getReference("Games");

        Intent i = getIntent();
        final String sport = i.getStringExtra("sport");

        recyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        table_games.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Games game = snapshot.getValue(Games.class);
                    double distance = getDistance(Double.parseDouble(game.getLatitude()),
                            Double.parseDouble(game.getLongitude()),
                            Double.parseDouble(PreferenceUtils.getsharedLatitude(ShowGamesActivity.this)),
                            Double.parseDouble(PreferenceUtils.getsharedLongitude(ShowGamesActivity.this)));
                    if(sport.equals(game.getSport()) && distance<10.0){
                        gg.add(game);
                    }
                    mAdapter = new MyAdapter(gg, getApplicationContext());
                    recyclerView.setAdapter(mAdapter);
                }
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
