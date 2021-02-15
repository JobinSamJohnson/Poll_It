package com.example.jobinj.pollit;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

public class forgot_password extends AppCompatActivity {

    String email;
    Socket msocket;
    String str = "changepass";
    MyApplication myApplication;
    int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        myApplication = ((MyApplication)this.getApplication());
        msocket = myApplication.getSocket();
        msocket.on("handlechangepass",newhandle);
    }

    public void changepass(View view){
        EditText et1 = findViewById(R.id.editText5);
        email = et1.getText().toString();
        POST post = new POST(this,str);
        post.params.put("email",email);
        post.params.put("sock",msocket.id());
        post.doInBackground();
    }

    private Emitter.Listener newhandle = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = (JSONObject) args[0];
                        if (Integer.parseInt(data.getString("message")) == 0) {
                            Toast.makeText(getApplicationContext(), "That email id doesnt exist in our database!", Toast.LENGTH_LONG).show();
                        }
                        if (Integer.parseInt(data.getString("message")) == 1) {
                            Toast.makeText(getApplicationContext(), "Account not verified!", Toast.LENGTH_LONG).show();
                        }
                        if (Integer.parseInt(data.getString("message")) == 2) {
                            Toast.makeText(getApplicationContext(), "Password reset link sent to your mail!", Toast.LENGTH_LONG).show();
                            new CountDownTimer(10000, 1000) {
                                Button b1 = findViewById(R.id.button10);

                                public void onTick(long l) {
                                    b1.setEnabled(false);
                                }

                                public void onFinish() {
                                    b1.setEnabled(true);
                                }
                            }.start();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

        });
    }
};
}
