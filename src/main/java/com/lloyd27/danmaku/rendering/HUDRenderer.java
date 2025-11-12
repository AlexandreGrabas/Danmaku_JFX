package com.lloyd27.danmaku.rendering;

import com.lloyd27.danmaku.entity.Player;
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

    public HUDRenderer(Canvas hudCanvas) {
        this.hudCanvas = hudCanvas;
        this.gc = hudCanvas.getGraphicsContext2D();
    }

    public void clear() {
        gc.clearRect(0, 0, hudCanvas.getWidth(), hudCanvas.getHeight());
    }

    public void drawHUD(Player player) {
        if (player == null) return;

        clear();
        gc.setFill(Color.rgb(0, 90, 0,1));
        gc.fillRect(0, 0, hudCanvas.getWidth(), hudCanvas.getHeight());

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3.0);
        gc.strokeLine(150, 0, 150, 900);


        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", 20));
        
        gc.fillText(" "+player.getName(), 0, 20);
        gc.fillText(" NEEDLE", 0, 50);

        gc.fillText(" SCORE:", 0, 150);
        gc.fillText(" "+(int)player.getScore(), 0, 180);
        gc.fillText(" LIFE:", 0, 450);
        for(int i=0;i < player.getHeart();i++){
            if(i>=7){
                gc.drawImage(coeur, i%7*20, 460+i/7*20, 30, 30);
            }else{
                gc.drawImage(coeur, i*20, 460, 30, 30);
            }
        }
        gc.fillText(" BOMB:", 0, 550);
        for(int i=0;i < player.getBomb();i++){
            if(i>=6){
                gc.drawImage(bomb, i%6*22, 560+i/6*25, 30, 30);
            }else{
                gc.drawImage(bomb, i*22, 560, 30, 30);
            }
        }
    }
}
