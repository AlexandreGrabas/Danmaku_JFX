package com.lloyd27.danmaku.entity.Bullet;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class NeedleBullet extends AbstractBullet{

    public NeedleBullet(double x, double y, double vx, double vy) {
        super(x, y, vx, vy, 15); // 15 de dégâts
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillOval(x - 4, y - 4, 2, 30);
    }
}
