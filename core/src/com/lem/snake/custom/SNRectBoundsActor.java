package com.lem.snake.custom;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SNRectBoundsActor extends Actor {

    private Rectangle rectangle;
    protected Texture texture;
    private ShapeRenderer shapeRenderer;
    private Color c;

    public SNRectBoundsActor(Texture image) {
        texture = image;
        rectangle = new Rectangle();
        shapeRenderer = new ShapeRenderer();
        this.setWidth(texture.getWidth());
        this.setHeight(texture.getHeight());
        c = new Color(Color.RED);
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
    	batch.draw(new TextureRegion(texture), this.getX(), this.getY(), this.getOriginX(), this.getOriginY(), this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation());
//        batch.end();
//      
    	
    	rectangle.set(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    	shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
//    
//        shapeRenderer.begin(ShapeType.Filled);
//    	  
//        shapeRenderer.setColor(c);
//        shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
//        shapeRenderer.end();
//      
//        batch.begin();
    }
    
    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
    }

    @Override
    public void setX(float x) {
        super.setX(x);
    }

    @Override
    public void setY(float y) {
        super.setY(y);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        super.setX(x);
        super.setY(y);
    }

    public void setPosition(Vector2 postition) {
        setPosition(postition.x, postition.y);
    }
    
    public void setTexture(Texture texture){
        this.texture = texture;
    }
    
    public void changeColor(Color c){
    	this.c = c;
    }


}
