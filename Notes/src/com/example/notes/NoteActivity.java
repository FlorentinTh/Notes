package com.example.notes;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

public class NoteActivity extends Activity implements OnClickListener{
	
	private String id;
	private String titre;
	private String content;
	private String password;
	private Boolean isPwdSet;
	private TextView textViewTitre;
	private TextView textViewContent;
	private Button buttonModifier;
	private ShareActionProvider shareActionProvider;
	private NoteDatabase noteDatabase = new NoteDatabase(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		password = intent.getStringExtra("password");
		isPwdSet = intent.getBooleanExtra("isPwdSet", false);
		
		titre = noteDatabase.getTitreById(id);
				
		textViewTitre = (TextView) findViewById(R.id.titreReadableNote);
		textViewTitre.setText(titre);
		
		
		content = noteDatabase.getContentById(id);
		
		if(isPwdSet == true && password != null){
			content = CryptUtil.decryptContentByPassword(noteDatabase.getContentById(id), password);
		}else{
			content = noteDatabase.getContentById(id);
		}
		
		textViewContent = (TextView) findViewById(R.id.contentReadableNote);
		textViewContent.setText(content);
		
		textViewContent.setMovementMethod(new ScrollingMovementMethod());
		
		buttonModifier = (Button) findViewById(R.id.btnEdit);
		buttonModifier.setOnClickListener(this);
		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		this.onCreate(null);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnEdit:
			Intent intent = new Intent(this, EditActivity.class);
			intent.putExtra("id", id);
			intent.putExtra("titre", titre);
			intent.putExtra("content", content);
			intent.putExtra("password", password);
			startActivityForResult(intent, 1);
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.note, menu);
		
		MenuItem item = menu.findItem(R.id.share);
		shareActionProvider = (ShareActionProvider) item.getActionProvider();
		shareActionProvider.setShareIntent(shareIntent());
		return true;
	}
	
	private void delete(){
		try{
			noteDatabase.supprimerNote(id);
		}catch(Exception e){
			Log.w("DB delete operation exception", e);
		}
		Toast.makeText(this, getResources().getString(R.string.deleted_note), Toast.LENGTH_SHORT).show();
	}
	
	private void afficherPopupSuppr(){
		AlertDialog.Builder alert = new AlertDialog.Builder(NoteActivity.this);
		alert.setTitle(getResources().getString(R.string.alertDeleteTitle));
		alert.setMessage(getResources().getString(R.string.alertDeleteContent));
		alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				delete();
				finish();
			}
		});
		
		alert.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		alert.show();
	}
	
	private Intent shareIntent(){
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
    	shareIntent.setType("text/plain");
    	shareIntent.putExtra(Intent.EXTRA_TEXT, titre + "\n\n" + content);
		return shareIntent;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    	case android.R.id.home:
	    		finish();
	    		return true;
	        case R.id.about:
	        	Intent intent = new Intent(this, AboutActivity.class);
				startActivity(intent);
	            return true;
	        case R.id.suppr:
	        	afficherPopupSuppr();
	            return true;
	        case R.id.share:
	        	Toast.makeText(NoteActivity.this, "CLICK", Toast.LENGTH_SHORT).show();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
