package orion.old.SMSCallMe;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts.People;
import android.telephony.gsm.SmsManager;
import android.telephony.gsm.SmsMessage;

public final class HelperClass {
	
	public static SmsMessage[] extractSMSmessages(Intent intent){
		Object[] pdus = (Object []) intent.getExtras().get("pdus");
		SmsMessage[] msgs = new SmsMessage[pdus.length];
		for (int i=0;i<pdus.length;i++){
			msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
		}
		return msgs;
	}
	
	public static Intent createCallIntent(String phoneNumber){
		Intent  call = new Intent (Intent.ACTION_CALL);
		call.setData(Uri.parse("tel:"+phoneNumber));
		call.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //Remover isso
		return call;
		
	}
	
	public static Notification createNotification(Context context,String phoneNumber,Intent intent){
		Notification notif = new Notification(android.R.drawable.stat_sys_phone_call,"Call me :" + phoneNumber,System.currentTimeMillis());
		PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0);
		notif.setLatestEventInfo(context, "Return call to", phoneNumber, pending);
		notif.flags |= Notification.FLAG_AUTO_CANCEL;
		return notif;
	}
	
	public static Intent createSMSDialogIntent(String phoneNumber){
		Intent intent  = new Intent(SMSDialog.ACTION_ANSWER);
		intent.putExtra(SMSDialog.EXTRA_PHONENUMBER, phoneNumber);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
		
	}
	
	public static void sendSMS(String destinationNumber, String message){
		SmsManager manager = SmsManager.getDefault();
		manager.sendTextMessage(destinationNumber, null,message, null, null);
	}
	
	public static String processContactsResult(Activity activity,Intent intent){
		String result = null;
		
		Uri contactData = intent.getData();
        Cursor c =  activity.managedQuery(contactData, null, null, null, null);
        if (c.moveToFirst()) 
          result = c.getString(c.getColumnIndexOrThrow(People.NUMBER));
        c.close();
        
        return result;
	}
}
