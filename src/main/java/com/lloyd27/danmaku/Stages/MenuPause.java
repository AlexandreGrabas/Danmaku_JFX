package com.lloyd27.danmaku.Stages;

import java.util.ArrayList;
import java.util.List;

import com.lloyd27.danmaku.entity.Entity;
import com.lloyd27.danmaku.managers.InputManager;
import com.lloyd27.danmaku.managers.SoundManager;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MenuPause extends AbstractStage{
    private boolean startGame = false;
    private boolean quitGame = false;
    private SoundManager soundManager = new SoundManager();
    private InputManager input;
    private List<Entity> entity = new ArrayList<>();
    private long index=0;

    @Override
    public void init() {
        soundManager.playMusic("musique dascenseur.mp3", 0.1, true);
    }

    @Override
    public void render(GraphicsContext gc) {
        
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 900, 900);

        gc.setFill(Color.WHITE);
        gc.fillText("DANMAKU PROJECT", 345, 200);

        gc.setFill(index == 0 ? Color.WHITE : Color.GRAY);
        gc.fillText("Continue", 370, 400);

        gc.setFill(index == 1 ? Color.WHITE : Color.GRAY);
        gc.fillText("Menu", 385, 450);
    }

    @Override
    public void update(double deltaTime) {
        // navigation
        if (input.isUp()) index = 0;
        if (input.isDown()) index = 1;

        // validation
        if (input.isShoot()) {
            if (index == 0) {
                startGame = true;
                soundManager.stopMusic();
            } else {
                quitGame = true;
            }
        }
    }

    @Override
    public List<Entity> getEntity() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEntity'");
    }

    @Override
    public boolean isFinished() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isFinished'");
    }

}
