package com.dino.gameObjects;

import com.dino.engine.Engine;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Horizon extends GameObject{
    Image image;

    public Horizon(Engine engine) {
        super(engine, 0, Engine.GAME_HEIGHT, Engine.GAME_WIDTH, Engine.GAME_HEIGHT);
        image = new Image(getClass().getResourceAsStream("/res/horizon.png"));
        
    }

    @Override
    public void update(double delta) {
        x+=engine.getSpeed() * delta;
        if(x>= image.getWidth())
            x = 0;
    }

    @Override
    public void render(GraphicsContext g) {
        if(x + Engine.GAME_WIDTH < image.getWidth()){
            g.drawImage(image, x, 0, Engine.GAME_WIDTH, image.getHeight(), 0, Engine.GAME_HEIGHT - image.getHeight(), Engine.GAME_WIDTH, image.getHeight());
        } else {
            g.drawImage(image, x, 0, image.getWidth() - x, image.getHeight(), 0, Engine.GAME_HEIGHT - image.getHeight(), image.getWidth() - x, image.getHeight());
            g.drawImage(image, 0, 0, Engine.GAME_WIDTH-(image.getWidth() - x), image.getHeight(), image.getWidth() - x, Engine.GAME_HEIGHT - image.getHeight(), Engine.GAME_WIDTH-(image.getWidth() - x), image.getHeight());
        }
    }
}
