/*********************************************************************
 FILE:    Gameboard
 PROGRAMMER: Bryan Butz

 FUNCTION:   This is the class to represent the background of the game
             board, depicted as either a scrolling background of 12
             bitmap images or 1 static (non-moving) bitmap image.
 *********************************************************************/

package com.example.brickbreakout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class Gameboard extends BreakoutGame
{
    private RectF rect;

    // How long and high the paddle will be
    private float length;
    private float height;

    // Bitmaps used for the game board animation
    private Bitmap bitmap1, bitmap2, bitmap3,
            bitmap4, bitmap5, bitmap6, bitmap7,
            bitmap8, bitmap9, bitmap10, bitmap11,
            bitmap12, bitmapDef;

    // X is the far left of the rectangle which forms the paddle
    private float x;

    // Y is the top coordinate
    private float y;

    // This is the constructor method
    public Gameboard(Context context, int screenX, int screenY)
    {
        // length and height the size of the screen
        length = screenX;
        height = screenY;

        // Start pos
        x = 0;
        y = 3;

        // Initialize rectangle
        rect = new RectF(x, y, x + length, y + height);





        // Initialize the bitmaps
        bitmapDef = BitmapFactory.decodeResource(context.getResources(), R.drawable.game_board);

        bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.tile000);
        bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.tile001);
        bitmap3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.tile002);
        bitmap4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.tile003);
        bitmap5 = BitmapFactory.decodeResource(context.getResources(), R.drawable.tile004);
        bitmap6 = BitmapFactory.decodeResource(context.getResources(), R.drawable.tile005);
        bitmap7 = BitmapFactory.decodeResource(context.getResources(), R.drawable.tile006);
        bitmap8 = BitmapFactory.decodeResource(context.getResources(), R.drawable.tile007);
        bitmap9 = BitmapFactory.decodeResource(context.getResources(), R.drawable.tile008);
        bitmap10 = BitmapFactory.decodeResource(context.getResources(), R.drawable.tile009);
        bitmap11 = BitmapFactory.decodeResource(context.getResources(), R.drawable.tile010);
        bitmap12 = BitmapFactory.decodeResource(context.getResources(), R.drawable.tile011);


        // stretch the bitmaps to a size
        // appropriate for the screen resolution
        bitmapDef = Bitmap.createScaledBitmap(bitmapDef,
                (int) (length),
                (int) (height),
                false);

        bitmap1 = Bitmap.createScaledBitmap(bitmap1,
                (int) (length),
                (int) (height),
                false);

        bitmap2 = Bitmap.createScaledBitmap(bitmap2,
                (int) (length),
                (int) (height),
                false);

        bitmap3 = Bitmap.createScaledBitmap(bitmap3,
                (int) (length),
                (int) (height),
                false);

        bitmap4 = Bitmap.createScaledBitmap(bitmap4,
                (int) (length),
                (int) (height),
                false);

        bitmap5 = Bitmap.createScaledBitmap(bitmap5,
                (int) (length),
                (int) (height),
                false);

        bitmap6 = Bitmap.createScaledBitmap(bitmap6,
                (int) (length),
                (int) (height),
                false);

        bitmap7= Bitmap.createScaledBitmap(bitmap7,
                (int) (length),
                (int) (height),
                false);

        bitmap8 = Bitmap.createScaledBitmap(bitmap8,
                (int) (length),
                (int) (height),
                false);

        bitmap9 = Bitmap.createScaledBitmap(bitmap9,
                (int) (length),
                (int) (height),
                false);

        bitmap10 = Bitmap.createScaledBitmap(bitmap10,
                (int) (length),
                (int) (height),
                false);

        bitmap11 = Bitmap.createScaledBitmap(bitmap11,
                (int) (length),
                (int) (height),
                false);

        bitmap12 = Bitmap.createScaledBitmap(bitmap12,
                (int) (length),
                (int) (height),
                false);

    }


    public RectF getRect(){
        return rect;
    }


    //Get methods for the bitmaps

    public Bitmap getBitmapDef(){
        return bitmapDef;
    }

    public Bitmap getBitmap1(){
        return bitmap1;
    }

    public Bitmap getBitmap2(){
        return bitmap2;
    }

    public Bitmap getBitmap3(){
        return bitmap3;
    }

    public Bitmap getBitmap4(){
        return bitmap4;
    }

    public Bitmap getBitmap5(){
        return bitmap5;
    }

    public Bitmap getBitmap6(){
        return bitmap6;
    }

    public Bitmap getBitmap7(){
        return bitmap7;
    }

    public Bitmap getBitmap8(){
        return bitmap8;
    }

    public Bitmap getBitmap9(){
        return bitmap9;
    }

    public Bitmap getBitmap10(){
        return bitmap10;
    }

    public Bitmap getBitmap11(){
        return bitmap11;
    }

    public Bitmap getBitmap12(){
        return bitmap12;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }
}
