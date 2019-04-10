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
    int largeur;
    int longueur;

    public PongView(Context context, int larg, int longu) {
        super(context);
        this.setOnTouchListener(this);
        largeur = larg;
        longueur = longu;
        mpaint = new Paint();
        xBall = largeur/2;
        yBall = longueur/2;
        xBarre = largeur/2;
        yBarre = (longueur/10)*8;
        xDir = 10;
        yDir = 10;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        xBarre = x;

        /*switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("TAG", "touched down: (" + x + ", " + y + ")");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("TAG", "moving: (" + x + ", " + y + ")");
                break;
            case MotionEvent.ACTION_UP:
                Log.i("TAG", "touched up");
                break;
        }*/

        invalidate();

        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawCircle(xBall, yBall, (float) 25.0, mpaint);
        canvas.drawRect(xBarre - 100, yBarre-15, xBarre + 100, yBarre+15, mpaint);
        moveBall();
    }

    public void moveBall() {
        xBall += xDir;
        yBall += yDir;
        if(xBall >= largeur-25) {
            xDir = -10;
        }
        else if(xBall <= 25) {
            xDir = 10;
        }
        else if(yBall <= 25) {
            yDir = 10;
        }
        else if(yBall >= longueur-25) {
            Log.i("TAG", "perdu");
            yDir = -10;
        }

        else if(xBall > xBarre - 100 && xBall < xBarre + 100 && yBall >= yBarre-40 && yBall <= yBarre+15) {
            yDir = -10;
        }

        invalidate();
    }
}
