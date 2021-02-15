package com.example.jobinj.pollit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
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

public class join_poll_view_poll extends AppCompatActivity {

    int numpoll;
    String question;
    Socket msocket;
    String password;
    String email;
    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_poll_view_poll);
        numpoll = getIntent().getIntExtra("numpoll",0);
        myApplication = ((MyApplication)this.getApplication());
        msocket = myApplication.getSocket();
        msocket.on("joinpollhandle1",jphh);
        msocket.on("votepollhandle",new1);
        showoptions();

        LinearLayout L = findViewById(R.id.linear_layout);

        //question box
        TextView t1 = new TextView(this);
        t1.setId(R.id.text_question);
        LinearLayout.LayoutParams l1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,60*(int)(getResources().getDisplayMetrics().density));
        t1.setLayoutParams(l1);
        t1.setTextSize(30);
        L.addView(t1);




           /* @Override
            public void onClick(View v) {
                Intent i = new Intent(join_poll_view_poll.this,join_poll_view_results.class);
                startActivity(i);

*/
    }

    public void getresults(int id,int pollnum){
        POST post = new POST(this,"votepoll");
        post.params.put("optionnumber",Integer.toString(id+1));
        post.params.put("pollnumber",Integer.toString(pollnum));
        post.params.put("token",myApplication.token);
        post.params.put("sock",msocket.id());
        post.doInBackground();
    }

    public void showoptions(){
        try{
            POST post = new POST(this,"displayjoin");
            post.params.put("num1",Integer.toString(numpoll));
            post.params.put("sock",msocket.id());
            post.doInBackground();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private Emitter.Listener jphh = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final JSONArray data = (JSONArray) args[0];
                    try{
                            question = data.getString(0);
                            TextView tv1 = findViewById(R.id.text_question);
                            tv1.setText(question);
                            numpoll = data.getInt(2);
                            for(int i=0;i<data.getInt(1);i++){
                                //option button
                                Button b1 = new Button(getApplicationContext());
                                b1.setId(i);
                                LinearLayout.LayoutParams l3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 60 * (int) (getResources().getDisplayMetrics().density));
                                b1.setLayoutParams(l3);
                                b1.setText(data.getJSONObject(i+3).getString("key1"));
                                LinearLayout L = findViewById(R.id.linear_layout);
                                L.addView(b1);
                                b1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int id = v.getId();
                                        getresults(id,numpoll);
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

    private Emitter.Listener new1 = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject)args[0];
                    try{
                        //0-not authenticated
                        if(Integer.parseInt(data.getString("message"))==0){
                            Toast.makeText(getApplicationContext(), "Token authentication failed!", Toast.LENGTH_LONG).show();
                        }
                        //1-internal error
                        else if(Integer.parseInt(data.getString("message"))==1){
                            Toast.makeText(getApplicationContext(), "Internal error!", Toast.LENGTH_LONG).show();
                        }
                        //2-success
                        else{
                            Intent i = new Intent(join_poll_view_poll.this,join_poll_view_results.class);
                            i.putExtra("num",numpoll);
                            startActivity(i);
                            finish();
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
