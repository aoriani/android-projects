package com.orion.homescreen.gcm;

import static com.orion.homescreen.gcm.GCMIntentService.SENDER_ID;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.orion.homescreen.gcm.R;

public class MainActivity extends Activity {

    private static final String TAG = "GCM-Test";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType("com.google");
        String userLogin = (accounts.length > 0) ? accounts[0].name : "Unknown";
        String device = android.os.Build.MODEL;
        TextView textView = (TextView) findViewById(R.id.welcome_text);
        textView.setText(userLogin + " on " + device);

        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
            GCMRegistrar.register(this, SENDER_ID);
        } else {
            Log.v(TAG, "Already registered");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }


}
