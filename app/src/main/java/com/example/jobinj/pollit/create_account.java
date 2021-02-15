package com.example.jobinj.pollit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

public class create_account extends AppCompatActivity {

    Socket msocket;

    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        myApplication = ((MyApplication)this.getApplication());
        msocket = myApplication.getSocket();
        msocket.on("signinhandle",signinhandle);
    }
    public void click_create_accnt(View view)
    {
        //get the username,email and password from textview
        String email;
        String username;
        String password;
        String password_confirm;

        EditText e1 = findViewById(R.id.editText7);
        EditText e2 = findViewById(R.id.editText8);
        EditText e3 = findViewById(R.id.editText9);
        EditText e4 = findViewById(R.id.editText10);

        email = e1.getText().toString();
        username = e2.getText().toString();
        password = e3.getText().toString();
        password_confirm = e4.getText().toString();

            if(password.equals(password_confirm)==true){

                try {
                    POST post = new POST(this, "signup");
                    post.params.put("username", username);
                    post.params.put("password", password);
                    post.params.put("email", email);
                    post.params.put("sock", msocket.id());
                    post.doInBackground();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            else
            {
                Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
            }



    }

    private Emitter.Listener signinhandle = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject)args[0];
                    try{
                        if(Integer.parseInt(data.getString("message"))==0){
                            Toast.makeText(getApplicationContext(), "Username already exists!", Toast.LENGTH_LONG).show();
                        }
                        else if(Integer.parseInt(data.getString("message"))==1){
                            Toast.makeText(getApplicationContext(), "Email id already exists!", Toast.LENGTH_LONG).show();
                        }
                        else if(Integer.parseInt(data.getString("message"))==2){
                            Toast.makeText(getApplicationContext(), "Account created!Check for a verification email!", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(create_account.this,login_activity.class);
                            startActivity(i);
                            //sign in is successfull!
                        }
                    }
                    catch(Exception e){

                    }
                }
            });
        }
    };
}