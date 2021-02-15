package com.example.jobinj.pollit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;

public class check_poll1 extends AppCompatActivity {

    int numpoll;
    String question;
    Socket msocket;
    String password;
    String email;
    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_poll_view_results);
        numpoll = getIntent().getIntExtra("num",0);
        myApplication = ((MyApplication)this.getApplication());
        msocket = myApplication.getSocket();
        msocket.on("displayresultshandle",drh);
        onopen();
    }

    public void onopen(){
        POST post = new POST(this,"displayresults");
        post.params.put("sock",msocket.id());
        post.params.put("num1",Integer.toString(numpoll));
        post.doInBackground();
    }

    public void click_done(View view)
    {
        Intent i = new Intent(check_poll1.this,MainActivity.class);
        startActivity(i);
        finish();
    }

    private Emitter.Listener drh = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final JSONArray data = (JSONArray) args[0];
                    try{
                        question = data.getString(0);
                        numpoll = data.getInt(2);
                        String str;
                        str ="";
                        str += question +"\n";
                        int c=3,d=4;
                        for(int i=0;i<data.getInt(1);i++){
                            str+= data.getJSONObject(i+c).getString("key1") +"-->\t" + data.getJSONObject(i+d).getInt("key1") +"\n";
                            c++;
                            d++;
                        }
                        TextView t = findViewById(R.id.textView2);
                        t.setText(str);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    };
}
