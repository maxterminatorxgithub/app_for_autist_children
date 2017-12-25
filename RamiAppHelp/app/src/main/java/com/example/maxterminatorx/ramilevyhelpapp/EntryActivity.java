package com.example.maxterminatorx.ramilevyhelpapp;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import android.support.design.widget.Snackbar;

public class EntryActivity extends AppCompatActivity {


    static class EntryHandler extends Handler{


        static final int NETWORK_CONNECTION_ERROR = -2;
        static final int SERVER_CONNECTION_ERROR = -1;
        static final int PARENT_NOT_EXIST = 0;
        static final int PARENT_EXIST = 1;
        static final int CHILD_NOT_FOUND = 6;
        static final int CHILD_FOUND = 7;

        private EntryActivity ea;

        private Toast serverErr,internetErr,childErr;


        public EntryHandler(EntryActivity ea){
            this.ea=ea;
            serverErr = Toast.makeText(ea, "יש תקלה זמנית בשרת נא לנסות מאוחר יותר", Toast.LENGTH_LONG);
            internetErr = Toast.makeText(ea, "שגיאה יש לבדוק הגדרות חיבור אינטרנט", Toast.LENGTH_LONG);
            childErr = Toast.makeText(ea, "ילד לא קיים, נא שוב.", Toast.LENGTH_LONG);
        }

        public void showUI(){
            ea.childCode.setVisibility(View.INVISIBLE);
            ea.btnChild.setVisibility(View.VISIBLE);
            ea.btnParent.setVisibility(View.VISIBLE);
            ea.btnSubmit.setVisibility(View.INVISIBLE);
        }

        @Override
        public void handleMessage(Message msg) {
            ea.progress.setVisibility(View.GONE);
            ea.txtProgress.setVisibility(View.GONE);



            switch(msg.what){
                case NETWORK_CONNECTION_ERROR:
                    showUI();
                    internetErr.show();
                    return;
                case SERVER_CONNECTION_ERROR:
                    showUI();
                    serverErr.show();
                    return;
                case PARENT_NOT_EXIST:

                    return;
                case PARENT_EXIST:
                    ea.parentMode();
                    return;
                case CHILD_NOT_FOUND:
                    childErr.show();
                    return;
                case CHILD_FOUND:
                    ea.childMode();
                    return;
            }
        }

        public void activateWait(){
            ea.progress.setVisibility(View.VISIBLE);
            ea.txtProgress.setVisibility(View.VISIBLE);

            ea.childCode.setVisibility(View.GONE);
            ea.btnChild.setVisibility(View.GONE);
            ea.btnParent.setVisibility(View.GONE);
            ea.btnSubmit.setVisibility(View.GONE);
        }
    }





    private EditText childCode;
    private Button btnSubmit;
    private Button btnParent;
    private Button btnChild;

    private ProgressBar progress;
    private TextView txtProgress;

    private SharedPreferences sp;
    private SharedPreferences.Editor editPrefs;

    private EntryHandler entryHandler;

    //snackbars

