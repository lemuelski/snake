package com.lem.snake.custom;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lem.snake.SnakeBlocks;

public class SNScreen implements Screen {

		protected Stage stage;
	    protected SnakeBlocks game;
	    
	    public SNScreen(SnakeBlocks game){
	        this.game = game;    
	        stage = new Stage(new FitViewport(SnakeBlocks.WIDTH, SnakeBlocks.HEIGHT));
	    }
	    
	    @Override
	    public void render(float delta) {
	        stage.act(delta);
	        stage.draw();
	    }

	    @Override
	    public void resize(int width, int height) {
	        stage.getViewport().update(width, height, true);
	    }

	    @Override
	    public void show() {
	        // TODO Auto-generated method stub
	        
	    }

	    @Override
	    public void hide() {
	        stage.dispose();
	    }

	    @Override
	    public void pause() {
	        // TODO Auto-generated method stub
	        
	    }

	    @Override
	    public void resume() {
	        // TODO Auto-generated method stub
	        
	    }

	    @Override
	    public void dispose() {
	    }
	    
	    public Stage getStage(){
	        return stage;
	    }
	
}
