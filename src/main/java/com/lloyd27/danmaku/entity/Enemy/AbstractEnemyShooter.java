package com.lloyd27.danmaku.entity.Enemy;

import java.util.List;

import com.lloyd27.danmaku.entity.Entity;
import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;


import javafx.scene.canvas.GraphicsContext;

public abstract class AbstractEnemyShooter extends Entity{
    private boolean canShoot=true;
    private double score=100;

    public AbstractEnemyShooter(double x,double y){
        super(x, y);
    }

    public boolean getCanShoot(){
        return(this.canShoot);
    }

    public void setCanShoot(boolean canShoot){
        this.canShoot=canShoot;
    }

    public double getScore(){
        return(this.score);
    }

    public void setScore(double score){
        this.score=score;
    }

    public abstract List<AbstractBullet> shoot();
    public abstract List<AbstractBullet> shootWired(double playerX, double playerY);

    @Override
    public abstract void update(double deltaTime);
    
    @Override
    public abstract void render(GraphicsContext gc);
    
    public boolean isOffScreen() {
        return y < -100 || y > 1000 || x < -100 || x > 900;
    }

}
