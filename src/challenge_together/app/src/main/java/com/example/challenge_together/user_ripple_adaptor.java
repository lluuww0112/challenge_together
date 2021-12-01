package com.example.challenge_together;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class user_ripple_adaptor extends RecyclerView.Adapter<user_ripple_adaptor.ViewHolder> {

    public ArrayList<user_ripple> arrayList;
    public user_ripple_adaptor(ArrayList<user_ripple> arraylist) {
        this.arrayList = arraylist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_ripple_item, parent, false);
        user_ripple_adaptor.ViewHolder holder = new user_ripple_adaptor.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        user_ripple user_ripple = arrayList.get(position);
        holder.nickname.setText(user_ripple.getUser_name());
        holder.ripple_text.setText(user_ripple.getRipple_text());
        holder.user_profile.setBackground(new ShapeDrawable(new OvalShape()));
        holder.user_profile.setClipToOutline(true);
        String url = user_ripple.getUser_profile();
        Glide.with(holder.itemView.getContext()).load(url).into(holder.user_profile);
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView user_profile;
        private TextView nickname;
        private TextView ripple_text;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.user_profile = itemView.findViewById(R.id.user_profile);
            this.nickname = itemView.findViewById(R.id.nickname);
            this.ripple_text = itemView.findViewById(R.id.ripple_text);

        }
    }
}
