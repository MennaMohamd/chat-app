package com.example.cchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
//name of class ,name of static inner class
public class adapter extends RecyclerView.Adapter<adapter.viewholder> {
    private final Context context;
    private RecyclerView FindFriendsRecyclerList;
    String[]staticname;
    String[]staticstatus;
    int[]img;
    public static class viewholder extends RecyclerView.ViewHolder
    {   RecyclerView FindFriendsRecyclerList;
        TextView username,userstatus;
        CircleImageView circleImageView;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.user_name);
            userstatus=itemView.findViewById(R.id.user_status);
            circleImageView=itemView.findViewById(R.id.userprofileimage);
            //FindFriendsRecyclerList = (RecyclerView) findViewById(R.id.recycle_view);
        }
    }

    public adapter(Context context,String[] staticname, String[] staticstatus, int[] img) {
        this.staticname = staticname;
        this.staticstatus = staticstatus;
        this.img = img;
        this.context=context;
    }

    @NonNull
    @Override
    public adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.userdisplaylayout,parent,false);
      //object of inner class
        viewholder viewHolder=new viewholder(view) ;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull adapter.viewholder holder, int position) {
holder.username.setText(staticname[position]);
        holder.userstatus.setText(staticstatus[position]);
        holder.circleImageView.setImageResource(img[position]);
    }

    @Override
    public int getItemCount() {
        return staticname.length;
    }
}
