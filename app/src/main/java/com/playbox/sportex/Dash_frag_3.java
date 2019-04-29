package com.playbox.sportex;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.playbox.sportex.utils.PreferenceUtils;


public class Dash_frag_3 extends Fragment {

    Button lgout_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dash_frag_3, container, false);

        lgout_btn = (Button)view.findViewById(R.id.log_out);

        lgout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                PreferenceUtils.saveName(null, getContext());
                PreferenceUtils.saveLatitude(null, getContext());
                PreferenceUtils.saveLongitude(null, getContext());
                startActivity(i);
            }
        });

        return view;
    }

}
