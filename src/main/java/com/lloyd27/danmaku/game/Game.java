package com.lloyd27.danmaku.game;
import com.lloyd27.danmaku.entity.AbstractEnemyShooter;
import com.lloyd27.danmaku.entity.Boss1;
import com.lloyd27.danmaku.entity.Enemy1;
import com.lloyd27.danmaku.entity.Enemy2;
import com.lloyd27.danmaku.entity.EnemyWired1;
import com.lloyd27.danmaku.entity.EnemyWired2;
import com.lloyd27.danmaku.entity.Entity;
import com.lloyd27.danmaku.entity.Player;
import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
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
        player = new Player(400, 800,3);
        entity.add(player);

        enemies.add(new Boss1(250, 50));
        entity.addAll(enemies);

    }

    public void start() {
        new AnimationTimer() {
            long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) lastTime = now;
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
        timeAfterPlayerDead -=1;

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


        List<Entity> toRemove = new ArrayList<>();
        for (Entity e : entity) {
            e.update(deltaTime);
            if (e instanceof AbstractBullet b && b.isOffScreen()) {
                toRemove.add(e);
            }
            if (e instanceof AbstractEnemyShooter b && b.isOffScreen()) {
                toRemove.add(e);
            }   
            if(e.isAlive()==false && !toRemove.contains(e)){
                toRemove.add(e);
            }
        }
        for (Entity e : entity) {
            if (!(e instanceof AbstractBullet bullet) || !bullet.isAlive()) continue;

            if ("player".equals(bullet.getOwnerType())) {
                for (AbstractEnemyShooter enemy : enemies) {
                    if (!enemy.isAlive()) continue;
                    if (enemy.intersects(bullet)) {
                        enemy.takeDamage(bullet.getDamage());
                        bullet.takeDamage(1); // supprime la balle après impact (car 1 pv)
                        break; // stop après un impact
                    }
                }
            } else if ("enemy".equals(bullet.getOwnerType()) && timeAfterPlayerDead<0) {
                if (player != null && player.isAlive() && player.intersects(bullet)) {
                    player.takeDamage(bullet.getDamage());
                    bullet.takeDamage(1);
                }
            }
        }
        if (toRemove.contains(player)){
            double heart = player.getHeart();
            heart -= 1;
            System.err.println(heart);
            if (heart>0){
                entity.removeAll(toRemove);
                player = new Player(400, 800,heart);
                entity.add(player);
                timeAfterPlayerDead=600;
            }else{
                entity.removeAll(toRemove);
                }
        }else {
            entity.removeAll(toRemove);
        }

    }

    private void spawnEnemy() {
        Enemy1 enemy = new Enemy1(0, 100);
        Enemy2 enemy2 = new Enemy2(900, 50);
        EnemyWired1 enemy3 = new EnemyWired1(0, 25);
        EnemyWired2 enemy4 = new EnemyWired2(900, 125);

        enemies.add(enemy);
        entity.add(enemy);
        enemies.add(enemy2);
        entity.add(enemy2);
        enemies.add(enemy3);
        entity.add(enemy3);
        enemies.add(enemy4);
        entity.add(enemy4);
    }

}


