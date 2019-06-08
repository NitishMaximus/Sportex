package com.playbox.sportex;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.playbox.sportex.utils.PreferenceUtils;


public class Dash_frag_3 extends Fragment {

    CardView hosted_games, joined_games, lgout_btn;
    TextView profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dash_frag_3, container, false);

        hosted_games = view.findViewById(R.id.HostedGames);
        joined_games = view.findViewById(R.id.JoinedGames);
        profile = view.findViewById(R.id.profile);
        lgout_btn = view.findViewById(R.id.log_out);

        profile.setText("Profile-"+PreferenceUtils.getName(getContext()));

        hosted_games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), UserHostedActivity.class);
                startActivity(i);
            }
        });

        joined_games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), UserJoinedGames.class);
                startActivity(i);
            }
        });

        lgout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                PreferenceUtils.saveName(null, getContext());
                PreferenceUtils.savePhone(null,getContext());
                PreferenceUtils.saveLatitude(null, getContext());
                PreferenceUtils.saveLongitude(null, getContext());
                startActivity(i);
            }
        });

        return view;
    }

}
