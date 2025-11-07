package com.lloyd27.danmaku.Stages;
import java.util.List;
import com.lloyd27.danmaku.entity.Entity;

import javafx.scene.canvas.GraphicsContext;

public abstract class AbstractStage {
    public abstract void init();
    public abstract void render(GraphicsContext gc);
    public abstract void update(double deltaTime);
    public abstract List<Entity> getEntity();
    public abstract boolean isFinished(); // Pour savoir si on passe au stage suivant
}
