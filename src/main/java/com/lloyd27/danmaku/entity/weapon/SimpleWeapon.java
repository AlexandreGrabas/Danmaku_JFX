package com.lloyd27.danmaku.entity.weapon;

import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.Bullet.SimpleBullet;

import java.util.ArrayList;

public class SimpleWeapon extends AbstractWeapon {
    protected double fireRate = 0.01; // secondes entre tirs
    protected double decalageX;
    protected double decalageY;

    public SimpleWeapon(){
    }

    public SimpleWeapon(double decalageX, double decalageY, double fireRate){
        this.decalageX=decalageX;
        this.decalageY=decalageY;        
        this.fireRate=fireRate;
    }

    @Override
    public List<AbstractBullet> shoot(double x, double y) {
        if (!canShoot()) return List.of();
        resetCooldown();

        List<AbstractBullet> bullets = new ArrayList<>();
        bullets.add(new SimpleBullet(x+decalageX, y+decalageY - 10, 0, -1000,"player")); // tire vers le haut
        return bullets;
    }

        protected void resetCooldown() {
        cooldown = fireRate;
    }
}
