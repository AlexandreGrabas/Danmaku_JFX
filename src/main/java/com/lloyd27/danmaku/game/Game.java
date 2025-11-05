package com.lloyd27.danmaku.game;

import com.lloyd27.danmaku.entity.Entity;
import com.lloyd27.danmaku.entity.Player;
import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.Enemy.AbstractEnemyShooter;
import com.lloyd27.danmaku.entity.Enemy.Boss1;
import com.lloyd27.danmaku.entity.Enemy.Enemy1;
import com.lloyd27.danmaku.entity.Enemy.Enemy1Stage1;
import com.lloyd27.danmaku.entity.Enemy.Enemy2;
import com.lloyd27.danmaku.entity.Enemy.Enemy2Stage1;
import com.lloyd27.danmaku.entity.Enemy.EnemyWired1;
import com.lloyd27.danmaku.entity.Enemy.EnemyWired2;
import com.lloyd27.danmaku.managers.InputManager;
import com.lloyd27.danmaku.rendering.Renderer;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Entity> entity = new ArrayList<>();
    private Renderer renderer;
    private Scene scene;
    private InputManager input;
    private Player player;
    private List<AbstractEnemyShooter> enemies = new ArrayList<>();
    private double timeSinceLastSpawn = 0;
    private double timeAfterPlayerDead = 0;
    private final double SPAWN_INTERVAL = 3;

    public Game(Canvas canvas, Scene scene) {
        this.renderer = new Renderer(canvas);
        this.scene = scene;
        this.input = new InputManager(scene);
        init();
    }

    private void init() {
        player = new Player(400, 800, 3);
        entity.add(player);

        // enemies.add(new Boss1(250, 50));
        // entity.addAll(enemies);

    }

    public void start() {
        new AnimationTimer() {
            long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0)
                    lastTime = now;
                double deltaTime = (now - lastTime) / 1e9;
                lastTime = now;

                update(deltaTime);
                renderer.render(entity);
            }
        }.start();
    }

    private void update(double deltaTime) {
        // variable qui garde le temp ecoulée
        timeSinceLastSpawn += deltaTime;
        timeAfterPlayerDead -= 1;

        // ajoute des enemie tout les x seconde
        if (timeSinceLastSpawn >= SPAWN_INTERVAL) {
            spawnEnemy();
            timeSinceLastSpawn = 0; // on remet le compteur à zéro
        }

        if (player != null) {
            player.setDirection(input.isUp(), input.isDown(), input.isLeft(), input.isRight());
            player.slowPlayer(input.isSlow());

            if (input.isShoot()) {
                var bullets = player.shoot();
                entity.addAll(bullets);
            }
        }

        // gestion des tirs enemy

        for (AbstractEnemyShooter ene : enemies) {
            var bullets = ene.shoot();
            bullets.addAll(ene.shootWired(player.getX(), player.getY()));
            entity.addAll(bullets);
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

        // gestion des domages
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
                if (player != null && player.isAlive() && player.intersects(bullet)) {
                    player.takeDamage(bullet.getDamage());
                    bullet.takeDamage(1);
                }
            }
        }

        for (Entity e : entity) {
            if (!(e instanceof AbstractEnemyShooter enemyShooter) || !enemyShooter.isAlive())
                continue;
            
            if (player != null && player.isAlive() && player.intersects(enemyShooter)) {
                if(timeAfterPlayerDead<=0){
                    player.takeDamage(1);
                    enemyShooter.takeDamage(15);
                }
            }
        }

        if (toRemove.contains(player)) {
            double heart = player.getHeart();
            heart -= 1;
            if (heart > 0) {
                entity.removeAll(toRemove);
                player = new Player(400, 800, heart);
                entity.add(player);
                timeAfterPlayerDead = 600;
            } else {
                entity.removeAll(toRemove);
            }
        } else {
            entity.removeAll(toRemove);
        }

    }

    private void spawnEnemy() {
        Enemy1 enemy = new Enemy1(0, 100);
        Enemy2 enemy2 = new Enemy2(900, 50);
        EnemyWired1 enemy3 = new EnemyWired1(0, 25);
        EnemyWired2 enemy4 = new EnemyWired2(900, 125);
        //Enemy droite
        Enemy1Stage1 enemy1Stage1a = new Enemy1Stage1(900, 100);
        Enemy1Stage1 enemy1Stage1b = new Enemy1Stage1(900, 75);
        Enemy1Stage1 enemy1Stage1c = new Enemy1Stage1(900, 50);
        Enemy1Stage1 enemy1Stage1d = new Enemy1Stage1(900, 25);
        Enemy1Stage1 enemy1Stage1e = new Enemy1Stage1(900, 0);
        //Enemy gauche
        Enemy2Stage1 enemy2Stage1a = new Enemy2Stage1(0, 100);
        Enemy2Stage1 enemy2Stage1b = new Enemy2Stage1(0, 75);
        Enemy2Stage1 enemy2Stage1c = new Enemy2Stage1(0, 50);
        Enemy2Stage1 enemy2Stage1d = new Enemy2Stage1(0, 25);
        Enemy2Stage1 enemy2Stage1e = new Enemy2Stage1(0, 0);

        enemies.add(enemy);
        enemies.add(enemy2);
        // enemies.add(enemy3);
        // enemies.add(enemy4);
        enemies.add(enemy1Stage1a);
        enemies.add(enemy1Stage1b);
        enemies.add(enemy1Stage1c);
        enemies.add(enemy1Stage1d);
        enemies.add(enemy1Stage1e);

        enemies.add(enemy2Stage1a);
        enemies.add(enemy2Stage1b);
        enemies.add(enemy2Stage1c);
        enemies.add(enemy2Stage1d);
        enemies.add(enemy2Stage1e);
        
        //ajouter les enemies 1 par 1 pour eviter de booster en vitesse les anscien enemies 
        entity.add(enemy);
        entity.add(enemy2);
        // entity.add(enemy3);
        // entity.add(enemy4);
        
        entity.add(enemy1Stage1a);
        entity.add(enemy1Stage1b);
        entity.add(enemy1Stage1c);
        entity.add(enemy1Stage1d);
        entity.add(enemy1Stage1e);

        entity.add(enemy2Stage1a);
        entity.add(enemy2Stage1b);
        entity.add(enemy2Stage1c);
        entity.add(enemy2Stage1d);
        entity.add(enemy2Stage1e);
    }

}
