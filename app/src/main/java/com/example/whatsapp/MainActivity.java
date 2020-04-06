package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    TabsAccessAdapter tabsAccessAdapter;

    FirebaseUser cureentUser;
    FirebaseAuth mAuth;
    DatabaseReference rootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.main_page_toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Whats App");

        mAuth= FirebaseAuth.getInstance();
        cureentUser=mAuth.getCurrentUser();
        rootRef= FirebaseDatabase.getInstance().getReference();

        viewPager=findViewById(R.id.main_viewPager_id);
        tabLayout=findViewById(R.id.main_tab_id);

        tabsAccessAdapter=new TabsAccessAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsAccessAdapter);

        tabLayout.setupWithViewPager(viewPager);


    }


    @Override
    protected void onStart() {
        super.onStart();
        if (cureentUser==null)
        {
            sendUserToLoginActivity();
        }else
        {
            VerifyUserExistance();
        }
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    private void sendUserToSettingActivity() {
        Intent settingIntent=new Intent(MainActivity.this,SettingsActivity.class);
        settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingIntent);
        finish();
    }

    private void VerifyUserExistance()
    {
        String currentUserId=mAuth.getCurrentUser().getUid();

        rootRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").exists())
                {
                    Toast.makeText(getApplicationContext(),"wellcoem",Toast.LENGTH_LONG).show();
                }else
                {
                    sendUserToSettingActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                sendUserToSettingActivity();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.option_menu,menu);
         return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);
         if (item.getItemId()==R.id.logOut_option_id)
         {
                mAuth.signOut();
             sendUserToLoginActivity();
         }
        if (item.getItemId()==R.id.main_find_friend_option_id)
        {

        }
        if (item.getItemId()==R.id.settings_option_id)
        {
            sendUserToSettingActivity();
        }
        return true;
    }
}
