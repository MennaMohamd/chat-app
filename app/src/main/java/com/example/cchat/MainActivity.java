package com.example.cchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Toolbar mtoolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FirebaseAuth firebaseAuth;
    private Thegetaccessoradapter thegetaccessadapter;
    private DatabaseReference ref;
private FirebaseUser currentuser;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mtoolbar=  findViewById(R.id.main_bar);
        ref= FirebaseDatabase.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        currentuser=firebaseAuth.getCurrentUser();
      // setSupportActionBar(mtoolbar);
        // getSupportActionBar().setTitle("whatsapp");
        viewPager=findViewById(R.id.viewpager);
        thegetaccessadapter=new Thegetaccessoradapter(getSupportFragmentManager());
        viewPager.setAdapter(thegetaccessadapter);
        tabLayout=findViewById(R.id.tab_bar);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentuser==null)
        {
            Sendusertologinactivity();
        }else
        {
            verfyuserExistance();
        }

    }

    private void verfyuserExistance() {
      //to know Is the current user exist or not?
      String currentuserId=firebaseAuth.getCurrentUser().getUid();
ref.child("Users").child(currentuserId).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        //check if have current user ,the same data of user
        if(snapshot.child("name").exists())
         {
             Toast.makeText(MainActivity.this,"welcome",Toast.LENGTH_LONG).show();
         }else
        {
            //to insert new data
            Intent settingintent=new Intent(MainActivity.this,SettingActivity.class);
           settingintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(settingintent);
            finish();

        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});
    }

    private void Sendusertologinactivity() {
        Intent login=new Intent(MainActivity.this,LoginActivity .class);
       login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(login);
        finish();
    }

    private void findnewfriendsactivity() {
        Intent login=new Intent(MainActivity.this,FindFriends .class);
        login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(login);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.newmenu,menu);
        return true;
    }
private void requestnewgroup()
{
    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);
    builder.setTitle("Enter group name");
    final EditText groupnamefield=new EditText(MainActivity.this);
    groupnamefield.setHint("e.g chat group for students");
    builder.setView(groupnamefield);
    builder.setPositiveButton("create", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
String groupname=groupnamefield.getText().toString();
if(TextUtils.isEmpty(groupname))
{
    Toast.makeText(MainActivity.this,"please enter the group name...",Toast.LENGTH_LONG).show();
}
else
{
createnewgroup(groupname);
}
        }
    });
    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
           }
          });builder.show();
          }

    private void createnewgroup(String groupname) {
      ref.child("Groups").child(groupname).setValue("")
              .addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      if(task.isSuccessful())
                      {
                          Toast.makeText(MainActivity.this,groupname+"is created",Toast.LENGTH_LONG).show();
                      }
                  }
              });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);
         if(item.getItemId()==R.id.main_find_friends_option)
         {

         }
         if(item.getItemId()==R.id.logout)
         {
             firebaseAuth.signOut();
             Sendusertologinactivity();
         }
         if(item.getItemId()==R.id.setting)
         {
             Intent settingintent=new Intent(MainActivity.this,SettingActivity.class);
             startActivity(settingintent);

         }
         if(item.getItemId()==R.id.create_group)
         {
        requestnewgroup();
         }

        if(item.getItemId()==R.id.main_find_friends_option)
        {
            findnewfriendsactivity();
        }
    return true;
  }
}