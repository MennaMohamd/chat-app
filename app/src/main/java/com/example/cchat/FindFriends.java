package com.example.cchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriends extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView FindFriendsRecyclerList;
    private DatabaseReference UsersRef;
    RecyclerView.Adapter programmadapter;
   RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    String[]staticname={"joe","martin","max","alex","pyden","ali"};
    String[]staticstatus={"available","hi guys","iam very busy","that's my account","whatsup world","you are welcome"};
    int[]img={R.drawable.images1,R.drawable.images2,R.drawable.images3,
            R.drawable.images4,R.drawable.images5,R.drawable.images6};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mToolbar = (Toolbar) findViewById(R.id.app_bar_finffriends);
        //setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find Friends");

        FindFriendsRecyclerList = (RecyclerView) findViewById(R.id.recycle_view);
        FindFriendsRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        layoutManager=new LinearLayoutManager(this);
      FindFriendsRecyclerList.setLayoutManager(layoutManager);
     // programmadapter=new adapter(this,staticname,staticstatus,img);


    }








    @Override
    protected void onStart() {

        super.onStart();
    FirebaseRecyclerOptions<Contacts> options=new FirebaseRecyclerOptions.Builder<Contacts>()
            .setQuery(UsersRef, Contacts.class)
            .build();


        FirebaseRecyclerAdapter<Contacts,FindFriendViewHolder>adapter=
        new FirebaseRecyclerAdapter<Contacts,FindFriendViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, int position, @NonNull Contacts model) {
           holder.username.setText(model.getName());
           holder.userstatus.setText(model.getStatus());
                Picasso.get().load(model.getImage()).into(holder.circleImageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String vist_user_ID=getRef(position).getKey();
                        Intent profileintent=new Intent(FindFriends.this,Profile.class);
                        profileintent.putExtra("visit_user_ID",vist_user_ID);
                        startActivity(profileintent);
                   finish();
                    }
                });
            }


            @NonNull
            @Override
            public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.userdisplaylayout,parent,false);
                FindFriendViewHolder viewHolder=new FindFriendViewHolder(view);
                return viewHolder;
            }


        };
        FindFriendsRecyclerList.setAdapter(adapter);
        adapter.startListening();
    }
public static class FindFriendViewHolder extends RecyclerView.ViewHolder
{

    TextView username,userstatus;
    CircleImageView circleImageView;

    public FindFriendViewHolder(@NonNull View itemView) {
        super(itemView);
    username=itemView.findViewById(R.id.user_name);
    userstatus=itemView.findViewById(R.id.user_status);
    circleImageView=itemView.findViewById(R.id.userprofileimage);


    }
}

}


