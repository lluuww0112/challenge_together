package com.example.challenge_together;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Challenge_page_challengers_report extends AppCompatActivity {

    private ImageView user_profile_imageView;

    private day_Report_Adaptor day_report_adaptor;
    private ArrayList<day_Report> arrayList;
    private RecyclerView Day_report;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_page_challengers_report);
        Intent intent = getIntent();
        String title = intent.getStringExtra("challenge_title");
        String user_name = intent.getStringExtra("user_name");
        String user_profile = intent.getStringExtra("user_profile");
        user_profile_imageView = findViewById(R.id.user_profile);
        String url = user_profile;
        Glide.with(this).load(url).into(user_profile_imageView);
        user_profile_imageView.setBackground(new ShapeDrawable(new OvalShape()));
        user_profile_imageView.setClipToOutline(true);

        arrayList = new ArrayList<>();
        Day_report = findViewById(R.id.Day_report);
        linearLayoutManager = new LinearLayoutManager(this);
        Day_report.setLayoutManager(linearLayoutManager);
        day_report_adaptor = new day_Report_Adaptor(arrayList);
        Day_report.setAdapter(day_report_adaptor);


        DatabaseReference db_reference = FirebaseDatabase.getInstance().getReference("challenges/"+title);
        db_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                challenge_data challenge_data = snapshot.getValue(challenge_data.class);
                arrayList.clear();
                int day = 0;
                for(DataSnapshot snap : snapshot.child("challengers/"+user_name+"/Data").getChildren()){
                    String str1 = snap.child("data").getValue(String.class);
                    String str2 = String.valueOf(snap.child("cnt").getValue(Integer.class));
                    String challenge_day = snap.child("challenge_day").getValue(String.class);
                    int success = snap.child("succesion").getValue(Integer.class);


                    if(day == 0){
                        day_Report day_report = new day_Report("D-day", str1, str2 , title, day, success, challenge_day, 0, challenge_data.getChallenge_type(), user_name);
                        arrayList.add(day_report);
                        day_report_adaptor.notifyDataSetChanged();
                    }
                    else {
                        day_Report day_report = new day_Report("D-" + day, str1, str2, title, day, success, challenge_day, 0, challenge_data.getChallenge_type(), user_name);
                        arrayList.add(day_report);
                        day_report_adaptor.notifyDataSetChanged();
                    }
                    day += 1;
                }
                Collections.reverse(arrayList);
            }




            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}