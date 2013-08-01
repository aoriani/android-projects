package com.orion.arduino.notification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LauchNotificationSettings extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
		startActivity(intent);
		finish();
	}
	

}
