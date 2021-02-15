package com.example.jobinj.pollit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

public class check_poll extends AppCompatActivity {

    int numpoll;
    String question;
    Socket msocket;
    String password;
    String email;
    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_poll);
        myApplication = ((MyApplication)this.getApplication());
        msocket = myApplication.getSocket();
        msocket.on("displaypollhandle",dph);

        LinearLayout L = findViewById(R.id.linear_layout);
        checkstuff();
    }

    void checkstuff(){
        POST post = new POST(this,"getpolls");
        post.params.put("sock",msocket.id());
        post.params.put("token",myApplication.getToken());
        post.doInBackground();
    }

    public void onopen(int num){
        Intent i = new Intent(this,check_poll1.class);
        i.putExtra("num",num);
        startActivity(i);
        finish();
    }


    private Emitter.Listener dph = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try{
                        JSONArray data = (JSONArray) args[0];
                        LinearLayout L = findViewById(R.id.linear_layout);
                        for(int i=0;i<data.length();i++) {

                            Button b1 = new Button(getApplicationContext());
                          b1.setId(data.getJSONObject(i).getInt("poll_no"));
                            LinearLayout.LayoutParams l3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            b1.setLayoutParams(l3);
                           b1.setText(data.getJSONObject(i).getString("poll_name"));
                           b1.setTextSize(16);
                            L.addView(b1);
                            b1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int id = v.getId();
                                    onopen(id);
                                }
                            });

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
