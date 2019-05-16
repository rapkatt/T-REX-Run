package com.dino.gameObjects;

import java.util.Random;
import com.dino.engine.Engine;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Polygon;

public class Cactus extends GameObject{
    com.dino.gameObjects.CactusBuilder.Cactussize size;
    
    
    
    Image img;
    int spriteNum=0;

    public Cactus(Engine engine) {
        super(engine, Engine.GAME_WIDTH,0,0,0);
    }
    
    public void setSize(com.dino.gameObjects.CactusBuilder.Cactussize size){
        this.size = size;
        
        if(com.dino.gameObjects.CactusBuilder.Cactussize.LARGE.equals(size)){
            img = new Image(getClass().getResourceAsStream("/res/obstacle-large.png"));
            w = 100;
            h = 100;
            spriteNum = (int) (new Random().nextDouble()*3); // generate a large cactus
        }
        if(com.dino.gameObjects.CactusBuilder.Cactussize.SMALL.equals(size)){
            img = new Image(getClass().getResourceAsStream("/res/obstacle-small.png"));
            w = 34;
            h = 70;
            spriteNum = (int) (new Random().nextDouble()*6); // generate a small cactus
        }
        y=Engine.GAME_HEIGHT-h;
    }

    @Override
    public void update(double delta) {

        x-= engine.getSpeed() *delta;
    }

    @Override
    public void render(GraphicsContext g) {

        g.drawImage(img, spriteNum*w, 0, w, h, x, y, w, h);
    }

    @Override
    public boolean isVisible() {

        return x >= 0-w;
    }
    
    @Override
    public Polygon getPolygon(){
        double[] xpoints = null;
        double[] ypoints = null;
        
        if(size.equals(com.dino.gameObjects.CactusBuilder.Cactussize.SMALL)){ //in my view this code about coordinate a small cactus
            switch(spriteNum){
                case 0: case 1:
                    xpoints = new double[]{x+0.5*w,x,x, x+0.3*w, x+0.3*w, x+0.63*w, x+0.63*w, x+w, x+w};
                    ypoints = new double[]{y+h,y+0.6*h,y+0.27*h, y + 0.27*h, y, y, y+0.15*h, y+0.15*h, y+0.5*h};
                    break;
                case 2:case 5:
                    xpoints = new double[]{x+0.5*w,x,x, x+0.3*w, x+0.3*w, x+0.63*w, x+0.63*w, x+w, x+w};
                    ypoints = new double[]{y+h,y+0.6*h,y+0.15*h, y + 0.15*h, y, y, y+0.15*h, y+0.15*h, y+0.5*h};
                    break;
                case 3:
                    xpoints = new double[]{x+0.5*w,x,x, x+0.3*w, x+0.3*w, x+0.63*w, x+0.63*w, x+w, x+w};
                    ypoints = new double[]{y+h,y+0.6*h,y+0.25*h, y + 0.25*h, y, y, y+0.15*h, y+0.15*h, y+0.5*h};
                    break;
                case 4:
                    xpoints = new double[]{x+0.5*w,x,x, x+0.3*w, x+0.3*w, x+0.63*w, x+0.63*w, x+w, x+w};
                    ypoints = new double[]{y+h,y+0.6*h,y+0.13*h, y + 0.13*h, y, y, y+0.25*h, y+0.25*h, y+0.5*h};
                    break;
            }
        }
        
        if(size.equals(com.dino.gameObjects.CactusBuilder.Cactussize.LARGE)){ //in my view this code about coordinate a large cactus
            switch(spriteNum){
                case 0:
                    xpoints = new double[]{x+0.5*w,x,x, x+0.17*w, x+0.17*w, x+0.82*w, x+0.82*w, x+w, x+w};
                    ypoints = new double[]{y+h,y+0.6*h,y+0.27*h, y + 0.27*h, y, y, y+0.23*h, y+0.23*h, y+0.5*h};
                    break;
                case 1:
                    xpoints = new double[]{x+0.5*w,x,x, x+0.17*w, x+0.17*w, x+0.82*w, x+0.82*w, x+w, x+w};
                    ypoints = new double[]{y+h,y+0.6*h,y+0.10*h, y + 0.10*h, y, y, y+0.23*h, y+0.23*h, y+0.5*h};
                    break;
                case 2:
                    xpoints = new double[]{x+0.5*w,x,x, x+0.12*w, x+0.12*w, x+0.82*w, x+0.82*w, x+w, x+w};
                    ypoints = new double[]{y+h,y+0.6*h,y+0.35*h, y + 0.35*h, y, y, y+0.23*h, y+0.23*h, y+0.5*h};
                    break;
            }
        }
        
        Polygon poly = new Polygon();
        for(int i = 0; i < xpoints.length; i++){
            poly.getPoints().add(xpoints[i]);
            poly.getPoints().add(ypoints[i]);
        }
        
        return poly;
    }
}
