package com.example.maxterminatorx.ramilevyhelpapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;
import android.support.v7.widget.AppCompatButton;

/**
 * Created by maxterminatorx on 18-Sep-17.
 */

public class BoardButton extends AppCompatButton {





    private int borderSize;
    private int borderColor;

    public BoardButton(Context context) {
        super(context);
    }

    public BoardButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BoardButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setBorderColor(int borderColor){
        this.borderColor=borderColor;
    }

    public void setBorderSize(int borderSize){
        this.borderSize=borderSize;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        Paint p = new Paint();
        p.setARGB(255,0,0,0);


        float w=getWidth(),h=getHeight();

        int val;
        if(AppMaster.Settings.deviceScreenSize.width<AppMaster.Settings.deviceScreenSize.height)
            val = AppMaster.Settings.deviceScreenSize.height*1/250;
        else
            val= AppMaster.Settings.deviceScreenSize.width*1/250;

        for(int i=0;i<val;i++) {
            canvas.drawLine(i, i, w - (i+1), i, p);

            canvas.drawLine(i, i, i, h - (i+1), p);

            canvas.drawLine(w - (i+1), i, w - (i+1), h - (i+1), p);

            canvas.drawLine(i, h - (i+1), w - (i+1), h - (i+1), p);
        }

    }
}
