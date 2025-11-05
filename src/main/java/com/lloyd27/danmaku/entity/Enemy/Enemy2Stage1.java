package com.lloyd27.danmaku.entity.Enemy;

import java.util.ArrayList;
import java.util.List;

import com.lloyd27.danmaku.entity.Bullet.AbstractBullet;
import com.lloyd27.danmaku.entity.weapon.AbstractWeapon;
import com.lloyd27.danmaku.entity.weapon.AbstractWiredWeapon;
import com.lloyd27.danmaku.entity.weapon.WiredWeaponEnemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Enemy2Stage1 extends AbstractEnemyShooter{
    private List<AbstractWeapon> weapons = new ArrayList<>();
    private List<AbstractWiredWeapon> wiredWeapons = new ArrayList<>();
    private double time=0;
    private double centerX = 0;
    private double centerY = 0;
    private double loopStartAngle = 0;
    private boolean loopInitialized = false;
    private double loopEnterTime = 0;


    public Enemy2Stage1(double x, double y) {
        super(x, y);
        this.life=25;
        this.height=20;
        this.width=20;
        wiredWeapons.add(new WiredWeaponEnemy(2));
    }

    public List<AbstractBullet> shoot() {
        List<AbstractBullet> allBullets = new ArrayList<>();
        for (AbstractWeapon w : weapons) {
            allBullets.addAll(w.shoot(x, y));
        }
        return allBullets;
    }

    public List<AbstractBullet> shootWired(double playerX, double playerY) {
        List<AbstractBullet> allBullets = new ArrayList<>();
        for (AbstractWiredWeapon w : wiredWeapons) {
            allBullets.addAll(w.shoot(x, y, playerX, playerY));
        }
        return allBullets;
    }

    @Override
    public void update(double deltaTime) {
        time += deltaTime;

        // paramètres de la boucle (peuvent être fields si tu veux les personnaliser)
        double radiusX = -100;
        double radiusY = -50;
        double speed = -3; // radians / sec

        // PHASE 1 : descente depuis le bord droit de l'ecran
        if(time<2){
            x += 0.7;
            y += 0.6;
        }
        // PHASE 2 : temps d'arret
        else if (time >= 2 && time < 3) {
        }
        // PHASE 3 : boucle relative à la position atteinte à la fin de la phase 1/2
        else if (time >= 3 && time < 7) {
            // on initialise une seule fois
            if (!loopInitialized) {
                loopInitialized = true;
                loopEnterTime = time;

                // Choisir l'angle de départ
                loopStartAngle = 0;

                // Calculer center de sorte que la position actuelle (x,y) soit sur la trajectoire
                // en fonction de l'angle pour éviter les téléportation.
                centerX = x - Math.cos(loopStartAngle) * radiusX;
                centerY = y - Math.sin(loopStartAngle) * radiusY;
            }

            // angle relatif au début de la boucle
            double t = time - loopEnterTime; // temps écoulé depuis l'entrée en boucle
            double angle = loopStartAngle + t * speed;

            // position sur la boucle (relative au centre calculé)
            x = centerX + Math.cos(angle) * radiusX;
            y = centerY + Math.sin(angle) * radiusY;
        }
         else {
            y-=1;
        } 
        
        // update arme classiques
        for (AbstractWeapon w : weapons) {
            w.update(deltaTime);
        }

        // update armes wired
        for (AbstractWiredWeapon w : wiredWeapons) {
            w.update(deltaTime);
        }
    }

    @Override
    public void render(GraphicsContext gc) {

        gc.setFill(Color.DARKRED);
        gc.fillOval(x - 20, y - 20, 20, 20);

    }
}
