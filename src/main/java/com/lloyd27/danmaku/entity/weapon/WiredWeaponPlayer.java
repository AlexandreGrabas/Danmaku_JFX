package com.lloyd27.danmaku.entity.weapon;

import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.Bullet.SimpleBullet;
import com.lloyd27.danmaku.entity.Bullet.WiredBulletPlayer;

import java.util.ArrayList;

public class WiredWeaponPlayer extends AbstractWiredWeapon {
    protected double fireRate = 0.05; // secondes entre tirs

    public WiredWeaponPlayer(){
    }

    public WiredWeaponPlayer(double fireRate){
        this.fireRate=fireRate;
    }
    

    @Override
    public List<AbstractBullet> shoot(double x, double y, double enemyX, double enemyY) {
        if (!canShoot()) return List.of();
        resetCooldown();

        List<AbstractBullet> bullets = new ArrayList<>();

        // vecteur direction vers le enemy
        double dx = enemyX - x;
        double dy = enemyY - y;

        // normalisation du vecteur pour garder une vitesse constante
        double length = Math.sqrt(dx*dx + dy*dy);
        double speed = 1000; // pixels/sec
        double vx = dx / length * speed;
        double vy = dy / length * speed;

        bullets.add(new WiredBulletPlayer(x-15, y-10, vx, vy,"player"));
        bullets.add(new WiredBulletPlayer(x+15, y-10, vx, vy,"player"));
        bullets.add(new WiredBulletPlayer(x-30, y-10, vx, vy,"player"));
        bullets.add(new WiredBulletPlayer(x+30, y-10, vx, vy,"player"));
        return bullets;
    }

        protected void resetCooldown() {
        cooldown = fireRate;
    }

    

}
