package com.lloyd27.danmaku.entity.weapon;

import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.Bullet.KunaiBullet;

import java.util.ArrayList;

public class KunaiWeapon extends AbstractWeapon {
    protected double fireRate = 0.01; // secondes entre tirs

    public KunaiWeapon(){
        this.fireRate=0.5;
    }

    public KunaiWeapon(double fireRate){
        this.fireRate=fireRate;
    }


    @Override
    public List<AbstractBullet> shoot(double x, double y) {
        if (!canShoot()) return List.of();
        resetCooldown();

        List<AbstractBullet> bullets = new ArrayList<>();
        bullets.add(new KunaiBullet(x, y - 10, 0, +300,"enemy")); // tire vers le haut
        return bullets;
    }

        protected void resetCooldown() {
        cooldown = fireRate;
    }
}
