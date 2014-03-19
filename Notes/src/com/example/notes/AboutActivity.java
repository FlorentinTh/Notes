package com.example.notes;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends Activity implements OnClickListener{

	private TextView textViewTitre;
	private TextView textViewContent;
	private Button buttonFermer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		
		textViewTitre = (TextView) findViewById(R.id.aboutTitle);
		textViewTitre.setText(getResources().getString(R.string.title_activity_about));
		
		textViewContent = (TextView) findViewById(R.id.aboutContent);
		textViewContent.setText(getResources().getString(R.string.content_about));
				
		buttonFermer = (Button) findViewById(R.id.closeBtn);
		buttonFermer.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.closeBtn:
				finish();
				break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case android.R.id.home:
	    		finish();
	    		return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
