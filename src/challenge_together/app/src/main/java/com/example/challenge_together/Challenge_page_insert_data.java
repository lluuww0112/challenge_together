package com.example.challenge_together;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Challenge_page_insert_data extends AppCompatActivity {

    private TextView d_day;
    private EditText memo;
    private String challenge_title;
    private String d_day_str;
    private String report;
    private int day_cnt;
    private Spinner select_success;
    private String challenge_day;
    private String challenge_type;

    private String is_success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_page_insert_data);

        d_day = findViewById(R.id.d_day);
        memo = findViewById(R.id.memo);

        Intent intent = getIntent();
        d_day_str = intent.getStringExtra("d-day");
        challenge_title = intent.getStringExtra("challenge_title");
        report = intent.getStringExtra("report");
        day_cnt = Integer.parseInt(intent.getStringExtra("day_cnt"));
        challenge_type = intent.getStringExtra("challenge_type");

        day_cnt += 1;
        d_day.setText(d_day_str);
        memo.setText(report);

        challenge_day = intent.getStringExtra("challenge_day");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(cal.getTime());
        Date today_ = null;
        Date challenge_day_ = null;
        try {
            today_ = simpleDateFormat.parse(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            challenge_day_ = simpleDateFormat.parse(challenge_day);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(challenge_day_.before(today_)){
            AlertDialog.Builder alrt = new AlertDialog.Builder(Challenge_page_insert_data.this);
            alrt
                    .setTitle("활동입력 기간이 지났어요 :/")
                    .setMessage("다음에는 꼭 시간 맞춰서 입력해봐요.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false);
            alrt.show();
        }
        else if(today_.before(challenge_day_)){
            AlertDialog.Builder alrt = new AlertDialog.Builder(Challenge_page_insert_data.this);
            alrt
                    .setMessage("아직 참여기간이 아니에요 :)")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false);
            alrt.show();
        }


        select_success = findViewById(R.id.select_success);
        if(challenge_type.equals("D-day")){
            is_success = "";
            select_success.setVisibility(View.INVISIBLE);
        }
        else {
            select_success.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    is_success = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        String data = memo.getText().toString();

        if(data.getBytes(StandardCharsets.UTF_8).length > 0){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String path = "challenges/"+challenge_title+"/challengers/"+user.getDisplayName()+"/Data/"+day_cnt;
            DatabaseReference db_reference = FirebaseDatabase
                    .getInstance()
                    .getReference(path);
            db_reference.child("data").setValue(data);
            if(challenge_type.equals("루틴")){
                if(is_success.equals("성공")){
                    db_reference.child("succesion").setValue(1);
                }
                else{
                    db_reference.child("succesion").setValue(-1);
                }
            }
            else{
                db_reference.child("succesion").setValue(2);
            }



        }
    }
}