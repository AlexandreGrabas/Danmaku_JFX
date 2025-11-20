package com.lloyd27.danmaku.entity.weapon;

import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.Bullet.BulletLineLeft;
import com.lloyd27.danmaku.entity.Bullet.SimpleBullet;

import java.util.ArrayList;

public class WeaponLineLeft extends AbstractWeapon {
    protected double fireRate = 1; // secondes entre tirs
    private double nbBullet = 10;
    private double speedX = 10;
    private double speedY = 10;
    private List<AbstractBullet> bullets = new ArrayList<>();

    public WeaponLineLeft(double nbBullet, double speedX, double speedY,double fireRate){
        this.nbBullet=nbBullet; 
        this.speedX=speedX;
        this.speedY=speedY;
        this.fireRate=fireRate;
    }

    public WeaponLineLeft(){
        this.fireRate=1;
    }


    @Override
    public List<AbstractBullet> shoot(double x, double y) {
        if (!canShoot()) return List.of();
        resetCooldown();

        List<AbstractBullet> newBullets = new ArrayList<>();

        for(int i=0;i<nbBullet;i++){
            BulletLineLeft b = new BulletLineLeft(880/nbBullet*i, 0, 0, speedY,"enemy");
            bullets.add(b);
            newBullets.add(b);  
            }
        return newBullets;
    }

        protected void resetCooldown() {
        cooldown = fireRate;
    }
    
    @Override
    public void update(double deltaTime) {

        if (cooldown > 0) cooldown -= deltaTime;

        for(AbstractBullet b : bullets){

            BulletLineLeft bl = (BulletLineLeft)b;

        if (bl.isAlternance()) {
            bl.setX(bl.getX() + speedX);
            bl.setCpt(bl.getCpt() + deltaTime);

            if (bl.getCpt() > 1.5) {
                bl.setAlternance(false);
            }

        } else {
            bl.setX(bl.getX() - speedX);
            bl.setCpt(bl.getCpt() - deltaTime);

            if (bl.getCpt() < -1.5) {
                bl.setAlternance(true);
            }
            }
        }
    }
}
