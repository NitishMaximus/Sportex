package com.playbox.sportex;

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

public class UserHostedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_hosted);

        final ArrayList<Games> gameList = new ArrayList<>();
        final ArrayList<String> keyList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        final DatabaseReference table_games = database.getReference("Games");

        recyclerView = (RecyclerView)findViewById(R.id.user_hosted_games_recycler_view);
        layoutManager = new LinearLayoutManager(this);

        final String user_name = PreferenceUtils.getName(getApplicationContext());
        Log.d("name", user_name);
        recyclerView.setLayoutManager(layoutManager);

        table_games.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Log.d("for", "1");
                    Games game = snapshot.getValue(Games.class);
                    if(user_name.equals(game.getOrganizer())){
                        Log.d("if", "lol");
                        gameList.add(game);
                        keyList.add(snapshot.getKey());
                    }
                }
                mAdapter = new MyAdapterRemoveGame(gameList, keyList, getApplicationContext());
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
