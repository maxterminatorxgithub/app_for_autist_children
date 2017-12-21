package com.example.maxterminatorx.ramilevyhelpapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by maxterminatorx on 08-Oct-17.
 */

public class Listeners{


    public static final OnChildMainOptionListener onChildMainOptionListener = new OnChildMainOptionListener();


      static class OnChildMainOptionListener implements View.OnClickListener{

        private Activity act;
        private ViewGroup container;
        private HashMap<String,ImageView> options;
        private AnimatedProccess animatedProccess;

          @Override
          public void onClick(View v) {

          }

          private class AnimatedProccess extends AsyncTask<Collection<ImageView>,Void,Integer>{

            private ImageView selected;
            private int selectedId;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                for(ImageView iv:options.values()){
                    if(iv==selected)
                        continue;
                    iv.animate().alpha(0);
                }

                selected.animate().scaleX(3).scaleY(2.5f).x(AppMaster.Settings.deviceScreenSize.width/3).y(AppMaster.Settings.deviceScreenSize.height/3).alpha(0);
                selectedId= selected.getId();
            }

            @Override
            public Integer doInBackground(Collection<ImageView>...params){
                try{
                    Thread.sleep(500);
                }catch(InterruptedException iex){}
                return selectedId;
            }

            //@Override
            /*
            protected void onPostExecute(Integer id) {
                super.onPostExecute(id);
                container.removeAllViews();
                switch (id){
                    case R.id.child_mode_option_how_i_feel:
                        LayoutInflater.from(act).inflate(R.layout.how_i_feel,container);



                        initHowIFeelComponents(container);


                        ((TextView)AppMaster.views.get("modeScreenLabel")).setText("איך אני מרגיש");
                        return;
                    case R.id.child_mode_option_play:
                        LayoutInflater.from(act).inflate(R.layout.play_layout,container);
                        ((TextView)AppMaster.views.get("modeScreenLabel")).setText("מה אני רוצה לשחק");
                        return;
                }
            }*/
            public void setSelectedImageView(ImageView selected){
                this.selected=selected;
            }
        }

        void setComponents(Activity act, ViewGroup vGroup, HashMap<String,ImageView> options){
            this.act = act;
            this.container = vGroup;
            this.options = options;
            animatedProccess = new OnChildMainOptionListener.AnimatedProccess();
        }

        private OnChildMainOptionListener(){

        }
/*
        @Override
        public void onClick(final View v){
            v.setEnabled(false);

            switch(v.getId()){
                case R.id.child_mode_option_play:
                    animatedProccess.setSelectedImageView((ImageView)v);
                    animatedProccess.execute(options.values());
                    break;
                case R.id.child_mode_option_eat:
                    animatedProccess.setSelectedImageView((ImageView)v);
                    animatedProccess.execute(options.values());
                    break;
                case R.id.child_mode_option_paint:

                    break;
                case R.id.child_mode_option_how_i_feel:
                    animatedProccess.setSelectedImageView((ImageView)v);
                    animatedProccess.execute(options.values());
                    break;
                case R.id.child_mode_option_toilet:

                    break;
                case R.id.child_mode_option_drink:
                    animatedProccess.setSelectedImageView((ImageView)v);
                    animatedProccess.execute(options.values());
                    break;
                case R.id.child_mode_option_hug:

                    break;
                case R.id.child_mode_option_watch_tv:

                    break;
                case R.id.child_mode_option_go_to_school:

                    break;
                case R.id.child_mode_option_play_tablet:

                    break;
                case R.id.child_mode_option_sleep:

                    break;
                case R.id.child_mode_option_music:

                    break;
            }

        }
        */
    }

    static void initHowIFeelComponents(ViewGroup container){
        int devideWidth=AppMaster.Settings.orientation == 1 ? 3:5,
                devideHeight=AppMaster.Settings.orientation == 1 ? 5:4;

        Log.i("divistion:","widthDevide:"+devideWidth+" heightDevide:"+devideHeight);


        container.findViewById(R.id.op1).getLayoutParams().width=
                AppMaster.Settings.deviceScreenSize.width/devideWidth;
        container.findViewById(R.id.op1).getLayoutParams().height=
                AppMaster.Settings.deviceScreenSize.height/devideHeight;

        container.findViewById(R.id.op2).getLayoutParams().width=
                AppMaster.Settings.deviceScreenSize.width/devideWidth;
        container.findViewById(R.id.op2).getLayoutParams().height=
                AppMaster.Settings.deviceScreenSize.height/devideHeight;

        container.findViewById(R.id.op3).getLayoutParams().width=
                AppMaster.Settings.deviceScreenSize.width/devideWidth;
        container.findViewById(R.id.op3).getLayoutParams().height=
                AppMaster.Settings.deviceScreenSize.height/devideHeight;

        container.findViewById(R.id.op4).getLayoutParams().width=
                AppMaster.Settings.deviceScreenSize.width/devideWidth;
        container.findViewById(R.id.op4).getLayoutParams().height=
                AppMaster.Settings.deviceScreenSize.height/devideHeight;

        container.findViewById(R.id.op5).getLayoutParams().width=
                AppMaster.Settings.deviceScreenSize.width/devideWidth;
        container.findViewById(R.id.op5).getLayoutParams().height=
                AppMaster.Settings.deviceScreenSize.height/devideHeight;

        container.findViewById(R.id.op6).getLayoutParams().width=
                AppMaster.Settings.deviceScreenSize.width/devideWidth;
        container.findViewById(R.id.op6).getLayoutParams().height=
                AppMaster.Settings.deviceScreenSize.height/devideHeight;

        container.findViewById(R.id.op7).getLayoutParams().width=
                AppMaster.Settings.deviceScreenSize.width/devideWidth;
        container.findViewById(R.id.op7).getLayoutParams().height=
                AppMaster.Settings.deviceScreenSize.height/devideHeight;

        container.findViewById(R.id.op8).getLayoutParams().width=
                AppMaster.Settings.deviceScreenSize.width/devideWidth;
        container.findViewById(R.id.op8).getLayoutParams().height=
                AppMaster.Settings.deviceScreenSize.height/devideHeight;

        container.findViewById(R.id.op9).getLayoutParams().width=
                AppMaster.Settings.deviceScreenSize.width/devideWidth;
        container.findViewById(R.id.op9).getLayoutParams().height=
                AppMaster.Settings.deviceScreenSize.height/devideHeight;

        container.findViewById(R.id.op10).getLayoutParams().width=
                AppMaster.Settings.deviceScreenSize.width/devideWidth;
        container.findViewById(R.id.op10).getLayoutParams().height=
                AppMaster.Settings.deviceScreenSize.height/devideHeight;

        container.findViewById(R.id.op11).getLayoutParams().width=
                AppMaster.Settings.deviceScreenSize.width/devideWidth;
        container.findViewById(R.id.op11).getLayoutParams().height=
                AppMaster.Settings.deviceScreenSize.height/devideHeight;

        container.findViewById(R.id.op12).getLayoutParams().width=
                AppMaster.Settings.deviceScreenSize.width/devideWidth;
        container.findViewById(R.id.op12).getLayoutParams().height=
                AppMaster.Settings.deviceScreenSize.height/devideHeight;


    }
}
