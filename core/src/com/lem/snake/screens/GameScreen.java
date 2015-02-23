package com.lem.snake.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.lem.snake.SnakeBlocks;
import com.lem.snake.custom.SNRectBoundsActor;
import com.lem.snake.custom.SNScreen;
import com.lem.snake.custom.SnakeParts;
import com.lem.snake.custom.SnakeParts.DIRECTIONS;

public class GameScreen extends SNScreen {

	private SnakeParts head;
	private SnakeParts tail;
	private SnakeParts feed;
	
	private List<Vector2> anchorPoints;
	private List<DIRECTIONS> anchorDirections;
	private List<Float> pressEvents;
	private List<SnakeParts> body;
	
	
	private float timer = 1.0f;

	public GameScreen(SnakeBlocks game) {
		super(game);
		
		anchorPoints = new ArrayList<Vector2>();
		anchorDirections = new ArrayList<DIRECTIONS>();
		pressEvents = new ArrayList<Float>();
		
		body = new ArrayList<SnakeParts>();
		
		head = new SnakeParts(new Texture(Gdx.files.internal("block.png")));
		body.add(new SnakeParts(new Texture(Gdx.files.internal("block.png"))));
		tail = new SnakeParts(new Texture(Gdx.files.internal("block.png")));
		tail.changeColor(Color.BLUE);
		feed = new SnakeParts(new Texture(Gdx.files.internal("block.png")));
		feed.setPosition(randomizePosition());
		
		stage.addActor(head);
		stage.addActor(body.get(0));
		stage.addActor(tail);
		stage.addActor(feed);
		
		head.setPosition(head.getWidth(), SnakeBlocks.HEIGHT/2-head.getHeight()/2);
		body.get(0).setPosition(head.getX()-head.getWidth(), head.getY());
		tail.setPosition(body.get(0).getX()-body.get(0).getWidth(), head.getY());
		
		stage.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
//				SNRectBoundsActor corner = new SNRectBoundsActor(new Texture(Gdx.files.internal("corner.png")));
//				corner.setPosition(head.getX(), head.getY());
//				stage.addActor(corner);
				pressEvents.add(x);
				
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
			}
		});
		
		Gdx.input.setInputProcessor(stage);
		
	}
	
	@Override
	public void render(float delta) {
		timer+= delta;
		
		if (timer>=0.1f){
			
		timer = 0.0f;
		
		readPressEvents();
		
		int indexAnchor = reachedAnchorPoint(tail.getX(), tail.getY());
		if(indexAnchor!=-1){
			Gdx.app.log("lem", "direction changed");
			tail.changeDirection(anchorDirections.get(indexAnchor));
			anchorDirections.remove(indexAnchor);
			anchorPoints.remove(indexAnchor);
		}
		tail.setPosition(tail.getX()+tail.getDirection().getDirection().x, tail.getY()+tail.getDirection().getDirection().y);
		
		for (SnakeParts parts:body){
			indexAnchor = reachedAnchorPoint(parts.getX(), parts.getY());
			if(indexAnchor!=-1){
				parts.changeDirection(anchorDirections.get(indexAnchor));
			}
			parts.setPosition(parts.getX()+parts.getDirection().getDirection().x, parts.getY()+parts.getDirection().getDirection().y);
		}
		
		head.setPosition(head.getX()+head.getDirection().getDirection().x, head.getY()+head.getDirection().getDirection().y);
		
		if (Intersector.overlaps(head.getRectangle(), feed.getRectangle())){
			feed.setPosition(randomizePosition());
			SnakeParts tempPart = new SnakeParts(new Texture(Gdx.files.internal("block.png")));
			
			tempPart.setPosition(tail.getX(), tail.getY());
			tempPart.changeDirection(tail.getDirection());
			
			body.add(tempPart);
			
			switch (tail.getDirection()) {
			case D1:
				tail.setPosition(tempPart.getX()-10, tempPart.getY());
				break;
			case D2:
				tail.setPosition(tempPart.getX(), tempPart.getY()-10); 
				break;
			case D3:
				tail.setPosition(tempPart.getX()+10, tempPart.getY());
				break;
			case D4:
				tail.setPosition(tempPart.getX(), tempPart.getY()+10);
				break;
			}
			
			stage.addActor(tempPart);
		}
		
		}
		
		
		
		super.render(delta);
	}
	
	private int reachedAnchorPoint(float x, float y){
		int index = 0;
		
		if (anchorPoints.size()==0)
			return -1;
		
		for (Vector2 anchor:anchorPoints){
			if(anchor.x == x && anchor.y == y){
				return index;
			}
			index++;
		}
		return -1;
	}
	
	private void readPressEvents(){
		if (pressEvents.size()==0)
			return;
		
		anchorPoints.add(new Vector2(head.getX(), head.getY()));
		
		float x = pressEvents.get(0);
		//right side
		if (x>SnakeBlocks.WIDTH/2){
			if (head.getDirection().getDirection().x != 0){
				head.changeDirection(DIRECTIONS.D2);
			}
			else{
				head.changeDirection(DIRECTIONS.D1);
			}
		}
		//Left side
		else{
			if (head.getDirection().getDirection().x != 0){
				head.changeDirection(DIRECTIONS.D4);
			}
			else{
				head.changeDirection(DIRECTIONS.D3);
			}			
		}
		anchorDirections.add(head.getDirection());
		pressEvents.remove(0);
	}
	
	private Vector2 randomizePosition(){
		return new Vector2((float)Math.random()*SnakeBlocks.WIDTH, (float)Math.random()*SnakeBlocks.HEIGHT);
	}
}
