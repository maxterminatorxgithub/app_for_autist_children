package com.example.maxterminatorx.ramilevyhelpapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.ViewGroup;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


/**
 * Created by maxterminatorx on 18-Jul-17.
 */

public class FileStorageManager {

    private File dirToSave;

    private MediaRecorder audioRecorder;
    private String tempPath;

    public FileStorageManager(String folder) {
        dirToSave = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+folder);
        if(!dirToSave.exists())
            dirToSave.mkdirs();


    }

    public void saveImageJPEGForCategory(Bitmap image,Category c) throws IOException{
        File categoryDir = new File(dirToSave.getAbsolutePath()+"/"+c.getLabelText());
        if(!categoryDir.exists())
            categoryDir.mkdirs();



        File file =new File(categoryDir,"category.jpg");
        FileOutputStream fOut = new FileOutputStream(file);
        image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);

        fOut.flush();
        fOut.close();
    }


    public void saveImageJPEGForCell(ImageCell ic,Bitmap image) throws IOException{
        File imgCellDir = new File(dirToSave.getAbsolutePath()+"/"
                +ic.getCategory().getLabelText()+"/"+ic.getCategory().getIndexOfCell(ic));
        if(!imgCellDir.exists())
            imgCellDir.mkdirs();



        File file =new File(imgCellDir,"img.jpg");
        FileOutputStream fOut = new FileOutputStream(file);
        image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);

        fOut.flush();
        fOut.close();
    }

    public void saveSoundForCell(ImageCell ic) throws IOException{
        File imgCellDir = new File(dirToSave.getAbsolutePath()+"/"
                +ic.getCategory().getLabelText()+"/"+ic.getCategory().getIndexOfCell(ic));
        if(!imgCellDir.exists())
            imgCellDir.mkdirs();



        File file =new File(imgCellDir,"sound.3gp");
        FileOutputStream fOut = new FileOutputStream(file);

        File temp = new File(dirToSave,"temp.3gp");
        FileInputStream fIn = new FileInputStream(temp);


        byte[] data = new byte[fIn.available()];
        fIn.read(data);
        fOut.write(data);



        fOut.flush();
        fOut.close();
    }

    public void loadSoundForCell(ImageCell ic)throws IOException{
        File imgCellDir = new File(dirToSave.getAbsolutePath()+"/"
                +ic.getCategory().getLabelText()+"/"+ic.getCategory().getIndexOfCell(ic)+"/sound.3gp");

        if(!imgCellDir.exists())
            return;

        MediaPlayer mp = new MediaPlayer();

        mp.setDataSource(imgCellDir.getAbsolutePath());
        ic.setMediaSound(mp);
    }



    public void uploadCategories(final Activity act,final ViewGroup vGroup ,final ViewGroup container,final ViewGroup addCategoryPanel,int size) throws IOException{



        new AsyncTask<Void,Void,File[]>(){

            @Override
            protected File[] doInBackground(Void... params) {
                ArrayList<Bitmap> images = new ArrayList<Bitmap>();

                return dirToSave.listFiles();
            }

            @Override
            protected void onPostExecute(File folders[]) {
                if(folders == null)
                    folders = new File[0];
                String name = "";
                for(File folder:folders){

                    if(folder.isDirectory()){
                        Category newCategory = new Category(folder.getName(),act);
                        newCategory.setViewGroupToLoadData(container);
                        newCategory.setOnSelectCategoryListener((ParentModeActivity)act);
                        vGroup.addView(newCategory,0);

                        Bitmap image = BitmapFactory.decodeFile(folder.getAbsolutePath()+"/category.jpg");
                        newCategory.setImage(image,size,size);



                        for(int i = 0;true;i++){
                            File cellDirection = new File(dirToSave.getAbsolutePath()+"/"
                                    + folder.getName()+"/"+i);
                            if(!cellDirection.exists())
                                break;
                            ImageCell ic = new ImageCell(act,newCategory);
                            newCategory.addImageCell(ic);
                        }


                        laodImageCells(newCategory);
                        newCategory.addImageCell(new ImageCell(AppMaster.mainActivity,newCategory));
                        AppMaster.categories.add(newCategory);

                    }
                }

                if(act instanceof ParentModeActivity) {
                    Category addCategory = new Category("הוסף קטגוריה", act);

                    addCategory.setOnSelectCategoryListener((Category.OnSelectCategoryListener) act);
                    addCategory.setViewGroupToLoadData(container);
                    addCategory.addImageCell(new ImageCell(act, addCategory));

                    addCategoryPanel.addView(addCategory);


                    Log.i("categories ids:", String.valueOf(AppMaster.categories.size()));
                }
            }
        }.execute();

    }





    public void laodImageCells(final Category c){
        new AsyncTask<Void,Void,ArrayList<Bitmap>>(){
            @Override
            protected ArrayList<Bitmap> doInBackground(Void... params) {

                ArrayList<Bitmap> dataimages = new ArrayList<Bitmap>();
                    for(int i=0;true;i++) {
                        File f = new File(dirToSave.getAbsolutePath()+"/"+c.getLabelText()+"/"
                        +i+"/img.jpg");

                        if(!f.exists()) {
                            return dataimages;
                        }
                        dataimages.add(BitmapFactory.decodeFile(f.getAbsolutePath()));
                    }
            }

            @Override
            protected void onPostExecute(ArrayList<Bitmap> s) {

                int count = 0;
                for(Bitmap bmp:s) {
                    ImageCell ic = c.getImageCellAt(count);
                    ic.getImage().setImageBitmap(bmp);
                    count++;
                }

            }
        }.execute();
    }


    public void startTempRecord() throws java.io.IOException{
        audioRecorder = new MediaRecorder();
        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        audioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        audioRecorder.setOutputFile(tempPath = dirToSave.getAbsolutePath()+"/temp.3gp");



        audioRecorder.prepare();
        audioRecorder.start();
    }

    public void stopTempRecord(){
        audioRecorder.stop();
        audioRecorder.release();
    }

    public String getTempRecord(){
        return tempPath;
    }


    public boolean deleteCategory(Category c){
        File f = new File(dirToSave.getAbsolutePath()+"/"+c.getLabelText());

        return deletePath(f);
    }

    public boolean resetCategory(Category c){
        File f = new File(dirToSave.getAbsolutePath()+"/"+c.getLabelText());

        boolean isDeleted = true;
        int counter = 0;
        File cell = new File(f.getAbsolutePath(),String.valueOf(counter));
        while(cell.exists()){
            isDeleted = deletePath(new File(f.getAbsolutePath(),String.valueOf(counter)));
            counter++;
            cell = new File(f.getAbsolutePath(),String.valueOf(counter));
        }
        return isDeleted;
    }

    public static boolean deletePath(File f){
        File[] files = f.listFiles();

        for(File file:files){
            if(file.isDirectory()){
                deletePath(file);
                continue;
            }

            file.delete();
        }

        return f.delete();

    }

}
