package com.example.challenge_together;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User_page extends AppCompatActivity {

    private ImageView user_profile;
    private Button googleLogOut_btn;
    private Button participating_challenge_btn;
    private Button challenge_before_btn;

    private FirebaseAuth auth;
    private GoogleApiClient googleApiClient;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        user_profile = findViewById(R.id.user_profile);

        user_profile.setBackground(new ShapeDrawable(new OvalShape()));
        user_profile.setClipToOutline(true);

        String url = String.valueOf(user.getPhotoUrl());
        Glide.with(this).load(url).into(user_profile);
        String name = user.getDisplayName();

        googleLogOut_btn = findViewById(R.id.googleLogOut_btn);
        googleLogOut_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        participating_challenge_btn = findViewById(R.id.participating_challenge_btn);
        participating_challenge_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        challenge_before_btn = findViewById(R.id.challenge_before_btn);
        challenge_before_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), User_page_challenge_before.class);
                startActivity(intent);
            }
        });





    }


}