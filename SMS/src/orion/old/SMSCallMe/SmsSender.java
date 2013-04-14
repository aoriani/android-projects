package orion.old.SMSCallMe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import orion.old.SMSCallMe.R;

public class SmsSender extends Activity {

	public static final String EXTRA_PHONENUMBER = "phoneNumber";
	private String mPhoneNumber = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mPhoneNumber = getIntent().getStringExtra(EXTRA_PHONENUMBER);
		
		setTitle("Reply to "+mPhoneNumber);
		setContentView(R.layout.smssender);
		final EditText msgComposer = (EditText) findViewById(R.id.msg);
		Button sendButton = (Button)findViewById(R.id.send);
		
		sendButton.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				HelperClass.sendSMS(mPhoneNumber,msgComposer.getText().toString());
				finish();
			}
			
		});
		
		
		
	}

}
