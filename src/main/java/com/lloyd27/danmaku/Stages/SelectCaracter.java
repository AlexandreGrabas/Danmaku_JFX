package com.lloyd27.danmaku.Stages;

import java.util.ArrayList;
import java.util.List;
import com.lloyd27.danmaku.entity.Entity;
import com.lloyd27.danmaku.entity.Player;
import com.lloyd27.danmaku.entity.PlayerEllie;
import com.lloyd27.danmaku.entity.PlayerErwin;
import com.lloyd27.danmaku.managers.InputManager;
import com.lloyd27.danmaku.managers.SoundManager;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SelectCaracter extends AbstractStage {
    private Player player;
    private boolean startGame = false;
    private boolean returnMenu = false;
    private long index=0;
    private InputManager input;
    private List<Entity> entity = new ArrayList<>();
    private SoundManager soundManager = new SoundManager();
    private double timeLastUp = 0;
    private double timeLastDown = 0;
    private Image ellie=new Image(getClass().getResourceAsStream("/sprites/Ellie.png"));
    private Image erwin=new Image(getClass().getResourceAsStream("/sprites/Erwin.png"));;

    public SelectCaracter(InputManager input, Canvas canvas, Player player, SoundManager soundManager) {
        this.input = input;
        this.canvas = canvas;
        this.hudCanvas = null;
        this.player=player;
        this.soundManager=soundManager;
        canvas.setWidth(950);
    }

    @Override
    public void init() {
        // On ne redémarre pas la musique si elle tourne déjà
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
        if (input.isPause()){
            returnMenu=true;
        }
        if (timeLastUp<=0 && input.isLeft()) {
            index -= 1;
            timeLastUp = 0.2;
            if(index==-1){index=1;}
        }
        if (timeLastDown<=0 && input.isRight()) {
            index += 1;
            timeLastDown = 0.2;
            if(index==2){index=0;}
        };

        // validation
        if (input.isAccepted()) {
            if (index == 0) {
                player=new PlayerEllie(400, 800, 4, 3);
                startGame = true;
                soundManager.stopMusic();
            } else {
                player=new PlayerErwin(400, 800, 4, 3);
                startGame = true;
                soundManager.stopMusic();
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
        gc.fillText("Select character", 125, 100);
        gc.setFont(new Font("Arial", 50));
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        if (index==0){            
            gc.setFill(Color.rgb(0, 90, 0,1));
            gc.fillText("Ellie", 410, 200);
            gc.drawImage(ellie, 260, 260,400,600);
        } else if(index==1){
            gc.setFill(Color.DARKORANGE);
            gc.fillText("Erwin", 410, 200);
            gc.drawImage(erwin, 330, 260,400,600);
        }
        gc.setLineWidth(10.0);
        gc.strokeLine(800, 450, 850, 500);
        gc.strokeLine(850, 500, 800, 550);
        gc.strokeLine(150, 450, 100, 500);
        gc.strokeLine(100, 500, 150, 550);
    }

    @Override
    public List<Entity> getEntity() {
        return entity;
    }

    @Override
    public boolean isFinished() {
        return startGame || returnMenu;
    }

    public void setStartGame(boolean startGame){
        this.startGame=startGame;
    }

    public boolean isStartGame() {
        return startGame;
    }

    public boolean isReturnMenu() {
        return returnMenu;
    }
    
    public Player getPlayer() {
        return player;
    }
}
