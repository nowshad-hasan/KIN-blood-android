package com.example.nowshadhasan.databaselab.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.nowshadhasan.databaselab.R;

// This activity is for describing this app.

public class AboutActivity extends AppCompatActivity {

    TextView textViewAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        textViewAbout=(TextView)findViewById(R.id.textViewAbout);

        textViewAbout.setText("We, KIN, A Voluntary Organization of SUST started our journey in 2003. We have " +
                "four wings KIN School, KIN Blood, Charity, Social Awareness and Winter Cloth Distribution." +
                "Now we launched an app for ease of our own blood secretary so that he can easily find " +
                "and manage blood donors. We hope, this app will be a great help.");



    }
}
