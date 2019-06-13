package com.playbox.sportex;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.playbox.sportex.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterGoing extends RecyclerView.Adapter<MyAdapterGoing.MyViewHolder> {
    private List<String> playerList, phoneList;
    private Context context;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView playerName;
        ImageView callPlayer;
        public MyViewHolder(View v) {
            super(v);
            playerName = v.findViewById(R.id.playerName);
            callPlayer = v.findViewById(R.id.callPlayer);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapterGoing(ArrayList<String> playerList, ArrayList<String> phoneList, Context context) {
        this.playerList = playerList;
        this.phoneList = phoneList;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapterGoing.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view

        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_going_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyAdapterGoing.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.playerName.setText(playerList.get(position));
        if(!(playerList.get(position).equals(PreferenceUtils.getName(context)+"-Host") ||
                playerList.get(position).equals(PreferenceUtils.getName(context)))) {
            holder.callPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri u = Uri.parse("tel:" + phoneList.get(position));
                    Intent i = new Intent(Intent.ACTION_DIAL, u);
                    try {
                        // Launch the Phone app's dialer with a phone
                        // number to dial a call.
                        context.startActivity(i);
                    } catch (SecurityException s) {
                        // show() method display the toast with
                        // exception message.
                        Toast.makeText(context, s.getMessage(), Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
        }
        else{
            holder.callPlayer.setVisibility(View.GONE);
        }

    }

    public int getItemCount() {
        return playerList.size();
    }
}