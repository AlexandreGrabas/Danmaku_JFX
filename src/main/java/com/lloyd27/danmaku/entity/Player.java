package com.lloyd27.danmaku.entity;

import java.util.ArrayList;
import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.Bullet.BombBullet;
import com.lloyd27.danmaku.entity.weapon.NeedleWeapon;
import com.lloyd27.danmaku.entity.weapon.SimpleWeapon;
import com.lloyd27.danmaku.entity.weapon.AbstractWeapon;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Player extends Entity {
    private double speed = 300;
    private boolean up, down, left, right;
    private List<AbstractWeapon> weapons = new ArrayList<>();
    private boolean slowMode =false;
    private double heart;
    private double bomb;
    private double spriteWidth=70;
    private double spriteHeight=70;
    private Image sprite;
    private Image sprite2;

    public Player(double x, double y, double heart, double bomb) {
        super(x, y);
        this.heart=heart;
        this.life=1;
        this.bomb=bomb;
        this.width=8;
        this.height=8;
        weapons.add(new SimpleWeapon());
        weapons.add(new NeedleWeapon());

        this.sprite2 = new Image(getClass().getResourceAsStream("/sprites/Fire_Effect.png"));
        sprite = new Image(getClass().getResourceAsStream("/sprites/player_v1.png"));
        // sprite = new Image(getClass().getResourceAsStream("/sprites/player_2.png"));
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public double setX(double x){
        return this.x=x;
    }

    public double setY(double y){
        return this.y=y;
    }

    public double getHeart(){
        return this.heart;
    }

    public double setHeart(double heart){
        return this.heart=heart;
    }

    public double getBomb(){
        return this.bomb;
    }

    public double setBomb(double bomb){
        return this.bomb=bomb;
    }

    public boolean setAlive(boolean alive){
        return this.alive=alive;
    }

    public void setSprite(Image sprite){
        this.sprite=sprite;
    }

    public List<AbstractBullet> shoot() {
        List<AbstractBullet> allBullets = new ArrayList<>();
        for (AbstractWeapon w : weapons) {
            w.setSlowMode(slowMode);
            allBullets.addAll(w.shoot(x, y));
        }
        return allBullets;
    }

    public AbstractBullet useBomb(){
        return (AbstractBullet) new BombBullet(x, y - 10, 0, -300, "player");
    }

    public void slowPlayer(boolean slow) {
        slowMode = slow ? true : false;
    }

    public void addWeapon(AbstractWeapon weapon) {
        weapons.add(weapon);
    }

    public void setDirection(boolean up, boolean down, boolean left, boolean right) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
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
    }

    @Override
    public void render(GraphicsContext gc) {
        // gc.setFill(Color.BLACK);
        // gc.fillRect(x - 10, y - 10, 20, 20);
        gc.drawImage(sprite, x - spriteWidth / 2, y - spriteHeight / 2, spriteWidth, spriteHeight);
        // gc.drawImage(sprite2,0,0,20,20, x - spriteWidth / 2, y - spriteHeight / 2, spriteWidth, spriteHeight);

        //hitbox
        gc.setFill(Color.RED);
        gc.fillOval(x - width/2, y - height/2, width, height);
    }
}
