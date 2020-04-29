package com.example.mystupidapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;


public class hello extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //final Context context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_photo);
        Intent intent = getIntent();
        final Bitmap bm= BitmapFactory.decodeByteArray(
                getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);//intent.getParcelableExtra("BitmapImage");
        ImageView iv = (ImageView) findViewById(R.id.image_iv);
        iv.setImageBitmap(bm);
        ScrollView sv = (ScrollView) findViewById(R.id.scroll);
        CharSequence cs = intent.getStringExtra("textString");//textView устанавливает текст не со string, а с charSequence
        TextView tv = new TextView(this);
        tv.setText(cs);
        tv.setTextSize(17);
        if (cs.length()<30){
            tv.setGravity(Gravity.CENTER);
        }
        sv.addView(tv);

    }
    public void clickEvent(View v)
    {
        Intent intent1 = getIntent();
        byte[] by =  getIntent().getByteArrayExtra("byteArray");
        Log.d("== My activity ===","OnClick is called");
        Intent intent2 = new Intent(this, image.class);
        intent2.putExtra("byteArray", by);
        startActivity(intent2);
    }
}
