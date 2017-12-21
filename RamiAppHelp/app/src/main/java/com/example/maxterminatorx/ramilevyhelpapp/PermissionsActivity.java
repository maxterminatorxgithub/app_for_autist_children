package com.example.maxterminatorx.ramilevyhelpapp;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PermissionsActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);


        findViewById(R.id.btn_open_requests).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_open_requests:
                AppMaster.requestRequeredPermissions(this);
                break;
            case R.id.btn_next:
                if(!AppMaster.checkPerimissions(this)){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                    dialog.setTitle("הודעה למשתמש:");
                    TextView txt = new TextView(this);
                    txt.setText("נא לפתוח הרשאות להמשך.");
                    dialog.setView(txt);

                    dialog.show();
                }else{
                    AppMaster.checkSimCard();
                    finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(AppMaster.checkPerimissions(this)){
            finish();
        }
    }
}
