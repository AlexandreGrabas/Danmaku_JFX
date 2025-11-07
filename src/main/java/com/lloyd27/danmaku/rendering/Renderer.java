package com.lloyd27.danmaku.rendering;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

import com.lloyd27.danmaku.entity.Entity;

public class Renderer {
    private Canvas canvas;
    private GraphicsContext gc;

    public Renderer(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public GraphicsContext getGraphicsContext() {
        return gc;
    }

    public void clear() {
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void render(List<Entity> entity) {
        clear();
        for (Entity e : entity) {
            e.render(gc);
        }
    }
}
