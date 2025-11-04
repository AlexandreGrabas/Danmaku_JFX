package com.lloyd27.danmaku.entity.Bullet;

import com.lloyd27.danmaku.entity.Entity;

import javafx.scene.canvas.GraphicsContext;

public abstract class AbstractBullet extends Entity {
    protected double vx, vy;
    protected double damage;

    public AbstractBullet(double x, double y, double vx, double vy, double damage) {
        super(x, y);
        this.vx = vx;
        this.vy = vy;
        this.damage = damage;
    }

    @Override
    public void update(double deltaTime) {
        x += vx * deltaTime;
        y += vy * deltaTime;
    }

    @Override
    public abstract void render(GraphicsContext gc);

    public boolean isOffScreen() {
        return y < -100 || y > 1000 || x < -10 || x > 810;
    }

    public double getDamage() {
        return damage;
    }
}