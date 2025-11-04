package com.lloyd27.danmaku.game;
import com.lloyd27.danmaku.entity.Entity;
import com.lloyd27.danmaku.entity.Player;
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

    public Game(Canvas canvas, Scene scene) {
        this.renderer = new Renderer(canvas);
        this.scene = scene;
        this.input = new InputManager(scene);
        init();
    }

    private void init() {
        player = new Player(400, 500);
        entity.add(player);
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
        
        if (player != null) {
            player.setDirection(input.isUp(), input.isDown(), input.isLeft(), input.isRight());
            player.slowPlayer(input.isSlow());

            if (input.isShoot()) {
                var bullets = player.shoot();
                entity.addAll(bullets);
            }
        }

        List<Entity> toRemove = new ArrayList<>();
        for (Entity e : entity) {
            e.update(deltaTime);
            if (e instanceof com.lloyd27.danmaku.entity.Bullet.AbstractBullet b && b.isOffScreen()) {
                toRemove.add(e);
            }
        }
        entity.removeAll(toRemove);
    }
}
