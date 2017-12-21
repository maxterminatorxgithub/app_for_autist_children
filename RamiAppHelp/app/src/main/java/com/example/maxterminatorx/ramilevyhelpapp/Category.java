package com.example.maxterminatorx.ramilevyhelpapp;

/**
 * Created by maxterminatorx on 20-Jul-17.
 */

import java.util.ArrayList;
import java.util.zip.Inflater;


import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Category extends android.widget.LinearLayout implements View.OnClickListener{

    private static int categoryCount = 0;

    private ImageView imgView;
    private TextView label;
    private ArrayList<ImageCell> categoryData;
    private Activity whereCreated;
    private OnSelectCategoryListener oscl;
    private ViewGroup container;
    private boolean empty = true;
    private static Category selectedCategory;
    private int offsetLoad = 0;


    public Category(String name,int id,Activity act){
        super(act);
        init(name,act);
        categoryCount--;

    }

    public Category(String name,Drawable image,Activity act){
        super(act);
        init(name,act);
        imgView.setImageDrawable(image);
    }

    public Category(String name,Activity act){
        super(act);
        init(name,act);
        imgView.setImageBitmap(setCircleBounds(230));


    }

    public Category(String name,Bitmap image,Activity act){
        super(act);
        init(name,act);
        imgView.setImageBitmap(setCircleBounds(image));
    }

    private void init(CharSequence name,Activity act){
        AppMaster.categories.add(this);
        whereCreated = act;
        LayoutInflater.from(whereCreated).inflate(R.layout.category_layout,this);
        imgView = (ImageView)this.findViewById(R.id.category_img);


        imgView.setOnClickListener(this);
        label = (TextView)this.findViewById(R.id.category_label);
        label.setText(name);
        label.setTextSize(AppMaster.Settings.fixedFontSize);
        //label.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width/6;
        label.setTypeface(AppMaster.mainTypeface);
        categoryData = new ArrayList<ImageCell>();
        categoryCount++;
    }



    public void removeImagesFromContainer(){
        this.container.removeAllViews();
    }

    public boolean isEmpty(){
        return empty;
    }

    public void select(){
        if(this==selectedCategory)
            return;
        animate().alpha(1).scaleX(1.15f).scaleY(1.15f);
        if(selectedCategory!=null)
            selectedCategory.deselect();
        selectedCategory = this;
    }

    public void deselect(){
        animate().alpha(0.5f).scaleX(1).scaleY(1);
    }

    public void addImageCell(ImageCell ic){
        categoryData.add(ic);
    }

    public static Bitmap setCircleBounds(Bitmap bt){
        int h = bt.getHeight();
        int w = bt.getWidth();
        int s = h>w?w:h;
        int offset = s/2-s/2;
        Bitmap newBit = Bitmap.createBitmap(s,s,Bitmap.Config.ARGB_8888);
        for(int i=0;i<s;i++){
            for(int j=0;j<s;j++){
                if(getDistance(j,i,s/2,s/2)<=s/2)
                    newBit.setPixel(j,i,bt.getPixel(offset+j,i));
                else
                    newBit.setPixel(j,i,0);
            }
        }
        return newBit;
    }

    public static Bitmap setCircleBounds(int size){
        int h = size;
        int w = (int)(size*1.5);
        int offset = w/2-h/2;
        Bitmap newBit = Bitmap.createBitmap(h,h,Bitmap.Config.ARGB_8888);
        int orangeapp = 0Xfff5b493;
        for(int i=0;i<h;i++){
            for(int j=0;j<h;j++){
                if(getDistance(j,i,h/2,h/2)<=h/2)
                    newBit.setPixel(j,i,orangeapp);
                else
                    newBit.setPixel(j,i,0);
            }
        }
        return newBit;
    }



    private static float getDistance(int x1,int y1,int x2,int y2){
        return (float)Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
    }

    public ImageCell getImageCellAt(int index){
        return this.categoryData.get(index);
    }


    @Override
    public void onClick(View v) {
        if(oscl!=null)
            oscl.onSelectCategory(this);

        loadDataToViewGroup();
    }

    public void setImage(Bitmap bmp,int w,int h){
        empty = false;
        imgView.setImageBitmap(setCircleBounds(bmp));

        this.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
        this.getLayoutParams().height = LayoutParams.MATCH_PARENT;
        imgView.getLayoutParams().width = w;
        imgView.getLayoutParams().height = h;
        this.setPadding(w/4,0,w/4,0);
        this.deselect();
        //this.loadDataToViewGroup();
    }

    public void setViewGroupToLoadData(ViewGroup group){
        container = group;
    }

    public void loadDataToViewGroup(){
        loadDataToViewGroup(AppMaster.Settings.inRow,AppMaster.Settings.inCol);
    }

    public void loadDataToViewGroup(int inRow, int inCol){
        container.removeAllViews();
        int w = container.getWidth(),h=container.getHeight(),c=0;
        ViewGroup.LayoutParams fl = new  ViewGroup.LayoutParams(w/inCol,
                h/inRow);

        Log.i("w:"+w,"h:"+h);

        for(int i=offsetLoad;i<categoryData.size()&&i<offsetLoad+AppMaster.Settings.inCol*AppMaster.Settings.inRow;i++) {
            categoryData.get(i).setLayoutParams(fl);
            container.addView(categoryData.get(i));
        }
    }

    public void nextOffset(){
        if(offsetLoad+AppMaster.Settings.inRow*AppMaster.Settings.inCol>=categoryData.size())
            return;
        offsetLoad+=AppMaster.Settings.inRow*AppMaster.Settings.inCol;
    }

    public void prevOffset(){
        if(offsetLoad-AppMaster.Settings.inRow*AppMaster.Settings.inCol<0)
            return;
        offsetLoad-=AppMaster.Settings.inRow*AppMaster.Settings.inCol;
    }

    public void setOnSelectCategoryListener(OnSelectCategoryListener oscl){
        this.oscl = oscl;
    }


    public String getLabelText(){
        return (String)label.getText();
    }

    public int getImageCellCount(){
        return this.categoryData.size();
    }

    public void setLabel(String categoryLabel) {
        label.setText(categoryLabel);
    }


    interface OnSelectCategoryListener{
        void onSelectCategory(Category c);
    }


    public void setImageSize(int size){
        this.imgView.getLayoutParams().width = size;this.imgView.getLayoutParams().height = size;
    }

    public int getIndexOfCell(ImageCell ic){
        return categoryData.indexOf(ic);
    }

    public void reset(){
        while(categoryData.size()!=0){
            categoryData.remove(0);
        }
        categoryData.add(new ImageCell(AppMaster.mainActivity,this));
    }
}
