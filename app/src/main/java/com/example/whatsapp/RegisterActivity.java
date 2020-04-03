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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    Button registerBtn;
    EditText userEmail,userPassword;
    TextView alreadyhaveaccout;

    FirebaseAuth mAuth;
    ProgressDialog loadingBar;
    DatabaseReference rootReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        InitializeFields();

        rootReference= FirebaseDatabase.getInstance().getReference();


        alreadyhaveaccout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLoginrActivity();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });
    }


    private void InitializeFields()
    {
        registerBtn=findViewById(R.id.register_button_id);
        userEmail=findViewById(R.id.registerEmailid);
        userPassword=findViewById(R.id.registerPasswordid);
        alreadyhaveaccout=findViewById(R.id.already_have_accout_id);

        loadingBar=new ProgressDialog(this);
    }
    private void sendUserToLoginrActivity() {
        Intent neddNewAccountInten=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(neddNewAccountInten);
    }
    private void CreateNewAccount() {
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
            loadingBar.setTitle("creating new Account");
            loadingBar.setMessage("please wait, whil we are creating new account for you..");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                String curentuserId=mAuth.getCurrentUser().getUid();
                                rootReference.child("Users").child(curentuserId).setValue("");

                                Toast.makeText(getApplicationContext(),"create account",Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                                Intent mainActivityIntent=new Intent(getApplicationContext(),MainActivity.class);
                                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainActivityIntent);
                                fileList();
                            }else {
                                String message=task.getException().toString();
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }

                        }
                    });
        }

    }
}
