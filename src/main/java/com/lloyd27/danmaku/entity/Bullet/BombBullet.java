package com.lloyd27.danmaku.entity.Bullet;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BombBullet extends AbstractBullet{

    public BombBullet(double x, double y, double vx, double vy,String ownerType) {
        super(x, y, vx, vy, 500); // 500 de dégâts
        this.ownerType = ownerType;
        this.height=800;
        this.width=800;
        this.life=999999999;
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(ownerType.equals("player") ? Color.DARKGOLDENROD : Color.DARKRED);
        gc.fillOval(x - width/2, y - height/2, width, height);
    }
}
