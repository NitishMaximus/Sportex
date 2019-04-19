package com.playbox.sportex;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class Dash_frag_1 extends Fragment {

    Button loc;
    TextView coor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dash_frag_1, container, false);

        loc = (Button)view.findViewById(R.id.btn_loc);
        coor = (TextView)view.findViewById(R.id.coor);



        return view;
    }
}
