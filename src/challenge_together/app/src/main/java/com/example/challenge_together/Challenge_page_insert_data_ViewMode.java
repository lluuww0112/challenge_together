package com.example.challenge_together;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

public class Challenge_page_insert_data_ViewMode extends AppCompatActivity {

    private TextView d_day;
    private String challenge_title;
    private String d_day_str;
    private String report;
    private int day_cnt;
    private String challenge_day;
    private String challenge_type;
    private Button ripple_btn;
    private String user_name;

    private String is_success;

    private EditText ripple;

    private user_ripple_adaptor user_ripple_adaptor;
    private ArrayList<user_ripple> arrayList;
    private RecyclerView ripples;
    private LinearLayoutManager linearLayoutManager;

    private TextView challenge_day_textview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_page_insert_data_view_mode);

        d_day = findViewById(R.id.d_day);

        Intent intent = getIntent();
        d_day_str = intent.getStringExtra("d-day");
        challenge_title = intent.getStringExtra("challenge_title");
        report = intent.getStringExtra("report");
        day_cnt = Integer.parseInt(intent.getStringExtra("day_cnt"));
        challenge_type = intent.getStringExtra("challenge_type");
        user_name = intent.getStringExtra("user_name");

        day_cnt += 1;
        d_day.setText(d_day_str);

        challenge_day = intent.getStringExtra("challenge_day");
        challenge_day_textview = findViewById(R.id.challenge_day);
        challenge_day_textview.setText(challenge_day);

        ripple = findViewById(R.id.ripple);

        ripple_btn = findViewById(R.id.ripple_btn);
        ripple_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                String ripple_ = ripple.getText().toString();
                user_ripple user_ripple = new user_ripple(user.getDisplayName(), user.getPhotoUrl().toString(), ripple_);

                DatabaseReference db_reference = FirebaseDatabase
                        .getInstance()
                        .getReference("challenges/"+challenge_title+"/challengers/"+user_name+"/Data/"+day_cnt);

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");
                String time = simpleDateFormat1.format(cal.getTime());
                db_reference.child("Ripple").child(time).setValue(user_ripple);

                ripple.setText("");
            }
        });



        arrayList = new ArrayList<>();
        ripples = findViewById(R.id.ripples);
        linearLayoutManager = new LinearLayoutManager(this);
        ripples.setLayoutManager(linearLayoutManager);
        user_ripple_adaptor = new user_ripple_adaptor(arrayList);
        ripples.setAdapter(user_ripple_adaptor);

        String path = "challenges/"+challenge_title+"/challengers/"+user_name+"/Data/"+day_cnt+"/Ripple";
        DatabaseReference databaseReference = FirebaseDatabase
                .getInstance()
                .getReference(path);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot snap : snapshot.getChildren()){
                    user_ripple user_ripple = snap.getValue(com.example.challenge_together.user_ripple.class);
                    arrayList.add(user_ripple);
                    user_ripple_adaptor.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}