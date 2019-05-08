package com.dino.gameObjects;

import com.dino.engine.Engine;
import javafx.geometry.BoundingBox;
import javafx.scene.shape.Polygon;

/**
 *
 * @author Dimitrije Muzur
 */
public abstract class GameObject implements com.dino.gameObjects.GameObjectInterface {
    protected double x;
    protected double y;
    protected double w;
    protected double h;
    protected Engine engine;
    
    public GameObject(Engine engine, double x, double y, double width, double height){
        this.engine = engine;
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
    }
    
    public BoundingBox bounds(){
        return new BoundingBox(x, y, w, h);
    }
   
    public boolean isVisible(){
        return true;
    }
    
    public Polygon getPolygon(){
        return null;
    }    
}
