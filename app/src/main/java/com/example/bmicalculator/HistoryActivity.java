package com.example.bmicalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    TextView tvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        tvHistory=(TextView)findViewById(R.id.tvHistory);

        tvHistory.setMovementMethod(new ScrollingMovementMethod());

        String d=WelcomeActivity.db.viewBMI();

        if (d.length()==0)
            tvHistory.setText("NO RECORDS TO SHOW!");
        else
            tvHistory.setText(d);

    }
}
