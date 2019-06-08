package com.playbox.sportex;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.playbox.sportex.Model.Games;
import com.playbox.sportex.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Games> gameList;
    private List<String> keyList;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
    public TextView tm, ad, or, ds, going;
    public CardView gamecard;
        public MyViewHolder(View v) {
            super(v);
            tm = v.findViewById(R.id.time);
            ad = v.findViewById(R.id.address);
            or = v.findViewById(R.id.organizer);
            ds = v.findViewById(R.id.distance);
            going = v.findViewById(R.id.going);
            gamecard = v.findViewById(R.id.gamecard);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Games> gameList, ArrayList<String> keyList, Context context) {
        this.gameList = gameList;
        this.keyList = keyList;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view

        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Games game = gameList.get(position);
        final String key = keyList.get(position);
        double distance = getDistance(Double.parseDouble(game.getLatitude()),
                Double.parseDouble(game.getLongitude()),
                Double.parseDouble(PreferenceUtils.getsharedLatitude(context)),
                Double.parseDouble(PreferenceUtils.getsharedLongitude(context)));
        holder.tm.setText(game.getTime()+"  " + game.getDate());
        holder.ad.setText(game.getAddress());
        holder.or.setText(game.getOrganizer());
        final String kms = Double.toString(distance);
        holder.ds.setText("(~"+kms.substring(0,3)+") kms");
        holder.going.setText(Integer.toString(game.getGoing().size()));
        holder.gamecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, JoinGameActivity.class);
                i.putExtra("Key", key);
                i.putExtra("Sport", game.getSport());
                i.putExtra("Date", game.getDate());
                i.putExtra("Time", game.getTime());
                i.putExtra("Address", game.getAddress());
                i.putExtra("Organizer", game.getOrganizer());
                i.putExtra("Latitude", game.getLatitude());
                i.putExtra("Longitude", game.getLongitude());
                i.putExtra("Going", game.getGoing());
                i.putExtra("Distance", kms.substring(0,3));
                context.startActivity(i);
            }
        });
        /*Double.toString(distance)*/
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return gameList.size();
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