package com.example.challenge_together;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class User_data_adaptor extends RecyclerView.Adapter<User_data_adaptor.ViewHolder> {
    public ArrayList<User_data> arraylist;
    public User_data_adaptor(ArrayList<User_data> arraylist) {
        this.arraylist = arraylist;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_data_display_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User_data user_data = arraylist.get(position);
        holder.user_name.setText("  "+user_data.getNickname());
        holder.user_profile.setBackground(new ShapeDrawable(new OvalShape()));
        holder.user_profile.setClipToOutline(true);

        String url = String.valueOf(user_data.getUser_profile());
        Glide.with(holder.itemView.getContext()).load(url).into(holder.user_profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), Challenge_page_challengers_report.class);
                intent.putExtra("challenge_title", user_data.getChallenge_title());
                intent.putExtra("user_name", user_data.getNickname());
                intent.putExtra("user_profile", user_data.getUser_profile());
                ContextCompat.startActivity(holder.itemView.getContext(), intent, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != arraylist ? arraylist.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView user_profile;
        private TextView user_name;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.user_profile = (ImageView) itemView.findViewById(R.id.user_profile);
            this.user_name = (TextView)itemView.findViewById(R.id.user_name);
        }
    }
}
