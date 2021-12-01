package com.example.challenge_together;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class challenge_adaptor extends RecyclerView.Adapter<challenge_adaptor.ViewHolder> {

    public ArrayList<challenge_data> arraylist;
    public challenge_adaptor(ArrayList<challenge_data> arraylist) {
        this.arraylist = arraylist;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.challenge_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        challenge_data challenge_data = arraylist.get(position);
        holder.title.setText("[ "+challenge_data.getTitle()+" ]");
        holder.goal.setText(challenge_data.getGoal());
        holder.challenge_type.setText(challenge_data.getChallenge_type());
        holder.date.setText("기간 : "+challenge_data.getStart_date()+" ~ "+challenge_data.getEnd_date());
        holder.challengers_cnt.setText("참여자 수 : "+String.valueOf(challenge_data.getChallengers_cnt()));

        holder.itemView.setTag(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), Challenge_page_main.class);
                intent.putExtra("challenge_title", arraylist.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("challenge_date", arraylist.get(holder.getAdapterPosition()).getDate());
                ContextCompat.startActivity(holder.itemView.getContext(), intent, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != arraylist ? arraylist.size() : 0);
    }

    public void remove(int posiiton){
        try{
            arraylist.remove(posiiton);
            notifyItemRemoved(posiiton);
        }
        catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        protected TextView title;
        protected TextView goal;
        protected TextView challenge_type;
        protected TextView date;
        protected TextView challengers_cnt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.goal = (TextView) itemView.findViewById(R.id.goal);
            this.challenge_type = (TextView) itemView.findViewById(R.id.challenge_type);
            this.date = (TextView) itemView.findViewById(R.id.date);
            this.challengers_cnt = (TextView) itemView.findViewById(R.id.challengers_cnt);


        }
    }
}
