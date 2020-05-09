package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    TabsAccessAdapter tabsAccessAdapter;


    private String currentUserID;
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

        currentUserID = mAuth.getCurrentUser().getUid();
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
       FirebaseUser cureentUser=mAuth.getCurrentUser();
        if (cureentUser==null)
        {
            sendUserToLoginActivity();
        }else
        {
            updateUserStatus("online");
            VerifyUserExistance();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser cureentUser=mAuth.getCurrentUser();
        if (cureentUser != null)
        {
            updateUserStatus("offline");
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        FirebaseUser cureentUser=mAuth.getCurrentUser();

        if (cureentUser != null)
        {
            updateUserStatus("offline");
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
        startActivity(settingIntent);

    }
    private void sendUserToFindFriendActivity() {
        Intent sendUserToFindFriendActivity=new Intent(MainActivity.this,FindFriendsActivity.class);
        startActivity(sendUserToFindFriendActivity);

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
             updateUserStatus("offline");
                mAuth.signOut();
             sendUserToLoginActivity();
         }
        if (item.getItemId()==R.id.main_find_friend_option_id)
        {
            sendUserToFindFriendActivity();
        }
        if (item.getItemId()==R.id.settings_option_id)
        {
            sendUserToSettingActivity();
        }if (item.getItemId()==R.id.main_cretae_group_option_id)
        {
            RequestNewGroup();
        }
        return true;
    }

    @SuppressLint("ResourceAsColor")
    private void RequestNewGroup() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);
        builder.setTitle("Enter Group Name:");

        final EditText groupNameField=new EditText(MainActivity.this);
        groupNameField.setHint("Example: CSE");
        groupNameField.setHintTextColor(R.color.colorPrimary);
        groupNameField.setTextColor(R.color.colorPrimary);
        builder.setView(groupNameField);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName=groupNameField.getText().toString();
                
                if (TextUtils.isEmpty(groupName))
                {
                    Toast.makeText(MainActivity.this, "Please Write Group Name", Toast.LENGTH_SHORT).show();
                }else
                {
                    CreateNewGroup(groupName);
                }

            }
        });

        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void CreateNewGroup(final String groupName) {

        rootRef.child("Groups").child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), groupName+"->Group Is Create successfully...", Toast.LENGTH_LONG).show();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }



    private void updateUserStatus(String state)
    {
        String saveCurrentTime, saveCurrentDate;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        HashMap<String, Object> onlineStateMap = new HashMap<>();
        onlineStateMap.put("time", saveCurrentTime);
        onlineStateMap.put("date", saveCurrentDate);
        onlineStateMap.put("state", state);

        rootRef.child("Users").child(currentUserID).child("userState")
                .updateChildren(onlineStateMap);

    }
}
