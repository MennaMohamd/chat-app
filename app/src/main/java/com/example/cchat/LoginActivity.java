package com.example.cchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class LoginActivity extends AppCompatActivity {
private FirebaseUser currenruser;
private DatabaseReference ref;
private FirebaseAuth firebaseAuth;
private ProgressDialog loadingbar;
private Button Loginbutton,phoneloginbutton;
private EditText email,password;
private TextView forgetpass,neednewaccount;
    /*@Override
    protected void onStart() {
        super.onStart();
    if(currenruser!=null)
    {
sendtomainactivity();
    }
    }*/
    private void sendusertomainactivity()
    {
        Intent mainintent=new Intent(LoginActivity.this,MainActivity.class);
        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        finish();
    }

    private void sendtomainactivity() {
        Intent login=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth=FirebaseAuth.getInstance();
currenruser=firebaseAuth.getCurrentUser();
        email=findViewById(R.id.edittext);
password=findViewById(R.id.editpass);
Loginbutton=findViewById(R.id.btnlogin);
phoneloginbutton=findViewById(R.id.phone);
forgetpass=findViewById(R.id.forget);
loadingbar=new ProgressDialog(this);
neednewaccount=findViewById(R.id.neednewwaccout);
phoneloginbutton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent phoneintent=new Intent(LoginActivity.this,PhoneLoginActivity.class);
        startActivity(phoneintent);
    }
});
neednewaccount.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent registerintent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerintent);
    }
});
Loginbutton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
    allowloginuser();
    }
});
    }
private void allowloginuser(){

        String useremaillog=email.getText().toString();
        String userpasslog=password.getText().toString();
        if(TextUtils.isEmpty(useremaillog))
        {
            Toast.makeText(LoginActivity.this, "please enter email...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(userpasslog))
        {
            Toast.makeText(LoginActivity.this, "please enter pass...", Toast.LENGTH_SHORT).show();
        }else {
loadingbar.setTitle("login");
loadingbar.setMessage("wait,while login");
loadingbar.setCanceledOnTouchOutside(true);
loadingbar.show();
            firebaseAuth.signInWithEmailAndPassword(useremaillog, userpasslog)
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendusertomainactivity();
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(LoginActivity.this, "Login successfull",Toast.LENGTH_LONG).show();

                                loadingbar.dismiss();
                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                            }
        }

});
    }
}
}