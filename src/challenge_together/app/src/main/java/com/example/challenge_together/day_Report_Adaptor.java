package com.example.challenge_together;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class day_Report_Adaptor extends RecyclerView.Adapter<day_Report_Adaptor.holder> {

    public ArrayList<day_Report> arrayList;
    public day_Report_Adaptor(ArrayList<day_Report> arrayList){
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_list_item, parent, false);
        holder holder = new holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {


        holder.dday.setText(arrayList.get(position).getDday());
        holder.diary.setText(arrayList.get(position).getDiary());
        holder.challenge_day.setText(arrayList.get(position).getChallenge_day());

        if(arrayList.get(position).getSuccess() == 1){
            holder.check_success.setText("성공");
            holder.check_success.setTextColor(Color.rgb(0, 0, 255));
        }
        else if(arrayList.get(position).getSuccess() == -1){
            holder.check_success.setText("실패");
            holder.check_success.setTextColor(Color.rgb(255, 0, 0));
        }
        else if(arrayList.get(position).getSuccess() == 2){
            holder.check_success.setText("");
        }
        else{
            holder.check_success.setText("진행중");
            holder.check_success.setTextColor(Color.rgb(66, 66, 66));
        }

        day_Report day_report = arrayList.get(holder.getAdapterPosition());
        if(day_report.getView_mode() == 1) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.itemView.getContext(), Challenge_page_insert_data.class);
                    intent.putExtra("d-day", arrayList.get(holder.getAdapterPosition()).getDday());
                    intent.putExtra("challenge_title", arrayList.get(holder.getAdapterPosition()).getChallenge_title());
                    intent.putExtra("day_cnt", String.valueOf(arrayList.get(holder.getAdapterPosition()).getDay_cnt()));
                    intent.putExtra("report", arrayList.get(holder.getAdapterPosition()).getDiary());
                    intent.putExtra("challenge_day", arrayList.get(holder.getAdapterPosition()).getChallenge_day());
                    intent.putExtra("challenge_type", arrayList.get(holder.getAdapterPosition()).getChallenge_type());
                    ContextCompat.startActivity(holder.itemView.getContext(), intent, null);
                }
            });
        }
        else{
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.itemView.getContext(), Challenge_page_insert_data_ViewMode.class);
                    intent.putExtra("d-day", arrayList.get(holder.getAdapterPosition()).getDday());
                    intent.putExtra("challenge_title", arrayList.get(holder.getAdapterPosition()).getChallenge_title());
                    intent.putExtra("day_cnt", String.valueOf(arrayList.get(holder.getAdapterPosition()).getDay_cnt()));
                    intent.putExtra("report", arrayList.get(holder.getAdapterPosition()).getDiary());
                    intent.putExtra("challenge_day", arrayList.get(holder.getAdapterPosition()).getChallenge_day());
                    intent.putExtra("challenge_type", arrayList.get(holder.getAdapterPosition()).getChallenge_type());
                    intent.putExtra("user_name", arrayList.get(holder.getAdapterPosition()).getUser_name());
                    ContextCompat.startActivity(holder.itemView.getContext(), intent, null);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }


    public class holder extends RecyclerView.ViewHolder {

        protected TextView dday;
        protected TextView diary;
        protected TextView check_success;
        protected TextView challenge_day;

        public holder(@NonNull View itemView) {
            super(itemView);

            this.dday = (TextView) itemView.findViewById(R.id.id_dday);
            this.diary = (TextView) itemView.findViewById(R.id.id_diary);
            this.check_success = (TextView) itemView.findViewById(R.id.check_success);
            this.challenge_day = (TextView) itemView.findViewById(R.id.challenge_day);
        }

    }
}
