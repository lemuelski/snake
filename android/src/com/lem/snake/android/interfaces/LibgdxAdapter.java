package com.lem.snake.android.interfaces;

import com.lem.snake.interfaces.AndroidAdapterInterface;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class LibgdxAdapter implements AndroidAdapterInterface {
	
	private Context context;
	
	private final String SHARED_PREF_HIGH = "HighScore";
	
	public LibgdxAdapter(Context context){
		this.context = context;
	}
	
	public void writeToSharedPref(int score){
		SharedPreferences prefs = context.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(SHARED_PREF_HIGH, score);
		editor.commit(); 
	}
	
	public int getHighScore(){
		SharedPreferences prefs = context.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
		int score = prefs.getInt(SHARED_PREF_HIGH, 0);
		return score;
	}
}
