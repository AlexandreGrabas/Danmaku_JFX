package com.lloyd27.danmaku.game;

import com.lloyd27.danmaku.Stages.Stage1;
import com.lloyd27.danmaku.managers.InputManager;
import com.lloyd27.danmaku.rendering.Renderer;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;

public class Game {
    private Stage1 stage1;
    private Renderer renderer;
    private Scene scene;
    private InputManager input;



    public Game(Canvas canvas, Scene scene) {
        this.renderer = new Renderer(canvas);
        this.scene = scene;
        this.input = new InputManager(scene);
        init();
    }

    private void init() {
        stage1 = new Stage1(input);
        stage1.init();
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
                renderer.render(stage1.getEntity());
            }
        }.start();
    }

    private void update(double deltaTime) {
        stage1.update(deltaTime);

    }
}
