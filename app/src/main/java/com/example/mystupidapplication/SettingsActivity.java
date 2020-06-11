package com.example.mystupidapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;

import me.bendik.simplerangeview.SimpleRangeView;

public class SettingsActivity extends Activity {
    private CheckBox chb0, chb1, chb2, chb3, chb4,chb5, chb6,chb7,chb8, chb9;;
    private Button btn;
    private ArrayList<Integer> array;
    private CharSequence chs;
    Context context;
    private boolean[] arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings_activity);
        arr = new boolean[10];
        for (int i = 0; i < 10; i++) {
            arr[i]=false;
        }
        TextView tv = findViewById(R.id.textView);
        context = getApplicationContext();
        chb0 = findViewById(R.id.checkBox0);
        chb1 = findViewById(R.id.checkBox1);
        chb2 = findViewById(R.id.checkBox2);
        chb3 = findViewById(R.id.checkBox3);
        chb4 = findViewById(R.id.checkBox4);
        chb5 = findViewById(R.id.checkBox5);
        chb6 = findViewById(R.id.checkBox6);
        chb7 = findViewById(R.id.checkBox7);
        chb8 = findViewById(R.id.checkBox8);
        chb9 = findViewById(R.id.checkBox9);
        array = new ArrayList<>();
        tv.setEnabled(false);
        btn = findViewById(R.id.button2);
        btn.setOnClickListener(OnClickListener);
    }
    public View.OnClickListener OnClickListener= new View.OnClickListener(){
        public void onClick(View view) {

            if (chb0.isChecked()){
                arr[0]=true;
            }
            if (chb1.isChecked()){
                arr[1]=true;
            }
            if (chb2.isChecked()){
                arr[2]=true;
            }
            if (chb3.isChecked()){
                arr[3]=true;
            }
            if (chb4.isChecked()){
                arr[4]=true;
            }
            if (chb5.isChecked()){
                arr[5]=true;
            }
            if (chb6.isChecked()) {
                arr[6]=true;
            }
            if (chb6.isChecked()) {
                arr[7]=true;
            }
            if (chb6.isChecked()) {
                arr[8]=true;
            }
            if (chb6.isChecked()) {
                arr[9]=true;
            }
            Intent intent = new Intent(context, MapsActivity.class);
            intent.putExtra("Array", arr);
            startActivity(intent);
        }
    };
}