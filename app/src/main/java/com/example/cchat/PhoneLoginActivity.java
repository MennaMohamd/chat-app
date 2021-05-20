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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {
private EditText Phonenumber,Vericationnumber;
private FirebaseAuth mauth;
private ProgressDialog progressDialog;
private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback;
private Button sendverificationbutton,verifiybutton;
private String   mVerificationId;

private PhoneAuthProvider.ForceResendingToken mResendToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        Phonenumber =findViewById(R.id.phonenumber);
        mauth=FirebaseAuth.getInstance();
                Vericationnumber=findViewById(R.id.verificationphone);
progressDialog=new ProgressDialog(this);
       sendverificationbutton=findViewById(R.id.buttonverification);
               verifiybutton=findViewById(R.id.verify);
               verifiybutton.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Phonenumber.setVisibility(View.INVISIBLE);
                       sendverificationbutton.setVisibility(View.INVISIBLE);
                       String verificationcode=Vericationnumber.getText().toString();
                       if(TextUtils.isEmpty(verificationcode))
                       {
                           Toast.makeText(PhoneLoginActivity.this,"please,enter verification code....",Toast.LENGTH_LONG).show();
                       }
                       else
                       {
                           progressDialog.setTitle(" verification code");
                           progressDialog.setMessage("please wait,we are authentication");
                           progressDialog.setCanceledOnTouchOutside(false);
                           progressDialog.show();
                           PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationcode);
                           signInWithPhoneAuthCredential(credential);
                       }
                   }
               });
               sendverificationbutton.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {

                       String phonenumber=Phonenumber.getText().toString();
                       if(TextUtils.isEmpty(phonenumber))
                       {
                           Toast.makeText(PhoneLoginActivity.this,"enter the phone number....",Toast.LENGTH_LONG).show();
                       }
                       else
                       {
                           progressDialog.setTitle("phone verification");
                           progressDialog.setMessage("please wait,we are authentication");
                           progressDialog.setCanceledOnTouchOutside(false);
                           progressDialog.show();
                            PhoneAuthOptions options =
                                   PhoneAuthOptions.newBuilder(mauth)
                                           .setPhoneNumber(phonenumber)       // Phone number to verify
                                           .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                           .setActivity(PhoneLoginActivity.this)                 // Activity (for callback binding)
                                           .setCallbacks(mcallback)          // OnVerificationStateChangedCallbacks
                                           .build();
                           PhoneAuthProvider.verifyPhoneNumber(options);
                       }
                   }
               });
mcallback=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    @Override
    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    @Override
    public void onVerificationFailed(@NonNull FirebaseException e) {
        progressDialog.dismiss();
Toast.makeText(PhoneLoginActivity.this,"pleases enter phone number correct....",Toast.LENGTH_LONG).show();
        sendverificationbutton.setVisibility(View.VISIBLE);
        Phonenumber.setVisibility(View.VISIBLE);
        Vericationnumber.setVisibility(View.INVISIBLE);
        verifiybutton.setVisibility(View.INVISIBLE);
    }
    public void onCodeSent(@NonNull String verificationId,
                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
progressDialog.dismiss();
        mVerificationId = verificationId;
        mResendToken = token;

        Toast.makeText(PhoneLoginActivity.this,"code has been sent",Toast.LENGTH_LONG).show();
        sendverificationbutton.setVisibility(View.INVISIBLE);
        Phonenumber.setVisibility(View.INVISIBLE);
        Vericationnumber.setVisibility(View.VISIBLE);
        verifiybutton.setVisibility(View.VISIBLE);
        // ...
    }
};

    };


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
              progressDialog.dismiss();
              Toast.makeText(PhoneLoginActivity.this,"congratulation,you are logged sucessfully",Toast.LENGTH_LONG).show();
               sendusertomainactivity();
                        } else {
               Toast.makeText(PhoneLoginActivity.this,"Error"+task.getException().toString(),Toast.LENGTH_LONG).show();
                            }
                        }

                });
    }

    private void sendusertomainactivity() {
        Intent intent=new Intent(PhoneLoginActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
