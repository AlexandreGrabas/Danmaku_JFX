package com.lloyd27.danmaku.Stages;

import java.util.ArrayList;
import java.util.List;
import com.lloyd27.danmaku.entity.Entity;
import com.lloyd27.danmaku.managers.InputManager;
import com.lloyd27.danmaku.managers.SoundManager;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Menu extends AbstractStage {
    private boolean startGame = false;
    private boolean quitGame = false;
    private long index=0;
    private InputManager input;
    private List<Entity> entity = new ArrayList<>();
    private SoundManager soundManager;
    private double timeLastUp = 0;
    private double timeLastDown = 0;

    public Menu(InputManager input, Canvas canvas, SoundManager soundManager) {
        this.input = input;
        this.canvas = canvas;
        this.hudCanvas = null;
        this.soundManager=soundManager;
        canvas.setWidth(950);
    }

    @Override
    public void init() {
        // Ne rejoue la musique que si elle n’est pas déjà en cours
        if (!soundManager.isMusicPlaying()) {
            soundManager.playMusic("The Boy Who Shattered Time (MitiS Remix).mp3", 0.1, true);
        }
        input.setAccepted(false);
    }

    @Override
    public void update(double deltaTime) {
        timeLastUp -= deltaTime;
        timeLastDown -= deltaTime;

        // navigation
        if (timeLastUp<=0 && input.isUp()) {
            index -= 1;
            timeLastUp = 0.2;
            if(index==-1){index=1;}
        }
        if (timeLastDown<=0 && input.isDown()) {
            index += 1;
            timeLastDown = 0.2;
            if(index==2){index=0;}
        };

        // validation
        if (input.isAccepted()) {
            if (index == 0) {
                startGame = true;
            } else {
                quitGame = true;
            }
        }
    }


    @Override
    public void render(GraphicsContext gc) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        gc.setFont(new Font("Arial", 95));
        gc.setFill(Color.WHITE);
        gc.fillText("DANMAKU PROJECT", 1, 200);

        gc.setFont(new Font("Arial", 50));
        gc.setFill(index == 0 ? Color.WHITE : Color.GRAY);
        gc.fillText("Start Game", 355, 400);

        gc.setFill(index == 1 ? Color.WHITE : Color.GRAY);
        gc.fillText("Quit", 425, 500);

    }

    @Override
    public List<Entity> getEntity() {
        return entity;
    }

    @Override
    public boolean isFinished() {
        return startGame || quitGame;
    }

    public void setStartGame(boolean startGame){
        this.startGame=startGame;
    }

    public boolean isStartGame() {
        return startGame;
    }

    public boolean isQuitGame() {
        return quitGame;
    }
}
