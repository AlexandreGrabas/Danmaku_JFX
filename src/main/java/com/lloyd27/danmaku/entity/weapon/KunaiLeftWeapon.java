package com.lloyd27.danmaku.entity.weapon;

import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.Bullet.KunaiBullet;
import com.lloyd27.danmaku.entity.Bullet.KunaiLeftBullet;

import java.util.ArrayList;

public class KunaiLeftWeapon extends AbstractWeapon {
    protected double fireRate = 0.01; // secondes entre tirs

    public KunaiLeftWeapon(){
        this.fireRate=0.5;
    }

    public KunaiLeftWeapon(double fireRate){
        this.fireRate=fireRate;
    }


    @Override
    public List<AbstractBullet> shoot(double x, double y) {
        if (!canShoot()) return List.of();
        resetCooldown();

        List<AbstractBullet> bullets = new ArrayList<>();
        bullets.add(new KunaiLeftBullet(x, y, -250, 0,"enemy")); // tire vers ladroite
        return bullets;
    }

        protected void resetCooldown() {
        cooldown = fireRate;
    }
}
