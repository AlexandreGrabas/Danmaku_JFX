package com.lloyd27.danmaku.entity.Bullet;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class BulletLineLeft extends AbstractBullet {
    // private Image sprite=new Image("/sprites/Fire_Effect.png");
    private Image sprite=new Image("/sprites/pitit_feu.png");
    private boolean alternance = true;
    private double cpt=0;

    public double getCpt(){ 
        return cpt; 
    }

    public void setCpt(double cpt){ 
        this.cpt = cpt; 
    }

    public boolean isAlternance(){
        return alternance; 
    }
    public void setAlternance(boolean a){
        this.alternance = a; 
    }
        
    public BulletLineLeft(double x, double y, double vx, double vy,String ownerType) {
        super(x, y, vx, vy, 10); // 10 de dégâts
        this.ownerType = ownerType;
        this.height=30;
        this.width=30;
    }

    @Override
    public void render(GraphicsContext gc) {

        gc.setFill(ownerType.equals("player") ? Color.DARKRED : Color.DARKRED);
        gc.fillOval(x - width/2, y - height/2, width, height);
        gc.drawImage(sprite, x - width / 2, y - height / 2, width, height);
    }
}
