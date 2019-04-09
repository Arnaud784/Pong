package com.example.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class PongView extends View implements OnTouchListener {
    Paint mpaint;
    int xBall, yBall, xDir, yDir, xBarre, yBarre;
    int maxX = 1080;
    int maxY = 1600;

    public PongView(Context context) {
        super(context);
        this.setOnTouchListener(this);
        mpaint = new Paint();
        xBall = 200;
        yBall = 500;
        xBarre = 520;
        yBarre = 1485;
        xDir = 10;
        yDir = 10;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        xBarre = x;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("TAG", "touched down: (" + x + ", " + y + ")");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("TAG", "moving: (" + x + ", " + y + ")");
                break;
            case MotionEvent.ACTION_UP:
                Log.i("TAG", "touched up");
                break;
        }

        return false;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawCircle(xBall, yBall, (float) 25.0, mpaint);
        canvas.drawRect(xBarre - 100, 1470.0F, xBarre + 100, 1500.0F, mpaint);
        moveBall();
    }

    public void moveBall() {
        xBall += xDir;
        yBall += yDir;
        if(xBall >= maxX-25) {
            xDir *= -1;
        }
        else if(xBall <= 25) {
            xDir *= -1;
        }
        else if(yBall <= 25) {
            yDir *= -1;
        }
        else if(yBall >= maxY-25) {
            Log.i("TAG", "perdu");
            yDir *= -1;
        }

        else if(xBall > xBarre - 100 && xBall < xBarre + 100 && yBall >= yBarre-40 && yBall <= yBarre+15) {
            yDir = -10;
        }

        invalidate();
    }
}
