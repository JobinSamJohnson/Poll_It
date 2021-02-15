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

public class resend_email_verification extends AppCompatActivity {

    Socket msocket;
    MyApplication myApplication;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resend_email_verification);
        myApplication = (MyApplication)getApplicationContext();
        msocket = myApplication.getSocket();
        msocket.on("resenduserhandle",resendhandle);
    }
    public void click_resend_verification(View view)
    {
        try {
            EditText et1 = findViewById(R.id.editText);
            POST post = new POST(this, "resendverifyemail");
            post.params.put("email", et1.getText().toString());
            post.params.put("sock",msocket.id());
            post.doInBackground();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private Emitter.Listener resendhandle = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try{
                        if(Integer.parseInt(data.getString("message").toString())==0){
                            Toast.makeText(getApplicationContext(), "Account is already verified!", Toast.LENGTH_LONG).show();
                        }
                        else if(Integer.parseInt(data.getString("message").toString())==1){
                            Toast.makeText(getApplicationContext(), "user not found!", Toast.LENGTH_LONG).show();
                        }
                        else if(Integer.parseInt(data.getString("message").toString())==2){
                            Toast.makeText(getApplicationContext(), "Verification email resent!", Toast.LENGTH_LONG).show();

                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    };
}
