package com.lloyd27.danmaku.entity;

import java.util.ArrayList;
import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.Bullet.BombBullet;
import com.lloyd27.danmaku.entity.weapon.NeedleWeapon;
import com.lloyd27.danmaku.entity.weapon.SimpleWeapon;
import com.lloyd27.danmaku.managers.SoundManager;
import com.lloyd27.danmaku.entity.weapon.AbstractWeapon;
import com.lloyd27.danmaku.entity.weapon.AbstractWiredWeapon;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Player extends Entity {
    protected String name = "unknow";
    protected double speed = 300;
    protected boolean up, down, left, right;
    protected List<AbstractWeapon> weapons = new ArrayList<>();
    protected List<AbstractWiredWeapon> wiredWeapons = new ArrayList<>();
    protected boolean slowMode =false;
    protected double heart;
    protected double bomb;
    protected double score;
    protected double spriteWidth=70;
    protected double spriteHeight=70;
    protected Image sprite;
    protected SoundManager soundManager = new SoundManager();

    // Animation
    protected int columns = 6;
    protected int rows = 6;
    protected int currentFrame = 0;
    protected double frameTime = 0.02; // durée d’une frame (en secondes)
    protected double timeSinceLastFrame = 0;
    private int nextExtraLifeScore = 100000;   // score de la prochaine vie
    private int lifeStep = 100000;    

    public Player(double x, double y, double heart, double bomb) {
        super(x, y);
        this.heart=heart;
        this.life=1;
        this.bomb=bomb;
        this.score=0;
        this.width=6;
        this.height=6;

        // sprite = new Image(getClass().getResourceAsStream("/sprites/sprite-256px-36.png"));
        // sprite = new Image(getClass().getResourceAsStream("/sprites/player_1_detourer.png"));
        // sprite = new Image(getClass().getResourceAsStream("/sprites/player_2.png"));
    }

    public void audioPlayerDeath(){
        soundManager.playSound("death.wav", 2);
    }

    public double getX(){
        return this.x;
    }

    public double setX(double x){
        return this.x=x;
    }
    public double getY(){
        return this.y;
    }
    public double setY(double y){
        return this.y=y;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name=name;
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

    public double getScore(){
        return this.score;
    }

    public double setScore(double score){
        return this.score=score;
    }

    public void earnScore(double score){
        this.score=this.score+score;
        if(score>nextExtraLifeScore){
            heart+=1;
            nextExtraLifeScore+=lifeStep;
        }
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

    public List<AbstractBullet> shootWired(double enemyX, double enemyY) {
        List<AbstractBullet> allBullets = new ArrayList<>();
        for (AbstractWiredWeapon w : wiredWeapons) {
            allBullets.addAll(w.shoot(x, y, enemyX, enemyY));
        }
        return allBullets;
    }

    public AbstractBullet useBomb(){
        return (AbstractBullet) new BombBullet(400, 890, 0, -300, "player");
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
