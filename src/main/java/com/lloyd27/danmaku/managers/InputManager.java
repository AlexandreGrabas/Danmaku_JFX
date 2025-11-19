package com.lloyd27.danmaku.managers;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class InputManager {
    private boolean up, down, left, right, shoot, slowPlayer, bombPlayer, pause, accepted, canceled, swap;
    public boolean isSlow() { return slowPlayer; }

    public InputManager(Scene scene) {
        setupKeyListeners(scene);
    }

    public void setAccepted(boolean accepted){
        this.accepted=accepted;
    }

    public void setCanceled(boolean canceled){
        this.canceled=canceled;
    }
    
    public void setSwap(boolean swap){
        this.swap=swap;
    }

    private void setupKeyListeners(Scene scene) {
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP -> up = true;
                case DOWN -> down = true;
                case LEFT -> left = true;
                case RIGHT -> right = true;
                case SHIFT -> slowPlayer = true;
                case ESCAPE -> pause = true;
                case C -> swap =(swap) ? false:true;
                case W -> {
                    shoot = true;
                    accepted=(accepted) ? false:true;
                }
                case X -> {
                    bombPlayer = true;
                    canceled=(canceled) ? false:true;
                }
                // case ESCAPE -> pause=((pause==true) ? false:true);
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
                case X -> bombPlayer = false;
                case ESCAPE -> pause = false;
            }
        });
    }

    // Getters pour le jeu
    public boolean isUp() { return up; }
    public boolean isDown() { return down; }
    public boolean isLeft() { return left; }
    public boolean isRight() { return right; }
    public boolean isShoot() { return shoot; }
    public boolean isAccepted() { return accepted;}
    public boolean isCanceled() { return canceled;}
    public boolean isBomb() { return bombPlayer; }
    public boolean isPause() { return pause; }
    public boolean isSwap() { return swap; }
    // public boolean isPause(SoundManager soundManagerStage) { 
    //     if(pause){
    //         soundManagerStage.PauseMusic();
    //     }else{
    //         soundManagerStage.UnPauseMusic();
    //     }
    //     return pause; 
    // }
}
