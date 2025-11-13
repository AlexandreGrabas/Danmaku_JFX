package com.lloyd27.danmaku.Stages;

import java.util.ArrayList;
import java.util.List;
import com.lloyd27.danmaku.entity.Entity;
import com.lloyd27.danmaku.entity.Player;
import com.lloyd27.danmaku.entity.TableauScore;
import com.lloyd27.danmaku.managers.InputManager;
import com.lloyd27.danmaku.managers.SoundManager;
import com.lloyd27.danmaku.managers.TableauScoresManager;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class StageTableauScore extends AbstractStage {
    private Player player;
    private boolean returnMenu = false;
    private InputManager input;
    private List<Entity> entity = new ArrayList<>();
    private SoundManager soundManager = new SoundManager();
    private int index=0;
    private double timeLastUp = 0;
    private double timeLastDown = 0;
    private List<TableauScore> scores = new ArrayList<>();
    private TableauScoresManager tableauScoresManager=new TableauScoresManager();

    public StageTableauScore(InputManager input, Canvas canvas, SoundManager soundManager) {
        this.input = input;
        this.canvas = canvas;
        this.hudCanvas = null;
        this.soundManager=soundManager;
        canvas.setWidth(950);
    }

    @Override
    public void init() {
        soundManager.stopMusic();
        if (!soundManager.isMusicPlaying()) {
            soundManager.playMusic("musique dascenseur.wav", 0.2, true);
        }
        input.setAccepted(false);
        // Charger les scores sauvegardés
        scores = tableauScoresManager.charger(getPersonnageActuel());
    }

    @Override
    public void update(double deltaTime) {
        // navigation
        if (input.isPause()){
            returnMenu=true;
        }

        timeLastUp -= deltaTime;
        timeLastDown -= deltaTime;

        // navigation
        if (input.isPause()){
            returnMenu=true;
        }
        if (timeLastUp<=0 && input.isLeft()) {
            index -= 1;
            timeLastUp = 0.2;
            if(index==-1){index=2;}
            scores = tableauScoresManager.charger(getPersonnageActuel());
        }
        if (timeLastDown<=0 && input.isRight()) {
            index += 1;
            timeLastDown = 0.2;
            if(index==3){index=0;}
            scores = tableauScoresManager.charger(getPersonnageActuel());
        };
    }


    @Override
    public void render(GraphicsContext gc) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFont(new Font("Arial", 95));
        gc.setFill(Color.WHITE);
        gc.fillText("Tableau des Score", 70, 100);
        gc.setFont(new Font("Arial", 36));
        
        if(index==0){
            gc.setFill(Color.rgb(0, 90, 0,1));
            gc.fillText("ELLIE", 390, 170);
            gc.fillText("NAME", 250, 230);
            gc.fillText("SCORE", 520, 230);
            gc.setFill(Color.WHITE);
            for(int i=0;i<10;i++){
                if (i < scores.size()) {
                    TableauScore scoreEllie = scores.get(i);
                    gc.fillText(scoreEllie.getName(), 250, 250 + 60 * (i + 1));
                    gc.fillText(String.format("%.0f", scoreEllie.getScore()), 520, 250 + 60 * (i + 1));
                }else {
                    gc.fillText("-", 250, 250 + 60 * (i + 1));
                    gc.fillText("-", 520, 250 + 60 * (i + 1));
                }
            }
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(10.0);
            gc.strokeLine(875, 450, 925, 500);
            gc.strokeLine(925, 500, 875, 550);
            gc.strokeLine(75, 450, 25, 500);
            gc.strokeLine(25, 500, 75, 550);
            
        }else if(index==1){
            gc.setFill(Color.DARKORANGE);
            gc.fillText("ERWIN", 380, 170);
            gc.fillText("NAME", 250, 230);
            gc.fillText("SCORE", 520, 230);
            gc.setFill(Color.WHITE);
            for(int i=0;i<10;i++){
                if (i < scores.size()) {
                    TableauScore scoreErwin = scores.get(i);
                    gc.fillText(scoreErwin.getName(), 250, 250 + 60 * (i + 1));
                    gc.fillText(String.format("%.0f", scoreErwin.getScore()), 520, 250 + 60 * (i + 1));
                }else {
                    gc.fillText("-", 250, 250 + 60 * (i + 1));
                    gc.fillText("-", 520, 250 + 60 * (i + 1));
                }
            }
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(10.0);
            gc.strokeLine(875, 450, 925, 500);
            gc.strokeLine(925, 500, 875, 550);
            gc.strokeLine(75, 450, 25, 500);
            gc.strokeLine(25, 500, 75, 550);
        }else if(index==2){
            gc.setFill(Color.rgb(0, 90, 0,1));
            gc.fillText("ELLIE", 290, 170);
            gc.fillText("NAME", 250, 230);
            gc.setFill(Color.DARKORANGE);
            gc.fillText("ERWIN", 490, 170);
            gc.fillText("SCORE", 520, 230);
            gc.setFill(Color.WHITE);
            gc.fillText("AND", 402, 170);
            for(int i=0;i<10;i++){
                if (i < scores.size()) {
                    TableauScore scoreEllieErwin = scores.get(i);
                    gc.fillText(scoreEllieErwin.getName(), 250, 250 + 60 * (i + 1));
                    gc.fillText(String.format("%.0f", scoreEllieErwin.getScore()), 520, 250 + 60 * (i + 1));
                }else {
                    gc.fillText("-", 250, 250 + 60 * (i + 1));
                    gc.fillText("-", 520, 250 + 60 * (i + 1));
                }
            }
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(10.0);
            gc.strokeLine(875, 450, 925, 500);
            gc.strokeLine(925, 500, 875, 550);
            gc.strokeLine(75, 450, 25, 500);
            gc.strokeLine(25, 500, 75, 550);
        }
    
    }

    private String getPersonnageActuel() {
        if (index == 0) return "ELLIE";
        if (index == 1) return "ERWIN";
        return "ELLIE_AND_ERWIN";
    }

    @Override
    public List<Entity> getEntity() {
        return entity;
    }

    @Override
    public boolean isFinished() {
        return returnMenu;
    }

    public boolean isReturnMenu() {
        return returnMenu;
    }
    
    public Player getPlayer() {
        return player;
    }
}
