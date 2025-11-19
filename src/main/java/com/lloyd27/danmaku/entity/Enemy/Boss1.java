package com.lloyd27.danmaku.entity.Enemy;

import java.util.ArrayList;
import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.weapon.AbstractWeapon;
import com.lloyd27.danmaku.entity.weapon.AbstractWiredWeapon;
import com.lloyd27.danmaku.entity.weapon.KunaiLeftWeapon;
import com.lloyd27.danmaku.entity.weapon.KunaiRightWeapon;
import com.lloyd27.danmaku.entity.weapon.WiredWeaponEnemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Boss1 extends AbstractEnemyShooter{
    private List<AbstractWeapon> weapons = new ArrayList<>();
    private List<AbstractWiredWeapon> wiredWeapons = new ArrayList<>();
    private int cpt=0;
    private boolean alternance=true;
    private Image sprite = new Image("/sprites/boss_v2.png");
    private double lifePourcentage=1;

    public Boss1(double x, double y) {
        super(x, y);
        this.life=30000;
        this.height=125;
        this.width=125;
        weapons.add(new KunaiLeftWeapon());
        weapons.add(new KunaiRightWeapon());
        wiredWeapons.add(new WiredWeaponEnemy(0.01));
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
        lifePourcentage=life/30000;
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

        // gc.setFill(Color.DARKRED);
        // gc.fillOval(x - width/2, y - height/2, width, height);
        gc.drawImage(sprite, x - width / 2, y - height / 2, width, height);
        gc.setLineWidth(10.0);
        gc.setStroke(Color.DARKRED);
        gc.strokeLine(10, 20, 790*lifePourcentage, 20);
        gc.setStroke(Color.WHITE);
    }


}
