package com.playbox.sportex;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.playbox.sportex.Model.Games;
import com.playbox.sportex.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;


public class Dash_frag_1 extends Fragment {
    Button foot,cric,volley,basket,hockey,swim,lawn,tt,bad;
    android.os.Handler customHandler = new android.os.Handler();
    TextView t;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dash_frag_1, container, false);
        foot = view.findViewById(R.id.foot);
        volley = view.findViewById(R.id.volley);
        cric = view.findViewById(R.id.cricket);
        hockey = view.findViewById(R.id.hockey);
        swim = view.findViewById(R.id.swim);
        lawn = view.findViewById(R.id.lawn);
        tt = view.findViewById(R.id.tt);
        bad = view.findViewById(R.id.bad);
        basket = view.findViewById(R.id.basket);

        customHandler.postDelayed(updateTimerThread, 0);

        t = view.findViewById(R.id.tvf);

        final Intent i = new Intent(getActivity(), ShowGamesActivity.class);

        foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("sport", "Football");
                startActivity(i);
            }
        });

        cric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("sport", "Cricket");
                startActivity(i);
            }
        });

        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("sport", "Basketball");
                startActivity(i);
            }
        });

        hockey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("sport", "Hockey");
                startActivity(i);
            }
        });

        bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("sport", "Badminton");
                startActivity(i);
            }
        });

        lawn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("sport", "Lawn Tennis");
                startActivity(i);
            }
        });

        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("sport", "Table Tennis");
                startActivity(i);
            }
        });

        volley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("sport", "Volleyball");
                startActivity(i);
            }
        });

        swim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("sport", "Swimming");
                startActivity(i);
            }
        });
        return view;
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {

            if(PreferenceUtils.getsharedLongitude(getContext()) != null){
                t.setText(PreferenceUtils.getsharedLatitude(getContext()) +"   " + PreferenceUtils.getsharedLongitude(getContext()));
                return;
            }
            customHandler.postDelayed(this, 100);
        }
    };



}
