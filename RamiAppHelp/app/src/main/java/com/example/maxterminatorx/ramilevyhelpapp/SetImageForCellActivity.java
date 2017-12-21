package com.example.maxterminatorx.ramilevyhelpapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by maxterminatorx on 24-Oct-17.
 */

public class SetImageForCellActivity extends AppCompatActivity implements View.OnClickListener{


    public static final int TAKE_PHOTO = 1;
    public static final int UPLOAD_FILE = 2;

    private ImageView img;
    private Button photo;
    private Button file;
    private Button update;
    private static Bitmap data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_image_for_cell);

        init();


    }

    private void init(){
        img = (ImageView) findViewById(R.id.img);

        photo = (Button)findViewById(R.id.photo_button);
        photo.setTypeface(AppMaster.mainTypeface);
        photo.setTextSize(AppMaster.Settings.fixedFontSize*2);
        photo.setOnClickListener(this);

        file = (Button)findViewById(R.id.file_buttton);
        file.setTypeface(AppMaster.mainTypeface);
        file.setTextSize(AppMaster.Settings.fixedFontSize*2);
        file.setOnClickListener(this);

        update = (Button)findViewById(R.id.add_button);
        update.setTypeface(AppMaster.mainTypeface);
        update.setTextSize(AppMaster.Settings.fixedFontSize*2);
        update.setVisibility(View.INVISIBLE);
        update.setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == TAKE_PHOTO){
                this.data = (Bitmap)data.getExtras().get("data");
                this.img.setImageBitmap(this.data);
                this.update.setVisibility(View.VISIBLE);
            }
            else if(requestCode == UPLOAD_FILE){
                Uri imageUri = data.getData();
                try {
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    this.data=BitmapFactory.decodeStream(is);
                    this.img.setImageBitmap(this.data);
                    this.update.setVisibility(View.VISIBLE);
                }catch(IOException ioex){
                    Toast.makeText(this,"אין אפשרות לפתוח את התמונה",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent in;
        switch(v.getId()){

            case R.id.photo_button:
                in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                this.startActivityForResult(in,TAKE_PHOTO);
                return;
            case R.id.file_buttton:
                in = new Intent(Intent.ACTION_PICK);

                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                Uri path = Uri.parse(directory.getPath());

                in.setDataAndType(path,"image/*");

                this.startActivityForResult(in,UPLOAD_FILE);
                return;
            case R.id.add_button:
                ImageCell.selectedCell.getImage().setImageBitmap(this.data);
                if(ImageCell.selectedCell.getCellType() == ImageCell.CellType.UNDEFINED) {
                    ImageCell.selectedCell.setCellType(ImageCell.CellType.PIC_AND_SOUND);
                    ImageCell.selectedCell.getCategory().addImageCell(new ImageCell(this,ImageCell.selectedCell.getCategory()));
                    ImageCell.selectedCell.getCategory().loadDataToViewGroup();
                }
                try {
                    AppMaster.storageManager.saveImageJPEGForCell(ImageCell.selectedCell, this.data);
                }catch(IOException ioex){
                    ioex.printStackTrace();
                }
                this.finish();

        }
    }
}
