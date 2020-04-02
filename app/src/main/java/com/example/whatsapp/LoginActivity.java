package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    FirebaseUser cureentUser;
    Button loginBtn,phoneLoginByn;
    EditText userEmail,userPassword;
    TextView needNewAccount,forgetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitializeFields();
        needNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToregisterActivity();
            }
        });

    }

    private void InitializeFields()
    {
        loginBtn=findViewById(R.id.login_button_id);
        phoneLoginByn=findViewById(R.id.login_with_phone_button_id);
        userEmail=findViewById(R.id.loginEmailid);
        userPassword=findViewById(R.id.loginPasswordid);
        needNewAccount=findViewById(R.id.already_havenot_accout_id);
        forgetPassword=findViewById(R.id.loginForgetPasswordid);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (cureentUser!=null)
        {
            sendUserToLoginActivity();
        }
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(loginIntent);
    }
    private void sendUserToregisterActivity() {
        Intent neddNewAccountInten=new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(neddNewAccountInten);
    }


}
