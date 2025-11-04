package com.lloyd27.danmaku.entity;

import javafx.scene.canvas.GraphicsContext;

public abstract class Entity {
    protected double x, y;

    public Entity(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public abstract void update(double deltaTime);
    public abstract void render(GraphicsContext gc);
}
