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

public class EditActivity extends Activity implements OnClickListener{

	private EditText editTextTitre;
	private EditText editTextNote;
	private Button buttonEdit;
	private String titre;
	private String id;
	private String content;
	private String password;
	private String newTitre;
	private String newContent;
	private NoteDatabase noteDatabase = new NoteDatabase(this);
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		titre = intent.getStringExtra("titre");
		content = intent.getStringExtra("content");
		password = intent.getStringExtra("password");
				
		editTextTitre = (EditText) findViewById(R.id.editTitre);
		editTextTitre.setText(titre);

		editTextNote = (EditText) findViewById(R.id.editNote);
		editTextNote.setText(content);
			
		buttonEdit = (Button) findViewById(R.id.btnEditer);
		buttonEdit.setOnClickListener(this);
	}
	
	private void update(){
		if(!"".equals(editTextTitre.getText().toString())){
			newTitre = editTextTitre.getText().toString();
			if(password != null){
				newContent = CryptUtil.cryptContentByPassword(editTextNote.getText().toString(),password);
			}else{
				newContent = editTextNote.getText().toString();
			}
	
			try{
				noteDatabase.modifierNote(id, newTitre, newContent);
			}catch(Exception e){
				Log.w("DB update operation exception", e);
			}
			Toast.makeText(this, getResources().getString(R.string.updated_note), Toast.LENGTH_SHORT).show();
			finish();
		}else{
			Toast.makeText(this, getResources().getString(R.string.required_title), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btnEditer:
				update();
				break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit, menu);
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
