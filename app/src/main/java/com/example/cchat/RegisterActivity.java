package com.example.cchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
private EditText emailreg,passwordreg;
private Button register;
private DatabaseReference roof;
private FirebaseAuth firebaseAuth;
private TextView haveaacount;
private ProgressDialog loadingdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailreg=findViewById(R.id.edittextregister);
        loadingdialog=new ProgressDialog(this);
        passwordreg=findViewById(R.id.editpassregister);
        register=findViewById(R.id.btnregister);
        roof= FirebaseDatabase.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        haveaacount=findViewById(R.id.alreadyhavenewaccount);
        haveaacount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useremail=emailreg.getText().toString();
                String userpass=passwordreg.getText().toString();
                if(TextUtils.isEmpty(useremail))
                {
                    Toast.makeText(RegisterActivity.this, "please enter email...", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(userpass))
                {
                    Toast.makeText(RegisterActivity.this, "please enter pass...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingdialog.setTitle("Create new account");
                    loadingdialog.setMessage("waiting , while creare account");
                    loadingdialog.setCanceledOnTouchOutside(true);
                    loadingdialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(useremail, userpass)
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                       sendusertomainactivity();
                                       String currentuserId=firebaseAuth.getCurrentUser().getUid();
                                       roof.child("Users").child(currentuserId).setValue("");
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(RegisterActivity.this, "createUserWithEmail:success",Toast.LENGTH_LONG).show();

                                       loadingdialog.dismiss();
                                    } else {
                                        // If sign in fails, display a message to the user.

                                        Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        loadingdialog.dismiss();
                                    }


                                }
                            });
                }
            }
        });
    }
    private void sendusertomainactivity()
    {
        Intent mainintent=new Intent(RegisterActivity.this,MainActivity.class);
        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        finish();
    }

}