package com.example.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class PongView extends View implements OnTouchListener {

    Paint mpaint;

    int score;
    int canvasWidth;
    int canvasHeight;
    int ballSpeed;
    int coordsBarre[] = {-100, -100};
    int coordsBall[] = {-100, -100};
    float direction[] = {1, 1};

    boolean lastBounceIsBar;
    boolean hasInit;

    final int BARRE_WIDTH = 200;
    final int BARRE_HEIGHT = 30;
    final int BALL_WIDTH = 50;


    public PongView(Context context) {
        super(context);
        this.setOnTouchListener(this);
        mpaint = new Paint();
        canvasWidth = 0;
        score = 0;
        ballSpeed = 10;
        lastBounceIsBar = false;
        hasInit = false;
    }

    public void initPong(Canvas canvas) {
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();
        coordsBarre[0] = canvasWidth/2;
        coordsBarre[1] = (canvasHeight/10)*9;
        coordsBall[0] = canvasWidth/2;
        coordsBall[1] = BALL_WIDTH/2;
        hasInit = true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        coordsBarre[0] = x;
        if(coordsBarre[0]+BARRE_WIDTH/2 > canvasWidth) {
            coordsBarre[0] = canvasWidth - BARRE_WIDTH/2;
        }
        else if(coordsBarre[0]-BARRE_WIDTH/2 < 0) {
            coordsBarre[0] = BARRE_WIDTH/2;
        }
        invalidate();
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        //Log.i("TAG", "test: " + canvas.getWidth() + " " + canvas.getHeight());

        if(!hasInit) {
            initPong(canvas);
        }

        canvas.drawCircle(coordsBall[0], coordsBall[1], (float) BALL_WIDTH/2, mpaint);
        canvas.drawRect(coordsBarre[0] - BARRE_WIDTH/2, coordsBarre[1] - BARRE_HEIGHT/2, coordsBarre[0] + BARRE_WIDTH/2, coordsBarre[1] + BARRE_HEIGHT/2, mpaint);

        mpaint.setColor(Color.BLACK);
        mpaint.setTextSize(70);
        canvas.drawText("Score : " + score, canvasWidth/2-130, canvasWidth/10, mpaint);

        moveBall(canvas);
    }

    public void moveBall(Canvas canvas) {

        coordsBall[0] += direction[0]*ballSpeed;
        coordsBall[1] += direction[1]*ballSpeed;

        if(coordsBall[0] >= canvasWidth-BALL_WIDTH/2) {
            lastBounceIsBar = false;
            direction[0] = -1;
        }
        else if(coordsBall[0] <= BALL_WIDTH/2) {
            lastBounceIsBar = false;
            direction[0] = 1;
        }
        else if(coordsBall[1] <= BALL_WIDTH/2) {
            lastBounceIsBar = false;
            direction[1] = 1;
        }
        else if(coordsBall[1] >= canvasHeight-BALL_WIDTH/2) {
            score = 0;
            lastBounceIsBar = false;
            direction[1] = -1;
            coordsBall[0] = canvasWidth/2;
            coordsBall[1] = BALL_WIDTH/2;
        }
        else if(coordsBall[0] > coordsBarre[0] - BARRE_WIDTH/2 && coordsBall[0] < coordsBarre[0] + BARRE_WIDTH/2 && coordsBall[1] >= coordsBarre[1]-(BARRE_HEIGHT/2+BALL_WIDTH/2) && coordsBall[1] <= coordsBarre[1]+BARRE_HEIGHT/2) {
            if(!lastBounceIsBar) {
                score += 1;
            }
            lastBounceIsBar = true;
            direction[1] = -1;
        }
        invalidate();

    }
}
