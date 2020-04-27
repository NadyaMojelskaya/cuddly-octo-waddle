package com.example.mystupidapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class hello extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_photo);
        Intent intent = getIntent();
        Bitmap bm= BitmapFactory.decodeByteArray(
                getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);//intent.getParcelableExtra("BitmapImage");
        ImageView iv = (ImageView) findViewById(R.id.image_iv);
        iv.setImageBitmap(bm);
        TextView tv = (TextView)findViewById(R.id.textViewHello);
        CharSequence cs = intent.getStringExtra("textString");//textView устанавливает текст не со string, а с charSequence
        tv.setText(cs);

    }

}
