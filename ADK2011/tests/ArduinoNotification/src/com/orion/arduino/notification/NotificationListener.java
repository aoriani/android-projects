package com.orion.arduino.notification;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationListener extends NotificationListenerService {
	
	private static final String LOG_TAG = "NotificationListener";

	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		Log.d(LOG_TAG, "Received notification from " + sbn.getPackageName() + 
				" with ticker " + sbn.getNotification().tickerText.toString());

	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		// TODO Auto-generated method stub

	}

}
