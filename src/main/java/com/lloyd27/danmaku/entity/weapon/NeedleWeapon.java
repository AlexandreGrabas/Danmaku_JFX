package com.lloyd27.danmaku.entity.weapon;

import java.util.ArrayList;
import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.Bullet.NeedleBullet;


public class NeedleWeapon extends AbstractWeapon{
    protected double fireRate = 0.1; // secondes entre tirs

    @Override
    public List<AbstractBullet> shoot(double x, double y) {
        if (!canShoot()) return List.of();
        resetCooldown();

        List<AbstractBullet> bullets = new ArrayList<>();

        if (slowMode == true){
        bullets.add(new NeedleBullet(x - 4, y - 10, 0, -800)); // tire vers le haut
        bullets.add(new NeedleBullet(x + 10, y - 10, 0, -800));
        return bullets;
        }
        else{
        bullets.add(new NeedleBullet(x - 16, y - 10, 0, -800)); // tire vers le haut
        bullets.add(new NeedleBullet(x + 22, y - 10, 0, -800));
        return bullets;
        }
    }

        protected void resetCooldown() {
        cooldown = fireRate;
    }
}
