package com.example.pong;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        PongView pongview = new PongView(this, metrics.widthPixels, metrics.heightPixels);*/
        PongView pongview = new PongView(this);

        setContentView(pongview);
    }
}
