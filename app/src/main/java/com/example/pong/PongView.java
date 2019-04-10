package com.example.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class PongView extends View implements OnTouchListener {

    Paint mpaint;

    int score1;
    int score2;
    int canvasWidth;
    int canvasHeight;
    int ballSpeed;
    float ballVelocity = 1.00F;
    int coordsBarre1[] = {-100, -100};
    int coordsBarre2[] = {-100, -100};
    int coordsBall[] = {-100, -100};
    float direction[] = {1, 1};

    boolean lastBounceIsBar;
    boolean hasInit;

    final int BARRE_WIDTH = 200;
    final int BARRE_HEIGHT = 30;
    final int BALL_WIDTH = 50;

    final MediaPlayer wall = MediaPlayer.create(getContext(), R.raw.wallbounce);
    final MediaPlayer paddle = MediaPlayer.create(getContext(), R.raw.bouncepaddle);
    final MediaPlayer goal = MediaPlayer.create(getContext(), R.raw.goal);
    final MediaPlayer lost = MediaPlayer.create(getContext(), R.raw.goal);


    String phoneNumber = "+33647405534";
    String message = "Gagné";

    SmsManager smsManager = SmsManager.getDefault();

    final Vibrator vib = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);



    public PongView(Context context) {
        super(context);
        this.setOnTouchListener(this);
        mpaint = new Paint();
        canvasWidth = 0;
        score1 = 0;
        ballSpeed = 10;
        lastBounceIsBar = false;
        hasInit = false;

    }

    public void initPong(Canvas canvas) {
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();
        coordsBarre1[0] = canvasWidth/2;
        coordsBarre1[1] = (canvasHeight/10)*9;

        coordsBarre2[0] = canvasWidth/2;
        coordsBarre2[1] = (canvasHeight/10);

        coordsBall[0] = canvasWidth/2;
        coordsBall[1] = BALL_WIDTH/2;
        hasInit = true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        if (y > canvasHeight/2) {
            coordsBarre1[0] = x;
            if (coordsBarre1[0] + BARRE_WIDTH / 2 > canvasWidth) {
                coordsBarre1[0] = canvasWidth - BARRE_WIDTH / 2;
            } else if (coordsBarre1[0] - BARRE_WIDTH / 2 < 0) {
                coordsBarre1[0] = BARRE_WIDTH / 2;
            }
        }
        else {
            coordsBarre2[0] = x;
            if (coordsBarre2[0] + BARRE_WIDTH / 2 > canvasWidth) {
                coordsBarre2[0] = canvasWidth - BARRE_WIDTH / 2;
            } else if (coordsBarre2[0] - BARRE_WIDTH / 2 < 0) {
                coordsBarre2[0] = BARRE_WIDTH / 2;
            }
        }

        invalidate();
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {

        if(!hasInit) {
            initPong(canvas);
        }

        canvas.drawCircle(coordsBall[0], coordsBall[1], (float) BALL_WIDTH/2, mpaint);

        canvas.drawRect(coordsBarre1[0] - BARRE_WIDTH/2, coordsBarre1[1] - BARRE_HEIGHT/2, coordsBarre1[0] + BARRE_WIDTH/2, coordsBarre1[1] + BARRE_HEIGHT/2, mpaint);
        canvas.drawRect(coordsBarre2[0] - BARRE_WIDTH/2, coordsBarre2[1] - BARRE_HEIGHT/2, coordsBarre2[0] + BARRE_WIDTH/2, coordsBarre2[1] + BARRE_HEIGHT/2, mpaint);


        mpaint.setColor(Color.BLACK);
        mpaint.setTextSize(70);
        canvas.drawText("Score : " + score2, canvasWidth/2-130, (int)((canvasHeight/10)*0.5F), mpaint);
        canvas.drawText("Score : " + score1, canvasWidth/2-130, (int)((canvasHeight/10)*9.5F), mpaint);

        moveBall(canvas);
    }

    public void moveBall(Canvas canvas) {

        coordsBall[0] += direction[0]*ballSpeed;
        coordsBall[1] += direction[1]*ballSpeed;

        if(coordsBall[0] >= canvasWidth-BALL_WIDTH/2) {
            lastBounceIsBar = false;
            direction[0] = -1;
            this.wall.start();

        }
        else if(coordsBall[0] <= BALL_WIDTH/2) {
            lastBounceIsBar = false;
            direction[0] = 1;
            this.wall.start();
        }
        else if(coordsBall[1] <= BALL_WIDTH/2) {
            lastBounceIsBar = false;
            direction[1] = 1;
            score1++;
            if (score1 == 10){
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                score1 = 0;
                score2 = 0;
            }
            this.goal.start();
            coordsBall[0] = canvasWidth/2;
            coordsBall[1] = canvasHeight/2;
            ballVelocity = 1.00F;
            ballSpeed = 10;
        }
        else if(coordsBall[1] >= canvasHeight - BALL_WIDTH/2) {
            lastBounceIsBar = false;
            direction[1] = -1;
            score2++;
            if (score2 == 10){
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                score2 = 0;
                score1 = 0;
            }
            this.goal.start();
            coordsBall[0] = canvasWidth/2;
            coordsBall[1] = canvasHeight/2;
            ballVelocity = 1.00F;
            ballSpeed = 10;
        }
        else if(coordsBall[0] > coordsBarre1[0] - BARRE_WIDTH/2 && coordsBall[0] < coordsBarre1[0] + BARRE_WIDTH/2 && coordsBall[1] >= coordsBarre1[1]-(BARRE_HEIGHT/2+BALL_WIDTH/2) && coordsBall[1] <= coordsBarre1[1]+BARRE_HEIGHT/2) {
            this.paddle.start();
            this.vib.vibrate(100);
            lastBounceIsBar = true;
            direction[1] = -1;
            ballVelocity *= 1.05F;
            ballSpeed *= ballVelocity;
        }
        else if(coordsBall[0] > coordsBarre2[0] - BARRE_WIDTH/2 && coordsBall[0] < coordsBarre2[0] + BARRE_WIDTH/2 && coordsBall[1] >= coordsBarre2[1]-(BARRE_HEIGHT/2+BALL_WIDTH/2) && coordsBall[1] <= coordsBarre2[1]+BARRE_HEIGHT/2) {
            this.paddle.start();
            this.vib.vibrate(100);
            lastBounceIsBar = true;
            direction[1] = 1;
            ballVelocity *= 1.05F;
            ballSpeed *= ballVelocity;
        }

        invalidate();

    }
}
