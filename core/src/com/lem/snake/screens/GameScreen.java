package com.lem.snake.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lem.snake.SnakeBlocks;
import com.lem.snake.custom.SNRectBoundsActor;
import com.lem.snake.custom.SNScreen;
import com.lem.snake.custom.SnakeParts;
import com.lem.snake.custom.SnakeParts.DIRECTIONS;

public class GameScreen extends SNScreen {

	private SnakeParts head;
	private SnakeParts tail;
	private SnakeParts feed;
	private SNRectBoundsActor retryBtn;
	
	private boolean start = false;
	private int scorePlayer =0;
	private Label score;
	private Label highScore;
	
	private int playerHS = 0;
	
	private SNRectBoundsActor background;
	
	private List<Vector2> anchorPoints;
	private List<DIRECTIONS> anchorDirections;
	private List<Float> pressEvents;
	private List<SnakeParts> body;
	
	private Label text;
	private LabelStyle textStyle;
	private BitmapFont font = new BitmapFont();
	
	private boolean isAlive = true;
	
	private float gameBounds[] = new float[4];
	private float timer = 1.0f;

	public GameScreen(SnakeBlocks game) {
		super(game);
		
		anchorPoints = new ArrayList<Vector2>();
		anchorDirections = new ArrayList<DIRECTIONS>();
		pressEvents = new ArrayList<Float>();
		
		body = new ArrayList<SnakeParts>();
		
		background = new SNRectBoundsActor(new Texture(Gdx.files.internal("bgGame.png")));
		background.setPosition(SnakeBlocks.WIDTH/2 - background.getWidth()/2, SnakeBlocks.HEIGHT/2 - background.getHeight()/2);
						//l, r, d, u
		gameBounds[0] =  background.getX();
		gameBounds[1] =  background.getRight();
		gameBounds[2] =  background.getY();
		gameBounds[3] =  background.getY()+(int)background.getHeight();
		
		retryBtn = new SNRectBoundsActor(new Texture(Gdx.files.internal("retry.png")));
		retryBtn.setY(background.getTop()+10);
		retryBtn.setX(10.0f);
		retryBtn.setScale(0.3f);
		
		retryBtn.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				initGameScreen();
			}
		});
		
		textStyle = new LabelStyle();
		textStyle.font = font;

		text = new Label("GameOver",textStyle);
		text.setColor(Color.WHITE);
		text.setFontScale(1f,1f);
		
		text.setPosition(SnakeBlocks.WIDTH/2 - text.getWidth(), SnakeBlocks.HEIGHT/2 - text.getHeight());
		text.setVisible(false);
		
		score = new Label("0", textStyle);
		score.setColor(Color.RED);
		score.setFontScale(2f,2f);
		score.setPosition(SnakeBlocks.WIDTH/2 - score.getWidth(), background.getTop()+10);
		
		playerHS = game.getAndroidAdapter().getHighScore();
		
		highScore = new Label("HighScore : " +playerHS, textStyle);
		highScore.setColor(Color.RED);
		highScore.setFontScale(1f,1f);
		
		highScore.setPosition(10, background.getY() - highScore.getHeight());
		
		initGameScreen();
		
	}
	
	@Override
	public void render(float delta) {
		timer+= delta;
		
		if (timer>=0.05f && isAlive){
			
		timer = 0.0f;
		
		readPressEvents();
		
		if (start) {
		if (Intersector.overlaps(head.getRectangle(), tail.getRectangle())){
			if (scorePlayer > playerHS)
				game.getAndroidAdapter().writeToSharedPref(scorePlayer);
			text.setVisible(true);
			isAlive = false;
			return;
		}
		
		if (colidedWithWall()){
			if (scorePlayer > playerHS)
				game.getAndroidAdapter().writeToSharedPref(scorePlayer);
			text.setVisible(true);
			isAlive = false;
			return;
		}
		
		for (SnakeParts part:body){
			if (Intersector.overlaps(head.getRectangle(), part.getRectangle())){
				text.setVisible(true);
				isAlive = false;
				return;
			}
		}

		tail.updateDirection();
		tail.setPosition(tail.getX()+tail.getDirection().getDirection().x, tail.getY()+tail.getDirection().getDirection().y);

		for (SnakeParts parts:body){
			parts.updateDirection();
		}
		head.setPosition(head.getX()+head.getDirection().getDirection().x, head.getY()+head.getDirection().getDirection().y);
		checkFeedCollision();
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
		start = true;
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
		float x = (float)Math.random()*(gameBounds[1] - feed.getWidth());
		float y = (float)Math.random()*(gameBounds[3]- feed.getHeight());
		
		x = x - (x%10);
		y = y - (y%10);
		
		Vector2 tempVector = new Vector2(x, y );
	
		if (tempVector.y < background.getY()){
			tempVector.set(tempVector.x, tempVector.y+background.getY());
		}
		return tempVector;
	}
	
	private void checkFeedCollision(){
		if (Intersector.overlaps(head.getRectangle(), feed.getRectangle())){
			scorePlayer++;
			score.setText(Integer.toString(scorePlayer));
			score.setPosition(SnakeBlocks.WIDTH/2 - score.getWidth(), background.getTop()+10);
			
			feed.setPosition(randomizePosition());
			SnakeParts tempPart = new SnakeParts(new Texture(Gdx.files.internal("block.png"))){
				@Override
				public void updateDirection() {	
					int indexAnchor = reachedAnchorPoint(getX(), getY());
					if(indexAnchor!=-1){
						changeDirection(anchorDirections.get(indexAnchor));
					}
					setPosition(getX()+getDirection().getDirection().x, getY()+getDirection().getDirection().y);
				}
			};
			
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
	
	private boolean colidedWithWall(){
		float headX = head.getX()+head.getWidth();
		float headY = head.getY()+head.getHeight();
		
		return headX >= SnakeBlocks.WIDTH || headX <= 0 || headY <= background.getY() || headY >= background.getTop();
	}
	
	private void initGameScreen(){
		body.clear();
		stage.clear();
		anchorPoints.clear();
		anchorDirections.clear();
		pressEvents.clear();
		start = false;
		isAlive = true;
		timer = 0.0f;
		scorePlayer = 0;
		
		playerHS = game.getAndroidAdapter().getHighScore();
		
		score.setText("0");
		highScore.setText("HighScore : "+playerHS);
		head = new SnakeParts(new Texture(Gdx.files.internal("block.png"))){
			@Override
			public void updateDirection() {	
			}
		};
		
		head.changeColor(Color.YELLOW);
		body.add(new SnakeParts(new Texture(Gdx.files.internal("block.png"))){
			@Override
			public void updateDirection() {
				int indexAnchor = reachedAnchorPoint(getX(), getY());
				if(indexAnchor!=-1){
					changeDirection(anchorDirections.get(indexAnchor));
				}
				setPosition(getX()+getDirection().getDirection().x, getY()+getDirection().getDirection().y);
			}
		});
		tail = new SnakeParts(new Texture(Gdx.files.internal("block.png"))){
			@Override
			public void updateDirection() {
				int indexAnchor = reachedAnchorPoint(tail.getX(), tail.getY());
				if(indexAnchor!=-1){
					tail.changeDirection(anchorDirections.get(indexAnchor));
					anchorDirections.remove(indexAnchor);
					anchorPoints.remove(indexAnchor);
				}
			}
		};
		
		tail.changeColor(Color.BLUE);
		feed = new SnakeParts(new Texture(Gdx.files.internal("block.png"))){
			@Override
			public void updateDirection() {	
			}
		};
		feed.setPosition(randomizePosition());
		
		stage.addActor(background);
		stage.addActor(head);
		stage.addActor(body.get(0));
		stage.addActor(tail);
		stage.addActor(feed);
		stage.addActor(retryBtn);
		stage.addActor(score);
		stage.addActor(highScore);
		
		text.setVisible(false);
		
		head.setPosition(head.getWidth(), background.getY()+head.getHeight()+10);
		body.get(0).setPosition(head.getX()-head.getWidth(), head.getY());
		tail.setPosition(body.get(0).getX()-body.get(0).getWidth(), head.getY());

		stage.addActor(text);
		
		stage.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
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
}
