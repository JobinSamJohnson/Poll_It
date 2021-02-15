package com.example.jobinj.pollit;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;


public class create_poll extends AppCompatActivity {

    String email;
    String username;
    String password;
    Socket msocket;
    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);
        myApplication = ((MyApplication)this.getApplication());
        msocket = myApplication.getSocket();
        msocket.on("createpollhandle",ch);

        LinearLayout L = findViewById(R.id.linear_layout);
        //name of the poll
        EditText e4 = new EditText(this);
        e4.setId(R.id.edit_text_pollname);
        LinearLayout.LayoutParams l6 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        e4.setLayoutParams(l6);
        e4.setEms(40);
        e4.setHint("Enter the name of the poll");
        e4.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        L.addView(e4);

        //question box
        final EditText e1 = new EditText(this);
        e1.setId(R.id.edit_text_question);
        LinearLayout.LayoutParams l1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        e1.setLayoutParams(l1);
        e1.setEms(40);
        e1.setHint(R.string._type_your_question_here_);
        e1.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        L.addView(e1);

        TextView t = new TextView(this);
        LinearLayout.LayoutParams l7 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        t.setLayoutParams(l7);
        t.setText("Press '+' to add an option");
        t.setTextSize(16);
        L.addView(t);



       /* //option box
        EditText e2 = new EditText(this);
        e2.setId(0);
        LinearLayout.LayoutParams l2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        e1.setLayoutParams(l2);
        e2.setEms(40);
        e2.setHint(R.string._option_+String.valueOf(0));
        e2.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);*/


        RelativeLayout RL = findViewById(R.id.relative_layout);

        //button to increase options

        Button b1 = new Button(this);
        b1.setId(R.id.button_add_option);
        RelativeLayout.LayoutParams l3 = new RelativeLayout.LayoutParams(60*(int)(getResources().getDisplayMetrics().density),60*(int)(getResources().getDisplayMetrics().density));
        b1.setLayoutParams(l3);
        b1.setText("+");
        b1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        RL.addView(b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout L = findViewById(R.id.linear_layout);
                EditText e2 = new EditText(create_poll.this);
                LinearLayout.LayoutParams l2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                e2.setId(L.getChildCount());
                e2.setLayoutParams(l2);
                e2.setEms(40);
                e2.setHint("Option: "+Integer.toString(L.getChildCount()-2));
                e2.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                L.addView(e2);
            }
        });




        //phrase box
        EditText e3 = new EditText(this);
        e3.setId(R.id.edit_text_phrase);
        RelativeLayout.LayoutParams l4 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        l4.addRule(RelativeLayout.BELOW,R.id.button_add_option);
        e3.setLayoutParams(l4);
        e3.setEms(40);
        e3.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        e3.setHint(R.string._enter_the_phrase_);
        RL.addView(e3);

        //button to create poll
        Button b2 = new Button(this);
        b2.setId(R.id.button_create_poll);
        RelativeLayout.LayoutParams l5 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        l5.addRule(RelativeLayout.BELOW,R.id.edit_text_phrase);
        // l5.setMargins(0,20*(int)(getResources().getDisplayMetrics().density),0,0);
        b2.setLayoutParams(l5);
        b2.setText(R.string._create_poll_);
        RL.addView(b2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout L = findViewById(R.id.linear_layout);
                try {
                    int count = L.getChildCount();
                    POST post = new POST(getApplicationContext(), "createpoll");
                    post.params.put("numoptions", Integer.toString(count));
                    post.params.put("token",myApplication.getToken());
                    for (int i = 3; i < count; i++) {
                        EditText e = findViewById(i);
                        post.params.put(Integer.toString(i - 3), e.getText().toString());
                        //e.getText().toString()
                        //add send data to server
                    }
                    EditText et = findViewById(R.id.edit_text_question);
                    EditText et1 = findViewById(R.id.edit_text_phrase);
                    EditText et2 = findViewById(R.id.edit_text_pollname);
                    post.params.put("question",et.getText().toString());
                    post.params.put("phrase",et1.getText().toString());
                    post.params.put("pollname",et2.getText().toString());
                    post.params.put("email",myApplication.getUser()) ;
                    post.params.put("sock",msocket.id());
                    post.doInBackground();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        //adding the extra options

    }

    private Emitter.Listener ch = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject)args[0];
                    try{
                        if(Integer.parseInt(data.getString("message").toString())==0){
                            Toast.makeText(getApplicationContext(), "Phrase already exists!", Toast.LENGTH_LONG).show();
                        }
                        else if(Integer.parseInt(data.getString("message").toString())==1){
                            Toast.makeText(getApplicationContext(), "Atleast 2 options!", Toast.LENGTH_LONG).show();
                        }
                        else if(Integer.parseInt(data.getString("message").toString())==2){
                            Toast.makeText(getApplicationContext(), "poll created successfully!", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(create_poll.this,MainActivity.class);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Internal error!", Toast.LENGTH_LONG).show();
                        }
                    }
                    catch(Exception e){

                    }
                }
            });
        }
    };


}
