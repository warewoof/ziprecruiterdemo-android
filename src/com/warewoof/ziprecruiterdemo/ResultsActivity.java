package com.warewoof.ziprecruiterdemo;

import java.net.URLEncoder;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class ResultsActivity extends Activity {

	public static final String TAG = "ResultsActivity";
	public static final String ARG_SEARCH_TEXT = "search_text";
	public static final String ARG_SEARCH_LOCATION = "search_loc";
	public static final String ARG_SEARCH_RADIUS = "search_rad";
	private WebView resultsView;
	
	
	 
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.main_results);
		

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		final ProgressBar Pbar;
		Pbar = (ProgressBar) findViewById(R.id.pB1);
		
		
		Intent intent = getIntent();
		
		resultsView = (WebView) findViewById(R.id.results_view);
		resultsView.getSettings().setJavaScriptEnabled(true);
		resultsView.getSettings().setBuiltInZoomControls(true);

		String optText = intent.getStringExtra(ARG_SEARCH_TEXT);
		String optLoc = intent.getStringExtra(ARG_SEARCH_LOCATION);
		int optRad =  intent.getIntExtra(ARG_SEARCH_RADIUS,50);
		Log.d(TAG, "Search text: " + optText);
		Log.d(TAG, "Location text: " + optLoc);

		String searchArgs = "";

		try {
			           	
			searchArgs += "search=";
			searchArgs += URLEncoder.encode(optText.trim(), "utf-8");				            
			searchArgs += "&location=";
			if (!optLoc.equals("")) {
				searchArgs += URLEncoder.encode(optLoc.trim(), "utf-8");
			} else {
				if (optText.equals("")) {
					searchArgs += URLEncoder.encode(MainActivity.DEFAULT_LOCATION, "utf-8");
				}
			}
			searchArgs += "&radius=";
			searchArgs += String.valueOf(optRad);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		searchArgs = "http://jobs.ziprecruiter.com/candidate/search?" + searchArgs;
		Log.d(TAG, "Loading: " + searchArgs);
		
		
		final Activity activity = this;
		
		resultsView.setWebChromeClient(new WebChromeClient() {
			
			public void onProgressChanged(WebView view, int progress) {
				try { 					
					
					if(progress < 100 && Pbar.getVisibility() == ProgressBar.GONE){
	                    Pbar.setVisibility(ProgressBar.VISIBLE);	                    
	                }
					activity.setTitle("Loading... " + String.valueOf(progress) + "%");
	                Pbar.setProgress(progress);
	                if(progress == 100) {
	                    Pbar.setVisibility(ProgressBar.GONE);
	                    activity.setTitle(view.getTitle().replace(" | ZipRecruiter", ""));
	                    
	                }
				} catch (Exception e) {
					e.printStackTrace();
					activity.setTitle("");
					Pbar.setVisibility(ProgressBar.GONE);
				}
			}			
		});

		resultsView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				try { 
					view.loadUrl(url);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				Log.d(TAG, "onPageFinished");
				/* further customization can be made to the returned HTML here	*/
			}  			
			
		});
		
		resultsView.loadUrl(searchArgs);
	}

	@Override
	public void onBackPressed() {
		if (resultsView.canGoBack()) {
			resultsView.goBack();			
		} else {
			super.onBackPressed();
		}
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		super.onBackPressed();
	    return true;

	}
	
	

}