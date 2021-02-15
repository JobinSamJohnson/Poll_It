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

public class join_poll extends AppCompatActivity {

    Socket msocket;
    String password;
    String email;
    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_poll);
        myApplication = ((MyApplication)this.getApplication());
        msocket = myApplication.getSocket();
        msocket.on("joinpollhandle",jph);
    }

    public void click_join_poll(View view)
    {
      try{
          POST post = new POST(this,"joinpoll");
          EditText et = findViewById(R.id.editText6);
          post.params.put("phrase",et.getText().toString());
          post.params.put("sock",msocket.id());
          post.params.put("token",myApplication.getToken());
          post.doInBackground();
      }
      catch(Exception e){
          e.printStackTrace();
      }
    }

    private Emitter.Listener jph = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject)args[0];
                    try{
                        if(Integer.parseInt(data.getString("message"))==0){
                            Toast.makeText(getApplicationContext(), "No such poll exists!", Toast.LENGTH_LONG).show();
                        }
                        else if(Integer.parseInt(data.getString("message"))==3){
                            Toast.makeText(getApplicationContext(), "You already voted for that poll!", Toast.LENGTH_LONG).show();
                        }
                        else if(Integer.parseInt(data.getString("message"))==2){
                            Toast.makeText(getApplicationContext(), "sending poll details!", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(join_poll.this,join_poll_view_poll.class);
                            i.putExtra("numpoll",Integer.parseInt(data.getString("numpoll")));
                            startActivity(i);
                            finish();
                            //sign in is successfull!
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
