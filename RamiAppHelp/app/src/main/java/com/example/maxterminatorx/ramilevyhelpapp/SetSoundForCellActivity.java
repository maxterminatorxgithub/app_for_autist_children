package com.example.maxterminatorx.ramilevyhelpapp;

import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class SetSoundForCellActivity extends AppCompatActivity implements View.OnClickListener{


    private Button record,play,stop,file,update;

    private MediaPlayer mp3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_record_for_cell);

        record = (Button)findViewById(R.id.record_button);
        record.setOnClickListener(this);
        record.setTypeface(AppMaster.mainTypeface);
        record.setTextSize(AppMaster.Settings.fixedFontSize*2.5f);

        play = (Button)findViewById(R.id.play_button);
        play.setOnClickListener(this);
        play.setTypeface(AppMaster.mainTypeface);
        play.setTextSize(AppMaster.Settings.fixedFontSize*2.5f);

        stop = (Button)findViewById(R.id.stop_button);
        stop.setOnClickListener(this);
        stop.setTypeface(AppMaster.mainTypeface);
        stop.setTextSize(AppMaster.Settings.fixedFontSize*2.5f);

        file = (Button)findViewById(R.id.file_button);
        file.setOnClickListener(this);
        file.setTypeface(AppMaster.mainTypeface);
        file.setTextSize(AppMaster.Settings.fixedFontSize*2.5f);

        update = (Button)findViewById(R.id.add_button);
        update.setOnClickListener(this);
        update.setTypeface(AppMaster.mainTypeface);
        update.setTextSize(AppMaster.Settings.fixedFontSize*2.5f);

        play.setEnabled(false);
        stop.setEnabled(false);

        update.setVisibility(View.INVISIBLE);

    }




    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.record_button:
                try {
                    record();

                }catch (java.io.IOException ioex){
                    this.record.setEnabled(true);
                    this.stop.setEnabled(false);
                    return;
                }

                return;
            case R.id.play_button:
                play();
                return;
            case R.id.stop_button:
                stop();
                this.update.setVisibility(View.VISIBLE);



                return;
            case R.id.file_button:

                return;
            case R.id.add_button:
                try {
                    AppMaster.storageManager.saveSoundForCell(ImageCell.selectedCell);
                    Log.i("maxis","save an sound");
                    AppMaster.storageManager.loadSoundForCell(ImageCell.selectedCell);

                }catch(IOException ioex){
                    ioex.printStackTrace();
                }
                this.finish();
        }
    }

    private void record() throws java.io.IOException{
        AppMaster.storageManager.startTempRecord();
        this.record.setEnabled(false);
        this.stop.setEnabled(true);
    }
    private void stop(){
        AppMaster.storageManager.stopTempRecord();
        this.stop.setEnabled(false);
        this.record.setEnabled(true);
        this.play.setEnabled(true);
    }
    private void play(){
        MediaPlayer mp3player = new MediaPlayer();
        try {
            mp3player.setDataSource(AppMaster.storageManager.getTempRecord());
            mp3player.prepare();
            mp3player.start();
        }catch (IOException ioex){
            Toast.makeText(this,"לא ניתן לנגן תקליט",Toast.LENGTH_SHORT).show();
        }
    }
}
