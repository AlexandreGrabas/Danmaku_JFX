package com.lloyd27.danmaku.entity;

import com.lloyd27.danmaku.entity.weapon.AbstractWeapon;
import com.lloyd27.danmaku.entity.weapon.NeedleWeapon;
import com.lloyd27.danmaku.entity.weapon.SimpleWeapon;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class PlayerEllie extends Player{
    // Animation
    private Image sprite;
    private double spriteWidth=70;
    private double spriteHeight=70;    
    private int columns = 6;
    private int rows = 6;
    private int currentFrame = 0;
    private double frameTime = 0.02; // durée d’une frame (en secondes)
    private double timeSinceLastFrame = 0;

    public PlayerEllie(double x, double y, double heart, double bomb) {
        super(x, y, heart, bomb);
        this.life=1;
        this.score=0;
        this.width=8;
        this.height=8;
        this.name="Ellie";
        weapons.add(new SimpleWeapon());
        weapons.add(new NeedleWeapon());

        sprite = new Image(getClass().getResourceAsStream("/sprites/Ellie_6x6.png"));
        // sprite = new Image(getClass().getResourceAsStream("/sprites/player_1_detourer.png"));
        // sprite = new Image(getClass().getResourceAsStream("/sprites/player_2.png"));
    }

    @Override
    public void update(double deltaTime) {

        double actualSpeed = speed * (slowMode ? 0.5 : 1.0);

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
