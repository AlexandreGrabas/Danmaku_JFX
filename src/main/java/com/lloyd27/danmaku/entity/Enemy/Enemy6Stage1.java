package com.lloyd27.danmaku.entity.Enemy;

import java.util.ArrayList;
import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.weapon.AbstractWeapon;
import com.lloyd27.danmaku.entity.weapon.AbstractWiredWeapon;
import com.lloyd27.danmaku.entity.weapon.SimpleWeaponEnemy1;
import com.lloyd27.danmaku.entity.weapon.WeaponBulletRound;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Enemy6Stage1 extends AbstractEnemyShooter{
    private List<AbstractWeapon> weapons = new ArrayList<>();
    private List<AbstractWiredWeapon> wiredWeapons = new ArrayList<>();
    private double time=0;
    private Image sprite=new Image(getClass().getResourceAsStream("/sprites/tête_dragon.png"));

    public Enemy6Stage1(double x, double y) {
        super(x, y);
        this.life=4000;
        this.height=80;
        this.width=80;
        this.setScore(3500);
        weapons.add(new WeaponBulletRound(50,1));
    }

    public List<AbstractBullet> shoot() {
        List<AbstractBullet> allBullets = new ArrayList<>();
        for (AbstractWeapon w : weapons) {
            allBullets.addAll(w.shoot(x, y));
        }
        return allBullets;
    }

    public List<AbstractBullet> shootWired(double playerX, double playerY) {
        List<AbstractBullet> allBullets = new ArrayList<>();
        for (AbstractWiredWeapon w : wiredWeapons) {
            allBullets.addAll(w.shoot(x, y, playerX, playerY));
        }
        return allBullets;
    }

    @Override
    public void update(double deltaTime) {
        time+=deltaTime;

        if(time < 2){
            y+=0.2;
        }
        else if(time >13){
             y+=1;
            this.setCanShoot(false);
        }
        else{
            this.setCanShoot(true);
        }

        // update arme classiques
        for (AbstractWeapon w : weapons) {
            w.update(deltaTime);
        }

        // update armes wired
        for (AbstractWiredWeapon w : wiredWeapons) {
            w.update(deltaTime);
        }
    }

    @Override
    public void render(GraphicsContext gc) {

        // gc.setFill(Color.DARKRED);
        // gc.fillOval(x - width/2, y - height/2, width, height);
        gc.drawImage(sprite, x - width/2, y - height/2, width, height);
    }

}
