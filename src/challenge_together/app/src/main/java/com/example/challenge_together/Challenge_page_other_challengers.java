package com.example.challenge_together;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class Challenge_page_other_challengers extends AppCompatActivity {
    private User_data_adaptor user_data_adaptor;
    private ArrayList<User_data> arrayList;
    private RecyclerView user_list;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_page_other_challengers);

        Intent intent = getIntent();
        String challenge_title =intent.getStringExtra("challenge_title");

        Intent intent1 = new Intent(getApplicationContext(), Challenge_page_challengers_report.class);
        intent1.putExtra("challenge_title", challenge_title);

        arrayList = new ArrayList<>();
        user_list = findViewById(R.id.user_list);
        linearLayoutManager = new LinearLayoutManager(this);
        user_list.setLayoutManager(linearLayoutManager);
        user_data_adaptor = new User_data_adaptor(arrayList);
        user_list.setAdapter(user_data_adaptor);

        DatabaseReference db_reference = FirebaseDatabase.getInstance().getReference("challenges/"+challenge_title);
        db_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot snap : snapshot.child("challengers").getChildren()){
                    User_data user_data = snap.getValue(User_data.class);
                        arrayList.add(user_data);
                        user_data_adaptor.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}