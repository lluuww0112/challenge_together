package com.example.challenge_together;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Add_challenge extends AppCompatActivity {

    private Button cancel_button;
    private Button setting_finish;
    private EditText set_title;
    private EditText set_goal;
    private Spinner set_challenge_type;
    private Button set_start_date;
    private TextView start_date_text_view;
    private Button set_end_date;
    private TextView end_date_text_view;

    private FirebaseDatabase mDataBase = FirebaseDatabase.getInstance();
    private DatabaseReference mDataBase_reference = mDataBase.getReference();

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String str3;

    private int y = 0, m = 0, d = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_challenge);

        Calendar cal = Calendar.getInstance();
        long now = System.currentTimeMillis();
        Date today = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today_date = simpleDateFormat.format(today);


        set_title = findViewById(R.id.set_title);
        set_goal = findViewById(R.id.set_goal);
        set_challenge_type = findViewById(R.id.set_challenge_type);

        set_challenge_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str3 = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        start_date_text_view = findViewById(R.id.start_date_text_view);
        start_date_text_view.setText(today_date);
        set_start_date = findViewById(R.id.set_start_date);
        set_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Add_challenge.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                start_date_text_view.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });

        end_date_text_view = findViewById(R.id.end_date_text_view);
        end_date_text_view.setText(today_date);
        set_end_date = findViewById(R.id.set_end_date);
        set_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Add_challenge.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                end_date_text_view.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });

        setting_finish = findViewById(R.id.setting_finish);
        setting_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1 = set_title.getText().toString();
                String str2 = set_goal.getText().toString();
                String start = start_date_text_view.getText().toString();
                String end = end_date_text_view.getText().toString();

                if(str1.getBytes(StandardCharsets.UTF_8).length > 0 || str2.getBytes(StandardCharsets.UTF_8).length > 0
                ){
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                    Date start_date = null;
                    Date end_date = null;
                    try {
                        start_date = format.parse(start);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    try {
                        end_date = format.parse(end);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long diff = (end_date.getTime() - start_date.getTime()) / (24*60*60*1000);

                    String Start_date = format.format(start_date);
                    String End_date = format.format(end_date);

                    int date = ((int) diff+1);
                    try {
                        upload_challenge(str1, str2, str3, date, Start_date, End_date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "모든 옵션을 추가해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



    public void upload_challenge(String title, String goal, String challenge_type, int date, String Start_date, String End_date) throws Exception {
        challenge_data challenge = new challenge_data(title, goal, challenge_type, date, 1, Start_date, End_date);
        mDataBase_reference.child("challenges").child(title).setValue(challenge);

        User_data user_data = new User_data(user.getDisplayName(), user.getEmail(), user.getPhotoUrl().toString(), title);
        mDataBase_reference.child("challenges/"+title).child("challengers").setValue(1);
        mDataBase_reference.child("challenges/"+title+"/challengers").child(user_data.getNickname()).setValue(user_data);

        String strDate = End_date;


        for(int i = 1; i <= date; i++){
            day_Data day_data;
            if(challenge_type.equals("루틴")){
                day_data = new day_Data("오늘의 챌린지 내용을 기록해봐요!", 0, 0);
            }
            else {
                int success;
                if(i == 1) success = 0;
                else success = 2;
                day_data = new day_Data("오늘의 챌린지 내용을 기록해봐요!", 0, success);
            }

            mDataBase_reference.child("challenges/"+title+"/challengers/"+user_data.getNickname())
                    .child("Data").child(String.valueOf(i)).setValue(day_data);
            mDataBase_reference.child("challenges/"+title+"/challengers/"+user_data.getNickname())
                    .child("Data").child(String.valueOf(i)).child("challenge_day").setValue(strDate);

            strDate = ADD_Date(strDate, 0, 0, -1);

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

}