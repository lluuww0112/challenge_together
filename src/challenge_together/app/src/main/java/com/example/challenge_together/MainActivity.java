package com.example.challenge_together;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private ArrayList<challenge_data> arrayList;
    private challenge_adaptor challenge_adaptor;
    private RecyclerView challenges;
    private LinearLayoutManager linearLayoutManager;
    private Button add_challenge_btn;
    private Button search_challenge_btn;

    private ImageView iv_profile;
    private TextView user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String url = String.valueOf(user.getPhotoUrl());

            user_name = findViewById(R.id.user_name);
            user_name.setText(name);

            iv_profile = findViewById(R.id.iv_profile);
            Glide.with(this).load(url).into(iv_profile);
            iv_profile.setBackground(new ShapeDrawable(new OvalShape()));
            iv_profile.setClipToOutline(true);
            iv_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), User_page.class);
                    startActivity(intent);
                }
            });

            arrayList = new ArrayList<>();

            challenges = (RecyclerView) findViewById(R.id.challenges);
            linearLayoutManager = new LinearLayoutManager(this);
            challenges.setLayoutManager(linearLayoutManager);
            challenge_adaptor = new challenge_adaptor(arrayList);
            challenges.setAdapter(challenge_adaptor);

            add_challenge_btn = findViewById(R.id.add_challenge_btn);
            add_challenge_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Add_challenge.class);
                    startActivity(intent);
                }
            });

            search_challenge_btn = findViewById(R.id.search_challenge_btn);
            search_challenge_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Search_challenge.class);
                    startActivity(intent);
                }
            });

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
                            try {
                                if(check_before(today, end_date) || today.equals(end_date)){
                                    arrayList.add(challenge);
                                    challenge_adaptor.notifyDataSetChanged();
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);						// 태스크를 백그라운드로 이동
        finishAndRemoveTask();						// 액티비티 종료 + 태스크 리스트에서 지우기
        android.os.Process.killProcess(android.os.Process.myPid());
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

    public boolean check_before(String before, String after) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date before_ = null;
        Date after_ = null;

        before_= simpleDateFormat.parse(before);
        after_ = simpleDateFormat.parse(after);


        if(before_.before(after_)){
            return true;
        }
        else{
            return false;
        }

    }


}