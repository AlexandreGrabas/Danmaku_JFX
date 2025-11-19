package com.lloyd27.danmaku.entity.Bullet;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class WiredBulletPlayer extends AbstractBullet {
    // private Image sprite=new Image("/sprites/Fire_Effect.png");
    private Image sprite=new Image("/sprites/autoWiredPlayer.png");

    public WiredBulletPlayer(double x, double y, double vx, double vy,String ownerType) {
        super(x, y, vx, vy, 2); // 2 de dégâts
        this.ownerType = ownerType;
        this.height=12;
        this.width=12;
    }

    @Override
    public void render(GraphicsContext gc) {

        gc.setFill(ownerType.equals("player") ? Color.DARKRED : Color.DARKRED);
        gc.fillOval(x - width/2, y - height/2, width, height);
        gc.drawImage(sprite, x - width / 2, y - height / 2, width, height);
    }
}
