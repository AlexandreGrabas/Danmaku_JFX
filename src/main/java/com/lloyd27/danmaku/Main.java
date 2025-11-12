package com.lloyd27.danmaku;

import com.lloyd27.danmaku.game.Game;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 900;

    @Override
    public void start(Stage stage) {
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
