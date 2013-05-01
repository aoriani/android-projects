package com.example.testjni;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

    static {
        System.loadLibrary("teste");
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sum(3,5);
        
    }

    public void setTextView(int i) {
        TextView tv = (TextView)findViewById(R.id.text);
        tv.setText("" +i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public native int sum(int a, int b);
}
