package com.dino.engine;

import com.dino.gameObjects.Dragon;
import com.dino.gameObjects.Horizon;
import com.dino.gameObjects.GameObject;
import java.util.HashSet;
import java.util.Set;
import javafx.animation.AnimationTimer;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Shape;


public class Engine extends StackPane{
    public static final int GAME_WIDTH      = 1000;
    public static final int GAME_HEIGHT     = 300;
    public static final int INITIAL_SPEED   = 300;
    
    private ListProperty<GameObject> ForegroundObjects = null;
    private ListProperty<GameObject> BackgroundObjects = null;

    private Dragon player = null;
    
    private final Canvas canvas;
    private final GraphicsContext g;
    private final ImageView btnRestart;
    
    private Set<KeyCode> keyPool = null;
    
    private final AnimationTimer timer;
    private GenerateCloundsTask generateClouds = null;
    private GenerateObstaclesTask generateObstacles = null;
    
    private double speed;
    private double elapsedTime;

    private long prevTime;
    
    public Engine(){
        this.setAlignment(Pos.CENTER);
        canvas = new Canvas(GAME_WIDTH, GAME_HEIGHT);
        
        canvas.setOnKeyPressed((KeyEvent event) -> {
            keyPool.add(event.getCode());
        });
        
        canvas.setOnKeyReleased((KeyEvent event) -> {
            keyPool.remove(event.getCode());
        });
        
        g = this.canvas.getGraphicsContext2D();
        
        btnRestart = new ImageView(new Image(getClass().getResourceAsStream("/res/restart.png")));
        btnRestart.setVisible(false);
        btnRestart.setOnMouseClicked((event) -> {
            this.init();
            this.start();
            btnRestart.setVisible(false);
        });
        
        keyPool = new HashSet<>();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                mainloop((now - prevTime) / 1000000000.0);
                prevTime = now;
            }
        };
    }
    
    public void init(){
        if(!this.getChildren().contains(canvas)){
            this.getChildren().add(canvas);
        }
        
        if(!this.getChildren().contains(btnRestart)){                    
            this.getChildren().add(btnRestart);
        }
        
        canvas.requestFocus();
        
        elapsedTime = 0;
        player = new Dragon(this);

        generateClouds = new GenerateCloundsTask(this);
        generateObstacles = new GenerateObstaclesTask(this);
        
        BackgroundObjects = new SimpleListProperty<>();
        ForegroundObjects = new SimpleListProperty<>();
        
        BackgroundObjects.bindBidirectional(generateClouds.getPartialResultProperty());
        ForegroundObjects.bindBidirectional(generateObstacles.getPartialResultProperty());
    }

    public void mainloop(double delta){
        elapsedTime +=delta;
        
        if(elapsedTime > 10){
            speed *=1.2;
            elapsedTime=0;
        }
        
        g.clearRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        
        BackgroundObjects.forEach((obj) -> {
            obj.update(delta);
        });
        BackgroundObjects.removeIf((GameObject t) -> !t.isVisible());
        
        ForegroundObjects.forEach((t) -> {
            t.update(delta);
        });
        ForegroundObjects.removeIf((GameObject t) -> !t.isVisible());
        
        player.update(delta);

        BackgroundObjects.forEach((obj) -> {
            obj.render(g);
        });
        ForegroundObjects.forEach((t) -> {
            t.render(g);
        });
        
        player.render(g);
        
        collisionDetection();
    }
    
    private void collisionDetection(){
        for(GameObject obj: ForegroundObjects){
            Shape colisionShape = Shape.intersect(player.getPolygon(), obj.getPolygon());
            if(colisionShape.getBoundsInLocal().getWidth() != -1){
                player.setState(Dragon.states.dead);
                player.render(g);
                
                stop();
                btnRestart.setVisible(true);
            }
        }
    }

    public void start(){
        speed = INITIAL_SPEED;
        prevTime = System.nanoTime();
        
        BackgroundObjects.add(new Horizon(this));
        
        Thread backgroundGenCloudsTread = new Thread(generateClouds);
        backgroundGenCloudsTread.setDaemon(true);
        
        Thread backgroundGenObstaclesThread = new Thread(generateObstacles);
        backgroundGenObstaclesThread.setDaemon(true);
        
        backgroundGenCloudsTread.start();
        backgroundGenObstaclesThread.start();
        this.timer.start();
    }
    
    public void stop(){
        generateClouds.cancel();
        generateObstacles.cancel();
        timer.stop();
    }
    
    public boolean isKeyPressed(KeyCode key){
        return keyPool.contains(key);
    }
    
    public double getSpeed(){
        return speed;
    }
}
