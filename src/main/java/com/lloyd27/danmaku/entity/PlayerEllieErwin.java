package com.lloyd27.danmaku.entity;

import java.util.ArrayList;
import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.weapon.AbstractWeapon;
import com.lloyd27.danmaku.entity.weapon.AbstractWiredWeapon;
import com.lloyd27.danmaku.entity.weapon.NeedleWeapon;
import com.lloyd27.danmaku.entity.weapon.SimpleWeapon;
import com.lloyd27.danmaku.entity.weapon.WiredWeaponPlayer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class PlayerEllieErwin extends Player{
    // Animation
    private Image sprite;
    private double spriteWidth=70;
    private double spriteHeight=70;    
    private int columns = 6;
    private int rows = 6;
    private int currentFrame = 0;
    private double frameTime = 0.02; // durée d’une frame (en secondes)
    private double timeSinceLastFrame = 0;
    private boolean ellie=true;
    private boolean upgrade40k = false;
    private boolean upgrade80k = false;

    public PlayerEllieErwin(double x, double y, double heart, double bomb) {
        super(x, y, heart, bomb);
        this.life=1;
        this.score=0;
        this.width=8;
        this.height=8;
        this.speed=450;
        this.name="ELLIE&ERWIN";
        weapons.add(new SimpleWeapon(0,0,0.01));
        weapons.add(new NeedleWeapon());
        // wiredWeapons.add(new WiredWeaponPlayer());

        sprite = new Image(getClass().getResourceAsStream("/sprites/Ellie_6x6.png"));

        // sprite = new Image(getClass().getResourceAsStream("/sprites/player_1_detourer.png"));
        // sprite = new Image(getClass().getResourceAsStream("/sprites/player_2.png"));
    }

    public void swapPlayer(){
        ellie = !ellie;
        if(ellie){
            this.speed=450;
            this.weapons.clear();
            this.wiredWeapons.clear();
            this.weapons.add(new SimpleWeapon(0,0,0.01));
            this.weapons.add(new NeedleWeapon());
            if(score>=40000){
                this.weapons.add(new SimpleWeapon(15,0,0.05));
                this.weapons.add(new SimpleWeapon(-15,0,0.05));
            }
            if(score>=80000){
                this.wiredWeapons.add(new WiredWeaponPlayer());
            }
            this.sprite = new Image(getClass().getResourceAsStream("/sprites/Ellie_6x6.png"));
        }else{
            this.speed=250;
            this.weapons.clear();
            this.wiredWeapons.clear();
            this.weapons.add(new SimpleWeapon(0,0,0.01));
            this.weapons.add(new SimpleWeapon(15,0,0.05));
            this.weapons.add(new SimpleWeapon(-15,0,0.05));
            this.wiredWeapons.add(new WiredWeaponPlayer());
            if(score>=40000){
                this.weapons.add(new NeedleWeapon(0.1));
            }
            if(score>=80000){
                this.weapons.add(new NeedleWeapon());
                this.speed=300;
            }
            this.sprite = new Image(getClass().getResourceAsStream("/sprites/Erwin_6x6.png"));
        }
    }

    private void checkUpgrades() {
        if (!upgrade40k && score >= 40000) {
            upgrade40k = true;

            if (ellie) {
                weapons.add(new SimpleWeapon(15, 0, 0.05));
                weapons.add(new SimpleWeapon(-15, 0, 0.05));
            } else {
                weapons.add(new NeedleWeapon(0.1));
            }
        }

        if (!upgrade80k && score >= 80000) {
            upgrade80k = true;

            if (ellie) {
                wiredWeapons.add(new WiredWeaponPlayer());
            } else {
                this.weapons.clear();
                this.wiredWeapons.clear();
                this.weapons.add(new SimpleWeapon(0,0,0.01));
                this.weapons.add(new SimpleWeapon(15,0,0.05));
                this.weapons.add(new SimpleWeapon(-15,0,0.05));
                this.wiredWeapons.add(new WiredWeaponPlayer());
                this.weapons.add(new NeedleWeapon());
                this.speed=300;
            }
        }
    }


    @Override
    public void update(double deltaTime) {
        
        checkUpgrades();

        double actualSpeed = speed * (slowMode ? 0.5 : 1);

        double dx = 0, dy = 0;
        if (up) {dy -= actualSpeed * deltaTime;}
        if (down) {dy += actualSpeed * deltaTime;}
        if (left) {dx -= actualSpeed * deltaTime;}
        if (right) {dx += actualSpeed * deltaTime;}
        if(x+dx<800 && x+dx>0){x += dx;}
        if(y+dy<900 && y+dy>0){y += dy;}
        

        for (AbstractWeapon w : weapons) {
            w.update(deltaTime);
        }
        for (AbstractWiredWeapon w : wiredWeapons) {
            w.update(deltaTime);
        }
        // Animation : avance la frame en fonction du temps
        timeSinceLastFrame += deltaTime;
        if (timeSinceLastFrame >= frameTime) {
            currentFrame = (currentFrame + 1) % (columns * rows);
            timeSinceLastFrame = 0;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        // gc.setFill(Color.BLACK);
        // gc.fillRect(x - 10, y - 10, 20, 20);
        // gc.drawImage(sprite, x - spriteWidth / 2, y - spriteHeight / 2, spriteWidth, spriteHeight);

        
        double frameWidth = sprite.getWidth() / columns;
        double frameHeight = sprite.getHeight() / rows;

        int col = currentFrame % columns;
        int row = currentFrame / columns;

        double sx = col * frameWidth;
        double sy = row * frameHeight;

        gc.drawImage(sprite,sx, sy, frameWidth, frameHeight,x - spriteWidth / 2, y - spriteHeight / 2,spriteWidth, spriteHeight);
        // gc.drawImage(sprite,0,0,sprite.getWidth()/6,sprite.getHeight()/6, x - spriteWidth / 2, y - spriteHeight / 2, spriteWidth, spriteHeight);
        // gc.drawImage(sprite2,0,0,20,20, x - spriteWidth / 2, y - spriteHeight / 2, spriteWidth, spriteHeight);

        //hitbox
        gc.setFill(Color.RED);
        gc.fillOval(x - width/2, y - height/2, width, height);
    }
}
