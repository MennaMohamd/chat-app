package com.example.cchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
private String receiveuserID,current_state,send_userID;
private CircleImageView imageView;
private DatabaseReference ref,chatrequest;
FirebaseAuth mauth;
private TextView userprofilename,userprofilestatus;
private Button sendmessage,cancelrequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    userprofilename=findViewById(R.id.username);
    ref= FirebaseDatabase.getInstance().getReference().child("Users");
        chatrequest= FirebaseDatabase.getInstance().getReference().child("chat request");
    userprofilestatus=findViewById(R.id.statment);
    cancelrequest=findViewById(R.id.buttoncancelrequest);
    sendmessage=findViewById(R.id.buttonsendmessage);
    mauth=FirebaseAuth.getInstance();
    send_userID=mauth.getCurrentUser().getUid();
    imageView=findViewById(R.id.visit_profile_image);
    current_state="new";
    receiveuserID=getIntent().getExtras().get("visit_user_ID").toString();

        RetrieveuserInfo();


    }

    private void RetrieveuserInfo() {
ref.child(receiveuserID).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if((snapshot.exists())&&(snapshot.hasChild("image"))) {
            String userImage = snapshot.child("image").getValue().toString();
            String username = snapshot.child("name").getValue().toString();
            String userstatus = snapshot.child("status").getValue().toString();
            Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(imageView);
           userprofilename.setText(username);
           userprofilestatus.setText(userstatus);
           managechatrequest();

        }else
        {
            String userstatus = snapshot.child("status").getValue().toString();
            String username = snapshot.child("name").getValue().toString();
            userprofilename.setText(username);
            userprofilestatus.setText(userstatus);
managechatrequest();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {


    }
});
    }

    private void sendchatrequest() {
    chatrequest.child(send_userID).child(receiveuserID).child("request_type").setValue("sent")
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    chatrequest.child(receiveuserID).child(send_userID)
                            .child("request_type").setValue("received")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    sendmessage.setEnabled(false);
                                    current_state="request_sent";
                                    sendmessage.setText("cancel request");
                                }
                            });
                }
            });

    }

    private void managechatrequest() {
/*
if(!send_userID.equals(receiveuserID))
{
    sendmessage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sendmessage.setEnabled(true);
            if(current_state.equals("new"))
            {
                sendchatrequest();
            }
            if(current_state.equals("request_sent"))
            {
             CancelChatRequest();
            }
        }
    });
}
else
{
    sendmessage.setVisibility(View.VISIBLE);
}*/
chatrequest.child(send_userID)
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(receiveuserID))
                {
                String request_type=snapshot.child(receiveuserID).child("request_type").getValue().toString();
                        if(request_type.equals("sent"))
                        {
                       current_state="sent";
                       sendmessage.setText("cancel chat request");

                        }
                        else if(request_type.equals("received"))
                        {
                       current_state="request_received";
                       sendmessage.setText("accept chat change");
                       sendmessage.setText("Accept chat request");
                        cancelrequest.setVisibility(View.VISIBLE);
                        cancelrequest.setEnabled(true);
                    cancelrequest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        CancelChatRequest();
                        }
                    });
                        }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void CancelChatRequest() {
        chatrequest.child(receiveuserID).child(send_userID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        sendmessage.setEnabled(true);
                        current_state="new";
                        sendmessage.setText("sendrequest");
                    }
                });
    }
}