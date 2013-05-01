package orion.old.SMSCallMe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import orion.old.SMSCallMe.R;

public class SMSDialog extends Activity {

	public static final String ACTION_ANSWER = "orion.old.SMSCallMe.ACTION_ANSWER";
	public static final String EXTRA_PHONENUMBER = "phoneNumber";
	private static final int   PICK_CONTACT_REQUEST = 1;
	
	private String mPhoneNumber = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPhoneNumber = getIntent().getStringExtra(EXTRA_PHONENUMBER);
		
		setTitle("SMS Call me ");
		setContentView(R.layout.main);
		Button callButton = (Button)findViewById(R.id.call);
		Button answer = (Button)findViewById(R.id.answer);
		Button forward = (Button)findViewById(R.id.forward);
	
		callButton.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				Intent intent= HelperClass.createCallIntent(mPhoneNumber);
				startActivity(intent);
				finish();
			}
		});
		
		answer.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				Intent intent = new Intent(SMSDialog.this,SmsSender.class); 
				intent.putExtra(SmsSender.EXTRA_PHONENUMBER, mPhoneNumber);
				startActivity(intent);
				finish();
			}
		});
		
		forward.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_PICK, People.CONTENT_URI); 
				startActivityForResult(intent,PICK_CONTACT_REQUEST);
			}
		});
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode){
		    case PICK_CONTACT_REQUEST:
		    	if(resultCode == Activity.RESULT_OK){
		    		  Intent sendSMS = new Intent(this,SmsSender.class);
		              sendSMS.putExtra(SmsSender.EXTRA_PHONENUMBER, HelperClass.processContactsResult(this, data));
		              startActivity(sendSMS);
		        }
		    break;
		    default:
				Log.e("SMSCallMe", "<SMSDialog> bad acitivity result");
		}
		finish();
	}

}
