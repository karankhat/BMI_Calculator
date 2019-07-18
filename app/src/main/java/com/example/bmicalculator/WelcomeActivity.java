package com.example.bmicalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WelcomeActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    TextView tvWelcome,tvHeight,tvWeight,tvFeet,tvInches,tvLocation,tvTemp;
    Button btnCalculate,btnHistory;
    Spinner spnFeet,spnInches;
    EditText etWeight;
    SharedPreferences sp;
    FloatingActionButton fabCall;

    static DatabaseAct db;

    GoogleApiClient gac;
    Location loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        int o= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setRequestedOrientation(o);

        tvLocation=(TextView)findViewById(R.id.tvLocation);
        tvTemp=(TextView)findViewById(R.id.tvTemp);
        tvWelcome=(TextView)findViewById(R.id.tvWelcome);
        tvHeight=(TextView)findViewById(R.id.tvHeight);
        tvWeight=(TextView)findViewById(R.id.tvWeight);
        tvFeet=(TextView)findViewById(R.id.tvFeet);
        tvInches=(TextView)findViewById(R.id.tvInches);
        btnCalculate=(Button)findViewById(R.id.btnCalculate);
        btnHistory=(Button)findViewById(R.id.btnHistory);
        spnFeet=(Spinner)findViewById(R.id.spnFeet);
        spnInches=(Spinner)findViewById(R.id.spnInches);
        etWeight=(EditText)findViewById(R.id.etWeight);
        fabCall=(FloatingActionButton)findViewById(R.id.fabCall);

        db=new DatabaseAct(this);

        GoogleApiClient.Builder builder=new GoogleApiClient.Builder(this);
        builder.addApi(LocationServices.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        gac=builder.build();

        sp=getSharedPreferences("p1",MODE_PRIVATE);

        final String n=sp.getString("n","");
        tvWelcome.setText("Welcome "+n);

        final int age=sp.getInt("age",0);
        final String p=sp.getString("p","");
        final String sex=sp.getString("sex","");

        final ArrayList<String> ft=new ArrayList<String>();
        ft.add("1");
        ft.add("2");
        ft.add("3");
        ft.add("4");
        ft.add("5");
        ft.add("6");
        ft.add("7");

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ft);

        spnFeet.setAdapter(adapter);

        final ArrayList<String> in=new ArrayList<String>();
        in.add("0");
        in.add("1");
        in.add("2");
        in.add("3");
        in.add("4");
        in.add("5");
        in.add("6");
        in.add("7");
        in.add("8");
        in.add("9");
        in.add("10");
        in.add("11");

        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,in);

        spnInches.setAdapter(adapter1);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(WelcomeActivity.this, sex, Toast.LENGTH_SHORT).show();
                String w = etWeight.getText().toString();
                try
                {
                    int wt = Integer.parseInt(w);


                    if (w.length() == 0 || wt == 0)
                    {
                        etWeight.setError("Enter valid weight!");
                        etWeight.requestFocus();
                        return;
                    }

                    int idf=spnFeet.getSelectedItemPosition();
                    String f=ft.get(idf);

                    int idi=spnInches.getSelectedItemPosition();
                    String i=in.get(idi);

                    int feet=Integer.parseInt(f);
                    int inches=Integer.parseInt(i);

                    int total_inches=(feet*12)+inches;

                    double metres=total_inches*0.0254;

                    double BMI=wt/(metres*metres);
                    DecimalFormat df=new DecimalFormat("#.##");

                    //Toast.makeText(WelcomeActivity.this, "BMI is "+df.format(BMI) , Toast.LENGTH_LONG).show();

                    Intent in=new Intent(WelcomeActivity.this,DisplayActivity.class);
                    Bundle b=new Bundle();
                    b.putDouble("BMI", Double.parseDouble(df.format(BMI)));
                    b.putString("n",n);
                    b.putInt("age",age);
                    b.putString("p",p);
                    b.putString("sex",sex);
                    in.putExtras(b);
                    startActivity(in);
                    finish();

                }
                catch (NumberFormatException n)
                {
                    Toast.makeText(WelcomeActivity.this, "Enter valid weight!", Toast.LENGTH_SHORT).show();
                    etWeight.requestFocus();
                    return;
                }

            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(WelcomeActivity.this,HistoryActivity.class);
                startActivity(i);
            }
        });

        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:"+"8551820983"));
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (gac!=null)
            gac.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (gac!=null)
            gac.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.about)
        {
            Toast.makeText(this, "App developed by Karan", Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId()==R.id.website)
        {
            Intent i=new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("http://"+"easybusinessfinance.net/bmi-height-and-weight-chart/bmi-chart-weight-in-kg-and-height-in-feet/"));
            startActivity(i);
        }

        if (item.getItemId()==R.id.rateUs)
        {
            Intent i=new Intent(WelcomeActivity.this,RateActivity.class);
            startActivity(i);
        }

        if (item.getItemId()==R.id.clrHist)
        {
            AlertDialog.Builder b=new AlertDialog.Builder(this);
            b.setMessage("Clear History?");

            b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    WelcomeActivity.db.deleteAllBMI();
                }
            });

            b.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            AlertDialog a=b.create();
            a.setTitle("Clear?");
            a.show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {

        loc=LocationServices.FusedLocationApi.getLastLocation(gac);

        if (loc!=null)
        {
            double lat=loc.getLatitude();
            double lon=loc.getLongitude();

            Geocoder g=new Geocoder(this, Locale.ENGLISH);

            try
            {
                List<android.location.Address> la=g.getFromLocation(lat,lon,1);
                android.location.Address address=la.get(0);

                String l=address.getLocality();

                tvLocation.setText(l);

                String url="http://api.openweathermap.org/";
                String sp="data/2.5/weather?units=metric";
                String qu="&q="+l;
                String id="e4c4aadb5cf75c2f2bc18a4e2ce8ce76";
                String m=url+sp+qu+"&appid="+id;

                MyTask mt=new MyTask();
                mt.execute(m);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this, "GPS not enabled/Issue in connection!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    class  MyTask extends AsyncTask<String,Void,Double>
    {

        @Override
        protected Double doInBackground(String... strings) {
            double temp=0.0;
            String line="",json="";

            try
            {
                URL url=new URL(strings[0]);
                HttpURLConnection con= (HttpURLConnection) url.openConnection();
                con.connect();

                InputStreamReader isr=new InputStreamReader(con.getInputStream());
                BufferedReader br=new BufferedReader(isr);

                while ((line=br.readLine())!=null)
                {
                    json=json+line+"\n";
                }

                JSONObject o=new JSONObject(json);
                JSONObject p=o.getJSONObject("main");
                temp=p.getDouble("temp");

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return temp;
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);

            tvTemp.setText(""+aDouble);

        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder b=new AlertDialog.Builder(this);
        b.setMessage("Exit BMI Calculator?");
        b.setCancelable(false);

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
