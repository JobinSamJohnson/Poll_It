package com.example.jobinj.pollit;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class POST extends AsyncTask<Void, Void, String> {

    String response = "";
    RequestQueue queue;
    StringRequest postRequest;
    String myurl ;
    String initurl = "http://ec2-18-218-47-162.us-east-2.compute.amazonaws.com:3000/";
    Context ch;
    Map<String, String> params = new HashMap<String,String>();

    POST(Context c,String url){
        ch=c;
        myurl = initurl+url;
        queue = Volley.newRequestQueue(ch);
        postRequest = new StringRequest(com.android.volley.Request.Method.POST, myurl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return params;
            }
        };

    }

    void addparam(String key,String value){
        params.put(key,value);
    }


    @Override
    protected String doInBackground(Void... arg0) {
        try {
            postRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}