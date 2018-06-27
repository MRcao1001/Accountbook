package com.part5.accountbook.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.part5.accountbook.MainActivity;
import com.part5.accountbook.R;
import com.part5.accountbook.mainList.MainListActivity;


public class splashActivity extends Activity {

	private static final long DELAY_TIME =3000L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent =new Intent(splashActivity.this, MainListActivity.class);
				startActivity(intent);
				finish();
			}
        	
        }, DELAY_TIME);
    }

}
