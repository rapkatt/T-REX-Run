package com.dino.gameObjects;

import java.util.Random;
import com.dino.engine.Engine;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 *
 * @author Dimitrije Muzur
 */
public class Cloud extends GameObject{
    Image image;
    
    double speed = 50;

    public Cloud(Engine engine) {
        super(engine, Engine.GAME_WIDTH, new Random().nextDouble() * (Engine.GAME_HEIGHT - 24 - 28),0,0);
        image = new Image(getClass().getResourceAsStream("/res/cloud.png"));
        w = image.getWidth();
        h = image.getHeight();
    }
    
    public void setX(double x){
        this.x = x;
    }

    @Override
    public void update(double delta) {
        x-=speed * delta;
    }

    @Override
    public void render(GraphicsContext g) {
        g.drawImage(image, 0, 0, w, h, x, y, w, h);
    }

    @Override
    public boolean isVisible() {
        return x+image.getWidth() > 0;
    }
}
