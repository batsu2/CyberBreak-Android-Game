/*********************************************************************
 FILE:    TitleScreen
 PROGRAMMER: Bryan Butz

 FUNCTION:   This is the main entry point of the entire app. It stores
             global variables (variables changed in the options menu and
             used in BreakoutGame.java), plays music with a media player,
             and sets a gif image as an animated background using Glide.
 *********************************************************************/

package com.example.brickbreakout;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class TitleScreen extends Activity
{

    static final int REQUEST_CODE = 1;
    private ImageButton musicBtn;
    public MediaPlayer titleMP;

    static public boolean musicPlaying = true;
    static public boolean soundEffects = true;
    static public boolean animBackground = true;
    static public String difficulty = "medium";
    static public int true_lives = 3;
    int screenX = getScreenWidth();
    static public String song = "moose";
    static public float paddleLength = 130;
    public int speed = 1000;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        //set the music button up
        musicBtn = findViewById(R.id.musicButton);
        musicBtn.setImageResource(R.drawable.ic_volume_up_black_24dp);

        //create the MediaPlayer for the MP3 and connect the audio
        titleMP = new MediaPlayer();

        //determine which song to play
        switch( song )
        {
            case "moose":
                titleMP = MediaPlayer.create(this, R.raw.moose);
                break;

            case "dance":
                titleMP = MediaPlayer.create(this, R.raw.dance);
                break;

            case "scifi":
                titleMP = MediaPlayer.create(this, R.raw.scifi);
                break;

            case "MUSIC OFF":
                break;

        }

        //start the song, set to repeat
        titleMP.start();
        titleMP.setLooping(true);




        //Set up image view with gif (using Glide)
        ImageView img= findViewById(R.id.img);
        Glide.with(this).load(R.raw.cyber_title).into(img);




    }//end onCreate



    //OnClick functions for each activity

    public void playGame(View view)
    {
        //Toast.makeText(getApplicationContext(),"Loading...", Toast.LENGTH_SHORT).show();

        //create the intent to call BreakoutGame
        Intent gameIntent = new Intent(TitleScreen.this, BreakoutGame.class);
        startActivityForResult(gameIntent, REQUEST_CODE);
    }

    public void options(View view)
    {
        //create the intent to call Options
        Intent optionsIntent = new Intent(TitleScreen.this, Options.class);
        startActivityForResult(optionsIntent, REQUEST_CODE);
    }

    public void aboutApp(View view)
    {
        //create the intent to call aboutApp
        Intent aboutIntent = new Intent(TitleScreen.this, AboutAppActivity.class);
        startActivity(aboutIntent);
    }



    // OnClick for mute/play button
    public void playSong(View view)
    {
        //if the song MP3 is currently playing
        if(titleMP.isPlaying() )
        {
            //pause the media player
            titleMP.pause();

            musicBtn.setImageResource(R.drawable.ic_volume_off_black_24dp);

            musicPlaying = false;
        }
        else //If MP3 is NOT playing
        {
            //play the MP3
            titleMP.start();

            musicBtn.setImageResource(R.drawable.ic_volume_up_black_24dp);

            musicPlaying = true;
        }

    }//end playSong


    //determine screen width
    public static int getScreenWidth()
    {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }






    @Override
    protected void onResume()
    {
        super.onResume();


        if( musicPlaying )
        {
            // Tell the gameView resume method to execute
            titleMP.start();
        }
    }


    // This method executes when the player quits the game
    @Override
    protected void onPause()
    {
        super.onPause();


        if( musicPlaying )
        {

            // Tell the gameView pause method to execute
            titleMP.pause();
        }
    }



}//end TitleScreen
