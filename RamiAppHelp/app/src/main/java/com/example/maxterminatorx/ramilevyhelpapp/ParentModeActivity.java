package com.example.maxterminatorx.ramilevyhelpapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Switch;
import android.view.Menu;
import android.widget.Toast;

import com.example.maxterminatorx.ramilevyhelpapp.pages.ContactUsActivity;
import com.example.maxterminatorx.ramilevyhelpapp.pages.OrderActivity;
import com.example.maxterminatorx.ramilevyhelpapp.pages.ProfileActivity;
import com.example.maxterminatorx.ramilevyhelpapp.pages.SettingsActivity;
import com.example.maxterminatorx.ramilevyhelpapp.pages.VideoTutorialActivity;

import java.util.HashMap;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ParentModeActivity extends AppCompatActivity
        implements View.OnClickListener,
                   Category.OnSelectCategoryListener{

    public static final int PORTRAIT_MODE = 1;
    public static final int LANDSCAPE_MODE = 2;


    private TextView appTitle;
    private TextView minusText;
    private TextView modeScreen;
    private HorizontalScrollView horizontalView;


    private BoardButton btnReset;
    private BoardButton btnDelete;
    private BoardButton btnDuplicate;
    private BoardButton btnStepRight;
    private BoardButton btnStepLeft;
    private ImageView options;
    private ImageView appIcon;
    private LinearLayout toolBarLayout;
    private LinearLayout mainPanelLayout;
    private RelativeLayout controlPanelLayout;
    private LinearLayout categoryLayout;
    private LinearLayout addCategoryLayout;
    private GridLayout contentLayout;
    static PopupMenu menuOptions;

    private FrameLayout lineSeparator;
    private boolean wasLoaded;

    private Handler resumeHandler;


    // @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.entry);

        AppMaster.mainActivity = this;

        AppMaster.init(this);

        resumeHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(AppMaster.selectedCategory!=null) {
                    AppMaster.selectedCategory.select();
                    AppMaster.selectedCategory.loadDataToViewGroup();
                }
            }
        };

        new AsyncTask<Void,Void,Void>(){

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    Thread.sleep(2000);
                }catch(InterruptedException ie){

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                setContentView(R.layout.parent_mode_layout);

                AppMaster.init(ParentModeActivity.this);
                init();

                AppMaster.initCategoriesFromStorage(ParentModeActivity.this,ParentModeActivity.this.categoryLayout,ParentModeActivity.this.contentLayout,ParentModeActivity.this.addCategoryLayout);

            }
        }.execute();


    }

    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.i("orientation: ",String.valueOf(newConfig.orientation));

        AppMaster.Settings.orientation = newConfig.orientation;

        if(wasLoaded == true) {
            contentLayout.removeAllViews();


            AppMaster.Settings.deviceScreenSize.width = getWindowManager().getDefaultDisplay().getWidth();
            AppMaster.Settings.deviceScreenSize.height = getWindowManager().getDefaultDisplay().getHeight();


            setContentView(R.layout.parent_mode_layout);
            Category c = (Category) this.addCategoryLayout.getChildAt(0);
            this.addCategoryLayout.removeView(c);
            this.categoryLayout.removeAllViews();
            this.addCategoryLayout.removeAllViews();
            init();
            this.addCategoryLayout.addView(c);

            for (Category cat : AppMaster.categories) {
                cat.removeImagesFromContainer();
                LinearLayout root = ((LinearLayout) cat.getParent());
                if (root != null) {
                    root.removeView(cat);
                }
                categoryLayout.addView(cat, 0);

                cat.setViewGroupToLoadData(this.contentLayout);

            }
            c.removeImagesFromContainer();
            c.setViewGroupToLoadData(this.contentLayout);
            if (c.getParent() != null)
                ((LinearLayout) c.getParent()).removeView(c);
            addCategoryLayout.addView(c);


            resumeHandler.sendEmptyMessage(0);
        }

        Log.i("rotation","detected");

        //super.onConfigurationChanged(newConfig);


    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        resumeHandler.sendEmptyMessage(0);
        super.onPostCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {

        super.onStart();

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        resumeHandler.sendEmptyMessage(0);
        return super.onWindowStartingActionMode(callback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }


    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
     void init(){

         //DisplayMetrics metrics = new DisplayMetrics();
         //getWindowManager().getDefaultDisplay().getMetrics(metrics);
         //switch(metrics.densityDpi){
         //    case DisplayMetrics.DENSITY_LOW:
         //        AppMaster.Settings.fixedFontSize = 90;
         //        break;
         //    case DisplayMetrics.DENSITY_MEDIUM:
         //        AppMaster.Settings.fixedFontSize = 140;
         //        break;
         //    case DisplayMetrics.DENSITY_HIGH:
         //        AppMaster.Settings.fixedFontSize = 150;
         //        break;
         //    case DisplayMetrics.DENSITY_XHIGH:
         //        AppMaster.Settings.fixedFontSize = 200;
         //        break;
         //    case DisplayMetrics.DENSITY_XXHIGH:
         //        AppMaster.Settings.fixedFontSize = 110;
        //        break;
         //    case DisplayMetrics.DENSITY_XXXHIGH:
         //        AppMaster.Settings.fixedFontSize = 125;
         //        break;
         //}


        if(AppMaster.Settings.deviceScreenSize.height>AppMaster.Settings.deviceScreenSize.width)
            AppMaster.Settings.orientation = ParentModeActivity.PORTRAIT_MODE;
         else
            AppMaster.Settings.orientation = ParentModeActivity.LANDSCAPE_MODE;


        toolBarLayout = (LinearLayout)findViewById(R.id.toolbar_layout);

        appTitle = (TextView)findViewById(R.id.app_title_txt);
        appTitle.setTypeface(AppMaster.mainTypeface);


        try {
            minusText = ((TextView) findViewById(R.id.minus_txt));
            minusText.setTypeface(AppMaster.mainTypeface);
        }catch(Exception ex){}

        modeScreen = ((TextView)findViewById(R.id.mode_screen_txt));
        modeScreen.setTypeface(AppMaster.mainTypeface);



        controlPanelLayout = (RelativeLayout)findViewById(R.id.control_panel_layout) ;
        mainPanelLayout = (LinearLayout)findViewById(R.id.main_panel_layout);


        horizontalView = (HorizontalScrollView)findViewById(R.id.horizontal_view);


        btnReset = (BoardButton)findViewById(R.id.button_reset);
        btnReset.setTypeface(AppMaster.mainTypeface);
        btnReset.setOnClickListener(this);

        btnDelete = (BoardButton)findViewById(R.id.button_delete);
        btnDelete.setTypeface(AppMaster.mainTypeface);
        btnDelete.setOnClickListener(this);

        btnDuplicate = (BoardButton)findViewById(R.id.button_duplicate);
        btnDuplicate.setTypeface(AppMaster.mainTypeface);
        btnDuplicate.setOnClickListener(this);

        btnStepRight = (BoardButton)findViewById(R.id.button_step_right);
        btnStepRight.setTypeface(AppMaster.mainTypeface);
        btnStepRight.setOnClickListener(this);

        btnStepLeft = (BoardButton)findViewById(R.id.button_step_left);
        btnStepLeft.setTypeface(AppMaster.mainTypeface);
        btnStepLeft.setOnClickListener(this);

        options = (ImageView)findViewById(R.id.options_view);
        options.setOnClickListener(this);

         menuOptions = new PopupMenu(this,options);
         menuOptions.getMenuInflater().inflate(R.menu.main_menu,menuOptions.getMenu());
         menuOptions.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
             @Override
             public boolean onMenuItemClick(MenuItem item) {
                 Intent in;

                 switch(item.getItemId()){
                     case R.id.profileMenuItem:
                         in = new Intent(AppMaster.mainActivity, ProfileActivity.class);
                         AppMaster.mainActivity.startActivity(in);
                         return true;
                     case R.id.settingsMenuItem:
                         in = new Intent(AppMaster.mainActivity, SettingsActivity.class);
                         AppMaster.mainActivity.startActivity(in);
                         return true;
                     case R.id.ordersMenuItem:
                         in = new Intent(AppMaster.mainActivity, OrderActivity.class);
                         AppMaster.mainActivity.startActivity(in);
                         return true;
                     case R.id.videoTutorialsMenuItem:
                         in = new Intent(AppMaster.mainActivity, VideoTutorialActivity.class);
                         AppMaster.mainActivity.startActivity(in);
                         return true;
                     case R.id.contactUsMenuItem:
                         in = new Intent(AppMaster.mainActivity, ContactUsActivity.class);
                         AppMaster.mainActivity.startActivity(in);
                         return true;
                     case R.id.returnToEntryMenuItem:
                         SharedPreferences sp = getApplicationContext().getSharedPreferences("app_data", Context.MODE_PRIVATE);
                         SharedPreferences.Editor edit = sp.edit();
                         edit.putString("mode_type","none");
                         edit.commit();


                         in = new Intent(AppMaster.mainActivity, EntryActivity.class);
                         AppMaster.mainActivity.startActivity(in);
                         ParentModeActivity.this.finish();
                         return true;

                     case R.id.child_list:
                         in = new Intent(AppMaster.mainActivity, ChildrenActivity.class);
                         AppMaster.mainActivity.startActivity(in);
                 }
                 return false;
             }
         });

        appIcon = (ImageView)findViewById(R.id.app_icon);

        lineSeparator = (FrameLayout)findViewById(R.id.line_separator);

        mainPanelLayout = (LinearLayout)findViewById(R.id.main_panel_layout);

        contentLayout = (GridLayout) findViewById(R.id.content_layout);
        //contentLayout.setRowCount(AppMaster.Settings.inRow);
        //contentLayout.setColumnCount(AppMaster.Settings.inCol);

         DisplayMetrics metrics = new DisplayMetrics();
         getWindowManager().getDefaultDisplay().getMetrics(metrics);
         switch(metrics.densityDpi){

             case DisplayMetrics.DENSITY_LOW:
                 AppMaster.Settings.fixedFontSize = 15;
                 contentLayout.setRowCount(AppMaster.Settings.orientation == PORTRAIT_MODE?2:1);
                 contentLayout.setColumnCount(AppMaster.Settings.orientation == PORTRAIT_MODE?2:3);
                 break;
             case DisplayMetrics.DENSITY_MEDIUM:
                 contentLayout.setRowCount(AppMaster.Settings.orientation == PORTRAIT_MODE?3:2);
                 contentLayout.setColumnCount(AppMaster.Settings.orientation == PORTRAIT_MODE?2:3);
                 AppMaster.Settings.fixedFontSize = 35;

                 break;
             case DisplayMetrics.DENSITY_XHIGH:
                 contentLayout.setRowCount(AppMaster.Settings.orientation == PORTRAIT_MODE?4:2);
                 contentLayout.setColumnCount(AppMaster.Settings.orientation == PORTRAIT_MODE?2:4);
                 AppMaster.Settings.fixedFontSize = 45;
                 break;
             case DisplayMetrics.DENSITY_XXHIGH:
                 contentLayout.setRowCount(AppMaster.Settings.orientation == PORTRAIT_MODE?4:2);
                 contentLayout.setColumnCount(AppMaster.Settings.orientation == PORTRAIT_MODE?2:4);
                 AppMaster.Settings.fixedFontSize = 20;
                 break;
         }

         AppMaster.Settings.inCol = contentLayout.getColumnCount();
         AppMaster.Settings.inRow = contentLayout.getRowCount();

         if(AppMaster.Settings.orientation == PORTRAIT_MODE) {
             contentLayout.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width;
             contentLayout.getLayoutParams().height = (int)(AppMaster.Settings.deviceScreenSize.height * (58 / 100f));
         }else{
             contentLayout.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width;
             contentLayout.getLayoutParams().height = (int)(AppMaster.Settings.deviceScreenSize.height * (53/ 100f));
         }


        categoryLayout = (LinearLayout) findViewById(R.id.category_list_layout);
        if(AppMaster.selectedCategory !=null) {
            AppMaster.selectedCategory.removeImagesFromContainer();
            AppMaster.selectedCategory.setViewGroupToLoadData(contentLayout);
            AppMaster.selectedCategory.loadDataToViewGroup();
        }

        addCategoryLayout = (LinearLayout)findViewById(R.id.add_category_layout);

        setFonts();
        setSizes();

         wasLoaded = true;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {


            case R.id.btn_add:
                addCategoryLayout.removeAllViews();


                this.categoryLayout.addView(AppMaster.imageCapturedCategory,0);
                Category addCategory = new Category("הוסף קטגוריה",this);

                addCategory.setOnSelectCategoryListener(this);
                addCategory.setViewGroupToLoadData(contentLayout);
                addCategory.addImageCell(new ImageCell(this,addCategory));
                addCategoryLayout.addView(addCategory);


            break;

            case R.id.button_delete:
                if(AppMaster.selectedCategory!=null) {
                    boolean ifDeleted = AppMaster.storageManager.deleteCategory(AppMaster.selectedCategory);

                    if (ifDeleted) {
                        AppMaster.selectedCategory.removeImagesFromContainer();
                        categoryLayout.removeView(AppMaster.selectedCategory);
                        AppMaster.categories.remove(AppMaster.categories.indexOf(AppMaster.selectedCategory));
                        if(categoryLayout.getChildCount()>0){
                            onSelectCategory(AppMaster.categories.get(0));
                            AppMaster.selectedCategory.loadDataToViewGroup();

                        }
                        Toast.makeText(this, "קטגוריה נמחקה בהצלחה!", Toast.LENGTH_LONG).show();

                    } else
                        Toast.makeText(this, "שגיאה בעת מחיקה!", Toast.LENGTH_LONG).show();
                }
            break;
            case R.id.button_duplicate:


            break;
            case R.id.button_reset:

                if(AppMaster.selectedCategory!=null) {
                    boolean ifDeleted = AppMaster.storageManager.resetCategory(AppMaster.selectedCategory);

                    if (ifDeleted) {
                        AppMaster.selectedCategory.removeImagesFromContainer();
                        AppMaster.selectedCategory.reset();
                        Toast.makeText(this, "קטגוריה אופסה בהצלחה!", Toast.LENGTH_LONG).show();

                    } else
                        Toast.makeText(this, "שגיאה בעת איפוס!", Toast.LENGTH_LONG).show();
                }

            break;
            case R.id.button_step_left:
                if(AppMaster.selectedCategory!=null) {
                    AppMaster.selectedCategory.prevOffset();
                    AppMaster.selectedCategory.loadDataToViewGroup();
                }

            break;
            case R.id.button_step_right:
                if(AppMaster.selectedCategory!=null) {
                    AppMaster.selectedCategory.nextOffset();
                    AppMaster.selectedCategory.loadDataToViewGroup();
                }
            break;
            case R.id.options_view:
                menuOptions.show();
             break;
        }


    }


    @Override
    public void onSelectCategory(Category c) {
        if(c.isEmpty()) {
            AppMaster.imageCapturedCategory = c;
            Intent in = new Intent("android.intent.action.ADD_CATEGORY");
            this.startActivity(in);
            return;
        }

        if(c == AppMaster.selectedCategory)
            return;
        c.select();
        AppMaster.selectedCategory = c;
    }



    public void setFonts(){
        appTitle.setTextSize((int)(AppMaster.Settings.fixedFontSize*1.5));
        if(AppMaster.Settings.orientation == LANDSCAPE_MODE)
            minusText.setTextSize((int)(AppMaster.Settings.fixedFontSize*1.8));

        modeScreen.setTextSize((int)(AppMaster.Settings.fixedFontSize*1.5));

        btnReset.setTextSize(AppMaster.Settings.fixedFontSize);
        btnDelete.setTextSize(AppMaster.Settings.fixedFontSize);
        btnDuplicate.setTextSize(AppMaster.Settings.fixedFontSize);
        btnStepRight.setTextSize(AppMaster.Settings.fixedFontSize);
        btnStepLeft.setTextSize(AppMaster.Settings.fixedFontSize);
    }

    public void setSizes(){

        Log.i("sstamus:",String.valueOf(AppMaster.Settings.orientation));

        switch(AppMaster.Settings.orientation) {

            case PORTRAIT_MODE:
                toolBarLayout.getLayoutParams().height = (int) (AppMaster.Settings.deviceScreenSize.height * (13.5f / 100));
                lineSeparator.getLayoutParams().height = (int) (AppMaster.Settings.deviceScreenSize.height * (0.5f / 100));
                mainPanelLayout.getLayoutParams().height = (int) (AppMaster.Settings.deviceScreenSize.height * (86f / 100));

                controlPanelLayout.getLayoutParams().height = (int) (AppMaster.Settings.deviceScreenSize.height * (7f / 100));

                horizontalView.getLayoutParams().height  = (int) (AppMaster.Settings.deviceScreenSize.height * (20f / 100));
                this.categoryLayout.getLayoutParams().height = (int) (AppMaster.Settings.deviceScreenSize.height * (20f / 100));

                btnReset.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width / 6;
                btnReset.getLayoutParams().height = (int) (AppMaster.Settings.deviceScreenSize.height * (7f / 100));
                btnDelete.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width / 6;
                btnDelete.getLayoutParams().height = (int) (AppMaster.Settings.deviceScreenSize.height * (7f / 100));
                btnDuplicate.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width / 6;
                btnDuplicate.getLayoutParams().height = (int) (AppMaster.Settings.deviceScreenSize.height * (7f / 100));
                btnStepRight.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width / 6;
                btnStepRight.getLayoutParams().height = (int) (AppMaster.Settings.deviceScreenSize.height * (7f / 100));
                btnStepLeft.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width / 6;
                btnStepLeft.getLayoutParams().height = (int) (AppMaster.Settings.deviceScreenSize.height * (7f / 100));
                break;

            case LANDSCAPE_MODE:
                toolBarLayout.getLayoutParams().height = (int) (AppMaster.Settings.deviceScreenSize.height * (11.5f / 100));
                lineSeparator.getLayoutParams().height = (int) (AppMaster.Settings.deviceScreenSize.height * (0.5f / 100));
                mainPanelLayout.getLayoutParams().height = (int) (AppMaster.Settings.deviceScreenSize.height * (88f / 100));

                controlPanelLayout.getLayoutParams().height = (int) (AppMaster.Settings.deviceScreenSize.height * (8f / 100));

                horizontalView.getLayoutParams().height  = (int) (AppMaster.Settings.deviceScreenSize.height * (27f / 100));

                btnReset.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width / 10;
                btnReset.getLayoutParams().height = (int) (AppMaster.Settings.deviceScreenSize.height * (8f / 100));
                btnDelete.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width / 10;
                btnDelete.getLayoutParams().height = (int) (AppMaster.Settings.deviceScreenSize.height * (8f / 100));
                btnDuplicate.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width / 10;
                btnDuplicate.getLayoutParams().height = (int) (AppMaster.Settings.deviceScreenSize.height * (8f / 100));
                btnStepRight.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width / 10;
                btnStepRight.getLayoutParams().height = (int) (AppMaster.Settings.deviceScreenSize.height * (8f / 100));
                btnStepLeft.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width / 10;
                btnStepLeft.getLayoutParams().height = (int) (AppMaster.Settings.deviceScreenSize.height * (8f / 100));

                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        resumeHandler.sendEmptyMessage(0);

    }



    /*@Override
    public void onBackPressed() {
        Log.i("backing","back pressed");
        super.onBackPressed();
    }*/

    @Override
    protected void onUserLeaveHint() {

    }

    @Override
    public void onBackPressed() {
        Log.i("backing","back pressed");
        System.exit(0);

    }
}

