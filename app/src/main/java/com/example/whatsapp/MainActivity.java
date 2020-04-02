package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    TabsAccessAdapter tabsAccessAdapter;

    FirebaseUser cureentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.main_page_toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Whats App");

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
        }
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(loginIntent);
    }
}
