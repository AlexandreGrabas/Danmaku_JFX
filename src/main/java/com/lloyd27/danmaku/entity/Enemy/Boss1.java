package com.lloyd27.danmaku.entity.Enemy;

import java.util.ArrayList;
import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.weapon.AbstractWeapon;
import com.lloyd27.danmaku.entity.weapon.AbstractWiredWeapon;
import com.lloyd27.danmaku.entity.weapon.SimpleWeaponEnemy1;
import com.lloyd27.danmaku.entity.weapon.WiredWeaponEnemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Boss1 extends AbstractEnemyShooter{
    private List<AbstractWeapon> weapons = new ArrayList<>();
    private List<AbstractWiredWeapon> wiredWeapons = new ArrayList<>();
    private int cpt=0;
    private boolean alternance=true;

    public Boss1(double x, double y) {
        super(x, y);
        this.life=500;
        this.height=50;
        this.width=50;
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
        cpt+=1;
        if (cpt>=300){
            alternance=false;
            cpt=0;
        }
        }
        else{
        x -= 1;
        cpt+=1;
        if (cpt>=300){
            alternance=true;
            cpt=0;
        }
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
        gc.fillOval(x - 20, y - 20, 50, 50);

    }


}
