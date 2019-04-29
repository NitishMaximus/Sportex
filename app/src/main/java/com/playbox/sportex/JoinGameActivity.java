package com.playbox.sportex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class JoinGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        Intent i = getIntent();
        Log.d("t", i.getStringExtra("Address"));

        HashMap<String, Boolean> map = (HashMap<String, Boolean>) i.getSerializableExtra("Going");
        for (HashMap.Entry<String, Boolean> entry : map.entrySet()) {
            Log.d("gong", entry.getKey() + "/" + entry.getValue());
        }
    }
}
