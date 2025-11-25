package com.lloyd27.danmaku.rendering;

import java.util.ArrayList;
import java.util.List;

import com.lloyd27.danmaku.entity.Player;
import com.lloyd27.danmaku.entity.PlayerEllie;
import com.lloyd27.danmaku.entity.PlayerErwin;
import com.lloyd27.danmaku.entity.TableauScore;
import com.lloyd27.danmaku.managers.TableauScoresManager;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HUDRenderer {
    private final Canvas hudCanvas;
    private final GraphicsContext gc;
    private Image coeur = new Image(getClass().getResourceAsStream("/sprites/red-heart.png"));
    private Image bomb = new Image(getClass().getResourceAsStream("/sprites/bomb_bleu.png"));
    private Image background=new Image(getClass().getResourceAsStream("/sprites/grotte_background.jpg"));
    private TableauScoresManager tableauScoresManager=new TableauScoresManager();
    private double score;

    public HUDRenderer(Canvas hudCanvas) {
        this.hudCanvas = hudCanvas;
        this.gc = hudCanvas.getGraphicsContext2D();
    }

    public void clear() {
        gc.clearRect(0, 0, hudCanvas.getWidth(), hudCanvas.getHeight());
    }

    public void drawHUD(Player player) {
        if (player == null) return;
        score = tableauScoresManager.getHiScore(getPersonnageActuel(player));
        clear();
        // gc.setFill(Color.rgb(0, 90, 0,1));
        // gc.fillRect(0, 0, hudCanvas.getWidth(), hudCanvas.getHeight());
        gc.drawImage(background, 0, 0, hudCanvas.getWidth(), hudCanvas.getHeight());

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3.0);
        gc.strokeLine(150, 0, 150, 900);


        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 20));
        
        gc.fillText(" CHARACTER:", 0, 20);
        gc.fillText(" "+player.getName(), 0, 50);

        gc.fillText(" HI SCORE:", 0, 150);
        gc.fillText(" "+(int)score, 0, 180);
        gc.fillText(" SCORE:", 0, 220);
        gc.fillText(" "+(int)player.getScore(), 0, 250);
        gc.fillText(" LIFE:", 0, 450);
        for(int i=1;i < player.getHeart();i++){
            if(i>=7){
                gc.drawImage(coeur, (i-1)%6*23, 460+(i-1)/6*25, 40, 40);
            }else{
                gc.drawImage(coeur, (i-1)*23, 460, 40, 40);
            }
        }
        gc.fillText(" BOMB:", 0, 600);
        for(int i=0;i < player.getBomb();i++){
            if(i>=6){
                gc.drawImage(bomb, i%6*22, 610+i/6*25, 30, 30);
            }else{
                gc.drawImage(bomb, i*22, 610, 30, 30);
            }
        }
    }
        private String getPersonnageActuel(Player player) {
            if (player instanceof PlayerEllie) return "ELLIE";
            if (player instanceof PlayerErwin) return "ERWIN";
            return "ELLIE_AND_ERWIN";
    }

}
