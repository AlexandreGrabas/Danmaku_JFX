package com.lloyd27.danmaku.entity.Bullet;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SimpleBullet extends AbstractBullet {

    public SimpleBullet(double x, double y, double vx, double vy,String ownerType) {
        super(x, y, vx, vy, 10); // 10 de dégâts
        this.ownerType = ownerType;
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(ownerType.equals("player") ? Color.DARKBLUE : Color.DARKRED);
        gc.fillOval(x - 4, y - 4, 8, 8);
    }
}
