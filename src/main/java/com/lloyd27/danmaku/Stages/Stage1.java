package com.lloyd27.danmaku.Stages;

import java.util.ArrayList;
import java.util.List;

import com.lloyd27.danmaku.entity.Entity;
import com.lloyd27.danmaku.entity.Player;
import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.Enemy.AbstractEnemyShooter;
import com.lloyd27.danmaku.entity.Enemy.Boss1;
import com.lloyd27.danmaku.entity.Enemy.Enemy1Stage1;
import com.lloyd27.danmaku.entity.Enemy.Enemy2Stage1;
import com.lloyd27.danmaku.entity.Enemy.Enemy3Stage1;
import com.lloyd27.danmaku.entity.Enemy.Enemy4Stage1;
import com.lloyd27.danmaku.entity.Enemy.EnemyKunai1;
import com.lloyd27.danmaku.managers.InputManager;
import com.lloyd27.danmaku.managers.SoundManager;

public class Stage1 {
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

    public Stage1(InputManager inputManager) {
        this.input=inputManager;
    }

    public Player getPlayer(){
        return this.player;
    }

    public List<Entity> getEntity(){
        return this.entity;
    }

    public void init() {
        // Spawn initial
        this.player = new Player(400, 800, 5, 3);
        entity.add(this.player);
        
        SoundManager.playMusic("Sora no Kiseki FC OST - Sophisticated Fight.mp3", 0.5, true);
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
        // variable qui garde le temp ecoulée
        timeSinceStart += deltaTime;
        timeSinceLastSpawn += deltaTime;
        timeSinceLastSpawn3 += deltaTime;
        timeSinceLastSpawnKunai3 +=deltaTime;
        timeAfterPlayerDead -= 1;
        timeLastBomb -=1;
        timeLastShootSound -=deltaTime;


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
                SoundManager.playMusic("【東方虹龍洞】Touhou 18 OST - Fortunate Kitten (Mike Goutokujis Theme).mp3", 0.5, true);
                spawnBoss();
        }

        if (this.player != null) {
            this.player.setDirection(input.isUp(), input.isDown(), input.isLeft(), input.isRight());
            this.player.slowPlayer(input.isSlow());

            if (input.isShoot()) {
                var bullets = this.player.shoot();
                if (timeLastShootSound<0 && this.player.isAlive()){
                     SoundManager.playSound("1760.wav", 0.3);
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
            double bomb = this.player.getBomb();
            heart -= 1;
            if (heart > 0) {
                this.player.setAlive(true);
                this.player.setHeart(heart);
                this.player.setX(400);
                this.player.setY(800);
                timeAfterPlayerDead = 600;
            } else {
                entity.removeAll(toRemove);
            }
        } else {
            entity.removeAll(toRemove);
        }

    }


    private void spawnEnemy(double time) {
        if (time >= 3){

            //AJOUT LISTE ENEMIES (pour tirer) : enemies.add()
            //AJOUT LISTE ENEMIES : entity.add()

            //Enemy droite
            Enemy1Stage1 enemy1Stage1a = new Enemy1Stage1(900, 100);
            Enemy1Stage1 enemy1Stage1b = new Enemy1Stage1(900, 75);
            Enemy1Stage1 enemy1Stage1c = new Enemy1Stage1(900, 50);
            Enemy1Stage1 enemy1Stage1d = new Enemy1Stage1(900, 25);
            Enemy1Stage1 enemy1Stage1e = new Enemy1Stage1(900, 0);
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
            Enemy2Stage1 enemy2Stage1a = new Enemy2Stage1(0, 100);
            Enemy2Stage1 enemy2Stage1b = new Enemy2Stage1(0, 75);
            Enemy2Stage1 enemy2Stage1c = new Enemy2Stage1(0, 50);
            Enemy2Stage1 enemy2Stage1d = new Enemy2Stage1(0, 25);
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
            Enemy4Stage1 enemy4Stage1 = new Enemy4Stage1(900, 15);
            enemies.add(enemy4Stage1);
            entity.add(enemy4Stage1);
            //Enemy gauche
            Enemy3Stage1 enemy3Stage1 = new Enemy3Stage1(0, 40);
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
            Enemy4Stage1 enemy4Stage1 = new Enemy4Stage1(900, 15);
            Enemy1Stage1 enemy1Stage1a = new Enemy1Stage1(900, 100);
            Enemy1Stage1 enemy1Stage1b = new Enemy1Stage1(900, 75);
            Enemy1Stage1 enemy1Stage1c = new Enemy1Stage1(900, 50);
            Enemy1Stage1 enemy1Stage1d = new Enemy1Stage1(900, 25);
            Enemy1Stage1 enemy1Stage1e = new Enemy1Stage1(900, 0);
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
            Enemy3Stage1 enemy3Stage1 = new Enemy3Stage1(0, 40);
            Enemy2Stage1 enemy2Stage1a = new Enemy2Stage1(0, 100);
            Enemy2Stage1 enemy2Stage1b = new Enemy2Stage1(0, 75);
            Enemy2Stage1 enemy2Stage1c = new Enemy2Stage1(0, 50);
            Enemy2Stage1 enemy2Stage1d = new Enemy2Stage1(0, 25);
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
            Enemy4Stage1 enemy4Stage1 = new Enemy4Stage1(900, 15);
            Enemy1Stage1 enemy1Stage1a = new Enemy1Stage1(900, 100);
            Enemy1Stage1 enemy1Stage1b = new Enemy1Stage1(900, 75);
            Enemy1Stage1 enemy1Stage1c = new Enemy1Stage1(900, 50);
            Enemy1Stage1 enemy1Stage1d = new Enemy1Stage1(900, 25);
            Enemy1Stage1 enemy1Stage1e = new Enemy1Stage1(900, 0);
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
            Enemy3Stage1 enemy3Stage1 = new Enemy3Stage1(0, 40);
            Enemy2Stage1 enemy2Stage1a = new Enemy2Stage1(0, 100);
            Enemy2Stage1 enemy2Stage1b = new Enemy2Stage1(0, 75);
            Enemy2Stage1 enemy2Stage1c = new Enemy2Stage1(0, 50);
            Enemy2Stage1 enemy2Stage1d = new Enemy2Stage1(0, 25);
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
        Boss1 boss1 = new Boss1(250, 100);
        enemies.add(boss1);
        entity.add(boss1);
        bossSpawn=true;
    }
}