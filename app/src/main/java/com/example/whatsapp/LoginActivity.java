package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class LoginActivity extends AppCompatActivity {


    Button loginBtn,phoneLoginByn;
    EditText userEmail,userPassword;
    TextView needNewAccount,forgetPassword;

    FirebaseAuth mAuth;
    ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();

        InitializeFields();
        needNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToregisterActivity();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserLogin();
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

        loadingBar=new ProgressDialog(this);
    }






    private void AllowUserLogin()
    {
        String email=userEmail.getText().toString();
        String password=userPassword.getText().toString();
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(getApplicationContext(),"please enter your email",Toast.LENGTH_LONG).show();
        }if (TextUtils.isEmpty(password))
    {
        Toast.makeText(getApplicationContext(),"please enter your password",Toast.LENGTH_LONG).show();
    }else
    {
        loadingBar.setTitle("login your Account");
        loadingBar.setMessage("please wait, whil we are login your account..");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Intent mainActivityIntent=new Intent(getApplicationContext(),MainActivity.class);
                            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainActivityIntent);
                            fileList();
                            Toast.makeText(getApplicationContext(),"Successfully login",Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();

                        }else {
                            String message=task.getException().toString();
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                        }
                    }
                });
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
    private void sendUserToMainActivity() {

    }


}
