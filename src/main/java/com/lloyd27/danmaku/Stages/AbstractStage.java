package com.lloyd27.danmaku.Stages;
import java.util.List;
import com.lloyd27.danmaku.entity.Entity;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public abstract class AbstractStage {
    public abstract void init();
    public abstract void render(GraphicsContext gc);
    public abstract void update(double deltaTime);
    public abstract List<Entity> getEntity();
    public abstract boolean isFinished(); // Pour savoir si on passe au stage suivant

    protected Canvas canvas; // Canvas principal
    protected Canvas hudCanvas; // Canvas HUD (optionnel)

    public Canvas getCanvas() { return canvas; }
    public Canvas getHudCanvas() { return hudCanvas; } // peut être null si pas de HUD

    public void setSize(double width, double height) {
    if (canvas != null) {
        canvas.setWidth(width);
        canvas.setHeight(height);
    }
    if (hudCanvas != null) {
        hudCanvas.setWidth(150);
        hudCanvas.setHeight(height);
    }
}
}
