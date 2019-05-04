
package com.dino.gameObjects;

import com.dino.engine.Engine;
import com.dino.gameObjects.Cactus;

/**
 *
 * @author Dimitrije Muzur
 */
public class CactusBuilder {
    public static enum Cactussize{ LARGE, SMALL}
    
    private Cactus cactus;
    
    public CactusBuilder(Engine engine){
        cactus = new Cactus(engine);
    }
    
    public CactusBuilder setSize(Cactussize size){
        cactus.setSize(size);
        return this;
    }
    
    public Cactus build(){
        return cactus;
    }
}
