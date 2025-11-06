package com.lloyd27.danmaku.entity.weapon;

import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;

public abstract class AbstractWeapon {
    protected double cooldown = 0;
    protected boolean slowMode = false;
    protected String ownerType = "neutral";

    public void setOwnerType(String type) {
        this.ownerType = type;
    }   

    public abstract List<AbstractBullet> shoot(double x, double y);
    
    public void update(double deltaTime) {
        if (cooldown > 0) cooldown -= deltaTime;
    }

    public boolean canShoot() {
        return cooldown <= 0;
    }

    public void setSlowMode(boolean slow) {
        this.slowMode = slow;
    }

}
