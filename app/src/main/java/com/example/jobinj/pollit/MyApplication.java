package com.example.jobinj.pollit;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.github.nkzawa.socketio.client.Socket;

public class MyApplication extends Application {
    public Socket socket;
    public String token;
    public String username;
    Context context;

    public Socket getSocket(){
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork = cm.getActiveNetworkInfo();
        if(activenetwork!=null&&activenetwork.isConnected()){
            return socket;
        }
        return null;
    }

    public void setSocket(Socket sockets){
        socket = sockets;
    }

    public String getToken(){
        return token;
    }
    public void setToken(String t){
        token = t;
    }
    public String getUser(){return username;}
    public void setUser(String s){
        username = s;
    }

}

