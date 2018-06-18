package com.example.nowshadhasan.databaselab.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.nowshadhasan.databaselab.R;

public class PrivacyStatementActivity extends AppCompatActivity {

    TextView textViewPrivacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_statement);

        textViewPrivacy=(TextView)findViewById(R.id.textViewPrivacy);

        textViewPrivacy.setText("This product is not for mass use. It is only available for KIN Officials and" +
                "KIN  Blood secretary. If anyone find this app among others who doesn't deserve to use this, Please let us " +
                "know. Inform us at KIN Blood- 01615467878 and KIN Office- 01975467878. ");
    }
}
