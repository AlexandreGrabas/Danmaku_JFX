package com.lloyd27.danmaku.managers;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.HashMap;

public class SoundManager {

    private static MediaPlayer currentMusic;
    private static final HashMap<String, AudioClip> soundCache = new HashMap<>();

    // Joue une musique (avec option de boucle)
    public static void playMusic(String fileName, double volume, boolean loop) {
        try {
            URL resource = SoundManager.class.getResource("/music/" + fileName);
            if (resource == null) {
                System.err.println("⚠️ Music not found: " + fileName);
                return;
            }

            // Stoppe la musique précédente si elle existe
            if (currentMusic != null) {
                currentMusic.stop();
            }

            Media media = new Media(resource.toString());
            currentMusic = new MediaPlayer(media);
            currentMusic.setVolume(volume);
            if (loop) {
                currentMusic.setCycleCount(MediaPlayer.INDEFINITE);
            }
            currentMusic.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Joue un son ponctuel (tir, explosion, etc.)
    public static void playSound(String fileName, double volume) {
        try {
            AudioClip clip = soundCache.computeIfAbsent(fileName, name -> {
                URL resource = SoundManager.class.getResource("/sound/" + name);
                if (resource == null) {
                    System.err.println("⚠️ Sound not found: " + name);
                    return null;
                }
                return new AudioClip(resource.toString());
            });

            if (clip != null) {
                clip.setVolume(volume);
                clip.play();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Stoppe la musique en cours
    public static void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }
}
