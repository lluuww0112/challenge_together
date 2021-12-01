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

public class User_page_challenge_before extends AppCompatActivity {

    private ArrayList<challenge_data> arrayList;
    private challenge_adaptor challenge_adaptor;
    private RecyclerView challenges;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page_challenge_before);

        arrayList = new ArrayList<>();

        challenges = (RecyclerView) findViewById(R.id.challenges);
        linearLayoutManager = new LinearLayoutManager(this);
        challenges.setLayoutManager(linearLayoutManager);
        challenge_adaptor = new challenge_adaptor(arrayList);
        challenges.setAdapter(challenge_adaptor);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("challenges");
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    arrayList.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        if(is_my_challenge(snap, FirebaseAuth.getInstance().getCurrentUser())){
                            challenge_data challenge = snap.getValue(challenge_data.class);
                            Calendar calendar = Calendar.getInstance();

                            String end_date = challenge.getEnd_date();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String today = simpleDateFormat.format(calendar.getTime());
                            if(check_before(end_date, today)){
                                arrayList.add(challenge);
                                challenge_adaptor.notifyDataSetChanged();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }
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

    public boolean check_before(String before, String after){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date before_ = null;
        Date after_ = null;
        try {
            before_= simpleDateFormat.parse(before);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            after_ = simpleDateFormat.parse(after);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(before_.before(after_)){
            return true;
        }
        else{
            return false;
        }

    }
}