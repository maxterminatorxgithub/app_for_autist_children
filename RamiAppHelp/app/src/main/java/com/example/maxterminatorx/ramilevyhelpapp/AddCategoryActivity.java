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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by maxterminatorx on 10-Oct-17.
 */

public class AddCategoryActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int UPLOAD_FILE = 1;
    public static final int TAKE_PHOTO = 2;

    private EditText categoryName;
    private Button takePhoto;
    private Button uploadFile;
    private Button addCategory;
    private ImageView subject;

    static String categoryLabel;
    static Bitmap photoForCategory;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        setContentView(R.layout.category_set_name_dialog);
        init();
        if(photoForCategory!=null)
            this.subject.setImageBitmap(photoForCategory);

        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.category_set_name_dialog);

        init();
    }

    private void init(){
        categoryName = (EditText)findViewById(R.id.category_name);
        categoryName.setTextSize(AppMaster.Settings.fixedFontSize*1.5f);
        categoryName.setTypeface(AppMaster.mainTypeface);


        takePhoto = (Button) findViewById(R.id.btn_photo);
        takePhoto.setTypeface(AppMaster.mainTypeface);
        takePhoto.setTextSize(AppMaster.Settings.fixedFontSize*1.5f);
        takePhoto.setOnClickListener(this);

        uploadFile = (Button) findViewById(R.id.btn_file);
        uploadFile.setTypeface(AppMaster.mainTypeface);
        uploadFile.setTextSize(AppMaster.Settings.fixedFontSize*1.5f);
        uploadFile.setOnClickListener(this);

        addCategory = (Button) findViewById(R.id.btn_add);
        addCategory.setTypeface(AppMaster.mainTypeface);
        addCategory.setTextSize(AppMaster.Settings.fixedFontSize*1.5f);
        addCategory.setVisibility(View.INVISIBLE);
        addCategory.setOnClickListener(this);

        subject = (ImageView)findViewById(R.id.image_selected);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {


            case R.id.btn_photo:
                Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                this.startActivityForResult(in,TAKE_PHOTO);

            return;
            case R.id.btn_file:
                in = new Intent(Intent.ACTION_PICK);

                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                Uri path = Uri.parse(directory.getPath());

                in.setDataAndType(path,"image/*");

                this.startActivityForResult(in,UPLOAD_FILE);
                return;
            case R.id.btn_add:
                if (categoryName.getText().toString().equals("")){
                    Toast.makeText(this,"לא הוכנס שם לקטגוריה",Toast.LENGTH_SHORT).show();
                    return;
                }

                //AppMaster.imageCapturedCategory.getLayoutParams().width= ViewGroup.LayoutParams.WRAP_CONTENT;
                //AppMaster.imageCapturedCategory.getLayoutParams().height=ViewGroup.LayoutParams.MATCH_PARENT;
                AppMaster.imageCapturedCategory.setImage(AddCategoryActivity.photoForCategory,AppMaster.iconCategorySize,AppMaster.iconCategorySize);
                AppMaster.imageCapturedCategory.setLabel(categoryName.getText().toString());
                try {
                    AppMaster.storageManager.saveImageJPEGForCategory(photoForCategory,
                            AppMaster.imageCapturedCategory);
                }catch(IOException ioex){

                }

                this.finish();
                ((ParentModeActivity)AppMaster.mainActivity).onClick(v);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == TAKE_PHOTO){
                Bitmap photo = (Bitmap)data.getExtras().get("data");
                photoForCategory = photo;
                this.subject.setImageBitmap(photo);

                this.addCategory.setVisibility(View.VISIBLE);

            }else if(requestCode == UPLOAD_FILE){
                Uri imageUri = data.getData();
                try {
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap photo = BitmapFactory.decodeStream(is);
                    photoForCategory = photo;
                    this.subject.setImageBitmap(photo);
                    this.addCategory.setVisibility(View.VISIBLE);

                }catch(IOException ioex){
                    Toast.makeText(this,"אין אפשרות לפתוח את התמונה",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
