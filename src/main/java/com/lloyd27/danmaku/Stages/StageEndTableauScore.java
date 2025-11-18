package com.lloyd27.danmaku.Stages;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.lloyd27.danmaku.entity.Entity;
import com.lloyd27.danmaku.entity.Player;
import com.lloyd27.danmaku.entity.PlayerEllie;
import com.lloyd27.danmaku.entity.PlayerErwin;
import com.lloyd27.danmaku.entity.TableauScore;
import com.lloyd27.danmaku.managers.InputManager;
import com.lloyd27.danmaku.managers.SoundManager;
import com.lloyd27.danmaku.managers.TableauScoresManager;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class StageEndTableauScore extends AbstractStage {
    private Player player;
    private boolean returnMenu = false;
    private InputManager input;
    private List<Entity> entity = new ArrayList<>();
    private SoundManager soundManager = new SoundManager();
    private int indexMot=0;
    private int indexLettres=0;
    private double timeLastUp = 0;
    private double timeLastDown = 0;
    private double timeLastAccepted = 0;
    private double timeLastCanceled = 0;
    private List<TableauScore> scores = new ArrayList<>();
    private TableauScoresManager tableauScoresManager=new TableauScoresManager();
    private String nameScoreTemp = "";
    private int indexNom = -1;
    private boolean scoreEnregistre = false;
    private boolean goTableScore = false;
    private List<Integer> lettres = new ArrayList<>(List.of(0, 0, 0, 0, 0, 0, 0, 0, 0, 0));

    public StageEndTableauScore(InputManager input, Canvas canvas, SoundManager soundManager, Player player) {
        this.input = input;
        this.canvas = canvas;
        this.hudCanvas = null;
        this.player=player;
        this.soundManager=soundManager;
        canvas.setWidth(950);
    }

    @Override
    public void init() {
        soundManager.stopMusic();
        if (!soundManager.isMusicPlaying()) {
            soundManager.playMusic("Mystical Power Plant - 02 Alleyway of Roaring Waves.mp3", 0.2, true);
        }
        input.setAccepted(false);
        // Charger les scores sauvegardés
        scores = tableauScoresManager.charger(getPersonnageActuel());

        TableauScore nouveau = new TableauScore("", player.getScore());
        scores.add(nouveau);

        scores.sort(Comparator.comparingDouble(TableauScore::getScore).reversed());

        indexNom = scores.indexOf(nouveau);

        if (scores.size() > 10) {
            scores = new ArrayList<>(scores.subList(0, 10));
        }
    }

    @Override
    public void update(double deltaTime) {
        timeLastUp -= deltaTime;
        timeLastDown -= deltaTime;
        timeLastAccepted -= deltaTime;
        timeLastCanceled -= deltaTime;

        if (scoreEnregistre){
            tableauScoresManager.ajouterScore(getPersonnageActuel(), nameScoreTemp, player.getScore());
            goTableScore=true;
        }

        // navigation
        if (timeLastUp<=0 && input.isUp()) {
            indexMot -= 1;
            timeLastUp = 0.2;
            if(indexMot==-1){indexMot=26;}
        }
        if (timeLastDown<=0 && input.isDown()) {
            indexMot += 1;
            timeLastDown = 0.2;
            if(indexMot==27){indexMot=0;}
        };


        // Validation d'une lettre
        if (timeLastAccepted<=0 && input.isAccepted()) {
            timeLastAccepted = 0.2;
            input.setAccepted(false);
            if (indexLettres < 10) {
                char lettreChoisie = (indexMot == 0) ? ' ' : (char) ('A' + indexMot - 1);
                lettres.set(indexLettres, (int) lettreChoisie);

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i <= indexLettres; i++) {
                    sb.append((char) lettres.get(i).intValue());
                }
                nameScoreTemp = sb.toString();

                indexLettres++;

            }else if (indexLettres >= 10) {
                    scoreEnregistre = true;
                }
        }

        // Retour en arrière (touche X)
        if (timeLastCanceled <= 0 && input.isCanceled()) { 
            timeLastCanceled = 0.2;
            input.setCanceled(false);

            if (indexLettres > 0) {
                indexLettres--;  // On recule d'une lettre
                lettres.set(indexLettres, 0); // On supprime la lettre

                // Reconstruire le nom temporaire
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i <= indexLettres; i++) {
                    int code = lettres.get(i);
                    if (code != 0) sb.append((char) code);
                }
                nameScoreTemp = sb.toString();
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFont(new Font("Arial", 95));
        gc.setFill(Color.WHITE);
        gc.fillText("Tableau des Scores", 70, 100);
        gc.setFont(new Font("Arial", 36));
        if(getPersonnageActuel()=="ELLIE"){
            gc.setFill(Color.rgb(0, 90, 0,1));
            gc.fillText("ELLIE", 410, 170);
            gc.fillText("NAME", 250, 230);
            gc.fillText("SCORE", 560, 230);
        } else if(getPersonnageActuel()=="ERWIN"){
            gc.setFill(Color.DARKORANGE);
            gc.fillText("ERWIN", 400, 170);
            gc.fillText("NAME", 250, 230);
            gc.fillText("SCORE", 560, 230);
        }else{
            gc.setFill(Color.rgb(0, 90, 0,1));
            gc.fillText("ELLIE", 305, 170);
            gc.fillText("NAME", 250, 230);
            gc.setFill(Color.DARKORANGE);
            gc.fillText("ERWIN", 505, 170);
            gc.fillText("SCORE", 560, 230);
            gc.setFill(Color.WHITE);
            gc.fillText("AND", 417, 170);
        }
        gc.setFill(Color.WHITE);
        for (int i = 0; i < 10; i++) {
            double y = 250 + 60 * (i + 1);

            if (i < scores.size()) {
                TableauScore s = scores.get(i);

                if (i == indexNom && !scoreEnregistre) {
                    // ==== Zone du joueur en cours de saisie ====

                    gc.setFill(Color.YELLOW);

                    StringBuilder nameDisplay = new StringBuilder();

                    // lettres validées
                    for (int j = 0; j < indexLettres; j++) {
                        nameDisplay.append((char) lettres.get(j).intValue());
                    }

                    // lettre en cours (uniquement si on est dans une position encore non saisie)
                    if (indexLettres < 10) {
                        if (lettres.get(indexLettres) == 0) {
                            char currentLetter = (indexMot == 0) ? ' ' : (char) ('A' + indexMot - 1);
                            nameDisplay.append(currentLetter);
                        }
                    }
                    // lettres restantes (espaces)
                    for (int j = indexLettres + 1; j < 10; j++) {
                        nameDisplay.append(' ');
                    }

                    gc.fillText(nameDisplay.toString(), 250, y);

                    // Soulignement dynamique
                    String validated = nameDisplay.substring(0, indexLettres);
                    double underlineX = 250 + getTextWidth(validated, gc.getFont());

                    if(indexLettres<10){
                        gc.fillText("_", underlineX+2, y + 8);
                    }else{
                        gc.fillText("OK", underlineX+10, y);
                        gc.fillText("__", underlineX+16, y + 8);
                    }

                    gc.setFill(Color.WHITE);
                } else {
                    // lignes des autres scores
                    gc.fillText(s.getName().isEmpty() ? "-" : s.getName(), 250, y);
                }

                gc.fillText(String.format("%.0f", s.getScore()), 560, y);

            } else {
                // lignes vides si pas de score
                gc.fillText("-", 250, y);
                gc.fillText("-", 560, y);
            }
        }
    }



    private String getPersonnageActuel() {
        if (player instanceof PlayerEllie) return "ELLIE";
        if (player instanceof PlayerErwin) return "ERWIN";
        return "ELLIE_AND_ERWIN";
    }

    @Override
    public List<Entity> getEntity() {
        return entity;
    }

    @Override
    public boolean isFinished() {
        return returnMenu || goTableScore;
    }

    public boolean isReturnMenu() {
        return returnMenu;
    }

    public boolean isGoTableScore() {
        return goTableScore;
    }
    
    public Player getPlayer() {
        return player;
    }

    private double getTextWidth(String text, Font font) {
        javafx.scene.text.Text temp = new javafx.scene.text.Text(text);
        temp.setFont(font);
        return temp.getLayoutBounds().getWidth();
    }
}
