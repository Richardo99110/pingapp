package com.example.pingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Boolean isConnected = false,
            isWiFi = false,
            isMobile = false;

    Timer timer = new Timer();
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ConnectivityManager cm = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            isWiFi = activeNetwork.getType() ==
                    ConnectivityManager.TYPE_WIFI;
            isMobile = activeNetwork.getType() ==
                    ConnectivityManager.TYPE_MOBILE;
            isConnected =
                    activeNetwork.isConnectedOrConnecting();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                boolean asdf = false;
                if (isConnected) {
                    if (isWiFi) {

                        if(isOnline()) {
                            System.out.println("true");
                            asdf = true;
                        } else {
                            System.out.println("false");
                            asdf = false;
                        }
                    }
                    if (isMobile) {

                        if(isOnline()) {
                            System.out.println("true");
                            asdf = true;
                        } else {
                            System.out.println("false");
                            asdf = false;
                        }
                    }
                } else {
                    System.out.println("false");
                    asdf = false;
                }


                if(!asdf){
                    play();
                }
            }
        }, 0, 1000);//wait 300000 ms before doing the action and do it evry 1000ms (1second)
    }

    public void play(){
        if(player == null){
            player =  MediaPlayer.create(this, R.raw.scar);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
        }

        player.start();
    }

    private void stopPlayer(){
        if(player != null){
            player.release();
            player = null;

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

    // ICMP
    public boolean isOnline() {

        //NEW
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) { return false; }


        //OLD
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
//            int     exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//        }
//        catch (IOException e)          { e.printStackTrace(); }
//        catch (InterruptedException e) { e.printStackTrace(); }
//
//        return false;
    }

    public boolean isConnectedToThisServer(String host) {
        Runtime runtime = Runtime.getRuntime();


        try {
            Process ipProcess = runtime.exec("/system/bin/ping-c 1 8.8.8.8" + host);
            int exitValue = ipProcess.waitFor();
            System.out.println(exitValue);
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

}