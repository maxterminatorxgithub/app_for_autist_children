package com.example.maxterminatorx.ramilevyhelpapp;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by maxterminatorx on 11-Jul-17.
 */



public class AppMaster {


    static TelephonyManager tManager;


    public static final int PORTRAIT_MODE = 1;
    public static final int LANDSCAPE_MODE = 2;

    public static Activity currentActivity;


    static class Settings {
        static Rectangle deviceScreenSize = new Rectangle();
        static int inRow;
        static int inCol;
        static String deviceLanguage;
        static int fixedFontSize;
        static Rectangle imageCellSize = new Rectangle();
        static int orientation;
    }
    static Category imageCapturedCategory;
    static Category selectedCategory;
    static Typeface mainTypeface;
    static FileStorageManager storageManager;

    static HashMap<String,View> views;

    static ArrayList<Category>categories;

    static Activity mainActivity;

    static String simCode = "";

    static int iconCategorySize = 100;

    static ServerConnectorSocket server;


    static boolean checkPerimissions(Context con){

        return  ContextCompat.checkSelfPermission(con, Manifest.permission.READ_PHONE_STATE)
                        == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(con, Manifest.permission.INTERNET)
                        == PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(con, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(con, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(con, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(con, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED;
    }

    static void requestRequeredPermissions(Context con){

        if(ContextCompat.checkSelfPermission(con, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions((Activity) con, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);

        if(ContextCompat.checkSelfPermission(con, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED)
        ActivityCompat.requestPermissions((Activity) con, new String[]{Manifest.permission.INTERNET}, 1);

        if(ContextCompat.checkSelfPermission(con, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        ActivityCompat.requestPermissions((Activity) con, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        if(ContextCompat.checkSelfPermission(con, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        ActivityCompat.requestPermissions((Activity) con, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        if(ContextCompat.checkSelfPermission(con, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        ActivityCompat.requestPermissions((Activity) con, new String[]{Manifest.permission.CAMERA}, 1);

        if(ContextCompat.checkSelfPermission(con, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED)
        ActivityCompat.requestPermissions((Activity) con, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
    }

    static void determineRowsAndCols(int longs,int shorts,int mode){
        Settings.inCol = mode == PORTRAIT_MODE ? shorts:longs;
        Settings.inRow = mode == LANDSCAPE_MODE ? shorts:longs;
    }

    static void init(Activity act){

        if(server == null)
            server = new ServerConnectorSocket(ServerCommandsAndKeys.MY_SERVER_IP, ServerCommandsAndKeys.SERVER_PORT);


        currentActivity = act;

        tManager = (TelephonyManager) act.getSystemService(Service.TELEPHONY_SERVICE);


        categories = new ArrayList();

        Settings.deviceScreenSize.width = act.getWindowManager().getDefaultDisplay().getWidth();
        Settings.deviceScreenSize.height = act.getWindowManager().getDefaultDisplay().getHeight();

        Settings.orientation = Settings.deviceScreenSize.width>Settings.deviceScreenSize.height
                ?LANDSCAPE_MODE:PORTRAIT_MODE;


        DisplayMetrics metrics = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int longs = 0,shorts = 0;
        switch(metrics.densityDpi){

            case DisplayMetrics.DENSITY_XXXHIGH:

                break;

            case 570:

                break;

            case DisplayMetrics.DENSITY_560:
                Settings.fixedFontSize = 25;
                iconCategorySize = 240;
                if(Settings.orientation == PORTRAIT_MODE) {
                    longs = 3;
                    shorts = 2;
                }
                else{
                    longs = 3;
                    shorts = 2;
                }

                break;

            case DisplayMetrics.DENSITY_XXHIGH:
                Settings.fixedFontSize = 17;
                longs = 3;
                shorts = 2;
                break;

            case DisplayMetrics.DENSITY_420:
                break;
            case DisplayMetrics.DENSITY_400:
                break;

            case DisplayMetrics.DENSITY_360:
                break;

            case DisplayMetrics.DENSITY_340:
                break;
            case DisplayMetrics.DENSITY_300:
                break;
            case DisplayMetrics.DENSITY_280:
                break;
            case DisplayMetrics.DENSITY_260:
                break;
            case DisplayMetrics.DENSITY_HIGH:
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                Settings.fixedFontSize = 20;
                iconCategorySize = 128;
                if(Settings.orientation==PORTRAIT_MODE && Settings.deviceScreenSize.width > 500 ||
                        Settings.orientation==LANDSCAPE_MODE && Settings.deviceScreenSize.width > 900) {
                    Settings.fixedFontSize = 35;
                    iconCategorySize = 180;
                }
                longs = 3;
                shorts = 2;
                break;


            case DisplayMetrics.DENSITY_LOW:
                Settings.fixedFontSize = 15;
                if(Settings.orientation == PORTRAIT_MODE) {
                    longs = 2;
                    shorts = 2;
                }
                else{
                    longs = 3;
                    shorts = 1;
                }

                break;


            case DisplayMetrics.DENSITY_XHIGH:
                Settings.fixedFontSize = 20;
                iconCategorySize = 156;

                if(Settings.orientation==PORTRAIT_MODE && Settings.deviceScreenSize.width > 800 ||
                        Settings.orientation==LANDSCAPE_MODE && Settings.deviceScreenSize.width > 1300) {
                    Settings.fixedFontSize = 45;
                    iconCategorySize = 240;
                }
                longs = 4;
                shorts = 2;
                break;


        }


        determineRowsAndCols(longs,shorts,Settings.orientation);

        mainTypeface = Typeface.createFromAsset(act.getAssets(),"fonts/FbBejerano-Bold.otf");

        storageManager = new FileStorageManager("ramiapps/outizem_help_app");

        Settings.deviceLanguage = Resources.getSystem().getConfiguration().locale.getLanguage();
    }

    static void initCategoriesFromStorage(Activity act,ViewGroup vGroup , ViewGroup container , ViewGroup addCategoryPanel){
        try {
            storageManager.uploadCategories(act, vGroup , container , addCategoryPanel,iconCategorySize);
        }catch(IOException ioex){

        }
    }






    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    static void initParentMode(Activity act , HashMap<String,View> views){
        ((TextView)views.get("childLabel")).setTextColor(act.getResources().getColor(R.color.appDark));
        ((TextView)views.get("parentLabel")).setTextColor(act.getResources().getColor(R.color.appRed));
        ((TextView)views.get("modeScreenLabel")).setText("מסך הורים");
        LinearLayout mainPanel = (LinearLayout) views.get("mainPanel");
        mainPanel.removeAllViews();

        mainPanel.addView(views.get("controlPanel"));
        mainPanel.addView(views.get("categoryPanel"));
        mainPanel.addView(views.get("contentPanel"));

    }

    static void initChildMode(Activity act , HashMap<String,View> views){
        AppMaster.views=views;
        ((TextView)views.get("childLabel")).setTextColor(act.getResources().getColor(R.color.appRed));
        ((TextView)views.get("parentLabel")).setTextColor(act.getResources().getColor(R.color.appDark));
        ((TextView)views.get("modeScreenLabel")).setText("מה אני רוצה");
        LinearLayout mainPanel = (LinearLayout) views.get("mainPanel");
        mainPanel.removeAllViews();

        LayoutInflater.from(act).inflate(R.layout.child_mode_layout,mainPanel);

        HashMap<String,ImageView> childOps = new HashMap<>();

        //childOps.put("optionPlay",(ImageView) mainPanel.findViewById(R.id.child_mode_option_play));
        //childOps.put("optionEat",(ImageView) mainPanel.findViewById(R.id.child_mode_option_eat));
        //childOps.put("optionPaint",(ImageView) mainPanel.findViewById(R.id.child_mode_option_paint));
        //childOps.put("optionHowIFeel",(ImageView) mainPanel.findViewById(R.id.child_mode_option_how_i_feel));
        //childOps.put("optionToilet",(ImageView) mainPanel.findViewById(R.id.child_mode_option_toilet));
        //childOps.put("optionDrink",(ImageView) mainPanel.findViewById(R.id.child_mode_option_drink));
        //childOps.put("optionHug",(ImageView) mainPanel.findViewById(R.id.child_mode_option_hug));
        //childOps.put("optionWatchTV",(ImageView) mainPanel.findViewById(R.id.child_mode_option_watch_tv));
        //childOps.put("optionGoToSchool",(ImageView) mainPanel.findViewById(R.id.child_mode_option_go_to_school));
        //childOps.put("optionPlayTablet",(ImageView) mainPanel.findViewById(R.id.child_mode_option_play_tablet));
        //childOps.put("optionSleep",(ImageView) mainPanel.findViewById(R.id.child_mode_option_sleep));
        //childOps.put("optionMusic",(ImageView) mainPanel.findViewById(R.id.child_mode_option_music));



        LinearLayout gl = mainPanel.findViewById(R.id.main_layout);

        Listeners.onChildMainOptionListener.setComponents(act,mainPanel,childOps);

        for(ImageView iv:childOps.values()) {
            iv.setOnClickListener(Listeners.onChildMainOptionListener);
            //iv.getLayoutParams().width = Settings.deviceScreenSize.width/ gl.getColumnCount();
            //iv.getLayoutParams().height = (Settings.deviceScreenSize.height-Settings.deviceScreenSize.height)/gl.getRowCount();
        }


    }




    static boolean checkChildCode(String code){

        return true;
    }

    static boolean checkParentCode(Activity act ,String code) {

        if(code == null || code.isEmpty()) {
            Toast.makeText(act,"לא הוזן ת.ז.",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    static void checkSimCard() {
        try {
            simCode = tManager.getSimSerialNumber();

        }catch(SecurityException se){
            Toast.makeText(AppMaster.currentActivity,"כרטיס SIM לא ניתן לקריאה",Toast.LENGTH_SHORT).show();
            simCode = "";
        }

        Log.i("Sim",simCode);
        System.out.print("the sim code is: "+simCode);
    }
}
