/*********************************************************************
 FILE:    Options
 PROGRAMMER: Bryan Butz
 LOGON ID:   z1836033
 DUE DATE:   5/04/2020

 FUNCTION:   This class gives the user the option to change game settings
             such as the game difficulty, music/audio options, and other
             settings.
 *********************************************************************/

package com.example.brickbreakout;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class Options extends TitleScreen
{
    private RadioGroup difficultyRG, soundEffectsRG, backgroundRG;
    private Spinner musicSpinner;





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        //hook up the radio groups
        difficultyRG = findViewById(R.id.difficultyRG);
        difficultyRG.setOnCheckedChangeListener( difListener );

        soundEffectsRG = findViewById(R.id.soundEffectsRG);
        soundEffectsRG.setOnCheckedChangeListener(soundEffectListener);

        backgroundRG = findViewById(R.id.backgroundRG);
        backgroundRG.setOnCheckedChangeListener(backgroundListener);



        //Spinner populated by data from strings.xml file
        musicSpinner = findViewById(R.id.infoSpinner);

        //create the array adapter with the information
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.spinnerArray, R.layout.spinner_view );


        musicSpinner.setAdapter(adapter1);

        musicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selection1;

                selection1 = parent.getItemAtPosition( position ).toString();

                song = selection1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


    }//end onCreate





    //OnChecked for difficulty radio group
    private RadioGroup.OnCheckedChangeListener difListener = new RadioGroup.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            switch( checkedId )
            {
                case R.id.difButton1: difficulty = "easy";
                    break;

                case R.id.difButton2: difficulty = "medium";
                    break;

                case R.id.difButton3: difficulty = "hard";
                    break;

            }

        }
    };


    //OnChecked for sound FX radio group
    private RadioGroup.OnCheckedChangeListener soundEffectListener = new RadioGroup.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            switch( checkedId )
            {
                case R.id.soundButton1: soundEffects = true;
                    break;

                case R.id.soundButton2: soundEffects = false;
                    break;

            }

        }
    };


    //OnChecked for background radio group
    private RadioGroup.OnCheckedChangeListener backgroundListener = new RadioGroup.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            switch( checkedId )
            {
                case R.id.backgroundButton1: animBackground = true;
                    break;

                case R.id.backgroundButton2: animBackground = false;
                    break;

            }

        }
    };


    //Back Button
    public void goBack( View view )
    { finish(); }//end goBack




    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        titleMP.stop();
    }//end Destroy


}//end Options
