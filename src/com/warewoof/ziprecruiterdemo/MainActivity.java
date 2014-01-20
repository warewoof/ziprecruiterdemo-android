package com.warewoof.ziprecruiterdemo;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	public static final String TAG = "MainActivity";
	public static final String DEFAULT_LOCATION = "Los Angeles";
	private EditText pLocationBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_search);

		setTitle(Html.fromHtml("<i><b>Zip</b></i>Recruiter"));

		TextView headerText = (TextView) findViewById(R.id.header_text);
		headerText.setText(Html.fromHtml("Search <b>hundreds of job boards</b> with one click."));

		/* Initialize SeekBar */
		SeekBar radiusSeek = (SeekBar) findViewById(R.id.radius_seekbar);
		final TextView radiusText = (TextView) findViewById(R.id.radius_text);
		radiusText.setText("Within "+String.valueOf(radiusSeek.getProgress()) + " miles");     
		radiusSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {        	
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				radiusText.setText("Within "+String.valueOf(arg0.getProgress()) + " miles");
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				radiusText.setText("Within "+String.valueOf(arg0.getProgress()) + " miles");
			}     	
		});

		EditText locationBox = (EditText) findViewById(R.id.job_location);
		pLocationBox = locationBox; // save reference
		locationBox.setOnKeyListener(new OnKeyListener () {

			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_ENTER:
						searchButtonClick(null);
						return true;
					default:
						break;
					}	
				}
				return false;
			}

		});
	}

	public void searchButtonClick(View view) {
		Intent results = new Intent(MainActivity.this, ResultsActivity.class);
		EditText jobText = (EditText) findViewById(R.id.job_description);
		EditText locationText = (EditText) findViewById(R.id.job_location);
		SeekBar radiusSeek = (SeekBar) findViewById(R.id.radius_seekbar);
		results.putExtra(ResultsActivity.ARG_SEARCH_TEXT, jobText.getText().toString());
		results.putExtra(ResultsActivity.ARG_SEARCH_LOCATION, locationText.getText().toString());
		results.putExtra(ResultsActivity.ARG_SEARCH_RADIUS, radiusSeek.getProgress());
		MainActivity.this.startActivity(results);
	}

	public void locationButtonClick(View view) {
		String location = getLocation();
		if (location != null) {
			pLocationBox.setText(location);
		} else {
			//Toast.makeText(MainActivity.this, "Current position not found,  please type in a location", Toast.LENGTH_SHORT).show();
			pLocationBox.setText(DEFAULT_LOCATION);	 
		}
		Log.d(TAG, "locationButtonClick clicked " + location);

	}

	private String getLocation() {
		try {
			LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			List<String> providers = lm.getProviders(true);

			Location l = null;

			for (int i=providers.size()-1; i>=0; i--) {
				l = lm.getLastKnownLocation(providers.get(i));
				if (l != null) break;
			}
			
			if (l == null) {
				return null;
			}
			
			Geocoder gcd = new Geocoder(MainActivity.this, Locale.getDefault());
			List<Address> addresses = null;

			addresses = gcd.getFromLocation(l.getLatitude(), l.getLongitude(), 1);

			if (addresses.size() > 0) { 
				if (addresses.get(0).getLocality() != null) {
					Log.d(TAG, "Returning getLocality");
					return addresses.get(0).getLocality();
				} else if (addresses.get(0).getSubAdminArea() != null) {
					Log.d(TAG, "Returning getSubAdminArea");
					return addresses.get(0).getSubAdminArea();
				} else if (addresses.get(0).getAdminArea() != null) {
					Log.d(TAG, "Returning getAdminArea");
					return addresses.get(0).getAdminArea().replace(" City", "");	// some international city names add "City"
				} else if (addresses.get(0).getCountryName() != null) {
					Log.d(TAG, "Returning getCountryName");
					return addresses.get(0).getCountryName();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(TAG, "Error fetching location");
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent about = new Intent(MainActivity.this, AboutActivity.class);
		MainActivity.this.startActivity(about);
		return true;
	}




}
