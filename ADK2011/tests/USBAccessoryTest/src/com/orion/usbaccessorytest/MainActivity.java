package com.orion.usbaccessorytest;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

/*
 *  ATTENTION : This is a draft code, only for testing purposes.
 *  There is no careful coding and managing of resources.
 *  You should not use as an example code. 
 *  It is only available for informative purposes
 */





public class MainActivity extends Activity {

	private static final String LOG_TAG = "USB_TEST_ARDUINO";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Hopefully it does not cause a ANR, albeit it is very likely
		
		IntentFilter intentFilter = new IntentFilter(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
		BroadcastReceiver usbDetachedReceiver = new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				Toast.makeText(context, "Accessory Detached", Toast.LENGTH_SHORT).show();
				
			}
			
		};
		
		registerReceiver(usbDetachedReceiver, intentFilter);
		
		UsbAccessory accessory = (UsbAccessory)getIntent().getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
		UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
		ParcelFileDescriptor pfd = manager.openAccessory(accessory);
	    if (pfd != null) {
	        FileDescriptor fd = pfd.getFileDescriptor();
	        FileInputStream inputStream = new FileInputStream(fd);
	        FileOutputStream outputStream = new FileOutputStream(fd);
	        try {
	        	byte[] buffer  = "Ola Android".getBytes("US-ASCII");
	        	outputStream.write((byte)buffer.length);
				outputStream.write(buffer);
				byte size = (byte) inputStream.read();
				buffer = new byte[size];
				inputStream.read(buffer);
				String text = new String(buffer, "US-ASCII");
				TextView textview = (TextView) findViewById(R.id.textview);
				textview.setText(text);
				
			} catch (UnsupportedEncodingException e) {
				Log.e(LOG_TAG, "ASCII is nit supported");
			} catch (IOException e) {
				Log.e(LOG_TAG, "IOPROBLEM");
			}finally{
				try {
					inputStream.close();
					outputStream.close();
				} catch (IOException e) {
					Log.e(LOG_TAG, "Fail at closing");
				}
			}
	    }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
