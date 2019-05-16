package com.dino.engine;

import com.dino.gameObjects.CactusBuilder;
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

public class GenerateObstaclesTask extends Task<ObservableList<GameObject>>{
    final Engine engine;
    private final ListProperty<GameObject> partialResult = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
    public Property<ObservableList<GameObject>> getPartialResultProperty(){ return partialResult;}

    public GenerateObstaclesTask(Engine engine){
        this.engine = engine;
    }
            
    @Override
    protected ObservableList<GameObject> call() throws Exception {
        Random rand = new Random();
        while(true){
            if(isCancelled())break;

            try {
                Thread.sleep((long)(2000/(engine.getSpeed()/300) + rand.nextDouble()*(1000/(engine.getSpeed()/300))));
            } catch (InterruptedException ex) {
                if(isCancelled()){
                    break;
                }
            }

            Platform.runLater(() -> {
                partialResult.get().add(
                    new CactusBuilder(engine)
                    .setSize(rand.nextBoolean()? 
                            CactusBuilder.Cactussize.SMALL:
                            CactusBuilder.Cactussize.LARGE).build());
            });
        }
        return partialResult.get();
    }
}
