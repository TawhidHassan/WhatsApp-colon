package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class SettingsActivity extends AppCompatActivity {

    CircleImageView profileImage;
    EditText userName, userstatus;
    Button updateBtn;

    String currentUserId;
    FirebaseAuth mAuth;
    DatabaseReference rootRef;
    static final int GalleryPic=1;
    Uri image;
    StorageReference UserProfileImageRef;
    ProgressDialog loadingBar;
    String dwonloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Initialfields();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

         UserProfileImageRef= FirebaseStorage.getInstance().getReference().child("Profile Images");


        userName.setVisibility(View.INVISIBLE);
//        userstatus.setVisibility(View.INVISIBLE);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSettings();
            }
        });

        RetriveUserData();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,101);
            }
        });



    }



    private void Initialfields() {
        profileImage = findViewById(R.id.update_profile_image_id);
        userName = findViewById(R.id.update_userName_id);
        userstatus = findViewById(R.id.update_status_id);
        updateBtn = findViewById(R.id.update_setting_btn_id);

        loadingBar=new ProgressDialog(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101)
        {
            if (resultCode==RESULT_OK)
            {
                loadingBar.setTitle("Uploading your image");
                loadingBar.setMessage("please wait, whil we a upload your data..");
                loadingBar.setCanceledOnTouchOutside(true);
                loadingBar.show();
                image=data.getData();
                profileImage.setImageURI(image);

                final StorageReference filepath=UserProfileImageRef.child(currentUserId);

                filepath.putFile(image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(SettingsActivity.this, "Profile pic uploaded", Toast.LENGTH_SHORT).show();

                            //get the download url
                            filepath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful())
                                    {
                                        dwonloadUrl=task.getResult().toString();

                                    }else
                                    {
                                        Toast.makeText(getApplicationContext(),"Some thing is wrong",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                            //get the url and set the url into realtime database users model
                            rootRef.child("Users").child(currentUserId).child("image")
                                    .setValue(dwonloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(SettingsActivity.this, "iamge save in database", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }else
                                    {
                                        String message = task.getException().toString();
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });

                        }else {
                            String message = task.getException().toString();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();

                        }
                    }
                });
            }
        }


    }



    private void sendUserToMainActivity() {
        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivityIntent);
        fileList();
    }


    private void updateSettings() {
        String setUserName = userName.getText().toString();
        String setUserStatusr = userstatus.getText().toString();

        if (TextUtils.isEmpty(setUserName)) {
            Toast.makeText(getApplicationContext(), "Please Enter Your Name", Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(setUserStatusr)) {
            Toast.makeText(getApplicationContext(), "Please Enter Your Status", Toast.LENGTH_LONG).show();
        } else {
            HashMap<String, String> profileMap = new HashMap<>();
            profileMap.put("uid", currentUserId);
            profileMap.put("name", setUserName);
            profileMap.put("status", setUserStatusr);
            rootRef.child("Users").child(currentUserId).setValue(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                sendUserToMainActivity();
                                Toast.makeText(getApplicationContext(), "Profile Update Successfully", Toast.LENGTH_LONG).show();
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                            }
                        }
                    });

        }
    }

    private void RetriveUserData() {

        rootRef.child("Users").child(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image"))))
                        {
                            String retriveUserName=dataSnapshot.child("name").getValue().toString();
                            String retriveUserStatus=dataSnapshot.child("status").getValue().toString();
                            String retriveUserimages=dataSnapshot.child("image").getValue().toString();

                            userName.setText(retriveUserName);
                            userstatus.setText(retriveUserStatus);

                            Glide.with(SettingsActivity.this)
                                    .load(retriveUserimages)
                                    .centerCrop()
                                    .placeholder(R.drawable.image)
                                    .into(profileImage);

                        }else if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")))
                        {
                            String retriveUserName=dataSnapshot.child("name").getValue().toString();
                            String retriveUserStatus=dataSnapshot.child("status").getValue().toString();
                            userName.setText(retriveUserName);
                            userstatus.setText(retriveUserStatus);
                        }else
                        {
                            userName.setVisibility(View.VISIBLE);
                            Toast.makeText(SettingsActivity.this, "Please set & update your profile infomation", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


}