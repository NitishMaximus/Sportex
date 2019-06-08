package com.playbox.sportex.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    public PreferenceUtils(){

    }

    public static boolean saveName(String name, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("name", name);
        prefsEditor.apply();
        return true;
    }

    public static String getName(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("name", null);
    }

    public static boolean savePhone(String phone, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("phone", phone);
        prefsEditor.apply();
        return true;
    }

    public static String getPhone(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("phone", null);
    }

    public static boolean saveLatitude(String latitude, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("latitude", latitude);
        prefsEditor.apply();
        return true;
    }

    public static String getsharedLatitude(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("latitude", null);
    }

    public static boolean saveLongitude(String longitude, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("longitude", longitude);
        prefsEditor.apply();
        return true;
    }

    public static String getsharedLongitude(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("longitude", null);
    }


    public static boolean saveTime(int hour, int min, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putInt("hour", hour);
        prefsEditor.putInt("min", min);
        prefsEditor.apply();
        return true;
    }

    public static int getHour(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt("hour", -1);
    }

    public static int getmin(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt("min", -1);
    }

    public static boolean saveDate(int year, int month, int day, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putInt("year", year);
        prefsEditor.putInt("month", month);
        prefsEditor.putInt("day", day);
        prefsEditor.apply();
        return true;
    }

    public static int getYear(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt("year", -1);
    }

    public static int getMonth(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt("month", -1);
    }

    public static int getDay(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt("day", -1);
    }
}