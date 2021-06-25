/*********************************************************************
 FILE:    Brick
 PROGRAMMER: Bryan Butz

 FUNCTION:   This is the class to represent a brick object to be drawn
             on a canvas. The brick is shown as one of two different
             bitmap images.
 *********************************************************************/

package com.example.brickbreakout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class Brick
{

    RectF rect;
    private Bitmap bitmap1, bitmap2;

    // How long and high our paddle will be
    private float length;
    private float height;

    // X is the far left of the rectangle which forms our paddle
    private float x;

    // Y is the top coordinate
    private float y;

    boolean isVisible;



    public Brick(Context context, int row, int column, int screenX, int screenY)
    {
        // Initialize a blank RectF
        rect = new RectF();

        int padding = 2;

        length = screenX / 8;
        height = screenY / 12;


        isVisible = true;

        x = column * (length + padding);
        y = row * (length + padding / 4 ) + 55;

        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;



        // Initialize the bitmap
        bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.virus1);
        bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.virus2);

        // stretch the first bitmap to a size appropriate for the screen resolution
        bitmap1 = Bitmap.createScaledBitmap(bitmap1,
                (int) (length),
                (int) (height),
                false);

        // stretch the first bitmap to a size appropriate for the screen resolution
        bitmap2 = Bitmap.createScaledBitmap(bitmap2,
                (int) (length),
                (int) (height),
                false);
    }

    public RectF getRect(){
        return rect;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean getVisibility(){
        return isVisible;
    }

    public Bitmap getBitmap(){
        return bitmap1;
    }

    public Bitmap getBitmap2(){
        return bitmap2;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

}
