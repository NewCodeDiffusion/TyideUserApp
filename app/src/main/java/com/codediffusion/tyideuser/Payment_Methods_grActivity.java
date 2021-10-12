package com.codediffusion.tyideuser;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class Payment_Methods_grActivity  extends AppCompatActivity implements View.OnClickListener {

    RadioButton radio, radio1;

    ActionBar actionBar;
    TextView amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment__methods_gr);
        actionBar = getSupportActionBar();
        actionBar.hide();

        //radio button onclick code
        radio = findViewById(R.id.radio);
        radio1 = findViewById(R.id.radio1);
        amt=findViewById(R.id.payamount);

        amt.setText(getString(R.string.Rs)+" "+"Pay 2650");

        amt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Payment_Methods_grActivity.this,BookingConfirmed.class));
            }
        });

        radio.setOnClickListener(this);
        radio1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radio:
                radio.setChecked(true);
                radio1.setChecked(false);
                break;
            case R.id.radio1:
                radio.setChecked(false);
                radio1.setChecked(true);
                break;
        }
    }
}