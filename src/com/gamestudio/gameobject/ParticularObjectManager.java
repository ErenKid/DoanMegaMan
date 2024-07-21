package com.gamestudio.gameobject;

import com.gamestudio.state.GameWorldState;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ParticularObjectManager {

    protected List<ParticularObject> particularObjects;
    private int score; 
    private GameWorldState gameWorld;
    private int points;
    
    
    public int getPoints() {
    return points;
}

    public ParticularObjectManager(GameWorldState gameWorld){
        particularObjects = Collections.synchronizedList(new LinkedList<ParticularObject>());
        this.gameWorld = gameWorld;
        this.score = 0;
    }
    
    public GameWorldState getGameWorld(){
        return gameWorld;
    }
    
    public void addObject(ParticularObject particularObject){
        synchronized(particularObjects){
            particularObjects.add(particularObject);
        }
    }
    
    public void RemoveObject(ParticularObject particularObject){
        synchronized(particularObjects){
            particularObjects.remove(particularObject);
        }
    }
    
    public ParticularObject getCollisionWidthEnemyObject(ParticularObject object){
        synchronized(particularObjects){
            for (ParticularObject objectInList : particularObjects) {
                if(object.getTeamType() != objectInList.getTeamType() && 
                        object.getBoundForCollisionWithEnemy().intersects(objectInList.getBoundForCollisionWithEnemy())) {
                    return objectInList;
                }
            }
        }
        return null;
    }
    
    public void UpdateObjects(){
        synchronized(particularObjects){
            Iterator<ParticularObject> iterator = particularObjects.iterator();
            while (iterator.hasNext()) {
                ParticularObject object = iterator.next();
                if(!object.isObjectOutOfCameraView()) object.Update();
                if(object.getState() == ParticularObject.DEATH){
                    iterator.remove();
                }
            }
        }
    }
    
    public void draw(Graphics2D g2){
        synchronized(particularObjects){
            for (ParticularObject object : particularObjects) {
                if(!object.isObjectOutOfCameraView()) {
                    object.draw(g2);
                }
            }
        }
    }

    public int checkCollisionWithParticularObject(Rectangle rect) {
        int points = 0;
        synchronized(particularObjects){
            Iterator<ParticularObject> iterator = particularObjects.iterator();
            while (iterator.hasNext()) {
                ParticularObject object = iterator.next();
                if (object.getBoundForCollisionWithEnemy().intersects(rect)) {
                    points += object.getPoints();
                    score += object.getPoints(); // Cập nhật điểm số
                    iterator.remove();
                    break; // Nếu chỉ muốn lấy điểm của va chạm đầu tiên, thêm break vào đây
                }
            }
        }
        return points;
    }

    public void draw(Graphics2D g2, Camera camera) {
        synchronized(particularObjects){
            for (ParticularObject object : particularObjects) {
                if(!object.isObjectOutOfCameraView()) {
                    object.draw(g2);
                }
            }
        }
    }
}
