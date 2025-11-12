package com.lloyd27.danmaku.managers;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.HashMap;

public class SoundManager {

    private MediaPlayer currentMusic;
    private final HashMap<String, AudioClip> soundCache = new HashMap<>();

    // Joue une musique (avec option de boucle)
    public void playMusic(String fileName, double volume, boolean loop) {
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

    public void PauseMusic(){
        currentMusic.pause();
    }

    public void UnPauseMusic(){
        currentMusic.play();
    }

    // Joue un son ponctuel (tir, explosion, etc.)
    public void playSound(String fileName, double volume) {
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

    public boolean isMusicPlaying() {
        return currentMusic != null && currentMusic.getStatus() == MediaPlayer.Status.PLAYING;
    }

    // Stoppe la musique en cours
    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }
}
