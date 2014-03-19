package com.example.notes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlLiteDatabase extends SQLiteOpenHelper{
	
	private static final String dbname = "notes";
	private static final Integer dbversion = 1;
	
	public SqlLiteDatabase(Context context) {
		super(context, dbname, null, dbversion);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + dbname + " ("
						+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ "titre TEXT NOT NULL, "
						+ "content TEXT NOT NULL, "
						+ "password TEXT NULL);");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + dbname);
		this.onCreate(db);
	}
}
