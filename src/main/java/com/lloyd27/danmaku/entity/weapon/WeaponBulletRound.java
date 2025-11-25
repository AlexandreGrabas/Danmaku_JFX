package com.lloyd27.danmaku.entity.weapon;

import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.Bullet.SimpleBullet;

import java.util.ArrayList;

public class WeaponBulletRound extends AbstractWeapon {
    protected double fireRate = 1; // secondes entre tirs
    private double nbBullet = 90;
       
    public WeaponBulletRound(){
        this.fireRate=1;
    }

    public WeaponBulletRound(double nbBullet,double fireRate){
        this.nbBullet=nbBullet; 
        this.fireRate=fireRate;
    }

    @Override
    public List<AbstractBullet> shoot(double x, double y) {
        if (!canShoot()) return List.of();
        resetCooldown();

        List<AbstractBullet> bullets = new ArrayList<>();
        for(int i=0;i<nbBullet;i++){
            bullets.add(new SimpleBullet(x, y - 10, Math.cos(2*Math.PI/nbBullet*i) * 150, Math.sin(2*Math.PI/nbBullet*i) * 150,"enemy"));
            }
        return bullets;
    }

        protected void resetCooldown() {
        cooldown = fireRate;
    }
}
