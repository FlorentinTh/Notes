package com.example.notes;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends Activity implements OnClickListener{
	
	private Button buttonCreer;
	private EditText addTitre;
	private EditText addNote;
	private EditText addPassword;
	private String titre;
	private String content;
	private String password;
	private NoteDatabase noteDatabase = new NoteDatabase(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		
		buttonCreer = (Button) findViewById(R.id.btnCreer);
		buttonCreer.setOnClickListener(this);
		
		addTitre = (EditText) findViewById(R.id.addTitre);
		addNote = (EditText) findViewById(R.id.addNote);
		addPassword = (EditText) findViewById(R.id.addPassword);
	}
	
	private void create(){
		if(!"".equals(addTitre.getText().toString())){
			titre = addTitre.getText().toString();
			
			if(!"".equals(addPassword.getText().toString().trim())){
				password = CryptUtil.hashMd5(addPassword.getText().toString());
				content = CryptUtil.cryptContentByPassword(addNote.getText().toString(), password);
			}else{
				content = addNote.getText().toString();
				password = null;
			}
			
			Note note = new Note(titre, content, password);
			
			try{
				noteDatabase.sauvegarderNote(note);
			}catch(Exception e){
				Log.w("DB save operation exception", e);
			}
			Toast.makeText(this, getResources().getString(R.string.saved_note), Toast.LENGTH_SHORT).show();
			finish();
		}else{
			Toast.makeText(this, getResources().getString(R.string.required_title), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btnCreer:
				create();
				break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add, menu);
		return true;
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
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