    private Snackbar skChildCodeFormat,
    skChildCodeEmpty,skChildNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);


        entryHandler = new EntryHandler(this);

        AppMaster.init(this);

        AppMaster.checkSimCard();

        sp = getApplicationContext().getSharedPreferences("app_data",Context.MODE_PRIVATE);
        editPrefs = sp.edit();

        String mode = sp.getString("mode_type","none");

        AppMaster.init(this);

        progress = findViewById(R.id.connection_progress);
        txtProgress = findViewById(R.id.connection_text);

        childCode = findViewById(R.id.txt_child_code);
        btnChild = findViewById(R.id.btn_child);
        btnParent = findViewById(R.id.btn_parent);
        btnSubmit = findViewById(R.id.btn_submit);

        skChildCodeFormat = Snackbar.make(btnSubmit, "קוד ילד חייב להכיל 10 תווים שהם אותיות באנגלית וספרות.", Snackbar.LENGTH_LONG);
        skChildCodeEmpty = Snackbar.make(btnSubmit, "נא להכניס קוד ילד!", Snackbar.LENGTH_LONG);


        if(mode.equals("parent")){
            AppMaster.checkSimCard();

            if(AppMaster.simCode.isEmpty())
                Toast.makeText(this,"לא זוהה כרטיס SIM של רמי לוי תקשורת במכשירך!",Toast.LENGTH_LONG).show();
            else
                AppMaster.server.checkSim(AppMaster.simCode,entryHandler,editPrefs);


        }
        else if(mode.equals("child")){
            childMode();
            String childCode = sp.getString("child_code","");
            if(childCode.length()==10) {
                String finalChildCode = childCode.toLowerCase();
                AppMaster.server.checkChildExist(finalChildCode,entryHandler);
            }
        }





        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();






        btnParent = findViewById(R.id.btn_parent);
        btnParent.setOnClickListener(v->{

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                AppMaster.checkSimCard();

                if (AppMaster.simCode == null || AppMaster.simCode.isEmpty() || AppMaster.simCode.length() < 19) {
                    Toast.makeText(this, "לא זוהה כרטיס SIM של רמי לוי תקשורת במכשירך!", Toast.LENGTH_LONG).show();
                    return;
                }

                AppMaster.server.checkSim(AppMaster.simCode, entryHandler,editPrefs);

                return;
            }
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},1);
        });



        btnChild.setOnClickListener(v -> {
            childCode.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.VISIBLE);
        });


        btnSubmit.setOnClickListener(v->{

            String finalChildCode = childCode.getText().toString().toLowerCase();

            if(finalChildCode.length()!=10){
                skChildCodeFormat.show();
                return;
            }


            AppMaster.server.checkChildExist(finalChildCode,entryHandler);


        });






        btnParent.setTextSize(AppMaster.Settings.fixedFontSize);
        btnParent.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width/2;
        btnParent.setX(AppMaster.Settings.deviceScreenSize.width/4);

        btnChild.setTextSize(AppMaster.Settings.fixedFontSize);
        btnChild.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width/2;
        btnChild.setX(AppMaster.Settings.deviceScreenSize.width/4);

        childCode.setTextSize(AppMaster.Settings.fixedFontSize);
        childCode.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width/2;
        childCode.setX(AppMaster.Settings.deviceScreenSize.width/4);

        btnSubmit.setTextSize(AppMaster.Settings.fixedFontSize);
        btnSubmit.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width/2;
        btnSubmit.setX(AppMaster.Settings.deviceScreenSize.width/4);

        //progress.setTextSize(AppMaster.Settings.fixedFontSize);
        progress.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width/2;
        progress.setX(AppMaster.Settings.deviceScreenSize.width/4);

        txtProgress.setTextSize(AppMaster.Settings.fixedFontSize);
        txtProgress.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width/2;
        txtProgress.setX(AppMaster.Settings.deviceScreenSize.width/4);

        //this.onRequestPermissionsResult();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
            Log.i("permission","accepted");
            AppMaster.checkSimCard();
            AppMaster.server.checkSim(AppMaster.simCode,entryHandler,editPrefs);
        }
    }


    private void parentMode(){

        editPrefs.putString("mode_type","parent");
        editPrefs.commit();

        Intent i = new Intent(EntryActivity.this,ParentModeActivity.class);
        this.startActivity(i);
        this.finish();
    }

    private void childMode(){

        editPrefs.putString("mode_type","child");
        editPrefs.commit();

        Intent i = new Intent(EntryActivity.this,ChildModeActivity.class);
        this.startActivity(i);
        this.finish();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        AppMaster.init(this);

        btnParent.setTextSize(AppMaster.Settings.fixedFontSize);
        btnParent.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width/2;
        btnParent.setX(AppMaster.Settings.deviceScreenSize.width/4);

        btnChild.setTextSize(AppMaster.Settings.fixedFontSize);
        btnChild.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width/2;
        btnChild.setX(AppMaster.Settings.deviceScreenSize.width/4);

        childCode.setTextSize(AppMaster.Settings.fixedFontSize);
        childCode.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width/2;
        childCode.setX(AppMaster.Settings.deviceScreenSize.width/4);

        btnSubmit.setTextSize(AppMaster.Settings.fixedFontSize);
        btnSubmit.getLayoutParams().width = AppMaster.Settings.deviceScreenSize.width/2;
        btnSubmit.setX(AppMaster.Settings.deviceScreenSize.width/4);


    }
}
