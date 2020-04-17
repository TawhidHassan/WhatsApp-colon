package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivity extends AppCompatActivity {


    Toolbar mToolbar;
    ImageButton sendMessageButton;
    EditText userMessageInput;
    ScrollView scrollView;
    TextView displayTextMessage;

    FirebaseAuth mAuth;
    DatabaseReference userReference,groupNameReference,groupMessageKeyRef;

    String currentGroupName,currentUserID,currentUserName,currentDate,currentTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);


        currentGroupName=getIntent().getExtras().get("groupName").toString();
        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        userReference= FirebaseDatabase.getInstance().getReference().child("Users");
        groupNameReference=FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName);

        InitialField();

        getUserInfo();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMassegeInfotoDatabase();
                userMessageInput.setText("");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        groupNameReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists())
                {
                    DisplayMessage(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists())
                {
                    DisplayMessage(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void InitialField() {
        mToolbar=findViewById(R.id.group_chat_bar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(currentGroupName);
        sendMessageButton=findViewById(R.id.send_message_button);
        userMessageInput=findViewById(R.id.input_group_message);
        scrollView=findViewById(R.id.my_scroll_view);
        displayTextMessage=findViewById(R.id.group_chat_text_dispaly);
    }

    private void getUserInfo() {
        userReference.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        currentUserName=dataSnapshot.child("name").getValue().toString();
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveMassegeInfotoDatabase() {
        String message=userMessageInput.getText().toString();
        String messageKEY=groupNameReference.push().getKey();
        if (TextUtils.isEmpty(message))
        {
            userMessageInput.setError("please write your message");
        }else{
            Calendar calendar=Calendar.getInstance();
            SimpleDateFormat currentDateFormate=new SimpleDateFormat("MMM dd,yyyy");
            currentDate=currentDateFormate.format(calendar.getTime());

            Calendar calendarTime=Calendar.getInstance();
            SimpleDateFormat currentTimeFormate=new SimpleDateFormat("hh:mm a");
            currentTime=currentTimeFormate.format(calendarTime.getTime());

            HashMap<String,Object>groupMessageKey=new HashMap<>();
            groupNameReference.updateChildren(groupMessageKey);

            groupMessageKeyRef=groupNameReference.child(messageKEY);
            HashMap<String ,Object>messageInfoMap=new HashMap<>();
                messageInfoMap.put("name",currentUserName);
                messageInfoMap.put("message",message);
                messageInfoMap.put("date",currentDate);
                messageInfoMap.put("time",currentTime);
            groupMessageKeyRef.updateChildren(messageInfoMap);
        }
    }

    private void DisplayMessage(DataSnapshot dataSnapshot) {

        Iterator iterator=dataSnapshot.getChildren().iterator();

        while (iterator.hasNext())
        {
            String chatDate=(String)((DataSnapshot)iterator.next()).getValue();
            String chatMessage=(String)((DataSnapshot)iterator.next()).getValue();
            String chatName=(String)((DataSnapshot)iterator.next()).getValue();
            String chatTime=(String)((DataSnapshot)iterator.next()).getValue();

            displayTextMessage.append(chatName+":\n"+chatMessage+"\n"+chatTime+" "+chatDate+"\n\n\n");
        }
    }
}
