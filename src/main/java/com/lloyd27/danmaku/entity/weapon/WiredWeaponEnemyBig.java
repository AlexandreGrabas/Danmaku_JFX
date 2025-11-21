package com.lloyd27.danmaku.entity.weapon;

import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.Bullet.SimpleBullet;

import java.util.ArrayList;

public class WiredWeaponEnemyBig extends AbstractWiredWeapon {
    protected double fireRate = 0.5; // secondes entre tirs

    public WiredWeaponEnemyBig(){
        this.fireRate=0.5;
    }

    public WiredWeaponEnemyBig(double fireRate){
        this.fireRate=fireRate;
    }
    

    @Override
    public List<AbstractBullet> shoot(double x, double y, double playerX, double playerY) {
        if (!canShoot()) return List.of();
        resetCooldown();

        List<AbstractBullet> bullets = new ArrayList<>();

        // vecteur direction vers le joueur
        double dx = playerX - x;
        double dy = playerY - y;

        // normalisation du vecteur pour garder une vitesse constante
        double length = Math.sqrt(dx*dx + dy*dy);
        double speed = 300; // pixels/sec
        double vx = dx / length * speed;
        double vy = dy / length * speed;

        bullets.add(new SimpleBullet(x, y, vx, vy,"enemy",50,50));
        return bullets;
    }

        protected void resetCooldown() {
        cooldown = fireRate;
    }

    

}
