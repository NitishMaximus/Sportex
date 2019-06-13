package com.playbox.sportex;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.playbox.sportex.Model.Games;
import com.playbox.sportex.Model.User;
import com.playbox.sportex.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class UserJoinedGames extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_joined_games);

        final ArrayList<Games> gameList = new ArrayList<>();
        final ArrayList<String> keyList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        final DatabaseReference table_games = database.getReference("Games");

        recyclerView = (RecyclerView)findViewById(R.id.user_joined_games_recycler_view);
        layoutManager = new LinearLayoutManager(this);

        final String user_phone = PreferenceUtils.getPhone(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);

        table_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.child(user_phone).getValue(User.class);
                try{
                    final HashMap<String, Boolean> joined = user.getJoinedGame();

                    if(joined.size()!=0) {
                        table_games.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (HashMap.Entry<String, Boolean> entry : joined.entrySet()) {
                                    Games game = dataSnapshot.child(entry.getKey()).getValue(Games.class);
                                    if(!PreferenceUtils.getName(getApplicationContext()).equals(game.getOrganizer())) {
                                        keyList.add(entry.getKey());
                                        gameList.add(game);
                                    }
                                }
                                mAdapter = new MyAdapterUnjoinGame(gameList, keyList, getApplicationContext());
                                recyclerView.setAdapter(mAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }catch(NullPointerException e){
                    Toast.makeText(getApplicationContext(),"No Games Joined", Toast.LENGTH_SHORT).show();
                    Log.d("afsad", "ih");
                    startActivity(new Intent(UserJoinedGames.this, MainActivity.class));
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
