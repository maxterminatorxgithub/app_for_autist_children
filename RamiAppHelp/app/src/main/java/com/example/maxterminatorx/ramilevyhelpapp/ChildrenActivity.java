package com.example.maxterminatorx.ramilevyhelpapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import serverredweb.Child;

public class ChildrenActivity extends AppCompatActivity {


    static class ChildListAdapter extends ArrayAdapter<Child>{

        private List<Child> childrenList;

        public ChildListAdapter(@NonNull Context context, @NonNull List<Child> objects) {
            super(context, 0, objects);
            childrenList = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            RelativeLayout v = new RelativeLayout(getContext());
            LayoutInflater.from(getContext()).inflate(R.layout.child_row , v);
            ((TextView)v.findViewById(R.id.txt_child_name)).setText(childrenList.get(position).getName());
            ((TextView)v.findViewById(R.id.txt_child_name)).setTextSize(AppMaster.Settings.fixedFontSize);
            ((TextView)v.findViewById(R.id.txt_child_hash)).setText("קוד ילד: "+childrenList.get(position).getHash());
            ((TextView)v.findViewById(R.id.txt_child_hash)).setTextSize(AppMaster.Settings.fixedFontSize);

            return v;
        }
    }


    private ListView childList;

    private LocalDatabase childDB;
    private ChildListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);

        childList = findViewById(R.id.children_list);

        childDB = new LocalDatabase(this);


        ArrayList<Child> datat = new ArrayList<>();
        datat.add(new Child(0,0,"nasfa","asf546ss84"));
        datat.add(new Child(0,0,"nsad","asf5123s84"));

        adapter = new ChildListAdapter(this,/*childDB.getAllChildren()*/datat);

        childList.setAdapter(adapter);



    }
}
