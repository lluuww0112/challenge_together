package com.example.challenge_together;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class Challenge_page_main extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private int challengers_cnt = 0;

    private day_Report_Adaptor day_report_adaptor;
    private ArrayList<day_Report> arrayList;
    private RecyclerView Day_report;
    private LinearLayoutManager linearLayoutManager;

    private TextView challenge_title;
    private TextView challenge_goal;

    private Button check_other_user_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_page_main);

        Intent intent = getIntent();
        String title = intent.getStringExtra("challenge_title");
        int date = intent.getIntExtra("challenge_date", 0);

        arrayList = new ArrayList<>();
        Day_report = findViewById(R.id.Day_report);
        linearLayoutManager = new LinearLayoutManager(this);
        Day_report.setLayoutManager(linearLayoutManager);
        day_report_adaptor = new day_Report_Adaptor(arrayList);
        Day_report.setAdapter(day_report_adaptor);

        challenge_title = findViewById(R.id.challenge_title);
        challenge_goal = findViewById(R.id.challenge_goal);
        check_other_user_btn = findViewById(R.id.check_other_user_btn);

        check_other_user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), Challenge_page_other_challengers.class);
                intent1.putExtra("challenge_title", title);
                startActivity(intent1);
            }
        });

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(cal.getTime());

        DatabaseReference db_reference = FirebaseDatabase.getInstance().getReference("challenges/"+title);
        db_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean flag = true;
                challenge_data challenge = snapshot.getValue(challenge_data.class);
                challenge_title.setText("[ " + challenge.getTitle() + " ]");
                challenge_goal.setText(challenge.getGoal());
                for(DataSnapshot snap : snapshot.child("challengers").getChildren()){
                    User_data user_data = snap.getValue(User_data.class);
                    if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(user_data.getUser_email())){
                        flag = false;
                    }
                }
                if(flag){
                    AlertDialog.Builder alrt = new AlertDialog.Builder(Challenge_page_main.this);
                    alrt
                            .setTitle(title)
                            .setMessage("다음 챌린지에 참여하시겠습니까?")
                            .setPositiveButton("참가", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    try {
                                        accept_challenge(title, user.getDisplayName(), user.getEmail(), date, challenge.getChallengers_cnt(), challenge, user.getPhotoUrl().toString());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent1);
                                    finish();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setCancelable(false);
                    alrt.show();
                }
                else{
                    if(challenge.getChallenge_type().equals("루틴")){
                        arrayList.clear();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        int day = 0;
                        int cnt = 1;
                        for(DataSnapshot snap : snapshot.child("challengers/"+user.getDisplayName()+"/Data").getChildren() ){
                            String str1 = snap.child("data").getValue(String.class);
                            String challenge_day = snap.child("challenge_day").getValue(String.class);
                            int success = snap.child("succesion").getValue(Integer.class);

                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                            Calendar cal = Calendar.getInstance();
                            try {
                                if(check_before(challenge_day, simpleDateFormat1.format(cal.getTime())) && success == 0){
                                    String path = "challengers/"+user.getDisplayName()+"/Data/" + String.valueOf(cnt);
                                    db_reference.child(path).child("succesion").setValue(-1);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if(day == 0){
                                day_Report day_report = new day_Report("D-day", str1, " " , challenge.getTitle(), day, success, challenge_day, 1, "루틴", user.getDisplayName());
                                arrayList.add(day_report);
                                day_report_adaptor.notifyDataSetChanged();
                            }
                            else{
                                day_Report day_report = new day_Report("D-"+day, str1, " ", challenge.getTitle(), day, success, challenge_day, 1, "루틴", user.getDisplayName());
                                arrayList.add(day_report);
                                day_report_adaptor.notifyDataSetChanged();
                            }
                            day += 1;
                            cnt += 1;
                        }
                        Collections.reverse(arrayList);
                        day_report_adaptor.notifyDataSetChanged();
                    }
                    else {
                        arrayList.clear();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        int day = 0;
                        int cnt = 1;
                        for(DataSnapshot snap : snapshot.child("challengers/"+user.getDisplayName()+"/Data").getChildren() ){
                            String str1 = snap.child("data").getValue(String.class);
                            String challenge_day = snap.child("challenge_day").getValue(String.class);
                            int success = snap.child("succesion").getValue(Integer.class);
                            if(day == 0){
                                day_Report day_report = new day_Report("D-day", str1, " " , challenge.getTitle(), day, success, challenge_day, 1, "루틴", user.getDisplayName());
                                arrayList.add(day_report);
                                day_report_adaptor.notifyDataSetChanged();
                            }
                            else{
                                day_Report day_report = new day_Report("D-"+day, str1, " ", challenge.getTitle(), day, success, challenge_day, 1, "D-day", user.getDisplayName());
                                arrayList.add(day_report);
                                day_report_adaptor.notifyDataSetChanged();
                            }
                            day += 1;
                            cnt += 1;
                        }
                        Collections.reverse(arrayList);
                        day_report_adaptor.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void accept_challenge(String title, String user_nick, String user_email,int date, int challengers_cnt, challenge_data challenge, String user_profile) throws Exception {
        User_data user = new User_data(user_nick, user_email, user_profile, title);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(cal.getTime());

        DatabaseReference db_reference = FirebaseDatabase
                .getInstance()
                .getReference("challenges/"+title);
        db_reference.child("challengers").child(user_nick).setValue(user);
        String challenge_day = challenge.getEnd_date();

        int success = 0;
        if(challenge.getChallenge_type().equals("D-day")){
            success = 2;
        }

        if(check_before(today, challenge.getStart_date())){
            for(int i = 1; i <= date; i++){
                day_Data day_data;
                if(challenge.getChallenge_type().equals("D-day") && i == 1){
                    day_data = new day_Data("오늘의 챌린지 내용을 기록해봐요!", 0, 0);
                }
                else{
                    day_data = new day_Data("오늘의 챌린지 내용을 기록해봐요!", 0, success);
                }

                db_reference.child("challengers/"+user_nick)
                        .child("Data").child(String.valueOf(i)).setValue(day_data);
                db_reference.child("challengers/"+user_nick+"/Data/"+String.valueOf(i))
                        .child("challenge_day").setValue(challenge_day);
                challenge_day = ADD_Date(challenge_day, 0, 0, -1);
            }

            db_reference = FirebaseDatabase.getInstance().getReference("challenges/"+title);
            challengers_cnt += 1;
            db_reference.child("challengers_cnt").setValue(challengers_cnt);
        }
        else{
            int cnt = get_date_diff(challenge.getEnd_date(), today);
            for(int i = 1; i <= cnt; i++){
                day_Data day_data;
                if(challenge.getChallenge_type().equals("D-day") && i == 1){
                    day_data = new day_Data("오늘의 챌린지 내용을 기록해봐요!", 0, 0);
                }
                else{
                    day_data = new day_Data("오늘의 챌린지 내용을 기록해봐요!", 0, success);
                }
                db_reference.child("challengers/"+user_nick)
                        .child("Data").child(String.valueOf(i)).setValue(day_data);
                db_reference.child("challengers/"+user_nick+"/Data/"+String.valueOf(i))
                        .child("challenge_day").setValue(challenge_day);
                challenge_day = ADD_Date(challenge_day, 0, 0, -1);
            }

            db_reference = FirebaseDatabase.getInstance().getReference("challenges/"+title);
            challengers_cnt += 1;
            db_reference.child("challengers_cnt").setValue(challengers_cnt);
        }
    }


    public String ADD_Date(String strDate, int year, int month, int day) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        Date date = simpleDateFormat.parse(strDate);

        cal.setTime(date);
        cal.add(Calendar.YEAR, year);
        cal.add(Calendar.MONTH, month);
        cal.add(Calendar.DATE, day);

        return simpleDateFormat.format(cal.getTime());
    }

    public boolean check_before(String before, String after) throws Exception {
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

    public int get_date_diff(String end_date, String start_date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date end_date_date = null;
        Date start_date_date = null;
        try {
            end_date_date = simpleDateFormat.parse(end_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            start_date_date = simpleDateFormat.parse(start_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = (end_date_date.getTime() - start_date_date.getTime()) / (24*60*60*1000);
        return ((int) diff+1);
    }

}