package com.example.cchat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class aadapter extends RecyclerView.Adapter<aadapter.Myviewholder> {
    String s1[];
    int[]image;
  chatfragement context;

    public aadapter(String[] s1, int[] image, chatfragement context) {
        this.s1 = s1;

        this.image = image;
        this.context = context;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        public Myviewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
