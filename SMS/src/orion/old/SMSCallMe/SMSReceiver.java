package orion.old.SMSCallMe;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.gsm.SmsMessage;


public class SMSReceiver extends BroadcastReceiver {


	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		final NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		SmsMessage[] msgs = HelperClass.extractSMSmessages(intent);
		for(SmsMessage msg:msgs){
			String phoneNumber = msg.getDisplayOriginatingAddress();
			manager.notify(1000,HelperClass.createNotification(context,phoneNumber, HelperClass.createSMSDialogIntent(phoneNumber)));
		}
	}
	
	
	

}
