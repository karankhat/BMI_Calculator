package com.example.bmicalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class DisplayActivity extends AppCompatActivity {

    TextView tvBMI,tvUnderweight,tvNormal,tvOverweight,tvObese,tvStatus;
    Button btnBack,btnShare,btnSave;
    TextToSpeech tts;
    FloatingActionButton fabTts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        int o= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setRequestedOrientation(o);

        tvBMI=(TextView)findViewById(R.id.tvBMI);
        tvUnderweight=(TextView)findViewById(R.id.tvUnderweight);
        tvNormal=(TextView)findViewById(R.id.tvNormal);
        tvOverweight=(TextView) findViewById(R.id.tvOverweight);
        tvObese=(TextView)findViewById(R.id.tvObese);
        tvStatus=(TextView)findViewById(R.id.tvStatus);
        btnBack=(Button)findViewById(R.id.btnBack);
        btnShare=(Button)findViewById(R.id.btnShare);
        btnSave=(Button)findViewById(R.id.btnSave);
        fabTts=(FloatingActionButton)findViewById(R.id.fabTts);

        tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage(Locale.ENGLISH);
            }
        });

        tvUnderweight.setText("BMI < 18.5: Underweight");
        tvNormal.setText("18.5 < BMI < 25: Normal");
        tvOverweight.setText("25 < BMI < 30: Overweight");
        tvObese.setText("BMI > 30: Obese");

        final Intent in=getIntent();
        Bundle b=in.getExtras();
        final double BMI=b.getDouble("BMI");
        final String n=b.getString("n","");
        final int age=b.getInt("age",0);
        final String p=b.getString("p","");
        final String sex=b.getString("sex","");

        //double BMI=in.getDoubleExtra("BMI",0.0);

        final String st;

        if(BMI<18.5)
        {
            st="Underweight";
            tvUnderweight.setTextColor(Color.parseColor("#0000FF"));
        }
        else if (18.5<BMI && BMI<25)
        {
            st="Normal";
            tvNormal.setTextColor(Color.parseColor("#0000FF"));
        }
        else if (25<BMI && BMI<30)
        {
            st="Overweigtht";
            tvOverweight.setTextColor(Color.parseColor("#0000FF"));
        }
        else
        {
            st="Obese";
            tvObese.setTextColor(Color.parseColor("#0000FF"));
        }

        tvBMI.setText("Your BMI is "+BMI);
        tvStatus.setText("You are "+st);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DisplayActivity.this,WelcomeActivity.class);
                startActivity(i);
                finish();
            }
        });

        final String finalSt = st;
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent in=getIntent();
                //Bundle b=in.getExtras();
                //double BMI=b.getDouble("BMI");

                String info="Name: "+n+"\nAge: "+age+"\nPhone: "+p+"\nSex: "+sex+"\nBMI: "+BMI+"\nYou are "+ st;

                Intent i=new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,info);
                startActivity(i);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WelcomeActivity.db.addBMI(BMI);
            }
        });

        fabTts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak("Your B M I is "+BMI+" and You are "+st,TextToSpeech.QUEUE_FLUSH,null);
            }
        });

    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder b=new AlertDialog.Builder(this);
        b.setMessage("Exit BMI Calculator?");

        b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        b.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog a=b.create();
        a.setTitle("Quit?");
        a.show();
    }


}
