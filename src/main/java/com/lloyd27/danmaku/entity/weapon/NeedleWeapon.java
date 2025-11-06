package com.lloyd27.danmaku.entity.weapon;

import java.util.ArrayList;
import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.Bullet.NeedleBullet;


public class NeedleWeapon extends AbstractWeapon{
    protected double fireRate = 0.05; // secondes entre tirs

    @Override
    public List<AbstractBullet> shoot(double x, double y) {
        if (!canShoot()) return List.of();
        resetCooldown();

        List<AbstractBullet> bullets = new ArrayList<>();

        if (slowMode == true){
        bullets.add(new NeedleBullet(x - 6, y - 10, 0, -800,"player")); // tire vers le haut
        bullets.add(new NeedleBullet(x + 6, y - 10, 0, -800,"player"));
        bullets.add(new NeedleBullet(x - 8, y - 10, 0, -800,"player"));
        bullets.add(new NeedleBullet(x + 8, y - 10, 0, -800,"player"));
        bullets.add(new NeedleBullet(x - 10, y - 10, 0, -800,"player"));
        bullets.add(new NeedleBullet(x + 10, y - 10, 0, -800,"player"));
        bullets.add(new NeedleBullet(x - 12, y - 10, 0, -800,"player"));
        bullets.add(new NeedleBullet(x + 12, y - 10, 0, -800,"player"));
        return bullets;
        }
        else{
        bullets.add(new NeedleBullet(x - 10, y - 10, 0, -800,"player")); // tire vers le haut
        bullets.add(new NeedleBullet(x + 10, y - 10, 0, -800,"player"));
        bullets.add(new NeedleBullet(x - 20, y - 10, 0, -800,"player")); 
        bullets.add(new NeedleBullet(x + 20, y - 10, 0, -800,"player"));
        bullets.add(new NeedleBullet(x - 30, y - 10, 0, -800,"player")); 
        bullets.add(new NeedleBullet(x + 30, y - 10, 0, -800,"player"));
        bullets.add(new NeedleBullet(x - 40, y - 10, 0, -800,"player")); 
        bullets.add(new NeedleBullet(x + 40, y - 10, 0, -800,"player"));
        return bullets;
        }
    }

        protected void resetCooldown() {
        cooldown = fireRate;
    }
}
