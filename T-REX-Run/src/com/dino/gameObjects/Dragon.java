package com.dino.gameObjects;

import java.util.HashMap;
import java.util.Map;
import com.dino.engine.Engine;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Polygon;

public class Dragon extends com.dino.gameObjects.GameObject {
    private static final int SPRITE_WIDTH   = 88;
    private static final int SPRITE_HEIGHT  =  94;
    private static final double GRAVITY     = 5000;
    private static final double SPEED       = 500;
    private int score = 0;
    private int highScore = 0;

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int i) {
        highScore = i;
    }


    public enum states{
        standing, running, dead
    }
    
    Image sprite;
    Map<states, int[]> maps;
 
    public double jumpvector = 0;

    public static states currentState;
    int spriteIndex;
    
    double elapsedTimeRunning;

    public Dragon(Engine engine){
        super(engine, 0, Engine.GAME_HEIGHT-SPRITE_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
        
        sprite = new Image(getClass().getResourceAsStream("/res/trex.png"));

        maps = new HashMap<>();
        maps.put(states.running, new int[] {2,3});//how often legs move
        maps.put(states.standing, new int[] {0});
        maps.put(states.dead, new int[]{4});

        spriteIndex=0;
        currentState = states.running;
        elapsedTimeRunning = 0.0;
    }
    public Dragon(Engine engine,int highScore){
        this(engine);
        this.highScore = highScore;
    }



    @Override
    public void update(double delta) {
        elapsedTimeRunning+=delta;
        
        if(engine.isKeyPressed(KeyCode.LEFT)){ //dragon move to left
            x -= SPEED *delta;
            currentState = states.running;
        }
        if(engine.isKeyPressed(KeyCode.RIGHT)){ //dragon move to right
            x += SPEED *delta;
            currentState = states.running;
        }
        if(engine.isKeyPressed(KeyCode.SPACE)){ //dragon jump
            if(jumpvector == 0){
                jumpvector = 1300;
            }
        }
        
        if(jumpvector!=0){ //code contains jump logic
            
            double move = jumpvector*delta- (GRAVITY * (delta*delta))/2;
            jumpvector -=GRAVITY*delta;
            y-=move;
            if(y > Engine.GAME_HEIGHT-h){
                jumpvector = 0;
            }
        }

        
        checkEdges();
    }

    private void checkEdges(){
        if(x<0)
            x = 0;
        if(x > Engine.GAME_WIDTH- w) //code responsible for horizontal layout
            x = Engine.GAME_WIDTH - w;
        if(y >Engine.GAME_HEIGHT-h)// code responsible for vertical layout
            y = Engine.GAME_HEIGHT-h;
    }

//    @Override
    public void render(GraphicsContext g) { //i dont know
        if(elapsedTimeRunning >= 0.1){
            if(spriteIndex++ >= maps.get(currentState).length-1){
                spriteIndex = 0;
                score++;
            }
            elapsedTimeRunning = 0;
        }
        g.drawImage(sprite, maps.get(currentState)[spriteIndex]*SPRITE_WIDTH, 0, SPRITE_WIDTH, SPRITE_HEIGHT, x, y, w, h);
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    public Polygon getPolygon(){ //cordinates of objects in polygon
        double[] xpoints = {x, x+0.5*w, x+w, x+w, x+0.55*w, x+0.35*w};
        double[] ypoints = {y+0.5*h, y, y, y+0.3*h, y+h, y+h};

        Polygon poly = new Polygon();
        poly.getPoints().addAll(new Double[]{xpoints[0],ypoints[0],
        xpoints[1],ypoints[1],
        xpoints[2],ypoints[2],
        xpoints[3],ypoints[3],
        xpoints[4],ypoints[4],
        xpoints[5],ypoints[5]});

        return poly;
    }
    
    public void setState(Dragon.states state) {
        currentState = state;
        spriteIndex = 0;
    }
    public int getScore(){
        return score;
    }
}
