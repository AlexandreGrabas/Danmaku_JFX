package com.lloyd27.danmaku.managers;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class InputManager {
    private boolean up, down, left, right, shoot, slowPlayer;
    public boolean isSlow() { return slowPlayer; }

    public InputManager(Scene scene) {
        setupKeyListeners(scene);
    }

    private void setupKeyListeners(Scene scene) {
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP -> up = true;
                case DOWN -> down = true;
                case LEFT -> left = true;
                case RIGHT -> right = true;
                case W -> shoot = true;
                case SHIFT -> slowPlayer = true;
            }
        });

        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case UP -> up = false;
                case DOWN -> down = false;
                case LEFT -> left = false;
                case RIGHT -> right = false;
                case W -> shoot = false;
                case SHIFT -> slowPlayer = false;
            }
        });
    }

    // Getters pour le jeu
    public boolean isUp() { return up; }
    public boolean isDown() { return down; }
    public boolean isLeft() { return left; }
    public boolean isRight() { return right; }
    public boolean isShoot() { return shoot; }
}
