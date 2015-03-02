package com.lem.snake.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.lem.snake.SnakeBlocks;
import com.lem.snake.android.interfaces.LibgdxAdapter;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		LibgdxAdapter libgdxAdapter = new LibgdxAdapter(this);
		
		initialize(new SnakeBlocks(libgdxAdapter), config);
	}
}
