package com.lloyd27.danmaku.entity.Enemy;

import java.util.ArrayList;
import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.weapon.AbstractWeapon;
import com.lloyd27.danmaku.entity.weapon.AbstractWiredWeapon;
import com.lloyd27.danmaku.entity.weapon.KunaiLeftWeapon;
import com.lloyd27.danmaku.entity.weapon.KunaiRightWeapon;
import com.lloyd27.danmaku.entity.weapon.WeaponBulletRound;
import com.lloyd27.danmaku.entity.weapon.WeaponLineLeft;
import com.lloyd27.danmaku.entity.weapon.WeaponLineRight;
import com.lloyd27.danmaku.entity.weapon.WiredWeaponEnemy;
import com.lloyd27.danmaku.managers.SoundManager;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Boss1 extends AbstractEnemyShooter{
    private List<AbstractWeapon> weapons = new ArrayList<>();
    private List<AbstractWiredWeapon> wiredWeapons = new ArrayList<>();
    private int cpt=0;
    private boolean alternance=true;
    private Image sprite = new Image("/sprites/boss_v2.png");
    private double lifePourcentage=1;
    private double heart = 6;
    private double lifeMax;
    private double swapPhase=0;
    private boolean canShoot=true;
    private double timeLastPhase = 50;
    private SoundManager soundManager=new SoundManager();
    private double lastFireSound = 0;
    private double FireSoundFireRate = 3;
    private SoundManager soundDeath=new SoundManager();

    public Boss1(double x, double y) {
        super(x, y);
        this.life=30000;
        this.lifeMax=30000;
        this.height=125;
        this.width=125;
        weapons.add(new WeaponLineLeft(12, 0.1, 50,2));
        weapons.add(new WeaponLineRight(12, 0.1, 50, 2));
        weapons.add(new WeaponBulletRound(60,3));
    }

    public List<AbstractBullet> shoot() {
        List<AbstractBullet> allBullets = new ArrayList<>();
        for (AbstractWeapon w : weapons) {
            allBullets.addAll(w.shoot(x, y));
            if(lastFireSound<=0){
                soundManager.playSound("feuSound.wav", 0.15);
                lastFireSound=FireSoundFireRate;
            }
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

    public double getHeart(){
        return this.heart;
    }

    @Override
    public boolean isAlive(){
        gestionPhase();
        return alive;
    }

    public void gestionPhase(){
        if(alive==false){
            if(heart>0){
                soundDeath.playSound("death.wav", 2);
            }
            heart-=1;
            canShoot = false;
            swapPhase=2;

            if(heart==5){
                this.life=30000;
                this.lifeMax=30000;
                this.timeLastPhase=50;
                weapons.clear();
                wiredWeapons.clear();
                weapons.add(new WeaponBulletRound(20,0.2));
                lastFireSound=2;
                FireSoundFireRate=0.2;
                soundManager.playSound("feuSound.wav", 0.15);
            }
            if(heart==4){
                this.life=20000;
                this.lifeMax=20000;
                this.timeLastPhase=50;
                weapons.clear();
                wiredWeapons.clear();
                weapons.add(new KunaiLeftWeapon());
                weapons.add(new KunaiRightWeapon());
                wiredWeapons.add(new WiredWeaponEnemy(0.01));
                lastFireSound=2;
                FireSoundFireRate=0.15;
                soundManager.playSound("feuSound.wav", 0.2);
            }
            if(heart==3){
                this.life=25000;
                this.lifeMax=25000;
                this.timeLastPhase=50;
                weapons.clear();
                wiredWeapons.clear();
                weapons.add(new WeaponBulletRound(40,0.15));
                lastFireSound=2;
                FireSoundFireRate=0.3;
                soundManager.playSound("feuSound.wav", 0.15);
            }
            if(heart==2){
                this.life=25000;
                this.lifeMax=25000;
                this.timeLastPhase=50;
                weapons.clear();
                wiredWeapons.clear();
                weapons.add(new WeaponBulletRound(60,1));
                lastFireSound=2;
                FireSoundFireRate=1;
                soundManager.playSound("feuSound.wav", 0.15);
            }
            if(heart==1){
                this.life=30000;
                this.lifeMax=30000;
                this.timeLastPhase=50;
                weapons.clear();
                wiredWeapons.clear();
                weapons.add(new WeaponBulletRound(60,0.15));
                lastFireSound=2;
                FireSoundFireRate=0.3;
                soundManager.playSound("feuSound.wav", 0.15);
            }
            if(heart>0){
                alive=true;
            }
            
        }
    }

    @Override
    public void update(double deltaTime) {
        lastFireSound-=deltaTime;
        this.timeLastPhase-=deltaTime;
        lifePourcentage=life/lifeMax;

        if(timeLastPhase<=0){
            alive=false;
        }
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
            
        // Décompte du timer de phase
        if (!canShoot) {
            swapPhase -= deltaTime;
            if (swapPhase <= 0) {
                canShoot = true;
            }
        }

        if (canShoot) {
            // update arme classiques
            for (AbstractWeapon w : weapons) {
                w.update(deltaTime);
            }

            // update armes wired
            for (AbstractWiredWeapon w : wiredWeapons) {
                w.update(deltaTime);
            }
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
        gc.setFont(new Font("Arial", 35));
        gc.setFill(timeLastPhase<10 ? Color.DARKRED : Color.WHITE);
        gc.fillText(""+Math.round(timeLastPhase), 760, 55);
    }


}
