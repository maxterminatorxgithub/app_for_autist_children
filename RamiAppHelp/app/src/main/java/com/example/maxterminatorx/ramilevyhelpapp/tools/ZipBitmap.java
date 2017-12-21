package com.example.maxterminatorx.ramilevyhelpapp.tools;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by maxterminatorx on 21-Dec-17.
 */

public class ZipBitmap {

    public static int[] parse(Bitmap bmp){

        int w=bmp.getWidth(),h=bmp.getHeight();


        ArrayList<Integer> dynamic = new ArrayList<>();
        dynamic.add(w);
        dynamic.add(h);

        int indentifier=bmp.getPixel(0,0),counter=0;

        for(int i=0;i<w;i++) {
            for (int j = 0; j < h; j++) {
                int currentColor = bmp.getPixel(i,j);

                if(currentColor==indentifier){
                    counter++;
                }else{
                    dynamic.add(indentifier);
                    dynamic.add(counter);

                    indentifier = currentColor;
                    counter=1;


                }

                if(i==w-1&&j==h-1){
                    dynamic.add(indentifier);
                    dynamic.add(counter);
                }
            }
        }

        int[] array = new int[dynamic.size()];

        for(int i=0;i<array.length;i++)
            array[i]=dynamic.get(i);

        return array;
    }

    public static Bitmap toBitmap(int[] array){

        int w=array[0],h=array[1];

        Bitmap bmp = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);


        int x=0,y=0;

        for(int i=2;i<array.length;i+=2){

            int color = array[i];
            int counter = array[i+1];

            while(counter>0){

                bmp.setPixel(x,y,color);

                counter--;
                x++;
                if(x==w){
                    y++;
                    x=0;
                }
            }

        }


        return null;
    }

}
