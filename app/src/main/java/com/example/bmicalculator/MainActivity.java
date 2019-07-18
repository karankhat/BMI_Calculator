package com.example.bmicalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView tvPersonal;
    EditText etName,etAge,etPhone;
    Button btnRegister;
    RadioGroup rgSex;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int o= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setRequestedOrientation(o);

        tvPersonal=(TextView) findViewById(R.id.tvPersonal);
        btnRegister=(Button)findViewById(R.id.btnRegister);
        etName=(EditText)findViewById(R.id.etName);
        etAge=(EditText)findViewById(R.id.etAge);
        etPhone=(EditText) findViewById(R.id.etPhone);
        rgSex=(RadioGroup)findViewById(R.id.rgSex);

        sp=getSharedPreferences("p1",MODE_PRIVATE);
        String n=sp.getString("n","");

        if (n.length()!=0)
        {
            Intent i=new Intent(MainActivity.this,WelcomeActivity.class);
            startActivity(i);
            finish();
        }
        else
        {
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int s=rgSex.getCheckedRadioButtonId();
                    RadioButton rbSex=(RadioButton)findViewById(s);
                    final String sex=rbSex.getText().toString();

                    //Toast.makeText(MainActivity.this,sex, Toast.LENGTH_SHORT).show();
                    String n=etName.getText().toString();
                    String a=etAge.getText().toString();
                    int age=Integer.parseInt(a);
                    String p=etPhone.getText().toString();

                    if(n.length()==0)
                    {
                        Toast.makeText(MainActivity.this, "Enter valid name!", Toast.LENGTH_SHORT).show();
                        etName.requestFocus();
                        return;
                    }

                    if (a.length()==0 || age==0)
                    {
                        etAge.setError("Enter valid age!");
                        etAge.requestFocus();
                        return;
                    }

                    if (p.length()!=10)
                    {
                        Snackbar.make(view,"Enter valid phone number!",Snackbar.LENGTH_SHORT).show();
                        //Toast.makeText(MainActivity.this, "Enter valid phone number!", Toast.LENGTH_SHORT).show();
                        etPhone.requestFocus();
                        return;
                    }

                    Toast.makeText(MainActivity.this, "You are now registered!", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString("n",n);
                    editor.putInt("age",age);
                    editor.putString("p",p);
                    editor.putString("sex",sex);
                    editor.commit();

                    Intent i=new Intent(MainActivity.this,WelcomeActivity.class);
                    startActivity(i);
                    finish();


                }
            });




        }


    }
}
