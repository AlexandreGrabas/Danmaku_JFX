package com.lloyd27.danmaku.entity;

import javafx.scene.canvas.GraphicsContext;

public abstract class Entity {
    protected double x, y;
    protected Boolean alive=true;
    protected double life;
    protected double width, height;

    public Entity(double x, double y) {
        this.x = x;
        this.y = y;
        this.width =3;
        this.height=3; 
    }

    public boolean isAlive() {
        return alive;
    }

    public abstract void update(double deltaTime);
    public abstract void render(GraphicsContext gc);

    public void takeDamage(double amount) {
        life -= amount;
        if (life <= 0) {
            alive = false;
        }
    }

    public boolean intersects(Entity other) {
        return (x < other.x + other.width &&
                x + width > other.x &&
                y < other.y + other.height &&
                y + height > other.y);
    }
}
