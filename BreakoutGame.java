/*********************************************************************
 FILE:    BreakoutGame
 PROGRAMMER: Bryan Butz

 FUNCTION:   This class holds the main logic of the game. It creates and
             draws all objects on screen as well as setting up and
             playing audio and acting accordingly for when the player
             touches the screen.
 *********************************************************************/

package com.example.brickbreakout;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import java.io.IOException;

public class BreakoutGame extends TitleScreen
{

    // This holds the logic from within
    // an inner class
    BreakoutView breakoutView;





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        //Set attributes based on difficulty
        switch ( difficulty )
        {
            case "easy":
                paddleLength = 200;
                speed = 1150;
                true_lives = 6;
                break;

            case "medium":
                paddleLength = 130;
                speed = 1000;
                true_lives = 4;
                break;

            case "hard":
                paddleLength = 90;
                speed = 700;
                true_lives = 3;
                break;


            default:
                break;
        }

        // Initialize gameView and set it as the main view
        breakoutView = new BreakoutView(this);

        setContentView(breakoutView);

    }






    // The implementation of BreakoutView
    // Implementing Runnable to override run method
    class BreakoutView extends SurfaceView implements Runnable
    {
        //The game thread
        Thread gameThread = null;

        SurfaceHolder ourHolder;

        // tell whether the game is running or not
        volatile boolean playing;

        boolean paused = true;

        // Canvas and Paint object
        Canvas canvas;
        Paint paint;

        long fps;

        // Used to calculate the fps
        private long timeThisFrame;

        // The size of the screen in pixels
        int screenX;
        int screenY;

        //Game board Background
        Gameboard gameboard;

        int animationCounter;

        // The players paddle & ball
        Paddle paddle;

        Ball ball;

        // Up to 200 bricks
        Brick[] bricks = new Brick[200];
        int numBricks = 0;


        int numCleared = 0;

        // For sound FX
        SoundPool soundPool;
        int paddleBounceID = -1;
        int backWallBounceID = -1;
        int wallBounceID = -1;
        int loseLifeID = -1;
        int explodeID = -1;
        int nextLevelID = -1;
        int oneUpID = -1;

        // The score
        int score = 0;

        // Lives
        int lives = true_lives;
        int extraLife = 500;

        // Level
        int level = 1;

        // The player's bullet
        private Bullet bullet;

        private Context context;






        // When the initialized (call new()) on gameView
        // This special constructor method runs
        public BreakoutView(Context context)
        {
            // Asks the SurfaceView class to set up our object.
            super(context);

            // Make a globally available copy of the context so we can use it in another method
            this.context = context;

            // Initialize ourHolder and paint objects
            ourHolder = getHolder();
            paint = new Paint();

            // Get a Display object to access screen details
            Display display = getWindowManager().getDefaultDisplay();

            // Load the resolution into a Point object
            Point size = new Point();
            display.getSize(size);

            screenX = size.x;
            screenY = size.y;

            paddle = new Paddle(context, screenX, screenY);

            gameboard = new Gameboard(context, screenX, screenY);

            animationCounter = 1;

            // Create a ball
            ball = new Ball( screenX, screenY);

            // Prepare the players bullet
            bullet = new Bullet(screenY);






            // Load the sounds

            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);


            try
            {
                // Create objects of the 2 required classes
                AssetManager assetManager = context.getAssets();
                AssetFileDescriptor descriptor;


                if( soundEffects )
                {
                    // Load sound fx
                    descriptor = assetManager.openFd("paddle_bounce.wav");
                    paddleBounceID = soundPool.load(descriptor, 0);

                    descriptor = assetManager.openFd("back_wall.wav");
                    backWallBounceID = soundPool.load(descriptor, 0);

                    descriptor = assetManager.openFd("wall_bounce.wav");
                    wallBounceID = soundPool.load(descriptor, 0);

                    descriptor = assetManager.openFd("lost_life.wav");
                    loseLifeID = soundPool.load(descriptor, 0);

                    descriptor = assetManager.openFd("box_destroy.wav");
                    explodeID = soundPool.load(descriptor, 0);

                    descriptor = assetManager.openFd("next_level.wav");
                    nextLevelID = soundPool.load(descriptor, 0);

                    descriptor = assetManager.openFd("1up.wav");
                    oneUpID = soundPool.load(descriptor, 0);
                }
                else
                {
                    // Load BLANK sound fx
                    descriptor = assetManager.openFd("");
                    paddleBounceID = soundPool.load(descriptor, 0);

                    descriptor = assetManager.openFd("");
                    backWallBounceID = soundPool.load(descriptor, 0);

                    descriptor = assetManager.openFd("");
                    wallBounceID = soundPool.load(descriptor, 0);

                    descriptor = assetManager.openFd("");
                    loseLifeID = soundPool.load(descriptor, 0);

                    descriptor = assetManager.openFd("");
                    explodeID = soundPool.load(descriptor, 0);

                    descriptor = assetManager.openFd("");
                    nextLevelID = soundPool.load(descriptor, 0);

                    descriptor = assetManager.openFd("");
                    oneUpID = soundPool.load(descriptor, 0);
                }

            }
            catch(IOException e)
            {
                // Print an error message to the console
                Log.e("error", "failed to load sound files");
            }

            createBricksAndRestart();

        }





        public void createBricksAndRestart()
        {
            bullet.reset(0, 0);
            bullet.setInactive();

            // Put the ball back to the start
            ball.reset(screenX, screenY - 30);


            numBricks = 0;
            numCleared = 0;


            //Build the wall of bricks/monsters
            for(int column = 0; column < 8; column++ )
            {
                for(int row = 0; row < 5; row++ )
                {
                    bricks[numBricks] = new Brick(context, row, column, screenX, screenY); //, brickWidth, brickHeight);
                    numBricks++;
                }
            }

            // if game over reset scores and lives
            if(lives == 0)
            {

                switch( difficulty )
                {
                    case "easy":
                        speed = 1100;
                        break;

                    case "medium":
                        speed = 1000;
                        break;

                    case "hard":
                        speed = 700;
                        break;

                    default:
                        break;
                }



                lives = true_lives;
                score = 0;
                level = 1;
            }

        }








        @Override
        public void run()
        {
            while(playing)
            {

                // Capture the current time in milliseconds
                long startFrameTime = System.currentTimeMillis();


                // Update the frame
                if(!paused)
                {
                    update();
                }

                // Draw the frame
                draw();

                // Calculate the fps this frame used
                timeThisFrame = System.currentTimeMillis() - startFrameTime;

                if (timeThisFrame >= 1)
                {//fps = 1000 / timeThisFrame;
                    fps = speed / timeThisFrame;
                }

            }
        }










        // Movement, collision detection, incrementing/decrementing
        // happens in here.
        public void update()
        {

            // Move the paddle if required
            paddle.update(fps);

            ball.update(fps);

            // Check for ball colliding with a brick
            for(int i = 0; i < numBricks; i++)
            {
                if (bricks[i].getVisibility())
                {
                    if(RectF.intersects(bricks[i].getRect(), ball.getRect()))
                    {
                        bricks[i].setInvisible();
                        ball.reverseYVelocity();
                        score = score + 10;
                        numCleared++;

                        //Achieve extra life every 500 points
                        if( (score % extraLife) == 0 )
                        {
                            lives++;
                            soundPool.play(oneUpID, 1, 1, 0, 0, 1);
                        }
                        else
                            soundPool.play(explodeID, 1, 1, 0, 0, 1);
                    }
                }
            }




            // If player's bullet hit a brick
            if(bullet.getStatus())
            {
                for (int i = 0; i < numBricks; i++)
                {
                    if (bricks[i].getVisibility())
                    {
                        if (RectF.intersects(bullet.getRect(), bricks[i].getRect()))
                        {
                            bricks[i].setInvisible();
                            bullet.reset(0, 0);
                            bullet.setInactive();
                            numCleared++;
                            score = score + 10;


                            //Achieve extra life every 500 points
                            if( (score % extraLife) == 0 )
                            {
                                lives++;
                                soundPool.play(oneUpID, 1, 1, 0, 0, 1);
                            }
                            else
                                soundPool.play(explodeID, 1, 1, 0, 0, 1);

                        }
                    }
                }
            }


            // Update the players bullet
            if(bullet.getStatus())
            {
                bullet.update(fps);
            }


            // Check for ball colliding with paddle
            if(RectF.intersects(paddle.getRect(),ball.getRect()))
            {
                ball.setRandomXVelocity();
                ball.reverseYVelocity();
                ball.clearObstacleY(paddle.getRect().top - 2);

                soundPool.play(paddleBounceID, 1, 1, 0, 0, 1);
            }



            // Detract life and reset ball if player misses
            if(ball.getRect().bottom > screenY)
            {
                bullet.reset(0, 0);
                bullet.setInactive();

                ball.reverseYVelocity();
                ball.setRandomXVelocity();

                // Lose a life
                lives--;
                soundPool.play(loseLifeID, 1, 1, 0, 0, 1);

                ball.reset(screenX, screenY - 80);
                paused = true;

                if(lives == 0)
                {
                    paused = true;
                    //Toast.makeText(getApplicationContext(),"GAME OVER\nFinal Score: " + score, Toast.LENGTH_SHORT).show();
                    createBricksAndRestart();
                }
            }



            // Bounce the ball back when it hits the top of screen
            if(ball.getRect().top < 0)
            {
                ball.reverseYVelocity();
                ball.clearObstacleY(12);

                soundPool.play(backWallBounceID, 1, 1, 0, 0, 1);
            }

            // If the ball hits left wall, bounce off
            if(ball.getRect().left < 0)
            {
                ball.reverseXVelocity();
                ball.clearObstacleX(2);

                soundPool.play(backWallBounceID, 0.6f, 0.6f, 0, 0, 1);
            }

            // If the ball hits right wall, bounce off
            if(ball.getRect().right > screenX - 10)
            {
                ball.reverseXVelocity();
                ball.clearObstacleX(screenX - 22);

                soundPool.play(backWallBounceID, 1, 1, 0, 0, 1);
            }



            // Has the player's bullet hit the top of the screen
            if(bullet.getImpactPointY() < 0)
            {
                bullet.reset(0, 0);
                bullet.setInactive();
            }


            // Go to next level, play sound, and pause for input
            if(numCleared == numBricks)
            {
                //Reverse direction if last brick
                //was hit from top
                if(ball.yVelocity > 0 )
                    ball.reverseYVelocity();


                paused = true;
                level++;
                speed = speed - 70;
                createBricksAndRestart();
                soundPool.play(nextLevelID, 2.3f, 2.3f, 0, 0, 1);
            }

        }//End update








        // Draw the newly updated scene
        public void draw()
        {

            // Checking if holder is valid before continuing
            if (ourHolder.getSurface().isValid())
            {
                // Lock the canvas to draw
                canvas = ourHolder.lockCanvas();

                // Draw background
                canvas.drawColor(Color.argb(255,  0, 0, 0));

                // Choose BLACK color for background
                paint.setColor(Color.argb(255,  255, 255, 255));





                if(animBackground)
                {
                    //Background animation
                    // 12 frames looping

                    if (animationCounter <= 7 && animationCounter >= 1) {
                        canvas.drawBitmap(gameboard.getBitmap1(), gameboard.getX(), gameboard.getY(), paint);
                        animationCounter++;
                    } else if (animationCounter <= 14 && animationCounter >= 8) {
                        canvas.drawBitmap(gameboard.getBitmap2(), gameboard.getX(), gameboard.getY(), paint);
                        animationCounter++;
                    } else if (animationCounter <= 21 && animationCounter >= 15) {
                        canvas.drawBitmap(gameboard.getBitmap3(), gameboard.getX(), gameboard.getY(), paint);
                        animationCounter++;
                    } else if (animationCounter <= 28 && animationCounter >= 22) {
                        canvas.drawBitmap(gameboard.getBitmap4(), gameboard.getX(), gameboard.getY(), paint);
                        animationCounter++;
                    } else if (animationCounter <= 35 && animationCounter >= 29) {
                        canvas.drawBitmap(gameboard.getBitmap5(), gameboard.getX(), gameboard.getY(), paint);
                        animationCounter++;
                    } else if (animationCounter <= 42 && animationCounter >= 36) {
                        canvas.drawBitmap(gameboard.getBitmap6(), gameboard.getX(), gameboard.getY(), paint);
                        animationCounter++;
                    } else if (animationCounter <= 49 && animationCounter >= 43) {
                        canvas.drawBitmap(gameboard.getBitmap7(), gameboard.getX(), gameboard.getY(), paint);
                        animationCounter++;
                    } else if (animationCounter <= 56 && animationCounter >= 50) {
                        canvas.drawBitmap(gameboard.getBitmap8(), gameboard.getX(), gameboard.getY(), paint);
                        animationCounter++;
                    } else if (animationCounter <= 63 && animationCounter >= 57) {
                        canvas.drawBitmap(gameboard.getBitmap9(), gameboard.getX(), gameboard.getY(), paint);
                        animationCounter++;
                    } else if (animationCounter <= 70 && animationCounter >= 64) {
                        canvas.drawBitmap(gameboard.getBitmap10(), gameboard.getX(), gameboard.getY(), paint);
                        animationCounter++;
                    } else if (animationCounter <= 77 && animationCounter >= 71) {
                        canvas.drawBitmap(gameboard.getBitmap11(), gameboard.getX(), gameboard.getY(), paint);
                        animationCounter++;
                    } else if (animationCounter <= 84 && animationCounter >= 78) {
                        canvas.drawBitmap(gameboard.getBitmap12(), gameboard.getX(), gameboard.getY(), paint);
                        animationCounter = 1;
                    } else {
                        animationCounter++;
                    }

                }
                else
                {
                    // Static background
                    canvas.drawBitmap(gameboard.getBitmapDef(), gameboard.getX(), gameboard.getY(), paint);
                }



                // Draw the paddle
                canvas.drawBitmap(paddle.getBitmap(), paddle.getX(), paddle.getY(), paint);


                // Draw the ball
                canvas.drawRect(ball.getRect(), paint);




                // Choose the brush color for drawing
                paint.setColor(Color.argb(255,  255, 255, 255));

                // Draw the bricks
                for(int i = 0; i < numBricks; i++)
                {
                    if(bricks[i].getVisibility())
                    {
                        if( (i % 2) == 0)
                        {
                            canvas.drawBitmap(bricks[i].getBitmap(), bricks[i].getX(), bricks[i].getY(), paint);
                        }
                        else
                        {
                            canvas.drawBitmap(bricks[i].getBitmap2(), bricks[i].getX(), bricks[i].getY(), paint);
                        }
                    }
                }





                // Choose the brush color for drawing
                paint.setColor(Color.argb(255,  255, 255, 255));

                // Draw the score
                paint.setTextSize(40);
                canvas.drawText("Score: " + score + "   Lives: " + lives + "    Level: " + level, 10,50, paint);




                // Draw the players bullet if active
                if(bullet.getStatus())
                {
                    canvas.drawRect(bullet.getRect(), paint);
                }



                // Draw everything to the screen
                ourHolder.unlockCanvasAndPost(canvas);
            }

        }





        // If SimpleGameEngine Activity is started then start the thread
        public void resume()
        {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }




        // If SimpleGameEngine Activity is paused/stopped then stop the thread
        public void pause()
        {
            playing = false;

            try
            {
                gameThread.join();
            }
            catch(InterruptedException e)
            {
                Log.e("Error:", "joining thread");
            }
        }





        // The SurfaceView class implements onTouchListener to detect screen touches
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent)
        {

            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK)
            {

                // Player has touched the screen
                case MotionEvent.ACTION_DOWN:

                    paused = false;

                    if(motionEvent.getX() > screenX / 2)
                    {
                            paddle.setMovementState(paddle.RIGHT);
                    }
                    else
                    {
                            paddle.setMovementState(paddle.LEFT);
                    }



                    if(motionEvent.getY() < screenY - screenY / 8)
                    {
                        // Shots fired
                        if(bullet.shoot(paddle.getX()+ paddle.getLength()/2,screenY,bullet.UP)){
                            soundPool.play(wallBounceID, 1, 1, 0, 0, 1);
                        }
                    }

                    break;


                // Player has removed finger from screen
                case MotionEvent.ACTION_UP:

                    paddle.setMovementState(paddle.STOPPED);
                    break;
            }


            return true;
        }

    }// End of BreakoutView







    //Override functions, mostly used to manage the music

    @Override
    protected void onStart()
    {
        super.onStart();

        if( musicPlaying )
        {
            titleMP.start();
        }

    }//end onStart



    @Override
    protected void onPause()
    {
        super.onPause();

        // Tell the gameView pause method to execute
        breakoutView.pause();


        if( musicPlaying )
        {

            // Tell the gameView pause method to execute
            titleMP.pause();
        }
    }




    @Override
    protected void onResume()
    {
        super.onResume();

        // Tell the gameView resume method to execute
        breakoutView.resume();

        if( musicPlaying )
        {
            // Tell the gameView resume method to execute
            titleMP.start();
        }
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        titleMP.stop();
    }//end onDestroy



}// End of BreakoutGame class
