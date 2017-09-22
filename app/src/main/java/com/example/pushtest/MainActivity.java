package com.example.pushtest;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends Activity {
    TextView txtToken;
    TextView txtResultLog;
    TextView txtInstanceID;
    Button btnRegister;
    Button btnPushMessage;

    ArrayList logList;
    SimpleDateFormat sdf;

    GoogleCloudMessaging gcm;
    Context context;

    String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = (Context) getApplicationContext();
        displayLayout();

        // initialize
        logList = new ArrayList();
        sdf = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");

        setGCMEnvironment();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerInBackground();

            }
        });

        btnPushMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(context, "push message", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    private void setGCMEnvironment(){
        // get Instance Id
        String iid = InstanceID.getInstance(getApplicationContext()).getId();
        txtInstanceID.setText(iid);

        // get Device Token
        String authorizedEntity = getString(R.string.gcm_defaultSenderId);
        String scope = GoogleCloudMessaging.INSTANCE_ID_SCOPE;
        getTokenInBackground(authorizedEntity, scope, null);
    }

    public void getTokenInBackground(final String authorizedEntity, final String scope,
                                     final Bundle extras) {
        new AsyncTask<Void, Void, Void>() {
            String token;

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    token = InstanceID.getInstance(getApplicationContext())
                            .getToken(authorizedEntity, scope, extras);
                    Log.d("boram", "getToken succeeded." +
                            "\nsenderId: " + authorizedEntity + "\ntoken: " + token);

                    /*
                    // Save the token in the address book
                    Sender entry = mSenders.getSender(authorizedEntity);
                    if (entry == null) {
                        mLogger.log(Log.ERROR, "Could not save token, missing sender id");
                        return null;
                    }
                    Token tokenModel = new Token();
                    tokenModel.token = token;
                    tokenModel.scope = scope;
                    if (extras != null) {
                        for (String key : extras.keySet()) {
                            tokenModel.extras.put(key, extras.getString(key));
                        }
                    }
                    tokenModel.createdAt = System.currentTimeMillis();
                    entry.appTokens.put(token, tokenModel);
                    mSenders.updateSender(entry);

                */
                } catch (final IOException e) {
                    Log.d("boram", "getToken failed." +
                            "\nsenderId: " + authorizedEntity + "\nerror: " + e.getMessage());
//                    MainActivity.showToast(context, R.string.iid_toast_error, e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //super.onPostExecute(aVoid);

                txtToken.setText(token);
                txtResultLog.append(getLogMessage("success get token"));
            }
        }.execute();
    }

    private void registerInBackground(){
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... params){
                String msg;
                try{
                    if(gcm == null){
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register("161600526144");
                    msg = "Device registered, registration ID=" + regid;
                }catch(IOException e){
                    msg = "";
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg){
                Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                toast.show();
            }
        }.execute();
    }

    private String getLogMessage(String message) {
        String logMessage = "";

        Date date = new Date();
        logMessage = sdf.format(date) + " " + message + "\n";

        return logMessage;
    }

    private void displayLayout() {
        txtInstanceID = (TextView) findViewById(R.id.txtInstanceID);
        txtToken = (TextView) findViewById(R.id.txtToken);
        txtResultLog = (TextView) findViewById(R.id.txtResultLog);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnPushMessage = (Button) findViewById(R.id.btnPushMessage);

        txtResultLog.setMovementMethod(new ScrollingMovementMethod());
    }


}
