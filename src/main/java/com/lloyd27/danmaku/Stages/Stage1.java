package com.lloyd27.danmaku.Stages;

import java.util.ArrayList;
import java.util.List;

import com.lloyd27.danmaku.entity.Entity;
import com.lloyd27.danmaku.entity.Player;
import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.Bullet.BombBullet;
import com.lloyd27.danmaku.entity.Enemy.AbstractEnemyShooter;
import com.lloyd27.danmaku.entity.Enemy.Boss1;
import com.lloyd27.danmaku.entity.Enemy.Enemy1Stage1;
import com.lloyd27.danmaku.entity.Enemy.Enemy2Stage1;
import com.lloyd27.danmaku.entity.Enemy.Enemy3Stage1;
import com.lloyd27.danmaku.entity.Enemy.Enemy4Stage1;
import com.lloyd27.danmaku.entity.Enemy.EnemyKunai1;
import com.lloyd27.danmaku.managers.InputManager;
import com.lloyd27.danmaku.managers.SoundManager;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Stage1 extends AbstractStage{
    private List<AbstractEnemyShooter> enemies = new ArrayList<>();
    private Player player;
    private List<Entity> entity = new ArrayList<>();
    private double timeSinceStart = 0;
    private double timeSinceLastSpawn = 0;
    private double timeSinceLastSpawn3 = 0;
    private double timeSinceLastSpawnKunai3 = 0;
    private double cptKunai2 = 3;
    private double cptKunai3 = 15;
    private double timeAfterPlayerDead = 0;
    private double timeLastBomb = 0;
    private InputManager input;
    private boolean bossSpawn = false;
    private double timeLastShootSound=0;
    private double timeLastPause = 0;
    private boolean pause = false;
    private boolean returnMenu = false;
    private boolean quitGame = false;
    private SoundManager soundManager = new SoundManager();
    private SoundManager soundManagerPause = new SoundManager();
    private long index=0;
    private double timeLastUp = 0;
    private double timeLastDown = 0;
    private double timeBossDead =0;
    private boolean bossDead = false;
    private Image background=new Image("/sprites/background.jpg");
    private double scrollSpeed = 100;

    public Stage1(InputManager inputManager, Canvas canvas, Player player) {
        this.input=inputManager;
        this.canvas = canvas;
        this.hudCanvas = new Canvas(150, 900);
        this.player=player;
        canvas.setWidth(800);
    }

    public Player getPlayer(){
        return this.player;
    }

    public List<Entity> getEntity(){
        return this.entity;
    }

    public void setReturnMenu(boolean returnMenu){
        this.returnMenu=returnMenu;
    }

    public boolean isReturnMenu() {
        return returnMenu;
    }

    public boolean isQuitGame() {
        return quitGame;
    }

    public void init() {
        // Spawn initial
        // this.player = new Player(400, 800, 20, 3);
        entity.add(this.player);
        
        soundManager.playMusic("Sora no Kiseki FC OST - Sophisticated Fight.mp3", 0.2, true);
        // SoundManager.playMusic("Mystical Power Plant - 02 Alleyway of Roaring Waves.mp3", 0.3, true);
        // SoundManager.playMusic("The Boy Who Shattered Time (MitiS Remix).mp3", 0.3, true);

        // entity.removeIf(e -> e instanceof AbstractEnemyShooter);
        // SoundManager.playMusic("【東方虹龍洞】Touhou 18 OST - Fortunate Kitten (Mike Goutokujis Theme).mp3", 0.5, true);
        // spawnBoss();    

    }

    public List<AbstractEnemyShooter> getEnemies() {
        return enemies;
    }

    public void update(double deltaTime) {

        if(bossDead && timeBossDead>10){
            soundManager.stopMusic();
            returnMenu = true;
        }

        if (timeLastPause<=0 && input.isPause()) {
            soundManager.PauseMusic();
            soundManagerPause.playMusic("musique dascenseur.wav", 0.2, true);
            input.setAccepted(false);
            pause=true;
            timeLastPause=1;
        }
        if (pause) {
            timeLastUp -= deltaTime;
            timeLastDown -= deltaTime;
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
                        this.player.setHeart(3);
                        this.player.setBomb(3);
                        this.player.setAlive(true);
                        this.player.setScore(0);
                        this.player.setX(400);
                        this.player.setY(800);
                        // this.player.setSprite(new Image(getClass().getResourceAsStream("/sprites/player_2_detourer.png")));
                        // this.player.setSprite(new Image(getClass().getResourceAsStream("/sprites/sprite-256px-36(2).png")));
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
        }else{
            // variable qui garde le temp ecoulée
            timeSinceStart += deltaTime;
            timeSinceLastSpawn += deltaTime;
            timeSinceLastSpawn3 += deltaTime;
            timeSinceLastSpawnKunai3 +=deltaTime;
            timeAfterPlayerDead -= 1;
            timeLastBomb -=1;
            timeLastShootSound -=deltaTime;
            timeLastPause -=deltaTime;
            timeBossDead +=deltaTime;


            // Gére les différentes phase du stage1
            if (timeSinceStart < 48 || (timeSinceStart>60 && timeSinceStart<129)) {
                spawnEnemy(timeSinceLastSpawn3); 
                spawnEnemy2(timeSinceLastSpawn);
            }
            if (timeSinceStart >= 52 && timeSinceStart <=60) {
                spawnEnemy3(timeSinceLastSpawn); 
                spawnKunai2(timeSinceLastSpawnKunai3);
            }
            if (timeSinceStart >= 100 && timeSinceStart <128) {
                spawnEnemy4(timeSinceLastSpawn);
                spawnKunai3(timeSinceLastSpawnKunai3);           
            }
            if (timeSinceStart > 129  && bossSpawn == false) {
                    entity.removeIf(e -> e instanceof AbstractEnemyShooter);
                    soundManager.playMusic("【東方虹龍洞】Touhou 18 OST - Fortunate Kitten (Mike Goutokujis Theme).mp3", 0.3, true);
                    spawnBoss();
            }

            if (this.player != null) {
                this.player.setDirection(input.isUp(), input.isDown(), input.isLeft(), input.isRight());
                this.player.slowPlayer(input.isSlow());

                if (input.isShoot()) {
                    var bullets = this.player.shoot();
                    if (timeLastShootSound<0 && this.player.isAlive()){
                        soundManager.playSound("1760.wav", 0.2);
                        timeLastShootSound=0.1;
                    }
                    entity.addAll(bullets);
                }

                if (input.isBomb() && timeLastBomb <=0 && this.player.getBomb()>0 && this.player.isAlive()) {
                    System.out.println("oui");
                    System.out.println(this.player.getBomb());
                    entity.add(this.player.useBomb());
                    this.player.setBomb(this.player.getBomb()-1);
                    timeLastBomb=600;
                    // Supprimer toutes les balles ennemies immédiatement pour éviter les balles fantomes
                    entity.removeIf(e -> e instanceof AbstractBullet b && "enemy".equals(b.getOwnerType()));
                }
            }

            // gestion des tirs enemy

            if ((timeLastBomb<=0)) {
                for (AbstractEnemyShooter ene : enemies) {
                    if (!ene.isAlive() || !ene.getCanShoot()) continue;
                    var bullets = ene.shoot();
                    bullets.addAll(ene.shootWired(this.player.getX(), this.player.getY()));
                    entity.addAll(bullets);
                }
            }

            //initialisation de la liste des entité a supprimer
            List<Entity> toRemove = new ArrayList<>();

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
                        
                        this.player.setScore(this.player.getScore()+((AbstractEnemyShooter)e).getScore());
                    }
                    toRemove.add(e);
                }
            }

            // gestion des domages des bullets
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
                            player.setScore(player.getScore()+1);
                            break; // stop après un impact
                        }
                    }
                } else if ("enemy".equals(bullet.getOwnerType()) && timeAfterPlayerDead < 0) {
                    if (this.player != null && this.player.isAlive() && this.player.intersects(bullet)) {
                        this.player.takeDamage(bullet.getDamage());
                        bullet.takeDamage(1);
                    }
                }
            }

            // gestion de dommage lorsque le joueur entre en colision avec un enemy
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

            if (toRemove.contains(player)) {
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
            boolean bossRemove = toRemove.stream().anyMatch(e -> e instanceof Boss1);
            if(bossRemove){
                bossDead=true;
                timeBossDead=0;
            }
            boolean hasBombBullet = toRemove.stream().anyMatch(e -> e instanceof BombBullet);
            if (hasBombBullet) {
               for (Entity e : entity) {
                    if (e instanceof AbstractEnemyShooter enemy) {
                        enemy.takeDamage(250);
                    }
                }
            }     
            entity.removeAll(toRemove);
        }
    }


    private void spawnEnemy(double time) {
        if (time >= 3){

            //AJOUT LISTE ENEMIES (pour tirer) : enemies.add()
            //AJOUT LISTE ENEMIES : entity.add()

            //Enemy droite
            Enemy1Stage1 enemy1Stage1a = new Enemy1Stage1(800, 160);
            Enemy1Stage1 enemy1Stage1b = new Enemy1Stage1(800, 120);
            Enemy1Stage1 enemy1Stage1c = new Enemy1Stage1(800, 80);
            Enemy1Stage1 enemy1Stage1d = new Enemy1Stage1(800, 40);
            Enemy1Stage1 enemy1Stage1e = new Enemy1Stage1(800, 0);
            enemies.add(enemy1Stage1a);
            enemies.add(enemy1Stage1b);
            enemies.add(enemy1Stage1c);
            enemies.add(enemy1Stage1d);
            enemies.add(enemy1Stage1e);
            entity.add(enemy1Stage1a);
            entity.add(enemy1Stage1b);
            entity.add(enemy1Stage1c);
            entity.add(enemy1Stage1d);
            entity.add(enemy1Stage1e);

            //Enemy gauche
            Enemy2Stage1 enemy2Stage1a = new Enemy2Stage1(0, 160);
            Enemy2Stage1 enemy2Stage1b = new Enemy2Stage1(0, 120);
            Enemy2Stage1 enemy2Stage1c = new Enemy2Stage1(0, 80);
            Enemy2Stage1 enemy2Stage1d = new Enemy2Stage1(0, 40);
            Enemy2Stage1 enemy2Stage1e = new Enemy2Stage1(0, 0);
            enemies.add(enemy2Stage1a);
            enemies.add(enemy2Stage1b);
            enemies.add(enemy2Stage1c);
            enemies.add(enemy2Stage1d);
            enemies.add(enemy2Stage1e);
            entity.add(enemy2Stage1a);
            entity.add(enemy2Stage1b);
            entity.add(enemy2Stage1c);
            entity.add(enemy2Stage1d);
            entity.add(enemy2Stage1e);

            timeSinceLastSpawn3 = 0; // on remet le compteur à zéro
        }
    }

    private void spawnEnemy2(double time) {
        if (time >= 1){
                        
            //AJOUT LISTE ENEMIES (pour tirer) : enemies.add()
            //AJOUT LISTE ENEMIES : entity.add()

            //Enemy droite
            Enemy4Stage1 enemy4Stage1 = new Enemy4Stage1(825, 20);
            enemies.add(enemy4Stage1);
            entity.add(enemy4Stage1);
            //Enemy gauche
            Enemy3Stage1 enemy3Stage1 = new Enemy3Stage1(0, 60);
            enemies.add(enemy3Stage1);
            entity.add(enemy3Stage1);

            timeSinceLastSpawn = 0; // on remet le compteur à zéro
        }       
    }

    private void spawnEnemy3(double time) {
        if (time >= 1){
            
            //AJOUT LISTE ENEMIES (pour tirer) : enemies.add()
            //AJOUT LISTE ENEMIES : entity.add()

            //Enemy droite
            Enemy4Stage1 enemy4Stage1 = new Enemy4Stage1(800, 20);
            Enemy1Stage1 enemy1Stage1a = new Enemy1Stage1(800, 160);
            Enemy1Stage1 enemy1Stage1b = new Enemy1Stage1(800, 120);
            Enemy1Stage1 enemy1Stage1c = new Enemy1Stage1(800, 80);
            Enemy1Stage1 enemy1Stage1d = new Enemy1Stage1(800, 40);
            Enemy1Stage1 enemy1Stage1e = new Enemy1Stage1(800, 0);
            enemies.add(enemy4Stage1);
            enemies.add(enemy1Stage1a);
            enemies.add(enemy1Stage1b);
            enemies.add(enemy1Stage1c);
            enemies.add(enemy1Stage1d);
            enemies.add(enemy1Stage1e);
            entity.add(enemy4Stage1);
            entity.add(enemy1Stage1a);
            entity.add(enemy1Stage1b);
            entity.add(enemy1Stage1c);
            entity.add(enemy1Stage1d);
            entity.add(enemy1Stage1e);

            //Enemy gauche
            Enemy3Stage1 enemy3Stage1 = new Enemy3Stage1(0, 60);
            Enemy2Stage1 enemy2Stage1a = new Enemy2Stage1(0, 160);
            Enemy2Stage1 enemy2Stage1b = new Enemy2Stage1(0, 120);
            Enemy2Stage1 enemy2Stage1c = new Enemy2Stage1(0, 80);
            Enemy2Stage1 enemy2Stage1d = new Enemy2Stage1(0, 40);
            Enemy2Stage1 enemy2Stage1e = new Enemy2Stage1(0, 0);
            enemies.add(enemy3Stage1);
            enemies.add(enemy2Stage1a);
            enemies.add(enemy2Stage1b);
            enemies.add(enemy2Stage1c);
            enemies.add(enemy2Stage1d);
            enemies.add(enemy2Stage1e);
            entity.add(enemy3Stage1);
            entity.add(enemy2Stage1a);
            entity.add(enemy2Stage1b);
            entity.add(enemy2Stage1c);
            entity.add(enemy2Stage1d);
            entity.add(enemy2Stage1e);
            
            timeSinceLastSpawn = 0; // on remet le compteur à zéro
        }
    }
    private void spawnEnemy4(double time) {
        if (time >= 1){
            
            //AJOUT LISTE ENEMIES (pour tirer) : enemies.add()
            //AJOUT LISTE ENEMIES : entity.add()

            //Enemy droite
            Enemy4Stage1 enemy4Stage1 = new Enemy4Stage1(800, 20);
            Enemy1Stage1 enemy1Stage1a = new Enemy1Stage1(800, 160);
            Enemy1Stage1 enemy1Stage1b = new Enemy1Stage1(800, 120);
            Enemy1Stage1 enemy1Stage1c = new Enemy1Stage1(800, 80);
            Enemy1Stage1 enemy1Stage1d = new Enemy1Stage1(800, 40);
            Enemy1Stage1 enemy1Stage1e = new Enemy1Stage1(800, 0);
            enemies.add(enemy4Stage1);
            enemies.add(enemy1Stage1a);
            enemies.add(enemy1Stage1b);
            enemies.add(enemy1Stage1c);
            enemies.add(enemy1Stage1d);
            enemies.add(enemy1Stage1e);
            entity.add(enemy4Stage1);
            entity.add(enemy1Stage1a);
            entity.add(enemy1Stage1b);
            entity.add(enemy1Stage1c);
            entity.add(enemy1Stage1d);
            entity.add(enemy1Stage1e);
            //Enemy gauche
            Enemy3Stage1 enemy3Stage1 = new Enemy3Stage1(0, 60);
            Enemy2Stage1 enemy2Stage1a = new Enemy2Stage1(0, 160);
            Enemy2Stage1 enemy2Stage1b = new Enemy2Stage1(0, 120);
            Enemy2Stage1 enemy2Stage1c = new Enemy2Stage1(0, 80);
            Enemy2Stage1 enemy2Stage1d = new Enemy2Stage1(0, 40);
            Enemy2Stage1 enemy2Stage1e = new Enemy2Stage1(0, 0);
            enemies.add(enemy3Stage1);
            enemies.add(enemy2Stage1a);
            enemies.add(enemy2Stage1b);
            enemies.add(enemy2Stage1c);
            enemies.add(enemy2Stage1d);
            enemies.add(enemy2Stage1e);
            entity.add(enemy3Stage1);
            entity.add(enemy2Stage1a);
            entity.add(enemy2Stage1b);
            entity.add(enemy2Stage1c);
            entity.add(enemy2Stage1d);
            entity.add(enemy2Stage1e);
            
            timeSinceLastSpawn = 0; // on remet le compteur à zéro
        }
    }


    private void spawnKunai2(double time) {
        if (time >= 3  && cptKunai2>0){
                        
            //AJOUT LISTE ENEMIES (pour tirer) : enemies.add()
            //AJOUT LISTE ENEMIES : entity.add()

            //Enemy kunai
            EnemyKunai1 enemyKunai1a = new EnemyKunai1(267, 15);
            EnemyKunai1 enemyKunai1b = new EnemyKunai1(533, 15);
                                                             
            enemies.add(enemyKunai1a);
            enemies.add(enemyKunai1b);

            entity.add(enemyKunai1a);
            entity.add(enemyKunai1b);

            cptKunai2 -=1;
            timeSinceLastSpawnKunai3 = 0; // on remet le compteur à zéro
        }
    }

    private void spawnKunai3(double time) {
        if (time >= 3 && cptKunai3>0){
                        
            //AJOUT LISTE ENEMIES (pour tirer) : enemies.add()
            //AJOUT LISTE ENEMIES : entity.add()

            
            //Enemy kunai
            EnemyKunai1 enemyKunai1a = new EnemyKunai1(600, 15);
            EnemyKunai1 enemyKunai1b = new EnemyKunai1(400, 15);
            EnemyKunai1 enemyKunai1c = new EnemyKunai1(200, 15);
                                            
            enemies.add(enemyKunai1a);
            enemies.add(enemyKunai1b);
            enemies.add(enemyKunai1c);

            entity.add(enemyKunai1a);
            entity.add(enemyKunai1b);
            entity.add(enemyKunai1c);

            cptKunai3 -=1;
            timeSinceLastSpawnKunai3 = 0; // on remet le compteur à zéro
        }
    }

    private void spawnBoss(){
        Boss1 boss1 = new Boss1(250, 125);
        enemies.add(boss1);
        entity.add(boss1);
        bossSpawn=true;
    }

    @Override
    public boolean isFinished() {
        return quitGame || returnMenu;
    }

    @Override
    public void render(GraphicsContext gc) {

    gc.clearRect(0, 0, 800, 900);
    // gc.setFill(Color.GREY);
    // gc.fillRect(0, 0, 800, 900);

    
    // décalage des 2 background
    double offset = (timeSinceStart * scrollSpeed) % canvas.getHeight();

    // l'image est déssiner 2 fois pour éviter les jump de background
    gc.drawImage(background, 0, -offset, canvas.getWidth(), canvas.getHeight());
    gc.drawImage(background, 0, canvas.getHeight() - offset, canvas.getWidth(), canvas.getHeight());


    for (Entity e : entity) {
        e.render(gc);
    }

    if(pause){
        
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
    if(bossDead && timeBossDead<=3){
        gc.setFill(Color.BLACK);
        gc.fillText("STAGE CLEAR", 220, 900-(230*timeBossDead));
    }else if(bossDead && timeBossDead>3){
        gc.setFill(Color.BLACK);
        gc.fillText("STAGE CLEAR", 220, 900-(230*3));
    }
}
}