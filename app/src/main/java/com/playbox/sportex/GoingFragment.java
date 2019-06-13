package com.playbox.sportex;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.playbox.sportex.Model.Games;
import com.playbox.sportex.Model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class GoingFragment extends Fragment {

    HashMap<String, Boolean> goingList;
    String organizer;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public GoingFragment(HashMap<String, Boolean> goingList, String organizer){
        this.goingList = goingList;
        this.organizer = organizer;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_going, container, false);

        final ArrayList<String> playerList = new ArrayList<>();
        final ArrayList<String> phoneList = new ArrayList<>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        recyclerView = (RecyclerView)v.findViewById(R.id.myGoing_recycler_view);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        for (final HashMap.Entry<String, Boolean> entry : goingList.entrySet()) {
            if(entry.getKey().equals(organizer)){
                playerList.add(entry.getKey()+"-Host");
            }else {
                playerList.add(entry.getKey());
            }
            table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);
                        if(entry.getKey().equals(user.getName())){
                            phoneList.add(snapshot.getKey());
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        mAdapter = new MyAdapterGoing(playerList, phoneList, getContext());
        recyclerView.setAdapter(mAdapter);

        return v;
    }

}
