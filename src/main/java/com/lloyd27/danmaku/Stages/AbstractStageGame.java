package com.lloyd27.danmaku.Stages;

import java.util.ArrayList;
import java.util.List;

import com.lloyd27.danmaku.entity.Entity;
import com.lloyd27.danmaku.entity.Player;
import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.Bullet.BombBullet;
import com.lloyd27.danmaku.entity.Bullet.WiredBulletPlayer;
import com.lloyd27.danmaku.entity.Enemy.AbstractEnemyShooter;
import com.lloyd27.danmaku.entity.Enemy.Boss1;
import com.lloyd27.danmaku.managers.InputManager;
import com.lloyd27.danmaku.managers.SoundManager;
import com.lloyd27.danmaku.managers.TableauScoresManager;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public abstract class AbstractStageGame extends AbstractStage{
    protected Player player;
    protected double timeBossDead =0;
    protected boolean bossDead = false;
    protected TableauScoresManager tableauScoresManager=new TableauScoresManager();
    protected SoundManager soundManager = new SoundManager();
    protected boolean goTableEndScore = false;
    protected boolean returnMenu = false;
    protected double timeLastBomb = 0;
    protected List<AbstractEnemyShooter> enemies = new ArrayList<>();
    protected List<Entity> entity = new ArrayList<>();
    protected InputManager input;
    protected double timeLastShootSound=0;
    protected double timeAfterPlayerDead = 0;
    protected SoundManager soundManagerPause = new SoundManager();
    protected boolean pause = false;
    protected long index=0;
    protected double timeLastUp = 0;
    protected double timeLastDown = 0;
    protected boolean quitGame = false;

    public abstract void init();
    public abstract void render(GraphicsContext gc);
    public abstract void update(double deltaTime);
    public abstract List<Entity> getEntity();
    public abstract boolean isFinished(); // Pour savoir si on passe au stage suivant

    protected Canvas canvas; // Canvas principal
    protected Canvas hudCanvas; // Canvas HUD (optionnel)

    public Canvas getCanvas() { return canvas; }
    public Canvas getHudCanvas() { return hudCanvas; } // peut être null si pas de HUD

    

    public void setSize(double width, double height) {
        if (canvas != null) {
            canvas.setWidth(width);
            canvas.setHeight(height);
        }
        if (hudCanvas != null) {
            hudCanvas.setWidth(150);
            hudCanvas.setHeight(height);
        }
    }

    public void gestionPause(double deltaTime){
            if (timeLastDown>=0){timeLastDown -= deltaTime;}
            if (timeLastUp>=0){timeLastUp -= deltaTime;}
        
            if (timeLastUp<=0 && input.isUp()) {
                index -= 1;
                timeLastUp = 0.2;
                if(index==-1){index=2;}
            }
            if (timeLastDown<=0 && input.isDown()) {
                index += 1;
                timeLastDown = 0.2;
                if(index==3){index=0;}
            };

            if (input.isAccepted()) {
                if (index == 0) {
                    soundManagerPause.stopMusic();
                    soundManager.UnPauseMusic();
                    if(!this.player.isAlive()){
                        this.player.respawn();
                        entity.add(this.player);
                    }
                    pause = false;
                }
                else if (index == 1){
                    soundManagerPause.stopMusic();
                    returnMenu=true;
                } 
                else if(index == 2){
                    quitGame = true;
                }
            }
        }


    public void stageEndNewScore(){
        if(bossDead && timeBossDead>10){
            if(player.getScore()>tableauScoresManager.getWorseScore(player.getName())){
                soundManager.stopMusic();
                goTableEndScore = true;
            }else{
                soundManager.stopMusic();
                returnMenu = true;
            }
        }
    }

    public void gestionTirEnemy(){
        if ((timeLastBomb<=0)) {
            for (AbstractEnemyShooter ene : enemies) {
                if (!ene.isAlive() || !ene.getCanShoot()) continue;
                var bullets = ene.shoot();
                bullets.addAll(ene.shootWired(this.player.getX(), this.player.getY()));
                entity.addAll(bullets);
            }
        }
    }

    public void gestionAutowiredPlayer(){
        for (Entity e1 : entity) {
            double xTarget=player.getX();
            double yTarget=0;
            if (!(e1 instanceof WiredBulletPlayer wiredBulletPlayer) || !wiredBulletPlayer.isAlive())
                continue;        
            double distance=Double.MAX_VALUE;;
            for (Entity e2 : entity) {
                if (!(e2 instanceof AbstractEnemyShooter enemy) || !enemy.isAlive())
                    continue;
                if(distance>Math.sqrt(Math.pow(enemy.getX() - wiredBulletPlayer.getX(), 2) + Math.pow(enemy.getY() - wiredBulletPlayer.getY(), 2))){
                    distance=Math.sqrt(Math.pow(enemy.getX() - wiredBulletPlayer.getX(), 2) + Math.pow(enemy.getY() - wiredBulletPlayer.getY(), 2));
                    xTarget=enemy.getX();
                    yTarget=enemy.getY();
                }
            }
            if (distance == Double.MAX_VALUE)
                continue;
            // vecteur direction vers le enemy
            double dx = xTarget - wiredBulletPlayer.getX();
            double dy = yTarget - wiredBulletPlayer.getY();

            // normalisation du vecteur pour garder une vitesse constante
            double length = Math.sqrt(dx*dx + dy*dy);
            double speed = 1000; // pixels/sec
            double vx = dx / length * speed;
            double vy = dy / length * speed;
            wiredBulletPlayer.setVx(vx);
            wiredBulletPlayer.setVy(vy);
        }
    }

    public void gestionTirsJoueur(){
            if (this.player != null) {
            this.player.setDirection(input.isUp(), input.isDown(), input.isLeft(), input.isRight());
            this.player.slowPlayer(input.isSlow());

            if (input.isShoot()) {
                var bullets = this.player.shoot();
                bullets.addAll(this.player.shootWired(player.getX(),0));
                
                if (timeLastShootSound<0 && this.player.isAlive()){
                    soundManager.playSound("1760.wav", 0.2);
                    timeLastShootSound=0.1;
                }
                entity.addAll(0,bullets);
            }
            if (input.isBomb() && timeLastBomb <=0 && this.player.getBomb()>0 && this.player.isAlive()) {
                entity.add(this.player.useBomb());
                this.player.setBomb(this.player.getBomb()-1);
                timeLastBomb=600;
            }
        }
    }

    public void addToRemoveOffScreen(List<Entity> toRemove,double deltaTime){
                //ajout a la liste des entités a supprimer les entité offscreen
            for (Entity e : entity) {
                e.update(deltaTime);
                if (e instanceof AbstractBullet b && b.isOffScreen()) {
                    toRemove.add(e);
                }
                if (e instanceof AbstractEnemyShooter b && b.isOffScreen()) {
                    toRemove.add(e);
                }
                if (e.isAlive() == false && !toRemove.contains(e)) {
                    if (e instanceof AbstractEnemyShooter) {
                        this.player.earnScore(((AbstractEnemyShooter)e).getScore());
                    }
                    toRemove.add(e);
                }
            }
        }

    // gestion des dommages des balles(bullet)
    public void gestionDmgBullet(){            
        for (Entity e : entity) {
            if (!(e instanceof AbstractBullet bullet) || !bullet.isAlive())
                continue;

            if ("player".equals(bullet.getOwnerType())) {
                for (AbstractEnemyShooter enemy : enemies) {
                    if (!enemy.isAlive())
                        continue;
                    if (enemy.intersects(bullet)) {
                        enemy.takeDamage(bullet.getDamage());
                        bullet.takeDamage(1); // supprime la balle après impact (car 1 pv)
                        this.player.earnScore(bullet.getDamage());
                        break; // stop après un impact
                    }
                }
            } else if ("enemy".equals(bullet.getOwnerType()) && timeAfterPlayerDead < 0) {
                if (this.player != null && this.player.isAlive() && this.player.intersects(bullet)) {
                    if(timeAfterPlayerDead<=0 && timeLastBomb<=0){
                        this.player.takeDamage(bullet.getDamage());
                        bullet.takeDamage(1);
                    }
                }
            }
        }
    }

    // gestion de dommage lorsque le joueur entre en colision avec un enemy
    public void gestionColisionJoueurEnemy(){        
        for (Entity e : entity) {
            if (!(e instanceof AbstractEnemyShooter enemyShooter) || !enemyShooter.isAlive())
                continue;
            
            if (this.player != null && this.player.isAlive() && this.player.intersects(enemyShooter)) {
                if(timeAfterPlayerDead<=0 && timeLastBomb<=0){
                    this.player.takeDamage(1);
                    enemyShooter.takeDamage(15);
                }
            }
        }
    }

    public void gestionSupressionEntity(List<Entity> toRemove){
                // gestion des entité a supprimer
        if (toRemove.contains(player)) {
            soundManager.playSound("death.wav", 2);
            double heart = this.player.getHeart();
            heart -= 1;
            if (heart > 0) {
                this.player.setAlive(true);
                this.player.setHeart(heart);
                this.player.setBomb(3);
                this.player.setX(400);
                this.player.setY(800);
                toRemove.remove(this.player);
                timeAfterPlayerDead = 600;
            } else {
                this.player.setHeart(heart);
                entity.removeAll(toRemove);
                soundManager.PauseMusic();
                soundManagerPause.playMusic("Mystical Power Plant - 02 Alleyway of Roaring Waves.mp3", 0.2, true);
                input.setAccepted(false);
                pause=true;
            }
        }
    }

    // Action a faire avant que le boss soit supprimer
    public void gestionAvantSupressionBoss(List<Entity> toRemove){        
        boolean bossRemove = toRemove.stream().anyMatch(e -> e instanceof Boss1);
        if(bossRemove){
            entity.removeIf(e -> e instanceof AbstractBullet b && "enemy".equals(b.getOwnerType()));
            bossDead=true;
            timeBossDead=0;
        }
    }

    // Action a faire avant que la bomb soit supprimer
    public void gestionAvantSupressionBomb(List<Entity> toRemove){               
            boolean hasBombBullet = toRemove.stream().anyMatch(e -> e instanceof BombBullet);
            if (hasBombBullet) {
                soundManager.playSound("explosion.wav", 0.1);
               for (Entity e : entity) {
                    if (e instanceof AbstractEnemyShooter enemy) {
                        enemy.takeDamage(250);
                    }
                }
            }
        }

        
    public void affichageBossDead(GraphicsContext gc){
        // Affichage lorsque le boss est mort/stage est finit
        if(bossDead && timeBossDead<=3){
            gc.setFill(Color.BLACK);
            gc.setFont(new Font("Arial", 50));
            gc.fillText("STAGE CLEAR", 220, 900-(230*timeBossDead));
        }else if(bossDead && timeBossDead>3){
            gc.setFill(Color.BLACK);
            gc.setFont(new Font("Arial", 50));
            gc.fillText("STAGE CLEAR", 220, 900-(230*3));
            gc.setFont(new Font("Arial", 36));
            gc.fillText("SCORE", 180, 300);
            gc.fillText(" "+(int)this.player.getScore(), 480, 300);
        }
    }

    public void affichagePause(GraphicsContext gc){   
        gc.setFill(new Color(0, 0, 0, 0.4));
        gc.fillRect(0, 0, 900, 900);
        
        gc.getCanvas().setEffect(null);
        gc.setFont(new Font("Arial", 100));
        if(this.player.getHeart()>0){
            gc.setFill(Color.BLACK);
            gc.fillText("PAUSE", 230, 200);
        }
        else{
            gc.setFill(Color.DARKRED);
            gc.fillText("GAME OVER", 100, 200);
            gc.setLineWidth(1);
            gc.setStroke(Color.BLACK);
            gc.strokeText("GAME OVER", 100, 200);
        }

        gc.setFont(new Font("Arial", 36));
        gc.setFill(index == 0 ? Color.WHITE: Color.BLACK);
        gc.fillText("Continuer", 100, 300);


        gc.setFill(index == 1 ? Color.WHITE : Color.BLACK);
        gc.fillText("Menu", 120, 400);

        gc.setFill(index == 2 ? Color.WHITE : Color.BLACK);
        gc.fillText("Quit", 140, 500);
    }

}
