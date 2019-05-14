package com.dino.engine;

import com.dino.gameObjects.Dragon;
import com.dino.gameObjects.GameObject;
import com.dino.gameObjects.Horizon;

import java.util.HashSet;
import java.util.Set;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Shape;


public class  Engine extends StackPane {
    public static final int GAME_WIDTH      = 1000;
    public static final int GAME_HEIGHT     = 300;
    private static final int INITIAL_SPEED   = 300;

    private ListProperty<GameObject> ForegroundObjects = null;
    private ListProperty<GameObject> BackgroundObjects = null;

    Label currentScore = new Label("score:");
    Label currentCounter = new Label("0 ");
    Label highScore = new Label("High score: ");
    Label highCounter = new Label("0");
    private Dragon player = null;

    private final Canvas canvas;
    private final GraphicsContext g;
    private final ImageView btnRestart;
    private final ImageView btnStart;
    private final ImageView btnExit;

    private Set<KeyCode> keyPool = null;
    private final AnimationTimer timer;
    private GenerateCloundsTask generateClouds = null;
    private GenerateObstaclesTask generateObstacles = null;

    private double speed;
    private double elapsedTime;
    ;

    private long prevTime;

    public Engine() {
        canvas = new Canvas(GAME_WIDTH, GAME_HEIGHT);

        canvas.setOnKeyPressed((KeyEvent event) -> {
            keyPool.add(event.getCode());
        });

        canvas.setOnKeyReleased((KeyEvent event) -> {
            keyPool.remove(event.getCode());
        });

        g = this.canvas.getGraphicsContext2D();

        btnRestart = new ImageView(new Image(getClass().getResourceAsStream("/res/gameOver.png"))); //game over code

        btnStart = new ImageView(new Image(getClass().getResourceAsStream("/res/start.png"))); //start button
        setAlignment(btnStart, Pos.CENTER);
        btnExit = new ImageView(new Image(getClass().getResourceAsStream("/res/exit.png"))); // exit button
        setAlignment(btnExit, Pos.CENTER);

        setMargin(btnExit,new Insets(150,0,0,0));
        btnRestart.setVisible(false);
        btnStart.setOnMouseClicked(e -> {
            start();
            btnStart.setVisible(false);
            btnExit.setVisible(false);
        });

        btnExit.setOnMouseClicked(e -> {
            System.exit(0);
            btnExit.setVisible(false);
        });

        canvas.setOnMouseClicked((event) -> {
            if (btnRestart.isVisible()) {
                reinitHighScore();
                this.init();
                this.start();
                btnRestart.setVisible(false);
            }
        });
        keyPool = new HashSet<>();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                mainloop((now - prevTime) / 1000000000.0); // animation speed
                prevTime = now;
                currentCounter.setText(String.valueOf(player.getScore()));
                if (player.getScore() > player.getHighScore())
                    player.setHighScore(player.getScore());

            }
        };
    }



    private void reinitHighScore() {
        changeHighScoreVisibleState(true);
        highCounter.setText(String.valueOf(player.getHighScore()));


    }
    public void init(){
        if(!this.getChildren().contains(canvas)){
            this.getChildren().add(canvas);
            this.getChildren().addAll(currentScore,currentCounter,highCounter,highScore);

        }

        if(!this.getChildren().contains(btnRestart)){                    
            this.getChildren().add(btnRestart);
        }

        if(!this.getChildren().contains(btnStart)){
            this.getChildren().add(btnStart);
        }
        if(!this.getChildren().contains(btnExit)){
            this.getChildren().add(btnExit);
        }
        canvas.requestFocus();
        elapsedTime = 0;
        if(player != null)
            player = new Dragon(this,player.getHighScore());
        else
            player = new Dragon(this);
        generateClouds = new GenerateCloundsTask(this);
        generateObstacles = new GenerateObstaclesTask(this);

        BackgroundObjects = new SimpleListProperty<>();
        ForegroundObjects = new SimpleListProperty<>();

        BackgroundObjects.bindBidirectional(generateClouds.getPartialResultProperty());
        ForegroundObjects.bindBidirectional(generateObstacles.getPartialResultProperty());
    }

    private void initScoreBoard() {
        if(player.getHighScore()<= 0)
            changeHighScoreVisibleState(false);
        setMargin(currentScore,new Insets(15,45,15,15));
        setMargin(highScore,new Insets(30,40,15,15));
        setMargin(currentCounter,new Insets(15,20,15,15));
        setMargin(highCounter,new Insets(30,20,15,15));
        setAlignment(currentScore,Pos.TOP_RIGHT);
        setAlignment(highScore,Pos.TOP_RIGHT);
        setAlignment(currentCounter,Pos.TOP_RIGHT);
        setAlignment(highCounter,Pos.TOP_RIGHT);
    }

    private void changeHighScoreVisibleState(boolean b) {
        highScore.setVisible(b);
        highCounter.setVisible(b);
    }

    public void mainloop(double delta) {

            if (elapsedTime > 10) { // speed
                speed *= 1.2;
                elapsedTime = 0;
            }

            g.clearRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

            BackgroundObjects.forEach((obj) -> { //?
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
//    }
    private void collisionDetection() { // dont know
            for (GameObject obj : ForegroundObjects) {
                Shape colisionShape = Shape.intersect(player.getPolygon(), obj.getPolygon());
                if (colisionShape.getBoundsInLocal().getWidth() != -1) {
                    player.setState(Dragon.states.dead);
                    player.render(g);
                    stop();
                    btnRestart.setVisible(true);
                }
            }
//        }
    }
    public void start(){
        speed = INITIAL_SPEED;
        prevTime = System.nanoTime();
        initScoreBoard();

        BackgroundObjects.add(new Horizon(this));
        Thread backgroundGenCloudsTread = new Thread(generateClouds); //start to generate clouds
        backgroundGenCloudsTread.setDaemon(true);
        
        Thread backgroundGenObstaclesThread = new Thread(generateObstacles); //start to generate obstacles
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
