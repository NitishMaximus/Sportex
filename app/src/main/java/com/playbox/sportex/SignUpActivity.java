package com.playbox.sportex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    Button btn_signup;
    EditText edtPhone, edtName, edtEmail,edtPass, edtConPass;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference table_user = database.getReference("User");
    private static final Pattern phone_pattern=
            Pattern.compile("^[7-9]\\d{9}$");

    private static final Pattern userNamePattern = Pattern.compile("^[a-zA-Z0-9_-]{6,14}$");


    private static final Pattern password_pattern=
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    private static final Pattern email_pattern=
            Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btn_signup = (Button)findViewById(R.id.btn_signup);
        edtPhone = (EditText)findViewById(R.id.edtPhone);
        edtName = (EditText)findViewById(R.id.edtName);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtPass = (EditText)findViewById(R.id.edtPass);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {

        String name= edtName.getText().toString();
        String phone=edtPhone.getText().toString();
        String password=edtPass.getText().toString();
        String email=edtEmail.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Please enter your name..",Toast.LENGTH_SHORT).show();
        }

        else if(!userNamePattern.matcher(name).matches())
        {
            Toast.makeText(this, "Please enter a valid username(more than 6 chars)", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"Please enter your phone number..",Toast.LENGTH_SHORT).show();
        }

        else if(!phone_pattern.matcher(phone).matches())
        {
            Toast.makeText(this, "Please enter a valid mobile number(10 digit)", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please enter your Email ID..",Toast.LENGTH_SHORT).show();
        }

        else if(!email_pattern.matcher(email).matches())
        {
            Toast.makeText(this, "Please enter a valid Email ID", Toast.LENGTH_SHORT).show();
        }


        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please enter your password..",Toast.LENGTH_SHORT).show();
        }
        else if(!password_pattern.matcher(password).matches())
        {
            Toast.makeText(this, "Atleast 1 special char, 1 digit req, Min 4 characters required", Toast.LENGTH_SHORT).show();
        }
        else {

            updateDatabase(name,phone,password,email);
        }

    }

    private void updateDatabase(final String name, final String phone, final String password, final String email){
        final ProgressDialog mDialog = new ProgressDialog(SignUpActivity.this);
        mDialog.setMessage("Please Wait...");
        mDialog.show();

        table_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(phone).exists()) {
                    mDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Phone no already Exists", Toast.LENGTH_SHORT).show();
                }
                else{
                    mDialog.dismiss();
                    User user = new User(name, password, email);
                    table_user.child(phone).setValue(user);
                    Toast.makeText(SignUpActivity.this, "Sign up Successful", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent homeintent = new Intent(SignUpActivity.this, DashboardActivity.class);
                    String qwe = user.getName();
                    PreferenceUtils.saveName(qwe,SignUpActivity.this);
                    PreferenceUtils.savePhone(phone, SignUpActivity.this);
                    startActivity(homeintent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
