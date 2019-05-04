package com.dino.gameObjects;

import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author Dimitrije Muzur
 */
public interface GameObjectInterface {
    public void update(double delta);
    public void render(GraphicsContext g);
}
