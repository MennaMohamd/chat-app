package com.example.cchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {
    private Button updateaccountsetting;
    private EditText username, userstatus;
    private CircleImageView userprofileimage;
    private String currentuserId;
    private FirebaseAuth mauth;
    private static int galler = 1;
    String usernamee;
    private StorageReference userprofileimageref;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mauth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
        currentuserId = mauth.getCurrentUser().getUid();
        userprofileimageref = FirebaseStorage.getInstance().getReference().child("Profile Images");
        Initialization();
        username.setVisibility(View.VISIBLE);
        updateaccountsetting = findViewById(R.id.update);
        retrieveuserinformation();
        userprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryintent = new Intent();
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, galler);

            }
        });
        updateaccountsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatesetting();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == galler && requestCode == RESULT_OK && data != null) {
            Uri imageuri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (requestCode == RESULT_OK) {
                Uri resulturi = result.getUri();
                StorageReference filepath = userprofileimageref.child(currentuserId + ".jpg");
                filepath.putFile(resulturi).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingActivity.this, "profile image uploading", Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(SettingActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }

    private void updatesetting() {
        usernamee = username.getText().toString();
        String userstatuss = userstatus.getText().toString();
        if (TextUtils.isEmpty(usernamee)) {
            Toast.makeText(SettingActivity.this, "please,enter username....", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(userstatuss)) {
            Toast.makeText(SettingActivity.this, "please,enter userstatus....", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, String> profilemap = new HashMap<>();
            profilemap.put("uid", currentuserId);
            profilemap.put("name", usernamee);
            profilemap.put("status", userstatuss);
            username.setVisibility(View.VISIBLE);
            ref.child("Users").child(currentuserId).setValue(profilemap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent mainintent = new Intent(SettingActivity.this, MainActivity.class);
                                mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainintent);
                                finish();

                                Toast.makeText(SettingActivity.this, "profile updated sucessfull...", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(SettingActivity.this, "Error" + task.getException().toString(), Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        }


    }

    private void sendusertomainactivity() {
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    private void retrieveuserinformation() {
        ref.child("Users").child(currentuserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //if exist userid in database , user create profile or not
                        if ((snapshot.exists() && snapshot.hasChild("name")) && (snapshot.hasChild("image"))) {
                            String retriveusrname = snapshot.child("name").getValue().toString();
                            String retrivestatus = snapshot.child("status").getValue().toString();
                            String retriveimage = snapshot.child("image").getValue().toString();
                            username.setText(retriveusrname);
                            userstatus.setText(retrivestatus);
                        } else if ((snapshot.exists() && snapshot.hasChild("name"))) {
                            String retriveusrname = snapshot.child("name").getValue().toString();
                            String retrivestatus = snapshot.child("status").getValue().toString();

                            username.setText(retriveusrname);
                            userstatus.setText(retrivestatus);
                        } else {
                            Toast.makeText(SettingActivity.this, "please se and update profile information...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void Initialization() {
        updateaccountsetting = findViewById(R.id.update);
        userprofileimage = findViewById(R.id.profile_image);
        username = findViewById(R.id.set_username);
        userstatus = findViewById(R.id.update_setting);
    }
}