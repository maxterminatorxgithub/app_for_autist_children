package com.example.maxterminatorx.ramilevyhelpapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maxterminatorx.ramilevyhelpapp.pages.ContactUsActivity;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

/**
   @author maxterminatorx
 * Created by maxterminatorx on 13-Nov-17.
 */

public class ChildModeActivity extends AppCompatActivity implements View.OnClickListener{


    private GridLayout table;
    private TextView title;

    private String page;
    private String category;
    private View categoryView;

    private EditText parentCode;
    private LinearLayout parentCodeLayout;
    private AlertDialog.Builder dialog;
    private MediaPlayer mainPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        setContentView(R.layout.child_mode_layout);

        category = "";
        table = (GridLayout)findViewById(R.id.layout_table_ops);

        if(AppMaster.Settings.orientation==AppMaster.PORTRAIT_MODE)
            table.setPadding(0,0,0,(int)(AppMaster.Settings.deviceScreenSize.height*(8f/100)));
        else
            table.setPadding(0,0,0,(int)(AppMaster.Settings.deviceScreenSize.height*(9f/100)));

        title = (TextView)findViewById(R.id.mode_screen_txt);
        title.setTypeface(AppMaster.mainTypeface);
        title.setTextSize(AppMaster.Settings.fixedFontSize*2.5f);

        initMain();

        dialog = new AlertDialog.Builder(this);



        parentCodeLayout = new LinearLayout(this);

