package com.example.jobinj.pollit;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URISyntaxException;


public class login_activity extends AppCompatActivity {

    String dest_Address = "http://ec2-18-218-47-162.us-east-2.compute.amazonaws.com:3000";
    Socket msocket;
    String password;
    String email;
    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        connect_socket();
        setemitters();
    }
    public void click_login(View view)
    {
        EditText e1 = findViewById(R.id.editText3);
        EditText e2 = findViewById(R.id.editText4);

        email = e1.getText().toString();
        password = e2.getText().toString();

        if(msocket.connected()==true) {
            //get the email and password from the textview
            try {
                POST post = new POST(this, "login");
                post.params.put("password", password);
                post.params.put("email", email);
                post.params.put("sock", msocket.id());
                post.doInBackground();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void click_forgot_pass(View view)
    {
        Intent i = new Intent(login_activity.this,forgot_password.class);
        startActivity(i);
    }
    public void click_create_accnt(View view)
    {
        Intent i = new Intent(login_activity.this,create_account.class);
        startActivity(i);
    }
    public void click_resend_email_verify(View view){
        Intent i = new Intent(login_activity.this,resend_email_verification.class);
        startActivity(i);
    }

    public void connect_socket(){
        try {
           myApplication  = (MyApplication)getApplicationContext();
            msocket = IO.socket(dest_Address);
            myApplication.setSocket(msocket);
            msocket.connect();
        }
        catch (URISyntaxException e){
            //handle error here
        }
    }
    public  void setemitters(){
        msocket.on("loginhandle",loginhandle);
    }

    private Emitter.Listener loginhandle = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try{
                        if(Integer.parseInt(data.getString("message").toString())==0){
                            Toast.makeText(getApplicationContext(), "Wrong password!", Toast.LENGTH_LONG).show();
                        }
                        else if(Integer.parseInt(data.getString("message").toString())==1){
                            Toast.makeText(getApplicationContext(), "user doesnt exist!", Toast.LENGTH_LONG).show();
                        }
                        else if(Integer.parseInt(data.getString("message").toString())==2){
                            myApplication.setToken(data.get("token").toString());
                            EditText e1 = findViewById(R.id.editText3);
                            email = e1.getText().toString();
                            myApplication.setUser(email);
                            Intent i = new Intent(login_activity.this,MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else if (Integer.parseInt(data.getString("message").toString())==3){
                            Toast.makeText(getApplicationContext(), "Account not verified...please check your email!", Toast.LENGTH_LONG).show();

                        }
                    }
                    catch(Exception e){

                    }
                }
            });
        }
    };
}



