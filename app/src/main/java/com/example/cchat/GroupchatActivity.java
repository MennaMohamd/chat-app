package com.example.cchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupchatActivity extends AppCompatActivity {
private Toolbar mtoolbar;
private ImageButton imageButton;
private EditText editText;
private ScrollView scrollView;
private TextView textView;

private FirebaseAuth firebaseAuth;
private String currentdate,currenttime,message;
private DatabaseReference groupnameref,groupmessagekeyref;
private String currentgroupname,currentuserId,currentusername;
private DatabaseReference userref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat);
        firebaseAuth=FirebaseAuth.getInstance();
        currentuserId=firebaseAuth.getCurrentUser().getUid();
        currentgroupname=getIntent().getExtras().get("groupname").toString();
        Toast.makeText(GroupchatActivity.this,currentgroupname,Toast.LENGTH_LONG).show();
        userref= FirebaseDatabase.getInstance().getReference().child("Users");
        groupnameref=FirebaseDatabase.getInstance().getReference().child("Groups").child(currentgroupname);
        initialization();
        getuserinfo();

    imageButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
    savemessageinfotodatabase(view);
    editText.setText("");
    startService(view);
    }
 });

    }
    public void startService(View view) {
        Intent serviceIntent = new Intent(this, exampleservice.class);
        serviceIntent.putExtra("inputExtra", message);
        startService(serviceIntent);
    }

    public void stopservice(Intent view) {


    }

    @Override
    protected void onStart() {
        super.onStart();

        groupnameref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
          if(snapshot.exists())
          {
              DisplayMessage(snapshot);
          }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists())
                {
                    DisplayMessage(snapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void savemessageinfotodatabase(View view) {
          message=editText.getText().toString();
        String messagekey=groupnameref.push().getKey();
        if(TextUtils.isEmpty(message)){
        Toast.makeText(this,"please enter message...",Toast.LENGTH_LONG).show();
        }else
        {
            Calendar ccalenddate =Calendar.getInstance();
            SimpleDateFormat currentdateformat=new SimpleDateFormat("MMM dd,yyyy");
            currentdate=currentdateformat.format(ccalenddate.getTime());

            Calendar ccalendtime =Calendar.getInstance();
            SimpleDateFormat currenttimeformat=new SimpleDateFormat("mm:hh a");
            currenttime=currenttimeformat.format(ccalendtime.getTime());

            HashMap<String,Object>groupmessagekey=new HashMap<>();
            groupnameref.updateChildren(groupmessagekey);
            groupmessagekeyref=groupnameref.child(messagekey);

            HashMap<String,Object>messageinfomap=new HashMap<>();
            messageinfomap.put("name",currentusername);
            messageinfomap.put("message",message);
            messageinfomap.put("date",currentdate);
            messageinfomap.put("time",currenttime);
            groupmessagekeyref.updateChildren(messageinfomap);
        }

    }

    private void DisplayMessage(DataSnapshot snapshot) {
        Iterator iterator=snapshot.getChildren().iterator();
        while (iterator.hasNext())
        {
            String chatdate=(String)((DataSnapshot)iterator.next()).getValue();
            String chatmessage=(String)((DataSnapshot)iterator.next()).getValue();
            String chatname=(String)((DataSnapshot)iterator.next()).getValue();
            String chattime=(String)((DataSnapshot)iterator.next()).getValue();
textView.append(chatname+"\n"+chatmessage+"\n"+chatdate+"\n"+chattime);
scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }

    }

    private void getuserinfo() {
    userref.child(currentuserId).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.exists()){
        currentusername=snapshot.child("name").getValue().toString();


        }
      }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
    });
    }

    private void initialization() {
         //mtoolbar=findViewById(R.id.groupchatactivity);
         //setSupportActionBar(mtoolbar);
         //getSupportActionBar().setTitle(currentgroupname);
         imageButton=findViewById(R.id.sendmessagebutton);
         editText=findViewById(R.id.sendmessage);
         textView=findViewById(R.id.textview);
         scrollView=findViewById(R.id.scrollview);

          }
        }