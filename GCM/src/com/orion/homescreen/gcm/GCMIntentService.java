package com.orion.homescreen.gcm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

    static final String SENDER_ID = "319432157457";
    private static final int NOTIF_SLOT = 0;


    public GCMIntentService() {
        super(SENDER_ID);

    }

    @Override
    protected void onError(Context ctx, String errorMsg) {
        String ticker = "Error on GCM";
        int ledColor = Color.RED;
        notify(ctx, errorMsg, ticker, ledColor);

    }

    @SuppressLint("NewApi")
    private void notify(Context ctx, String errorMsg, String ticker,
            int ledColor) {
        @SuppressWarnings("deprecation")
        Notification notif = new Notification.Builder(ctx)
        .setSmallIcon(android.R.drawable.stat_notify_chat)
        .setTicker(ticker).setLights(ledColor, 500, 500)
        .setSound(
                RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(errorMsg).getNotification();
        NotificationManager nm = (NotificationManager) ctx
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIF_SLOT, notif);
    }

    @Override
    protected void onMessage(Context arg0, Intent intent) {
        Bundle extras = intent.getExtras();
        String message = (String) extras.get("msg");
        notify(arg0, message, "New Message!", Color.GREEN);

    }

    @Override
    protected void onRegistered(Context context, String regId) {
        notify(context, "Registed on GCM as " + regId, "Registered!",
                Color.BLUE);

        // Register on our server
        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType("com.google");
        String userLogin = URLEncoder
                .encode((accounts.length > 0) ? accounts[0].name : "Unknown");
        String device = URLEncoder.encode(android.os.Build.MODEL);
        String urlstr = "http://geomessaging.appspot.com/gcm/reg?regid="
                + regId + "&device=" + device + "&user=" + userLogin;
        HttpURLConnection urlConnection = null;
        try {

            URL url = new URL(urlstr);
            urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            Log.i("GCMIntentService", "Server response:" + sb.toString());
        } catch (IOException e) {
            Log.e("GCMIntentService", e.getMessage(), e);

        } finally {
            urlConnection.disconnect();
        }

    }

    @Override
    protected void onUnregistered(Context arg0, String arg1) {
        // Ignored for demo

    }

}
