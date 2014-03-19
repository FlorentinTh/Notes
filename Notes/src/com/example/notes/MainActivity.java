package com.example.notes;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	
	private Button buttonAjouter;
	private List<Note> notes;
	private ArrayAdapterNote adapter;
	private ListView notesListview;
	private String ident;
	private String password;
	private NoteDatabase noteDatabase = new NoteDatabase(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
				
		buttonAjouter = (Button) findViewById(R.id.btnAjouter);
		buttonAjouter.setOnClickListener(this);
		
		notes = noteDatabase.listerNotes();

		adapter = new ArrayAdapterNote(this, R.layout.single_row_item, notes);
		notesListview = (ListView) findViewById(R.id.liste);
		notesListview.setAdapter(adapter);

		notesListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				TextView textViewTitre = ((TextView) view.findViewById(R.id.textViewTitre));
				ident = textViewTitre.getTag().toString();
								
				password = noteDatabase.getPasswordById(ident);

				if(password == null){
					Intent intent = new Intent(MainActivity.this, NoteActivity.class);
					intent.putExtra("id", ident);
					intent.putExtra("password", password);
					intent.putExtra("isPwdSet", false);
					startActivity(intent);
				}else{
					AlertDialog alert = buildPopupPwd(false, ident).create();
					alert.show();	 
				}
			}
		});

		notesListview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				TextView textViewTitre = ((TextView) view.findViewById(R.id.textViewTitre));
				String ident = textViewTitre.getTag().toString();
				String password = noteDatabase.getPasswordById(ident);

				if(password == null){
					afficherPopupSuppr(ident, false);
				}else{
					afficherPopupSuppr(ident, true); 
				}
				return true;
			}
		});
	}
	
	private AlertDialog.Builder buildPopupPwd(boolean delete, String id){
		final boolean pwdDelete = delete;
		final String ident = id;
		final String password = noteDatabase.getPasswordById(ident);
		LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
		View pwdPrompt = layoutInflater.inflate(R.layout.prompt, null);
		AlertDialog.Builder alertPassword = new AlertDialog.Builder(MainActivity.this);
		alertPassword.setView(pwdPrompt);
		alertPassword.setTitle(getResources().getString(R.string.prompTitle));
		final EditText pwdInput = (EditText) pwdPrompt.findViewById(R.id.pwdInput);
		alertPassword.setCancelable(false).setPositiveButton(getResources().getString(R.string.alertValidBtn), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(password.equals(CryptUtil.hashMd5(pwdInput.getText().toString().trim()))){
					if(pwdDelete == true){
						delete(ident);
						onResume();
					}else{
						Intent intent = new Intent(MainActivity.this, NoteActivity.class);
						intent.putExtra("id", ident);
						intent.putExtra("password", password);
						intent.putExtra("isPwdSet", true);
						startActivity(intent); 
					}
				}else{
					Toast.makeText(MainActivity.this, getResources().getString(R.string.wrong_pwd), Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				}
			}
		}).setNegativeButton(getResources().getString(R.string.alertCancelBtn), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).setOnCancelListener(new OnCancelListener(){
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				
			}
		}).setOnKeyListener(new OnKeyListener(){
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_BACK){
					dialog.dismiss();
				}else{
					return false;
				}
				return true;
			}
		});
		return alertPassword;
	}
	
	private void afficherPopupSuppr(String id, boolean pwd){
		final String ident = id;
		final boolean password = pwd;
		AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
		alert.setTitle(getResources().getString(R.string.alertDeleteTitle));
		alert.setMessage(getResources().getString(R.string.alertDeleteContent));
		alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(password == true){
					AlertDialog alert = buildPopupPwd(true, ident).create();
					alert.show();
				}else{
					delete(ident);
					onResume();
				}
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
	
	private void delete(String ident) {
		try{
			noteDatabase.supprimerNote(ident);
		}catch(Exception e){
			Log.w("DB delete operation exception", e);
		}
		
		Toast.makeText(this, getResources().getString(R.string.deleted_note), Toast.LENGTH_SHORT).show();
	}
		
	@Override
	protected void onResume(){
		super.onResume();
		this.onCreate(null);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btnAjouter:
				Intent intent = new Intent(this, AddActivity.class);
				startActivity(intent);
				break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.about:
	        	Intent intent = new Intent(this, AboutActivity.class);
	        	intent.putExtra("isPasswordSet", true);
	        	intent.putExtra("isPasswordSet", false);
				startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}