package com.lloyd27.danmaku.entity.weapon;

import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.Bullet.SimpleBullet;

import java.util.ArrayList;

public class SimpleWeapon extends AbstractWeapon {
    protected double fireRate = 0.1; // secondes entre tirs

    @Override
    public List<AbstractBullet> shoot(double x, double y) {
        if (!canShoot()) return List.of();
        resetCooldown();

        List<AbstractBullet> bullets = new ArrayList<>();
        bullets.add(new SimpleBullet(x, y - 10, 0, -500)); // tire vers le haut
        return bullets;
    }

        protected void resetCooldown() {
        cooldown = fireRate;
    }
}
