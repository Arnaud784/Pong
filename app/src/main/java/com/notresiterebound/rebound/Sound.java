package com.notresiterebound.rebound;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;

public class Sound extends View{

    final MediaPlayer wall = MediaPlayer.create(getContext(), R.raw.wallbounce);
    final MediaPlayer paddle = MediaPlayer.create(getContext(), R.raw.bouncepaddle);
    final MediaPlayer goal = MediaPlayer.create(getContext(), R.raw.goal);
    final MediaPlayer ambiant = MediaPlayer.create(getContext(), R.raw.ambiant);

    public Sound(Context context){
        super(context);
    }

    public void mediaPlayerWall(){
        new Thread(new Runnable() {
            public void run() {
                new Sound(getContext()).wall.start();
            }
        }).start();
    }

    public void mediaPlayerPaddle(){
        new Thread(new Runnable() {
            public void run() {
                new Sound(getContext()).paddle.start();
            }
        }).start();
    }

    public void mediaPlayerGoal(){
        new Thread(new Runnable() {
            public void run() {
                new Sound(getContext()).goal.start();
            }
        }).start();
    }

    public void mediaPlayerAmbiant(){
        new Thread(new Runnable() {
            public void run() {
               new Sound(getContext()).ambiant.start();
            }
        }).start();
    }

}
