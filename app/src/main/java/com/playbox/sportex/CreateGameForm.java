package com.playbox.sportex;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;

import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.playbox.sportex.Model.User;
import com.playbox.sportex.utils.PreferenceUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateGameForm extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button btn_picktime, btn_pickdate;
    android.os.Handler customHandler = new android.os.Handler();
    String address, date, time, lat, lon,timemillis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_game_form);
        Places.initialize(getApplicationContext(), "Insert API key here");
        PlacesClient placesClient = Places.createClient(this);

        btn_picktime = (Button)findViewById(R.id.btn_picktime);
        PreferenceUtils.saveTime(-1, -1,CreateGameForm.this);
        PreferenceUtils.saveDate(-1, -1,-1,CreateGameForm.this);
        btn_picktime.setText("Pick Time");
        btn_pickdate = (Button)findViewById(R.id.btn_pickdate);
        Button btn_create = (Button)findViewById(R.id.btn_create);
        final Spinner spinner = (Spinner)findViewById(R.id.spinner_sports);

        final String username = PreferenceUtils.getName(CreateGameForm.this);

        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CreateGameForm.this,
                R.array.sports_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
                address = place.getAddress();
                try {
                    LatLng ll = place.getLatLng();
                    lat = Double.toString(ll.latitude);
                    lon = Double.toString((ll.longitude));
                }catch(Exception e){
                    lat = "0";
                    lon = "0";
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });

        autocompleteFragment.setCountry("IN");

        customHandler.postDelayed(updateTimerThread, 0);

        btn_picktime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });
        btn_pickdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference table_games = database.getReference("Games");
                final DatabaseReference table_user = database.getReference("User");

                Calendar cal = Calendar.getInstance();
                long current_time = cal.getTimeInMillis();

                HashMap<String, Boolean> host_going = new HashMap<String,Boolean>();
                host_going.put(PreferenceUtils.getName(getApplicationContext()),true);

                final String key = table_games.push().getKey();
                Map<String, Object> game_details = new HashMap<String, Object>();
                game_details.put("Sport", spinner.getSelectedItem().toString());
                game_details.put("Organizer", username);
                game_details.put("Time", time);
                game_details.put("GameTimeInMillis", timemillis);
                game_details.put("Date", date);
                game_details.put("Address", address);
                game_details.put("Latitude", lat);
                game_details.put("Longitude", lon);
                game_details.put("Going", host_going);
                game_details.put("Time_Created", current_time);

                Map<String, Object> game_id = new HashMap<String, Object>();
                game_id.put(key, game_details);
                table_games.updateChildren(game_id);

                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.child(PreferenceUtils.getPhone(getApplicationContext())).getValue(User.class);
                        HashMap<String, Boolean> joinedgames;
                        try{
                            joinedgames = user.getJoinedGame();
                            joinedgames.put(key, true);
                        }catch (NullPointerException e){
                            joinedgames = new HashMap<String, Boolean>(){{put(key, true);}};
                        }
                        table_user.child(PreferenceUtils.getPhone(getApplicationContext())).child("JoinedGame").setValue(joinedgames);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent i = new Intent(CreateGameForm.this,DashboardActivity.class);
                if(Long.parseLong(timemillis)-current_time<0) {
                    Toast.makeText(CreateGameForm.this, "Enter Valid Date and Time", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(CreateGameForm.this, "Game Created Successfully", Toast.LENGTH_SHORT).show();
                }
                startActivity(i);
            }
        });
    }

    private Runnable updateTimerThread = new Runnable()
    {
        public void run()
        {
            int hour = PreferenceUtils.getHour(CreateGameForm.this);
            int min = PreferenceUtils.getmin(CreateGameForm.this);
            int year = PreferenceUtils.getYear(CreateGameForm.this);
            int month = PreferenceUtils.getMonth(CreateGameForm.this);
            int day = PreferenceUtils.getDay(CreateGameForm.this);

            String ap = "AM";
            String zero_min = Integer.toString(min);
            if (hour>=12){
                ap = "PM";
                if(hour>12) {
                    hour = hour - 12;
                }
            }
            if(hour == 0){
                hour = 12;
            }
            if(zero_min.length() == 1){
                zero_min = "0" + zero_min;
            }
            if (hour != -1 && min != -1) {
                btn_picktime.setText(Integer.toString(hour)+" : "+ zero_min+" "+ap);
            }
            if(year != -1){
                btn_pickdate.setText(Integer.toString(day)+"/"+Integer.toString(month)+"/"+Integer.toString(year));
            }
            time = Integer.toString(hour) +":" + zero_min +" " +ap;
            date = Integer.toString(day) +"-" + Integer.toString(month)+"-"+Integer.toString(year);

            String zero_hour = Integer.toString(hour);
            if(zero_hour.length() == 1){
                zero_hour = "0" + zero_hour;
            }

            String zero_month = Integer.toString(month);
            if(zero_month.length() == 1){
                zero_month = "0" + zero_month;
            }

            String zero_day = Integer.toString(day);
            if(zero_day.length() == 1){
                zero_day = "0" + zero_day;
            }

            String myDate = Integer.toString(year)+"/"
                    + zero_month+"/"
                    +zero_day+" "
                    +zero_hour+":"
                    +zero_min+":"
                    +"00";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            long millis = 0;
            try {
                Date date = sdf.parse(myDate);
                millis = date.getTime()+43243696;
                timemillis = Long.toString(millis);
            }catch(ParseException e){
                e.printStackTrace();
            }

            customHandler.postDelayed(this, 100);
        }
    };

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        FragmentManager lol = getFragmentManager();
        newFragment.show(lol, "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        FragmentManager pol = getFragmentManager();
        newFragment.show(pol, "datePicker");
    }

}
