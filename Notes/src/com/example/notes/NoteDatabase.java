package com.example.notes;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NoteDatabase {
	
	private static final String dbname = "notes";
	private SqlLiteDatabase DB;
	
	public NoteDatabase(Context context){
		DB = new SqlLiteDatabase(context);
	}
	
	public List<Note> listerNotes() {
		List<Note> results = new ArrayList<Note>();
		Cursor cursor = DB.getReadableDatabase().query(dbname, new String[] {"id", "titre", "content", "password"}, null, null, null, null, null);
		
		while(cursor.moveToNext()){
			Integer id = cursor.getInt(0);
			String titre = cursor.getString(1);
			String content = cursor.getString(2);
			String password = cursor.getString(3);
			Note uneNote = new Note(id, titre, content, password);
			results.add(uneNote);
		}
		cursor.close();
		
		return results;
	}
	
	public String getTitreById(String id){
		Cursor cursor = DB.getReadableDatabase()
				.query(dbname, 
				new String[] {"id", "titre"}, "id = " + id, null,
				null, null, null);
		cursor.moveToFirst();
		String result = cursor.getString(1);
		cursor.close();
		return result;
	}
	
	public String getContentById(String id){
		Cursor cursor = DB.getReadableDatabase()
				.query(dbname, 
				new String[] {"id", "content"}, "id = " + id, null,
				null, null, null);
		cursor.moveToFirst();
		String result = cursor.getString(1);
		cursor.close();
		return result;
	}
	
	public String getPasswordById(String id){
		Cursor cursor = DB.getReadableDatabase()
				.query(dbname, 
				new String[] {"id", "password"}, "id = " + id, null,
				null, null, null);
		cursor.moveToFirst();
		String result = cursor.getString(1);
		cursor.close();
		return result;
	}
	
	public void sauvegarderNote(Note note){
		ContentValues values = new ContentValues();
		values.put("titre", note.getTitre());
		values.put("content", note.getContent());
		values.put("password", note.getPassword());
		
		SQLiteDatabase writeDB = DB.getWritableDatabase();
		writeDB.insert(dbname, null, values);
		writeDB.close();
	}
	
	public void modifierNote(String id, String titre, String content){
		ContentValues values = new ContentValues();
		values.put("titre", titre);
		values.put("content", content);
		
		SQLiteDatabase writeDB = DB.getWritableDatabase();
		writeDB.update(dbname, values, "id = " + id, null);
		writeDB.close();
	}
	
	public void supprimerNote(String id){
		SQLiteDatabase writeDB = DB.getWritableDatabase();
		writeDB.delete(dbname, "id = " + id, null);
		writeDB.close();
	}
}
