package com.playbox.sportex.Model;

import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class Games{
    private String Sport;
    private String Organizer;
    private String Address;
    private String Time;
    private String Date;
    private String Latitude;
    private String Longitude;
    private HashMap<String, Boolean> Going;
    private long Time_Created;

    public Games() {
    }

    public String getSport() {
        return Sport;
    }

    public String getOrganizer() {
        return Organizer;
    }

    public String getAddress() {
        return Address;
    }

    public String getTime() {
        return Time;
    }

    public String getDate() {
        return Date;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public HashMap<String, Boolean> getGoing() {
        return Going;
    }

    public long getTime_Created() {
        return Time_Created;
    }
}
