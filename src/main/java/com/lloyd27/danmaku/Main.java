package com.lloyd27.danmaku;

import com.lloyd27.danmaku.game.Game;
import com.lloyd27.danmaku.managers.TableauScoresManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 900;
    public TableauScoresManager tableauScoresManager=new TableauScoresManager();;

    @Override
    public void start(Stage stage) {
        
        tableauScoresManager.ajouterScore("ellie", "ELLIE", 5000);
        tableauScoresManager.ajouterScore("ellie", "ERWIN", 4500);
        tableauScoresManager.ajouterScore("erwin", "ERWIN", 5000);
        tableauScoresManager.ajouterScore("erwin", "ELLIE", 4500);
        tableauScoresManager.ajouterScore("ELLIE_AND_ERWIN", "E&E", 10000);

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        Canvas hudCanvas = new Canvas(150, HEIGHT);
        HBox root = new HBox(canvas);
        // StackPane root = new StackPane(canvas,hudCanvas);
        Scene scene = new Scene(root);
        Game game = new Game(canvas,hudCanvas, scene, root);

        stage.setTitle("Danmaku Project");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();

        game.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
