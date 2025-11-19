package com.lloyd27.danmaku.entity.Enemy;

import java.util.ArrayList;
import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.weapon.AbstractWeapon;
import com.lloyd27.danmaku.entity.weapon.AbstractWiredWeapon;
import com.lloyd27.danmaku.entity.weapon.SimpleWeaponEnemy1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Enemy4Stage1 extends AbstractEnemyShooter{
    private List<AbstractWeapon> weapons = new ArrayList<>();
    private List<AbstractWiredWeapon> wiredWeapons = new ArrayList<>();
    private boolean alternance=true;
    private Image sprite=new Image(getClass().getResourceAsStream("/sprites/gobelin_mage.png"));

    public Enemy4Stage1(double x, double y) {
        super(x, y);
        this.life=50;
        this.height=40;
        this.width=40;
        weapons.add(new SimpleWeaponEnemy1());
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
        x -= 1;
        // y += 1;
        alternance=false;
        }
        else{
        x -= 1;
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

        // gc.setFill(Color.DARKRED);
        // gc.fillOval(x - width/2, y - height/2, width, height);
        gc.drawImage(sprite, x - width/2, y - height/2, width, height);
    }

}
