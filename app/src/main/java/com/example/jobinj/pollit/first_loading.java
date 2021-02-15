package com.example.jobinj.pollit;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class  first_loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_loading);

        new CountDownTimer(5000,1000)
        {

            public void onTick(long l)
            {

            }
            public void onFinish()
            {
                Intent i = new Intent(first_loading.this,login_activity.class);
                startActivity(i);
                finish();
            }
        }.start();

    }
}
