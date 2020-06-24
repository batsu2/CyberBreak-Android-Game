/*********************************************************************
 FILE:    Paddle
 PROGRAMMER: Bryan Butz
 LOGON ID:   z1836033
 DUE DATE:   5/04/2020

 FUNCTION:   This is the class used to represent a paddle to be drawn
             on a canvas. The paddle will be shown as a bitmap image
             paddle1.png taken from the drawable folder.
 *********************************************************************/

package com.example.brickbreakout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;


public class Paddle extends BreakoutGame
{

    private RectF rect;

    // How long and high the paddle will be
    private float length;
    private float height;


    private Bitmap bitmapP;

    // X is the far left of the rectangle which forms the paddle
    private float x;

    // Y is the top coordinate
    private float y;

    // This will hold the pixels per second speed that the paddle will move
    private float paddleSpeed;

    // Which ways can the paddle move
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    // Is the paddle moving and in which direction
    private int paddleMoving = STOPPED;




    // This is the constructor method
    public Paddle(Context context, int screenX, int screenY)
    {
        // 130 pixels wide and 40 pixels high
        length = paddleLength;
        height = 40;

        // Start paddle in roughly the screen center
        x = screenX / 2;
        y = screenY - 60;

        // Initialize rectangle
        rect = new RectF(x, y, x + length, y + height);



        // Initialize the bitmap
        bitmapP = BitmapFactory.decodeResource(context.getResources(), R.drawable.paddle1);

        // stretch the first bitmap to a size appropriate for the screen resolution
        bitmapP = Bitmap.createScaledBitmap(bitmapP,
                (int) (length),
                (int) (height),
                false);

        // How fast is the paddle in pixels per second
        paddleSpeed = 450;
    }


    // This is a getter method to return the rectangle that
    // defines the paddle available in BreakoutView class
    public RectF getRect(){
        return rect;
    }

    // This method will be used to change/set if the paddle is going left, right or still
    public void setMovementState(int state){
        paddleMoving = state;
    }


    // This determines if the paddle needs to move and changes the coordinates
    // contained in rect if necessary
    public void update(long fps)
    {
        if(paddleMoving == LEFT)
        {
            if( rect.left >= 0 )
                x = x - paddleSpeed / fps;
        }

        if(paddleMoving == RIGHT)
        {
            if( rect.right <= screenX )
                x = x + paddleSpeed / fps;
        }

        rect.left = x;
        rect.right = x + length;
    }


    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getLength(){
        return length;
    }

    public Bitmap getBitmap(){
        return bitmapP;
    }


}//end Paddle

