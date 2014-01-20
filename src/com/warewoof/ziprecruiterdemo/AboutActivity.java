package com.warewoof.ziprecruiterdemo;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class AboutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("About this app");

		setContentView(R.layout.menu_about);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		super.onBackPressed();
	    return true;

	}
}