        parentCode = new EditText(this);
        parentCode.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        parentCode.setTextSize(AppMaster.Settings.fixedFontSize);
        parentCode.setHint("הורה נא להכניס ת.ז.");
        parentCode.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        parentCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!(s.length()==0))
                    if(s.charAt(s.length()-1)<'0'||s.charAt(s.length()-1)>'9') {
                        if(s.length()==1)
                            s.delete(0,1);
                        else
                            s.delete(s.length() - 1, s.length());

                    }
                if(s.length()>9)
                    s.delete(s.length() - 1, s.length());
            }
        });


        parentCodeLayout.addView(parentCode);


        dialog.setTitle( "קל להיות בקשר - הודעה למשתמש" )
                .setIcon(R.mipmap.ic_launcher)
                .setView(parentCodeLayout)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        if(AppMaster.checkParentCode(ChildModeActivity.this,parentCode.getText().toString())) {

                            SharedPreferences sp = getApplicationContext().getSharedPreferences("app_data",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("mode_type","none");
                            editor.commit();

                            Intent data = new Intent(ChildModeActivity.this,EntryActivity.class);
                            startActivity(data);
                            finish();
                        }

                    }
                });


    }


    public void initMain(){
        page = "main";
        title.setText("מה אני רוצה?");
        table.removeAllViews();
        table.getLayoutParams().height=ViewGroup.LayoutParams.WRAP_CONTENT;
        table.setRowCount(12/AppMaster.Settings.inRow);
        table.setColumnCount(AppMaster.Settings.inCol);
        table.getLayoutParams().height=table.getHeight()+(int)(AppMaster.Settings.deviceScreenSize.height*(20f/100));



        int width = AppMaster.Settings.deviceScreenSize.width/AppMaster.Settings.inCol,
                height = AppMaster.Settings.deviceScreenSize.height/AppMaster.Settings.inRow;

        table.addView(createOp(R.drawable.child_mode_option_paint,"לצייר",width,height));
        table.addView(createOp(R.drawable.child_mode_option_eat,"לאכול",width,height));
        table.addView(createOp(R.drawable.child_mode_option_play,"לשחק",width,height));

        table.addView(createOp(R.drawable.child_mode_option_drink,"לשתות",width,height));
        table.addView(createOp(R.drawable.child_mode_option_how_i_feel,"איך אני מרגיש",width,height));
        table.addView(createOp(R.drawable.child_mode_option_toilet,"להתפנות",width,height));

        table.addView(createOp(R.drawable.child_mode_option_go_to_school,"ללכת לבית הספר",width,height));
        table.addView(createOp(R.drawable.child_mode_option_watch_tv,"לצפות טלויזיה",width,height));
        table.addView(createOp(R.drawable.child_mode_option_hug,"רוצה חיבוק",width,height));

        table.addView(createOp(R.drawable.child_mode_option_music,"לשמוע מוזיקה",width,height));
        table.addView(createOp(R.drawable.child_mode_option_sleep,"ללכת לישון",width,height));
        table.addView(createOp(R.drawable.child_mode_option_tablet,"טאבלט",width,height));

        table.getLayoutParams().height=table.getHeight()+(int)(AppMaster.Settings.deviceScreenSize.height*(80f/100));





    }


    public void initPlay() {
        page = "play";
        title.setText("מה אני רוצה לשחק?");
        table.removeAllViews();
        table.getLayoutParams().height=ViewGroup.LayoutParams.WRAP_CONTENT;

        int width = AppMaster.Settings.deviceScreenSize.width/AppMaster.Settings.inCol,
                height = AppMaster.Settings.deviceScreenSize.height/AppMaster.Settings.inRow;

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ramiapps/outizem_help_app";
        File[] categoryList = new File(path).listFiles();


        table.setRowCount(12+(categoryList==null?0:categoryList.length)/AppMaster.Settings.inRow);
        table.setColumnCount(AppMaster.Settings.inCol);
        table.getLayoutParams().height=AppMaster.Settings.deviceScreenSize.height+(int)(AppMaster.Settings.deviceScreenSize.height*(20f/100));

        table.addView(createOp(R.drawable.play_op_1,"לצבוע",width,height));
        table.addView(createOp(R.drawable.play_op_2,"ספורט",width,height));
        table.addView(createOp(R.drawable.play_op_3,"לשחק בכדור",width,height));
        table.addView(createOp(R.drawable.play_op_4,"לבנות",width,height));
        table.addView(createOp(R.drawable.play_op_5,"להתחפש",width,height));
        table.addView(createOp(R.drawable.play_op_6,"לשחק עם חיות",width,height));
        table.addView(createOp(R.drawable.play_op_7,"כדורגל",width,height));
        table.addView(createOp(R.drawable.play_op_8,"לשחק עם בובות",width,height));
        table.addView(createOp(R.drawable.play_op_9,"ללכת לטייל",width,height));
        table.addView(createOp(R.drawable.play_op_10,"לשחק בטאבלט",width,height));

        table.getLayoutParams().height=table.getHeight()+(int)(AppMaster.Settings.deviceScreenSize.height*(20f/100));

        addCategories(width,height);
    }

    public void initPaint() {
        page = "paint";
        title.setText("מה אני רוצה לשחק?");
        table.removeAllViews();
        table.getLayoutParams().height=ViewGroup.LayoutParams.WRAP_CONTENT;

        int width = AppMaster.Settings.deviceScreenSize.width/AppMaster.Settings.inCol,
                height = AppMaster.Settings.deviceScreenSize.height/AppMaster.Settings.inRow;

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ramiapps/outizem_help_app";
        File[] categoryList = new File(path).listFiles();
        table.setRowCount(12+(categoryList==null?0:categoryList.length)/AppMaster.Settings.inRow);
        table.setColumnCount(AppMaster.Settings.inCol);
        table.getLayoutParams().height=AppMaster.Settings.deviceScreenSize.height+(int)(AppMaster.Settings.deviceScreenSize.height*(20f/100));


        table.getLayoutParams().height=table.getHeight()+(int)(AppMaster.Settings.deviceScreenSize.height*(20f/100));

        addCategories(width,height);
    }

    public void initHowIFeel(){
        page = "how_i_feel";
        title.setText("איך אני מרגיש?");
        table.removeAllViews();
        table.getLayoutParams().height=ViewGroup.LayoutParams.WRAP_CONTENT;

        int width = AppMaster.Settings.deviceScreenSize.width/AppMaster.Settings.inCol,
                height = AppMaster.Settings.deviceScreenSize.height/AppMaster.Settings.inRow;

        table.setRowCount(12/AppMaster.Settings.inRow);
        table.setColumnCount(AppMaster.Settings.inCol);
        table.getLayoutParams().height=AppMaster.Settings.deviceScreenSize.height+(int)(AppMaster.Settings.deviceScreenSize.height*(20f/100));

        table.addView(createOp(R.drawable.smile_1,"לתת הגדרה",width,height));
        table.addView(createOp(R.drawable.smile_cool,"לתת הגדרה",width,height));
        table.addView(createOp(R.drawable.smile_happy,"לתת הגדרה",width,height));
        table.addView(createOp(R.drawable.smile_heat,"לא מרגיש טוב",width,height));
        table.addView(createOp(R.drawable.smile_like,"אוהב",width,height));
        table.addView(createOp(R.drawable.smile_mad,"כועס",width,height));
        table.addView(createOp(R.drawable.smile_panic,"מתפלא",width,height));
        table.addView(createOp(R.drawable.smile_prey,"לתת הגדרה",width,height));
        table.addView(createOp(R.drawable.smile_sick,"חולה",width,height));
        table.addView(createOp(R.drawable.smile_simple,"שמח",width,height));
        table.addView(createOp(R.drawable.smile_sleep,"עייף",width,height));
        table.addView(createOp(R.drawable.smile_think,"תן לי לחשוב",width,height));

        table.getLayoutParams().height=table.getHeight()+(int)(AppMaster.Settings.deviceScreenSize.height*(20f/100));

    }

    public void initFood(){
        page = "food";
        title.setText("מה אני רוצה לאכול?");
        table.removeAllViews();
        table.getLayoutParams().height=ViewGroup.LayoutParams.WRAP_CONTENT;

        int width = AppMaster.Settings.deviceScreenSize.width/AppMaster.Settings.inCol,
                height = AppMaster.Settings.deviceScreenSize.height/AppMaster.Settings.inRow;

        table.setRowCount(12/AppMaster.Settings.inRow);
        table.setColumnCount(AppMaster.Settings.inCol);
        table.getLayoutParams().height=AppMaster.Settings.deviceScreenSize.height+(int)(AppMaster.Settings.deviceScreenSize.height*(20f/100));

        table.addView(createOp(R.drawable.food_01,"מלפפון",width,height));
        table.addView(createOp(R.drawable.food_02,"עגבניה",width,height));
        table.addView(createOp(R.drawable.food_03,"טונה",width,height));
        table.addView(createOp(R.drawable.food_04,"חביתה",width,height));
        table.addView(createOp(R.drawable.food_05,"ביצים",width,height));
        table.addView(createOp(R.drawable.food_06,"גבינה",width,height));
        table.addView(createOp(R.drawable.food_07,"קורסון",width,height));
        table.addView(createOp(R.drawable.food_08,"זיתים",width,height));
        table.addView(createOp(R.drawable.food_09,"אבוקדו",width,height));
        table.addView(createOp(R.drawable.food_10,"תירס",width,height));
        table.addView(createOp(R.drawable.food_11,"חמאה",width,height));
        table.addView(createOp(R.drawable.food_12,"פנקייקס",width,height));


        addCategories(width,height);
    }

    public void addCategories(int width,int height){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ramiapps/outizem_help_app";
        File[] categoryList = new File(path).listFiles();

        if(categoryList == null){
            Toast.makeText(this,"לא נמצאו קטגוריות.",Toast.LENGTH_LONG).show();
            return;
        }

        category = "";



        for(File file:categoryList){
            if(file.isDirectory()) {
                String fileName = file.getName();
                Bitmap map = BitmapFactory.decodeFile(file.getAbsolutePath()+"/category.jpg");
                table.addView(createOp(map,file.getName(),width,height));
            }
        }

        table.getLayoutParams().height=table.getHeight()+(int)(AppMaster.Settings.deviceScreenSize.height*(10f/100));



        table.setRowCount(categoryList.length/AppMaster.Settings.inRow+1);
        table.setColumnCount(AppMaster.Settings.inCol);
    }

    public void initCategory(View view){
        category = (String)view.getTag();
        categoryView = view;
        table.removeAllViews();
        table.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        File[] imageCells = new File(Environment.getExternalStorageDirectory().
                getAbsolutePath()+"/ramiapps/outizem_help_app/"+view.getTag()).listFiles();
        for(File ic:imageCells){
            if(ic.isDirectory()){
                Bitmap bmp = BitmapFactory.decodeFile(ic.getAbsolutePath()+"/img.jpg");
                ImageView iv = new ImageView(this);
                iv.setLayoutParams(new ViewGroup.LayoutParams(AppMaster.Settings.deviceScreenSize.width/
                        AppMaster.Settings.inCol,
                        AppMaster.Settings.deviceScreenSize.height/
                                AppMaster.Settings.inRow));
                iv.setImageBitmap(bmp);
                iv.setOnClickListener(v->{
                    if(mainPlayer==null||!mainPlayer.isPlaying()) {
                        mainPlayer = new MediaPlayer();
                    }else
                        return;
                    try {
                        if(mainPlayer!=null&&!mainPlayer.isPlaying()) {
                            mainPlayer.setDataSource(ic.getAbsolutePath() + "/sound.3gp");
                            mainPlayer.prepare();
                            mainPlayer.start();
                        }
                    }catch(IOException ioex){
                        Toast.makeText(this,"שגיאה בקובץ אודיו.",Toast.LENGTH_LONG).show();
                    }
                });
                table.addView(iv);

            }

        }
        table.getLayoutParams().height=table.getHeight()+(int)(AppMaster.Settings.deviceScreenSize.height*(20f/100));
    }

    private LinearLayout createOp(int drawableRef,java.lang.String text,int width,int height){

        Drawable dr = this.getResources().getDrawable(drawableRef);

        ImageView iv = new ImageView(this);
        LinearLayout.LayoutParams ill = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height*80/100);
        iv.setLayoutParams(ill);
        iv.setImageDrawable(dr);
        iv.setTag(text);
        iv.setOnClickListener(this);

        TextView tv = new TextView(this);
        LinearLayout.LayoutParams tll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height*20/100);
        tv.setLayoutParams(tll);
        tv.setText(text);
        tv.setTextSize((int)(AppMaster.Settings.fixedFontSize*1.25));
        tv.setTypeface(AppMaster.mainTypeface);
        tv.setGravity(Gravity.CENTER);

        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(iv);
        layout.addView(tv);

        return layout;
    }

    private LinearLayout createOp(Bitmap map, java.lang.String text, int width, int height){

        ImageView iv = new ImageView(this);
        LinearLayout.LayoutParams ill = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height*80/100);
        iv.setLayoutParams(ill);
        iv.setImageBitmap(map);
        iv.setTag(text);
        iv.setOnClickListener(this);

        TextView tv = new TextView(this);
        LinearLayout.LayoutParams tll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height*20/100);
        tv.setLayoutParams(tll);
        tv.setText(text);
        tv.setTextSize((int)(AppMaster.Settings.fixedFontSize*1.5));
        tv.setTypeface(AppMaster.mainTypeface);
        tv.setGravity(Gravity.CENTER);

        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(iv);
        layout.addView(tv);

        return layout;
    }


    @Override
    public void onClick(View v) {
        switch((String)v.getTag()){
            case "לצייר":
                initPaint();
                return;
            case "לאכול":
                initFood();
                return;
            case "לשחק":
                initPlay();
                return;
            case "לשתות":

                return;
            case "איך אני מרגיש":
                initHowIFeel();
                return;
            case "להתפנות":

                return;
            case "ללכת לבית הספר":

                return;
            case "לצפות טלויזיה":

                return;
            case "רוצה חיבוק":

                return;
            case "לשמוע מוזיקה":

                return;
            case "ללכת לישון":

                return;
            case "טאבלט":

                return;

//cases for initPlay
            case "לצבוע":

                return;
            case "ספורט":

                return;
            case "לשחק בכדור":

                return;
            case "לבנות":

                return;
            case "להתחפש":

                return;
            case "לשחק עם חיות":

                return;
            case "כדורגל":

                return;
            case "לשחק עם בובות":

                return;
            case "ללכת לטייל":

                return;
            case "לשחק בטאבלט":


                return;
//cases for how i feel

            case "לתת הגדרה":
            case "לא מרגיש טוב":
            case "אוהב":
            case "כועס":
            case "מתפלא":
            case "חולה":
            case "שמח":
            case "עייף":
            case "תן לי לחשוב":


            return;
            default:

                initCategory(v);



        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        AppMaster.Settings.deviceScreenSize.width = getWindowManager().getDefaultDisplay().getWidth();
        AppMaster.Settings.deviceScreenSize.height = getWindowManager().getDefaultDisplay().getHeight();

        AppMaster.Settings.orientation = newConfig.orientation;
        int temp = AppMaster.Settings.inRow;
        AppMaster.Settings.inRow = AppMaster.Settings.inCol;
        AppMaster.Settings.inCol = temp;

        table.removeAllViews();



        if(category.isEmpty())
        switch(page){
            case "main":
                initMain();
                return;
            case "paint":
                initPaint();
                return;
            case "play":
                initPlay();
                return;
            case "how_i_feel":
                initHowIFeel();
                return;
            case "food":
                initFood();
                return;

        }
        table.setColumnCount(AppMaster.Settings.inCol);

        if(!category.isEmpty())
            initCategory(categoryView);

        if(AppMaster.Settings.orientation==AppMaster.PORTRAIT_MODE)
            table.setPadding(0,0,0,(int)(AppMaster.Settings.deviceScreenSize.height*(8f/100)));
        else
            table.setPadding(0,0,0,(int)(AppMaster.Settings.deviceScreenSize.height*(9f/100)));
    }

    @Override
    public void onBackPressed() {

        if(category != null&&!category.isEmpty()){
            switch (page){
                case "paint":
                    initPaint();
                    break;
                case "play":
                    initPlay();
                    break;
                case "food":
                    initFood();
                    break;
            }
            category="";
            return;
        }



        if(page.equals("paint")||
           page.equals("play")||
           page.equals("how_i_feel")|| page.equals("food")){
            initMain();
            return;
        }

        if(page.equals("main")){
            showAlert();
            return;
        }



        //super.onBackPressed();
    }



    public void showAlert(){
        parentCode.setText("");

        if(parentCodeLayout.getParent()!=null)
            ((ViewGroup)parentCodeLayout.getParent()).removeView(parentCodeLayout);
        dialog.setView(parentCodeLayout);
        dialog.show();
    }
}
