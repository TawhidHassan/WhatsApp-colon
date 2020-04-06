package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    CircleImageView profileImage;
    EditText userName,userstatus;
    Button updateBtn;

    String currentUserId;
    FirebaseAuth mAuth;
    DatabaseReference rootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Initialfields();
        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        rootRef=FirebaseDatabase.getInstance().getReference();


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSettings();
            }
        });
    }

    private void Initialfields()
    {
        profileImage=findViewById(R.id.update_profile_image_id);
        userName=findViewById(R.id.update_userName_id);
        userstatus=findViewById(R.id.update_status_id);
        updateBtn=findViewById(R.id.update_setting_btn_id);
    }

    private void updateSettings()
    {
        String setUserName=userName.getText().toString();
        String setUserStatusr=userstatus.getText().toString();

        if (TextUtils.isEmpty(setUserName))
        {
            Toast.makeText(getApplicationContext(),"Please Enter Your Name",Toast.LENGTH_LONG).show();
        }if (TextUtils.isEmpty(setUserStatusr))
        {
            Toast.makeText(getApplicationContext(),"Please Enter Your Status",Toast.LENGTH_LONG).show();
        }
        else{
            HashMap<String ,String >profileMap=new HashMap<>();
                profileMap.put("uid",currentUserId);
                profileMap.put("name",setUserName);
                profileMap.put("status",setUserStatusr);
            rootRef.child("Users").child(currentUserId).setValue(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Profile Update Successfully",Toast.LENGTH_LONG).show();
                            }else {
                                String message=task.getException().toString();
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

                            }
                        }
                    });

        }
    }
    private void sendUserToMainActivity() {
        Intent mainActivityIntent=new Intent(getApplicationContext(),MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivityIntent);
        fileList();
    }
}
