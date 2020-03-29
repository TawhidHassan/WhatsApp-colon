package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    TabsAccessAdapter tabsAccessAdapter;
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
}
