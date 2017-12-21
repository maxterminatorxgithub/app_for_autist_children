package com.example.maxterminatorx.ramilevyhelpapp.pages;

import android.app.Activity;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.maxterminatorx.ramilevyhelpapp.AppMaster;
import com.example.maxterminatorx.ramilevyhelpapp.GifView;
import com.example.maxterminatorx.ramilevyhelpapp.NetworkConnector;
import com.example.maxterminatorx.ramilevyhelpapp.R;

import java.util.HashMap;

/**
 * Created by maxterminatorx on 11-Sep-17.
 */

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener{


    private Button sendBtn;
    private EditText txtName;
    private EditText txtMsg;
    private GifView loading;

    @Override
    public void onCreate(Bundle b){

        super.onCreate(b);
        getSupportActionBar().hide();

        setContentView(R.layout.contact_us_page);

        (sendBtn = (Button)findViewById(R.id.send_msg_button)).setOnClickListener(this);
        txtName = (EditText)findViewById(R.id.nametxt);
        txtMsg = (EditText)findViewById(R.id.massagetxt);
        loading = (GifView)findViewById(R.id.loadingGif);
        loading.setVisibility(View.INVISIBLE);

        //Class.forName("");



    }

    @Override
    public void onClick(View v) {

        sendBtn.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);



        final String NAME = ContactUsActivity.this.txtName.getText().toString();
        final String MSG = ContactUsActivity.this.txtMsg.getText().toString();

        new AsyncTask<Void, Void, Void>() {

            private NetworkConnector nc;

            @Override
            protected Void doInBackground(Void... prms) {


                String msg = "<!DOCTYPE html>"
                        + "<html>"
                        + "<head><meta charset='UTF-8'>"
                        + "</head>"
                        + "<body dir='rtl' style='font-family:david;font-size:24px;'>"
                        + "<h1 style='font-size:32px;'>שם הפונה: " +  NAME + "</h1>"
                        + "<p>" + MSG + "</p>"
                        + "</body></html>";


                nc = new NetworkConnector("https://polls.rami-levy.co.il/SendMail.php");

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email1", "autism.rl@rami-levy.co.il");
                params.put("email2", "");
                params.put("subject", "פנייה מאפליקציה");
                params.put("msg", msg);
                nc.httpGet(params);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ContactUsActivity.this.loading.setVisibility(View.INVISIBLE);
                if(nc.isSended()&&nc.isSuccess())
                    showAlert("ההודעה נשלחה בהצלחה.",ContactUsActivity.this,true);
                else
                    showAlert("שגיאה ההודעה לא נשלחה נסה שוב.",ContactUsActivity.this,false);
            }
        }.execute();

    }



    public static void showAlert(String msg, final ContactUsActivity act, final boolean close){
        AlertDialog.Builder dialog = new AlertDialog.Builder(act);

        dialog.setTitle( "קל להיות בקשר - הודעה למשתמש" )
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(msg)
//  .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//      public void onClick(DialogInterface dialoginterface, int i) {
//          dialoginterface.cancel();
//          }})
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        if(close) {
                            act.finish();
                            return;
                        }


                        act.sendBtn.setVisibility(View.VISIBLE);

                    }
                }).show();
    }




}
