/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dino.engine;

import com.dino.gameObjects.Cloud;
import com.dino.gameObjects.GameObject;
import java.util.ArrayList;
import java.util.Random;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class GenerateCloundsTask extends Task<ObservableList<GameObject>> {
    Engine engine;
    
    private final ListProperty<GameObject> partialResult = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
    public Property<ObservableList<GameObject>> getPartialResultProperty(){ return partialResult;} //???

    public GenerateCloundsTask(Engine engine) {
        this.engine = engine;

    }
    
    @Override
    protected ObservableList<GameObject> call() throws Exception {
        Random rand = new Random();
        
        for(int i = 0; i < 5; i++){  //how many will generate
            Cloud seedCloud = new Cloud(engine);
            seedCloud.setX(rand.nextDouble() * Engine.GAME_WIDTH);
            
            partialResult.get().add(seedCloud);
        }
        
                
        while(true){
            if(isCancelled()) break;
            try{
                Thread.sleep((long) (2000 + rand.nextDouble()*100)); // distance between
            }catch (InterruptedException exc){
                if(isCancelled()){
                    break;
                }
            }

            Platform.runLater(() -> {
                partialResult.get().add(new Cloud(engine));
            });
        }
        return partialResult.get();
    }
}
