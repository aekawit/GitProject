package com.aeke.Project;

import com.example.mainapp.R;
import com.example.mainapp.R.id;
import com.example.mainapp.R.layout;
import com.example.mainapp.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Main2Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main2, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final MediaPlayer mpBtn3 = MediaPlayer.create(this, R.raw.buttonclickk);
		
		int id = item.getItemId();
		if (id == R.id.reply_icon) {
			mpBtn3.start();
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
