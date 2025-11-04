package com.lloyd27.danmaku.entity;

import java.util.ArrayList;
import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.weapon.NeedleWeapon;
import com.lloyd27.danmaku.entity.weapon.SimpleWeapon;
import com.lloyd27.danmaku.entity.weapon.AbstractWeapon;

import javafx.scene.paint.Color;

public class Player extends Entity {
    private double speed = 300;
    private boolean up, down, left, right;
    private double shootCooldown = 0;
    private List<AbstractWeapon> weapons = new ArrayList<>();
    private boolean slowMode =false;


    public Player(double x, double y) {
        super(x, y);
        weapons.add(new SimpleWeapon());
        weapons.add(new NeedleWeapon());
    }

    public List<AbstractBullet> shoot() {
        List<AbstractBullet> allBullets = new ArrayList<>();
        for (AbstractWeapon w : weapons) {
            w.setSlowMode(slowMode);
            allBullets.addAll(w.shoot(x, y));
        }
        return allBullets;
    }

    public void slowPlayer(boolean slow) {
        slowMode = slow ? true : false;
    }

    public void addWeapon(AbstractWeapon weapon) {
        weapons.add(weapon);
    }

    public void setDirection(boolean up, boolean down, boolean left, boolean right) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }

    @Override
    public void update(double deltaTime) {

        double actualSpeed = speed * (slowMode ? 0.5 : 1.0);

        double dx = 0, dy = 0;
        if (up) {dy -= actualSpeed * deltaTime;}
        if (down) {dy += actualSpeed * deltaTime;}
        if (left) {dx -= actualSpeed * deltaTime;}
        if (right) {dx += actualSpeed * deltaTime;}
        if(x+dx<800 && x+dx>0){x += dx;}
        if(y+dy<900 && y+dy>0){y += dy;}
        

        for (AbstractWeapon w : weapons) {
            w.update(deltaTime);
        }
    }

    @Override
    public void render(javafx.scene.canvas.GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(x - 10, y - 10, 20, 20);

        //hitbox
        gc.setFill(Color.DARKRED);
        gc.fillOval(x - 4, y - 4, 8, 8);
    }
}
