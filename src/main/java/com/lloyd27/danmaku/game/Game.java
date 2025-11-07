package com.lloyd27.danmaku.game;

import com.lloyd27.danmaku.Stages.*;
import com.lloyd27.danmaku.managers.InputManager;
import com.lloyd27.danmaku.rendering.Renderer;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;

public class Game {
    private Renderer renderer;
    private InputManager input;
    private AbstractStage currentStage;
    private long lastTime = 0;

    public Game(Canvas canvas, Scene scene) {
        this.renderer = new Renderer(canvas);
        this.input = new InputManager(scene);

        currentStage = new Menu(input);
        currentStage.init();
    }

    public void start() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime == 0) lastTime = now;
                double deltaTime = (now - lastTime) / 1e9;
                lastTime = now;

                currentStage.update(deltaTime);
                renderer.clear();
                currentStage.render(renderer.getGraphicsContext());

                if (currentStage.isFinished()) {
                    handleStageTransition();
                    lastTime = 0;
                }
            }
        }.start();
    }

    private void handleStageTransition() {
        lastTime=0;
        if (currentStage instanceof Menu menu) {
            if (menu.isStartGame()) {
                currentStage = new Stage1(input);
                currentStage.init();
            } else if (menu.isQuitGame()) {
                System.exit(0);
            }
        }
        else if (currentStage instanceof Stage1 stage1) {
            if (stage1.isReturnMenu()) { 
                currentStage = new Menu(input);
                currentStage.init();
            } else if (stage1.isQuitGame()) {
                System.exit(0);
            }
        }
    }
}
