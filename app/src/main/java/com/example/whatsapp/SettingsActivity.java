package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    CircleImageView profileImage;
    EditText userName,userstatus;
    Button updateBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Initialfields();
    }

    private void Initialfields()
    {
        profileImage=findViewById(R.id.update_profile_image_id);
        userName=findViewById(R.id.update_userName_id);
        userstatus=findViewById(R.id.update_status_id);
        updateBtn=findViewById(R.id.update_setting_btn_id);
    }
}
