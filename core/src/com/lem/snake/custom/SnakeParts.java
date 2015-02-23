package com.lem.snake.custom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class SnakeParts extends SNRectBoundsActor {
	
	public static enum DIRECTIONS{
		D1(new Vector2(10,0)),
		D2(new Vector2(0,10)),
		D3(new Vector2(-10,0)),
		D4(new Vector2(0,-10));
		
		private Vector2 direction;
		
		private DIRECTIONS(Vector2 direction){
			this.direction = direction;
		}
		
		public Vector2 getDirection(){
			return direction;
		}
	}
	
	private DIRECTIONS currentDirection;
	
	public SnakeParts(Texture image) {
		super(image);
		//def direction ---> x:1, y:0
		currentDirection = DIRECTIONS.D1;
	}
	
	public void changeDirection(DIRECTIONS newDirection){
		this.currentDirection = newDirection;
	}
	
	public DIRECTIONS getDirection(){
		return currentDirection;
	}
	
	public void doMovement(){
		this.setPosition(this.getX()+currentDirection.getDirection().x, this.getY()+currentDirection.getDirection().y);
	}

}
