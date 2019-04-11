package com.notresiterebound.rebound;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    int coordsBarre1[] = {-100, -100};
    int coordsBarre2[] = {-100, -100};
    int coordsBall[] = {-100, -100};
    float direction[] = {0, 1};
    float ballSpeed;

    boolean hasBall = false;

    boolean hasInit;

    final int BARRE_WIDTH = 200;
    final int BARRE_HEIGHT = 30;
    final int BALL_WIDTH = 50;
    final int MAX_BOUNCE_ANGLE = 75;


    Sound sound = new Sound(getContext());


    String message = "Gagné";

    SmsManager smsManager = SmsManager.getDefault();

    final Vibrator vib = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);



    public PongView(Context context) {
        super(context);
        this.setOnTouchListener(this);

        sound.mediaPlayerAmbiant();

        mpaint = new Paint();
        canvasWidth = 0;
        score1 = 0;
        ballSpeed = 15.0F;
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
        coordsBall[1] = canvasHeight/2;
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
        invalidate();
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {

        if(!hasInit) {
            initPong(canvas);
        }

        if (hasBall){
            if (coordsBall[0] < coordsBarre2[0]){
                int temp = coordsBarre2[0] - coordsBall[0];
                if (temp >= 20){
                    coordsBarre2[0] -= 20;
                }
                else{
                    coordsBarre2[0] -= temp;
                }
            }
            else {
                int temp = coordsBall[0] - coordsBarre2[0];
                if (temp >= 20){
                    coordsBarre2[0] += 20;
                }
                else{
                    coordsBarre2[0] += temp;
                }
            }

            if(coordsBarre2[0]+BARRE_WIDTH/2 > canvasWidth) {
                coordsBarre2[0] = canvasWidth - BARRE_WIDTH/2;
            }
            else if(coordsBarre2[0]-BARRE_WIDTH/2 < 0) {
                coordsBarre2[0] = BARRE_WIDTH/2;
            }
        }

        canvas.drawCircle(coordsBall[0], coordsBall[1], (float) BALL_WIDTH/2, mpaint);
        canvas.drawLine((float)(0), (float)(canvasHeight/2), (float)(canvasWidth), (float)(canvasHeight/2), mpaint);
        canvas.drawRect(coordsBarre1[0] - BARRE_WIDTH/2, coordsBarre1[1] - BARRE_HEIGHT/2, coordsBarre1[0] + BARRE_WIDTH/2, coordsBarre1[1] + BARRE_HEIGHT/2, mpaint);
        canvas.drawRect(coordsBarre2[0] - BARRE_WIDTH/2, coordsBarre2[1] - BARRE_HEIGHT/2, coordsBarre2[0] + BARRE_WIDTH/2, coordsBarre2[1] + BARRE_HEIGHT/2, mpaint);


        mpaint.setColor(Color.BLACK);
        mpaint.setTextSize(70);

        canvas.rotate(-90);
        canvas.drawText("" + score2, -(canvasHeight/2) + 50, 100, mpaint);
        canvas.drawText("" + score1, -(canvasHeight/2) - 100, 100, mpaint);
        canvas.rotate(90);
        moveBall(canvas);
    }

    public void moveBall(Canvas canvas) {

        coordsBall[0] += direction[0]*ballSpeed;
        coordsBall[1] += direction[1]*ballSpeed;

        if(coordsBall[0] >= canvasWidth-BALL_WIDTH/2) {
            direction[0] = -Math.abs(direction[0]);
            sound.mediaPlayerWall();

        }
        else if(coordsBall[0] <= BALL_WIDTH/2) {
            direction[0] = Math.abs(direction[0]);
            sound.mediaPlayerWall();
        }
        else if(coordsBall[1] <= BALL_WIDTH/2) {
            direction[0] = -0.70711F;
            direction[1] = 0.70711F;
            score1++;
            /*if (score1 == 10){
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                score1 = 0;
                score2 = 0;
            }*/
            sound.mediaPlayerWall();
            coordsBall[0] = canvasWidth/2;
            coordsBall[1] = canvasHeight/2;
            ballSpeed = 10;
            hasBall = false;
        }
        else if(coordsBall[1] >= canvasHeight - BALL_WIDTH/2) {
            direction[0] = 0.70711F;
            direction[1] = -0.70711F;
            score2++;
            /*if (score2 == 10){
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            }*/
            sound.mediaPlayerGoal();;
            coordsBall[0] = canvasWidth/2;
            coordsBall[1] = canvasHeight/2;
            ballSpeed = 10;
            hasBall = true;
        }
        else if(coordsBall[0] > coordsBarre1[0] - BARRE_WIDTH/2 && coordsBall[0] < coordsBarre1[0] + BARRE_WIDTH/2 && coordsBall[1] >= coordsBarre1[1]-(BARRE_HEIGHT/2+BALL_WIDTH/2) && coordsBall[1] <= coordsBarre1[1]+BARRE_HEIGHT/2) {
            sound.mediaPlayerPaddle();
            this.vib.vibrate(100);

            float relativeIntersectX = coordsBarre1[0]-coordsBall[0];
            float normalizedRelativeIntersectionX = relativeIntersectX/(BARRE_WIDTH/2);
            float bounceAngle =normalizedRelativeIntersectionX * MAX_BOUNCE_ANGLE;
            direction[0] = (float)(Math.sin(bounceAngle*(Math.PI/180))*(-1));
            direction[1] = (float)(Math.cos(bounceAngle*(Math.PI/180))*(-1));

            ballSpeed *= 1.1F;
            hasBall = true;
        }
        else if(coordsBall[0] > coordsBarre2[0] - BARRE_WIDTH/2 && coordsBall[0] < coordsBarre2[0] + BARRE_WIDTH/2 && coordsBall[1] >= coordsBarre2[1]-(BARRE_HEIGHT/2+BALL_WIDTH/2) && coordsBall[1] <= coordsBarre2[1]+BARRE_HEIGHT/2) {
            this.vib.vibrate(100);
            sound.mediaPlayerPaddle();
            float relativeIntersectX = coordsBarre2[0]-coordsBall[0];
            float normalizedRelativeIntersectionX = relativeIntersectX/(BARRE_WIDTH/2);
            float bounceAngle =normalizedRelativeIntersectionX * MAX_BOUNCE_ANGLE;
            direction[0] = (float)(Math.sin(bounceAngle*(Math.PI/180))*(-1));
            direction[1] = (float)(Math.cos(bounceAngle*(Math.PI/180)));

            ballSpeed *= 1.1F;
            hasBall = false;
        }

        invalidate();

    }
}
