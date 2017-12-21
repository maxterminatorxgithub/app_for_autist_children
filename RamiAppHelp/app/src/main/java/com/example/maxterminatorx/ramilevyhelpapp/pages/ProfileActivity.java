package com.example.maxterminatorx.ramilevyhelpapp.pages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.maxterminatorx.ramilevyhelpapp.R;

/**
 * Created by maxterminatorx on 11-Sep-17.
 */

public class ProfileActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);
        getSupportActionBar().hide();

        setContentView(R.layout.profile_page);

    }

}
