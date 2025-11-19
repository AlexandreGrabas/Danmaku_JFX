package com.lloyd27.danmaku.entity.Bullet;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class KunaiLeftBullet extends AbstractBullet {
    private Image sprite=new Image("/sprites/trait_de_feu_gauche.png");

    public KunaiLeftBullet(double x, double y, double vx, double vy,String ownerType) {
        super(x, y, vx, vy, 10); // 10 de dégâts
        this.ownerType = ownerType;
        this.height=8;
        this.width=30;
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(ownerType.equals("player") ? Color.DARKBLUE : Color.DARKORANGE);
        gc.fillOval(x - width/2, y - height/2, width, height);
        gc.drawImage(sprite,x - width/2, y - height/2,width,height);
    }
}
