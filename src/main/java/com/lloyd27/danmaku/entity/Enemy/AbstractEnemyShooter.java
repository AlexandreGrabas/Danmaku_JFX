package com.lloyd27.danmaku.entity.Enemy;

import java.util.List;

import com.lloyd27.danmaku.entity.Entity;
import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;


import javafx.scene.canvas.GraphicsContext;

public abstract class AbstractEnemyShooter extends Entity{

    public AbstractEnemyShooter(double x,double y){
        super(x, y);
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
