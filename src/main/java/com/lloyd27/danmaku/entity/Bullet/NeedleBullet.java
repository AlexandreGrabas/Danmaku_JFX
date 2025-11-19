package com.lloyd27.danmaku.entity.Bullet;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class NeedleBullet extends AbstractBullet{
    private Image sprite=new Image("/sprites/trait_de_feu.png");

    public NeedleBullet(double x, double y, double vx, double vy,String ownerType) {
        super(x, y, vx, vy, 10); // 15 de dégâts
        this.ownerType = ownerType;
        this.height=30;
        this.width=4;
    }

    @Override
    public void render(GraphicsContext gc) {
        
        gc.setFill(Color.WHITE);
        gc.fillOval(x - width/2, y - height/2, width, height);

        gc.drawImage(sprite,x - width/2, y - height/2,width,height);
    }
}
