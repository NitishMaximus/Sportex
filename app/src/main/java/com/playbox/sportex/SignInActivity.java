package com.playbox.sportex;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.playbox.sportex.Model.User;
import com.playbox.sportex.utils.PreferenceUtils;


import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;

public class SignInActivity extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPhone = (EditText)findViewById(R.id.edtPhone);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        login = (Button)findViewById(R.id.button);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //get User Information
                            if(dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                                if (user.getPassword().equals(edtPassword.getText().toString())) {
                                    Toast.makeText(SignInActivity.this, "Sign In Success", Toast.LENGTH_SHORT).show();

                                    try{
                                        Intent homeintent = new Intent(SignInActivity.this, DashboardActivity.class);
                                        PreferenceUtils.saveName(user.getName(),SignInActivity.this);
                                        PreferenceUtils.savePhone(edtPhone.getText().toString(), SignInActivity.this);
                                        startActivity(homeintent);
                                    }catch(NullPointerException e){
                                        Toast.makeText(SignInActivity.this, "Preference Error", Toast.LENGTH_SHORT).show();
                                    }


                                } else {
                                    Toast.makeText(SignInActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(SignInActivity.this, "User not exists", Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
