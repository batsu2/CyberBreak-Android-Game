/*********************************************************************
 FILE:    Bullet
 PROGRAMMER: Bryan Butz

 FUNCTION:   This is the class to represent a bullet object to be drawn
             on a canvas. It will be used to fire from the paddle towards
             the bricks.
 *********************************************************************/

package com.example.brickbreakout;

import android.graphics.RectF;

public class Bullet
{
    private float x;
    private float y;

    private RectF rect;

    // Which way it is shooting
    public int UP = 0;
    public int DOWN = 1;

    // Staying still
    int heading = -1;
    float speed =  350;

    private int width = 3;
    private int height;

    private boolean isActive;




    //the Constructor
    public Bullet(int screenY)
    {

        height = screenY / 30;
        isActive = false;

        rect = new RectF();
    }




    public boolean shoot(float startX, float startY, int direction)
    {
        if (!isActive)
        {
            x = startX;
            y = startY;
            heading = direction;
            isActive = true;
            return true;
        }

        // Bullet already active
        return false;
    }



    //to update where the bullet is
    public void update(long fps)
    {

        // Just move up or down
        if(heading == UP)
        {
            y = y - speed / fps;
        }
        else
        {
            y = y + speed / fps;
        }

        // Update rect
        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;

    }



    public RectF getRect(){
        return  rect;
    }

    public boolean getStatus(){
        return isActive;
    }

    public void setInactive(){
        isActive = false;
    }

    public float getImpactPointY()
    {
        if (heading == DOWN)
        {
            return y + height;
        }
        else
        {
            return  y;
        }

    }


    public void reset(int x, int y)
    {
        rect.left = x - 20;
        rect.top = y - 20;
        rect.right = x - 20;
        rect.bottom = y - 20;
    }


}
