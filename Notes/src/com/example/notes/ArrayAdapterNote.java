package com.example.notes;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArrayAdapterNote extends ArrayAdapter<Note>{
	Context ctx;
	int layoutResourceId;
	List<Note> data = null;
	
	public ArrayAdapterNote(Context ctx, int layoutResourceId, List<Note> data){
		super(ctx, layoutResourceId, data);
		
		this.ctx = ctx;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if(convertView == null){
			LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
			convertView = inflater.inflate(layoutResourceId, parent, false);
		}
		
		Note note = data.get(position);
		
		TextView textViewTitle = (TextView) convertView.findViewById(R.id.textViewTitre);
		textViewTitle.setText(note.getTitre());
		if(note.getPassword() != null){
			textViewTitle.setTextColor(Color.parseColor("#7f8c8d"));
		}
		textViewTitle.setTag(note.getId());
		return convertView;
	}
}
