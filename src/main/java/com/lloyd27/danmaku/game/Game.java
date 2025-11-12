package com.lloyd27.danmaku.game;

import com.lloyd27.danmaku.Stages.*;
import com.lloyd27.danmaku.entity.Player;
import com.lloyd27.danmaku.managers.InputManager;
import com.lloyd27.danmaku.managers.SoundManager;
import com.lloyd27.danmaku.rendering.HUDRenderer;
import com.lloyd27.danmaku.rendering.Renderer;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Game {
    private Player player;
    private Renderer renderer;
    private HUDRenderer hudRenderer;
    private InputManager input;
    private AbstractStage currentStage;
    private long lastTime = 0;
    private HBox root;
    private Canvas hudCanvas;
    private final SoundManager soundManager = new SoundManager();

    public Game(Canvas canvas, Canvas hudCanvas, Scene scene, HBox root) {
        this.renderer = new Renderer(canvas);
        this.hudRenderer = new HUDRenderer(hudCanvas);

        this.input = new InputManager(scene);
        this.root = root;
        this.hudCanvas = hudCanvas;

        // Stage initial : Menu
        currentStage = new Menu(input, renderer.getCanvas(),soundManager);
        currentStage.init();
        hudCanvas.setVisible(false); // HUD invisible au départ
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
                hudRenderer.clear();

                // Appel correct de render avec GraphicsContext
                currentStage.render(renderer.getGraphicsContext());

                // Affiche HUD uniquement si Stage1
                if (currentStage instanceof Stage1 stage1) {
                    hudRenderer.drawHUD(stage1.getPlayer());
                }

                if (currentStage.isFinished()) {
                    handleStageTransition();
                    lastTime = 0;
                }
            }
        }.start();
    }

    private void handleStageTransition() {
        lastTime = 0;
        Stage stage = (Stage) root.getScene().getWindow();

        if (currentStage instanceof Menu menu) {
            if (menu.isStartGame()) {
                currentStage = new SelectCaracter(input, renderer.getCanvas(),player,soundManager);
                currentStage.init();
                
                // Redimensionner
                stage.sizeToScene();

            } else if (menu.isQuitGame()) {
                System.exit(0);
            }
        }
        else if (currentStage instanceof SelectCaracter selectCaracter) {
            if (selectCaracter.isReturnMenu()) {
                currentStage = new Menu(input, renderer.getCanvas(),soundManager);
                currentStage.init();

                // Supprime le HUD
                root.getChildren().remove(hudCanvas);

                // Redimensionner
                stage.sizeToScene();
            } else if (selectCaracter.isStartGame()) {

                player = selectCaracter.getPlayer();
                currentStage = new Stage1(input, renderer.getCanvas(),player);
                currentStage.init();

                // Ajoute le HUD
                root.getChildren().add(0, hudCanvas);
                hudCanvas.setVisible(true);

                // Redimensionner
                stage.sizeToScene();
            }
        }
        else if (currentStage instanceof Stage1 stage1) {
            if (stage1.isReturnMenu()) {
                currentStage = new Menu(input, renderer.getCanvas(),soundManager);
                currentStage.init();

                // Supprime le HUD
                root.getChildren().remove(hudCanvas);

                // Redimensionner
                stage.sizeToScene();

            } else if (stage1.isQuitGame()) {
                System.exit(0);
            }
        }
    }
}
