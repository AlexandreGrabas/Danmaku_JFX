package com.lloyd27.danmaku.managers;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.lloyd27.danmaku.entity.TableauScore;

public class TableauScoresManager {

    // Retourne le nom du fichier selon le personnage
    private static String getFileName(String personnage) {
        return "scores_" + personnage.toLowerCase() + ".dat";
    }

    public void sauvegarder(String personnage, List<TableauScore> scores) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getFileName(personnage)))) {
            oos.writeObject(scores);
            System.out.println("Scores sauvegardés pour " + personnage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<TableauScore> charger(String personnage) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(getFileName(personnage)))) {
            return (List<TableauScore>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    // Ajouter un score et ne garder que les 10 meilleurs
    public void ajouterScore(String personnage, String nameScore, double valeur) {
        List<TableauScore> scores = charger(personnage);
        scores.add(new TableauScore(nameScore, valeur));

        scores.sort(Comparator.comparingDouble(TableauScore::getScore).reversed());

        if (scores.size() > 10) {
            scores = new ArrayList<>(scores.subList(0, 10));
        }

        sauvegarder(personnage, scores);
    }

    public double getHiScore(String personnage) {
        List<TableauScore> scores = charger(personnage);
        if (scores.isEmpty()) {
            return 0.0;
        }
        return scores.get(0).getScore();
    }

public double getWorseScore(String personnage) {
    List<TableauScore> scores = charger(personnage);
    if (scores.isEmpty()) {
        return 0.0;
    }

    int index = Math.min(9, scores.size() - 1);
    return scores.get(index).getScore();
}

}
