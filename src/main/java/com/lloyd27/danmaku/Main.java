package com.lloyd27.danmaku;

import com.lloyd27.danmaku.game.Game;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 900;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);
        Game game = new Game(canvas, scene);

        stage.setTitle("Danmaku Test");
        stage.setScene(scene);
        stage.show();

        game.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
