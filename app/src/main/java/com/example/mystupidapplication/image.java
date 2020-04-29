package com.example.mystupidapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class image extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_layout);
        Intent intent = getIntent();
        Bitmap bm = BitmapFactory.decodeByteArray(
                getIntent().getByteArrayExtra("byteArray"), 0, getIntent().getByteArrayExtra("byteArray").length);//intent.getParcelableExtra("BitmapImage");
        ImageView iv = (ImageView) findViewById(R.id.full_image);
        iv.setImageBitmap(bm);
    }
}
