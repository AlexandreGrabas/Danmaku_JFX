package com.lloyd27.danmaku.entity.Bullet;

import com.lloyd27.danmaku.managers.SoundManager;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class BombBullet extends AbstractBullet{
    private Image sprite=new Image("/sprites/étoilev2.png");
    private double cpt=0;
    private SoundManager soundManager=new SoundManager();
    private double cptSound=0;

    public BombBullet(double x, double y, double vx, double vy,String ownerType) {
        super(x, y, vx, vy, 5); // 5 de dégâts
        this.ownerType = ownerType;
        this.height=800;
        this.width=800;
        this.life=999999999;
    }

    public void playSound(){
        if(cptSound<=0){
            cptSound=50;
            soundManager.playSound("fireRotating.wav", 0.15);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.save();
        gc.translate(x, y);
        gc.rotate(cpt);
        cpt+=4;
        cptSound-=1;
        playSound();
        gc.drawImage(sprite, -width / 2, -height / 2, width, height); 
        gc.restore();   
    }
}
