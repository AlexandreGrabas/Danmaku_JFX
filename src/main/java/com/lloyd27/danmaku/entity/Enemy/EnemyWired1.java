package com.lloyd27.danmaku.entity.Enemy;

import java.util.ArrayList;
import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.weapon.AbstractWeapon;
import com.lloyd27.danmaku.entity.weapon.AbstractWiredWeapon;
import com.lloyd27.danmaku.entity.weapon.WiredWeaponEnemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EnemyWired1 extends AbstractEnemyShooter{
    private List<AbstractWeapon> weapons = new ArrayList<>();
    private List<AbstractWiredWeapon> wiredWeapons = new ArrayList<>();
    private boolean alternance=true;

    public EnemyWired1(double x, double y) {
        super(x, y);
        this.life=25;
        this.height=20;
        this.width=20;
        // weapons.add(new SimpleWeaponEnemy1());
        wiredWeapons.add(new WiredWeaponEnemy());
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
        if(alternance){
        x += 1;
        y += 1;
        alternance=false;
        }
        else{
        x += 1;
        // y += 1;
        alternance=true;
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

        gc.setFill(Color.DARKRED);
        gc.fillOval(x - 20, y - 20, 20, 20);

    }
}
