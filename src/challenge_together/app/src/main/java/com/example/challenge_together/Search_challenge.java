package com.example.challenge_together;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Search_challenge extends AppCompatActivity {

    private ArrayList<challenge_data> arrayList;
    private challenge_adaptor challenge_adaptor;
    private RecyclerView challenges;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_challenge);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        arrayList = new ArrayList<>();

        challenges = (RecyclerView) findViewById(R.id.challenges);
        linearLayoutManager = new LinearLayoutManager(this);
        challenges.setLayoutManager(linearLayoutManager);
        challenge_adaptor = new challenge_adaptor(arrayList);
        challenges.setAdapter(challenge_adaptor);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("challenges");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot snap : snapshot.getChildren()){
                    challenge_data challenge_data = snap.getValue(challenge_data.class);
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String today = format.format(cal.getTime());
                    Date today_ = null;
                    Date end_day = null;
                    try {
                        today_ = format.parse(today);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        end_day = format.parse(challenge_data.getEnd_date());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(is_my_challenge(snap, user) == false && (today_.before(end_day) || today.equals(challenge_data.getEnd_date()))){
                        arrayList.add(challenge_data);
                        challenge_adaptor.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public boolean is_my_challenge(DataSnapshot snap, FirebaseUser user){
        for(DataSnapshot snap_ : snap.child("challengers").getChildren()){
            User_data user_data = snap_.getValue(User_data.class);
            String str1 = user_data.getNickname();
            String str2 = user.getDisplayName();
            if(str1.equals(str2)){
                return true;
            }

        }
        return false;
    }
}