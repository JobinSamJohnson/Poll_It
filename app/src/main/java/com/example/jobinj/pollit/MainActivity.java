package com.example.jobinj.pollit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click_check(View view)
    {
        Intent i = new Intent(MainActivity.this,check_poll.class);
        startActivity(i);
    }

    public void click_create(View view)
    {
        Intent i = new Intent(MainActivity.this,create_poll.class);
        startActivity(i);
    }

    public void click_join(View view)
    {
        Intent i = new Intent(MainActivity.this,join_poll.class);
        startActivity(i);
    }
}
