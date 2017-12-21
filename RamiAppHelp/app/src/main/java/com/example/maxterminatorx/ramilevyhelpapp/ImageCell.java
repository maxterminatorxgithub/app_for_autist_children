package com.example.maxterminatorx.ramilevyhelpapp;

/**
 * Created by maxterminatorx on 11-Jul-17.
 */

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.IOException;


public class ImageCell extends RelativeLayout implements View.OnClickListener{

    public static ImageCell selectedCell = null;

    private ImageView imgView;
    private ImageView imgBtnPic;
    private ImageView imgBtnSound;
    private ImageView imgBtnOps;
    private LinearLayout mainLayout;


    private CellType ct;
    private Activity whereCreated;
    private Category root;

    private MediaPlayer mp3cell;


    public ImageCell(Activity act, Category root){
        super(act);
        this.root=root;
        whereCreated=act;
        ct = CellType.UNDEFINED;

        LayoutInflater.from(act).inflate(R.layout.image_cell_layout,this);
        mainLayout = (LinearLayout)findViewById(R.id.main_layout);
        imgView = (ImageView)findViewById(R.id.main_image);
        imgView.setOnClickListener(this);

        imgBtnPic = (ImageView)findViewById(R.id.camera_img);
        imgBtnPic.setOnClickListener(this);
        imgBtnSound = (ImageView)findViewById(R.id.sound_img);
        imgBtnSound.setOnClickListener(this);
        imgBtnOps = (ImageView)findViewById(R.id.options_img);
        imgBtnOps.setOnClickListener(this);





    }



    public void hideButtons(){
        imgBtnPic.setVisibility(INVISIBLE);
        imgBtnSound.setVisibility(INVISIBLE);
        imgBtnOps.setVisibility(INVISIBLE);
    }

    public void showButtons(){
        imgBtnPic.setVisibility(VISIBLE);
        imgBtnSound.setVisibility(VISIBLE);
        imgBtnOps.setVisibility(VISIBLE);
    }


    public ImageView getImage(){
        return this.imgView;
    }

    public void setCellType(CellType ct){
        this.ct=ct;
    }


    public CellType getCellType(){
        return ct;
    }

    public Category getCategory(){
        return root;
    }


    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        int w=params.width,h=params.height;
        mainLayout.getLayoutParams().height = h;
        mainLayout.getLayoutParams().width = w;
        imgView.getLayoutParams().height=h/5*4;
        imgBtnPic.getLayoutParams().height=h/5;
        imgBtnSound.getLayoutParams().height = h/5;
        imgBtnOps.getLayoutParams().height = h/5;
        super.setLayoutParams(params);
    }

    @Override
    public void onClick(View v){
        Intent n;
        switch (v.getId()){


            case R.id.camera_img:
                ImageCell.selectedCell =this;
                n = new Intent(whereCreated,SetImageForCellActivity.class);
                whereCreated.startActivity(n);

                break;
            case R.id.sound_img:
                ImageCell.selectedCell =this;
                n = new Intent(whereCreated,SetSoundForCellActivity.class);
                whereCreated.startActivity(n);
                break;
            case R.id.options_img:

                break;
            case R.id.main_image:
                playSound();
        }
    }


    public void setMediaSound(MediaPlayer mp3){
        this.mp3cell = mp3;
    }

    public void playSound(){
        try {
            AppMaster.storageManager.loadSoundForCell(this);
            if (mp3cell != null) {
                try {
                    mp3cell.prepare();
                    mp3cell.start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        catch(IOException ioex){}

    }


    public enum CellType{
        UNDEFINED,PIC_AND_SOUND,LINK
    }


}
