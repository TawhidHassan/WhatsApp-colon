package com.example.whatsapp;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ImageViewctivity extends AppCompatActivity {

    ImageView image;
    String imaheUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewctivity);

        image=findViewById(R.id.imageId);
        imaheUrl=getIntent().getStringExtra("url");

        Picasso.get().load(imaheUrl).into(image);


    }
}
